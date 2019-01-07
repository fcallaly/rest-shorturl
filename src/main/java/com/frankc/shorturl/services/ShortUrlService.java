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
package com.frankc.shorturl.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;

import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
/**
 * Interface to ShortUrl Service layer.
 *
 * @author Frank Callaly
 */
import com.frankc.shorturl.entities.ShortUrl;

public interface ShortUrlService {

    List<ShortUrl> findAll(Pageable pageRequest);

    ShortUrl findByShortUrlPath(String shortUrlPath)
                                throws NoSuchElementException;

    ShortUrl createShortUrl(String redirectTo)
                            throws IllegalArgumentException,
                                   MaxPathGenerationRetriesException;

    void deleteByShortUrlPath(String shortUrlPath)
                              throws NoSuchElementException;
}
