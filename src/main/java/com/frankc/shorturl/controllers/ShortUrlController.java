/*******************************************************************************
 * Copyright (C) 2019 Frank Callaly
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.frankc.shorturl.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.frankc.shorturl.controllers.exceptions.ImmutableShortUrlPathException;
import com.frankc.shorturl.controllers.exceptions.InvalidRedirectToException;
import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
import com.frankc.shorturl.controllers.exceptions.ShortUrlNotFoundException;
import com.frankc.shorturl.controllers.hateoas.ShortUrlResource;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Implements RESTful HTTP/HAL+JSON interface to ShortUrl Repository.
 *
 * @author Frank Callaly
 */
@RestController
@RequestMapping(ShortUrlController.BASE_PATH)
public class ShortUrlController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String BASE_PATH = "/short-urls/";

    @Autowired
    private ShortUrlService shortUrlService;

    /**
     * Find a collection of all ShortUrls in the repository.
     *
     * @return a List of ShortUrls
     */
    @ApiOperation(value = "Find all shortUrls on the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @GetMapping(produces = "application/hal+json")
    public Resources<ShortUrlResource> findAllShortUrls() {
        logger.debug("GET findAllShortUrls");

        List<ShortUrlResource> collection =
                shortUrlService.findAll().stream()
                    .map(ShortUrlResource::new)
                    .collect(Collectors.toList());

        return new Resources<>(
                    collection,
                    linkTo(methodOn(this.getClass()).findAllShortUrls())
                        .withRel("self"));
    }

    /**
     * Find a single particular ShortUrl in the repository.
     *
     * @param shortUrlPath of ShortUrl to find
     * @return a Resource<ShortUrl> corresponding to the given shortUrlPath
     */
    @ApiOperation(value = "Find a particular shortUrl by it's shortUrlPath")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404,
                         message = "The requested shortUrl was not found")})
    @GetMapping(path = "{shortUrlPath}", produces = "application/hal+json")
    public HttpEntity<ShortUrlResource> findShortUrl(
                        @PathVariable("shortUrlPath") final String shortUrlPath)
                        throws ShortUrlNotFoundException {
        logger.debug("GET findShortUrl: " + shortUrlPath);
        ShortUrlResource resource = null;

        try {
            resource = new ShortUrlResource(
                           shortUrlService.findByShortUrlPath(shortUrlPath));

        } catch (NoSuchElementException ex) {
            logger.warn("Failed attempt to find short url: " + shortUrlPath);
            throw new ShortUrlNotFoundException();
        }

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Create a new ShortUrl and add to the repository.
     *
     * @param newShortUrl ShortUrl corresponding to data sent in request body
     * @return a Resource<ShortUrl> for the object that has been added
     */
    @ApiOperation(value = "Create a new ShortUrl",
                  notes = "ShortUrlPath is read-only. "
                          + "Only include the redirectTo field in request.")
    @ApiResponses(value = {
            @ApiResponse(code = 201,
                         message = "The requested shortUrl was created"),
            @ApiResponse(code = 400,
                         message = "The given redirectTo is invalid"),
            @ApiResponse(code = 409,
                         message = "ShortUrlPath should not be included in "
                                   + "request as it is a read-only property")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json",
                 produces = "application/hal+json")
    public HttpEntity<ShortUrlResource> createShortUrl(
                                   @RequestBody final ShortUrl newShortUrl)
                                   throws MaxPathGenerationRetriesException,
                                          InvalidRedirectToException {
        logger.debug("POST createShortUrl: " + newShortUrl);

        if (newShortUrl.getShortUrlPath() != null) {
            throw new ImmutableShortUrlPathException();
        }

        ShortUrl createdShortUrl = null;
        try {
            createdShortUrl = shortUrlService
                                .createShortUrl(newShortUrl.getRedirectTo());
        } catch (IllegalArgumentException ex) {
            logger.warn("Service refused to create ShortUrl for redirect: "
                        + newShortUrl.getRedirectTo());
            throw new InvalidRedirectToException();
        }

        return new ResponseEntity<ShortUrlResource>(
                        new ShortUrlResource(createdShortUrl),
                        HttpStatus.CREATED);
    }

    /**
     * Delete a single particular ShortUrl from the repository.
     *
     * @param shortUrlPath of ShortUrl to delete
     * @return a Resource<ShortUrl> corresponding to the given shortUrlPath
     */
    @ApiOperation(value = "Delete a shortUrl")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 404,
                     message = "The requested shortUrlPath was not found")})
    @DeleteMapping("{shortUrlPath}")
    public void deleteShortUrl(
                    @PathVariable("shortUrlPath") final String shortUrlPath)
                    throws ShortUrlNotFoundException {
        logger.debug("DELETE deleteShortUrl: " + shortUrlPath);

        try {
            shortUrlService.deleteByShortUrlPath(shortUrlPath);
        } catch (NoSuchElementException ex) {
            logger.warn("Failed to find short url for deletion: "
                        + shortUrlPath);
            throw new ShortUrlNotFoundException();
        }
    }
}
