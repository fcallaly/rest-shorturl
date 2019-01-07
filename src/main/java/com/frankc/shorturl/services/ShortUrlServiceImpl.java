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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.repositories.ShortUrlRepo;
import com.frankc.shorturl.utils.ShortUrlPathGenerator;
import com.frankc.shorturl.utils.RedirectUrlUtils;

/**
 * ShortUrl Service layer default implementation.
 *
 * @author Frank Callaly
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShortUrlRepo shortUrlRepo;

    @Autowired
    private ShortUrlPathGenerator shortUrlGenerator;

    @Value("${com.frankc.shorturl.service.maxShortUrlPathGenRetries:3}")
    private int maxShortUrlPathGenerationRetries;

    public List<ShortUrl> findAll() {
        return shortUrlRepo.findAll();
    }

    public ShortUrl findByShortUrlPath(final String shortUrlPath)
                                       throws NoSuchElementException {
        ShortUrl foundShortUrl =
                    shortUrlRepo.findShortUrlByShortUrlPath(shortUrlPath);

        if (foundShortUrl == null) {
            throw new NoSuchElementException();
        }
        return foundShortUrl;
    }

    public ShortUrl createShortUrl(final String redirectTo)
                                   throws IllegalArgumentException,
                                          MaxPathGenerationRetriesException {
        String fixedRedirectTo = RedirectUrlUtils.fixUrlProtocol(redirectTo);

        try {
            new URL(fixedRedirectTo);
        } catch (MalformedURLException ex) {
            logger.error("Request to create shortUrl with invalid redirectTo:"
                         + fixedRedirectTo);
            throw new IllegalArgumentException();
        }

        ShortUrl newShortUrl = new ShortUrl(fixedRedirectTo);

        int numAttempts = 1;
        while (true) {
            newShortUrl.setShortUrlPath(shortUrlGenerator.generateBase62());

            try {
                return shortUrlRepo.save(newShortUrl);
            } catch (DataIntegrityViolationException ex) {
                logger.warn("Short URL Path generation failed to create unique "
                            + "path : " + numAttempts + " of "
                            + maxShortUrlPathGenerationRetries + " attempts"
                            + " : " + ex);
            }
            if (numAttempts >= maxShortUrlPathGenerationRetries) {
                throw new MaxPathGenerationRetriesException();
            }
            ++numAttempts;
        }
    }

    public void deleteByShortUrlPath(final String shortUrlPath)
                                     throws NoSuchElementException {
        if (!shortUrlRepo.existsByShortUrlPath(shortUrlPath)) {
            throw new NoSuchElementException();
        }
        shortUrlRepo.deleteByShortUrlPath(shortUrlPath);
    }
}
