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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

/**
 * Unit Tests for ShortUrlRedirectController.
 *
 * @author Frank Callaly
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("nojpa")
public class ShortUrlRedirectControllerTests {

    private static final String TEST_SHORTURLPATH = "abcdABCD1234";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService mockShortUrlService;

    @Test
    public void findAllShortUrls_returnsMovedPermanently() throws Exception {
        when(mockShortUrlService.findByShortUrlPath(TEST_SHORTURLPATH))
            .thenReturn(new ShortUrl("http://www.redirectto.ie"));

        this.mockMvc
                .perform(get(ShortUrlRedirectController.BASE_PATH
                             + TEST_SHORTURLPATH))
                .andExpect(status().isMovedPermanently());
    }

    @Test
    public void findAllShortUrls_unknownReturnsNotFound() throws Exception {
        when(mockShortUrlService.findByShortUrlPath(TEST_SHORTURLPATH))
            .thenThrow(new NoSuchElementException());

        this.mockMvc
                .perform(get(ShortUrlRedirectController.BASE_PATH
                             + TEST_SHORTURLPATH))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShortUrls_invalidRedirectReturnsUnprocessable()
                                                         throws Exception {
        when(mockShortUrlService.findByShortUrlPath(TEST_SHORTURLPATH))
            .thenReturn(new ShortUrl("spaces 1nval1D"));

        this.mockMvc
                .perform(get(ShortUrlRedirectController.BASE_PATH
                             + TEST_SHORTURLPATH))
                .andExpect(status().isUnprocessableEntity());
    }
}
