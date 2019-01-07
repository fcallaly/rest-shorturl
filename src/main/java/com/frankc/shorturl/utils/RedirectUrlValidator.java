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
package com.frankc.shorturl.utils;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Component
public class RedirectUrlValidator {

    public static final UrlValidator URL_VALIDATOR = new UrlValidator();

    public static final String DEFAULT_URL_PROTOCOL = "http://";

    private static final Pattern URL_WITH_PROTOCOL =
            Pattern.compile("(?i)^(https?|ftp|file)://.*$");

    public String fixUrlProtocol(final String url) {
        if (URL_WITH_PROTOCOL.matcher(url).matches()) {
            return url;
        }
        return DEFAULT_URL_PROTOCOL + url;
    }

    public void validateUrl(final String url)
                            throws MalformedURLException {
        if (!URL_VALIDATOR.isValid(url)) {
            throw new MalformedURLException();
        }
    }
}
