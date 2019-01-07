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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.frankc.shorturl.controllers.exceptions.ShortUrlNotFoundException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Implements redirects from a ShortUrlPath to the corresponding redirectTo URI.
 *
 * @author Frank Callaly
 */
@RestController
@RequestMapping(ShortUrlRedirectController.BASE_PATH)
public class ShortUrlRedirectController {

    public static final String BASE_PATH = "/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShortUrlService shortUrlService;

    /**
     * Redirect to the redirectTo field of a ShortUrl.
     *
     * @param shortUrlPath of shortUrl to be redirected by
     * @throws ShortUrlNotFoundException
     */
    @ApiOperation(value = "Redirect according to a shortUrl")
    @ApiResponses(value = {
        @ApiResponse(code = 301, message = "Successful redirect"),
        @ApiResponse(code = 404,
                     message = "The requested shortUrlPath was not found"),
        @ApiResponse(code = 422,
                     message = "ShortUrl contains an invalid redirect")})
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("{shortUrlPath:[a-zA-Z0-9]+$}")
    public HttpEntity<Void> redirectByShortUrl(
                    @PathVariable("shortUrlPath") final String shortUrlPath)
                    throws ShortUrlNotFoundException {
        ShortUrl requestedShortUrl = null;

        try {
            requestedShortUrl =
                    shortUrlService.findByShortUrlPath(shortUrlPath);
        } catch (NoSuchElementException ex) {
            logger.warn("Failed attempt to find short url: " + shortUrlPath);
            throw new ShortUrlNotFoundException();
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            responseHeaders.setLocation(
                    new URI(requestedShortUrl.getRedirectTo()));
        } catch (URISyntaxException ex) {
            logger.error("Invalid URI in shortUrl: " + requestedShortUrl);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        logger.debug("Redirecting From [" + shortUrlPath + "] to ["
                     + requestedShortUrl.getRedirectTo() + "]");
        return new ResponseEntity<>(responseHeaders,
                                    HttpStatus.MOVED_PERMANENTLY);
    }
}
