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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements RESTful HTTP/JSON interface to ShortUrl Repository.
 *
 * @author Frank Callaly
 */
@RestController
@RequestMapping(ShortUrlController.BASE_PATH)
public class ShortUrlController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String BASE_PATH = "/short-urls/";

    /**
     * Get a collection of all ShortUrls in the repository.
     *
     * TODO: Currently using Object as placeholder for ShortURL DTO/Entity
     * 
     * @return a List of ShortUrls
     */
    @GetMapping
    public List<Object> findAllShortUrls() {
        logger.debug("Find All Short URLs");
        return new ArrayList<Object>();
    }
}
