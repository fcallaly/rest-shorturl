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
package com.frankc.shorturl.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when an attempt is made to write shortUrlPath in during an
 * operation where that field is read only.
 * 
 * Sets HTTP status code to 409 with sensible message.
 *
 * @author Frank Callaly
 */
@ResponseStatus(code = HttpStatus.CONFLICT,
                reason = "shortUrlPath is read_only")
public class ImmutableShortUrlPathException extends RuntimeException {
    private static final long serialVersionUID = 8784771960248183750L;
}
