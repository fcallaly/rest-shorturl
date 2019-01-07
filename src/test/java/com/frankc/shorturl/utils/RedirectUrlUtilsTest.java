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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectUrlUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void fixUrlProtocol_withProtocol() {
        String result;
        String url = "http://www.google.ie";

        logger.debug("Test standard url string:");
        result = RedirectUrlUtils.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equals(url));
    }

    @Test
    public void fixUrlProtocol_noProtocol() {
        String result;
        String url = "www.google.ie";
        logger.debug("Test url string with no protcol:");
        result = RedirectUrlUtils.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equals("http://" + url));
    }

    @Test
    public void testFixUrlProtocolCaseInsensitivity() {
        String result;
        String url = "HTtP://www.google.ie";
        logger.debug("Test url string with changing case protocol:");
        result = RedirectUrlUtils.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equalsIgnoreCase(url));
    }
}
