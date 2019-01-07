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
package com.frankc.shorturl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.frankc.shorturl.entities.ShortUrl;

/**
 * JPA Repository interface for ShortUrl Entity.
 *
 * @author Frank Callaly
 */
@Repository
public interface ShortUrlRepo
                 extends JpaRepository<ShortUrl, Long> {

    ShortUrl findShortUrlByShortUrlPath(String shortUrlPath);

    boolean existsByShortUrlPath(String shortUrlPath);

    @Transactional
    Long deleteByShortUrlPath(String shortUrlPath);
}
