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

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

/**
 * Unit Tests for ShortUrlController.
 *
 * @author Frank Callaly
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("nojpa")
public class ShortUrlControllerTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TEST_SHORTURLPATH = "abcdABCD1234";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService mockShortUrlService;

    @Test
    public void findAllShortUrls_returnsList() throws Exception {
        when(mockShortUrlService.findAll(any()))
            .thenReturn(new ArrayList<ShortUrl>());

        MvcResult result = this.mockMvc
                .perform(get(ShortUrlController.BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNumber())
                .andReturn();

        logger.info("Result from findAllShortUrl: "
                    + result.getResponse().getContentAsString());
    }

    @Test
    public void findAllShortUrls_truncatesLargePageSize() throws Exception {
        Integer largePageSize = 20000;
        when(mockShortUrlService.findAll(any()))
            .thenReturn(new ArrayList<ShortUrl>());

        MvcResult result = this.mockMvc
                .perform(get(ShortUrlController.BASE_PATH)
                    .param("pageSize", largePageSize.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNumber())
                .andReturn();

        ArgumentCaptor<Pageable> argument =
                        ArgumentCaptor.forClass(Pageable.class);
        verify(mockShortUrlService).findAll(argument.capture());
        assertTrue(argument.getValue().getPageNumber() < largePageSize);

        logger.info("Result from findAllShortUrl: "
                    + result.getResponse().getContentAsString());
    }

    @Test
    public void findAllShortUrls_invalidPageSizeReturnsBadRequest()
                                                throws Exception {
        this.mockMvc.perform(get(ShortUrlController.BASE_PATH)
                        .param("pageSize", "-200"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllShortUrls_invalidPageNumReturnsBadRequest()
                                                throws Exception {
        this.mockMvc.perform(get(ShortUrlController.BASE_PATH)
                        .param("pageNumber", "-200"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void findShortUrl_returnsOk() throws Exception {
        ShortUrl testShortUrl = new ShortUrl("http://www.testfind.com");
        testShortUrl.setShortUrlPath(TEST_SHORTURLPATH);

        when(mockShortUrlService
                .findByShortUrlPath(testShortUrl.getShortUrlPath()))
            .thenReturn(testShortUrl);

        this.mockMvc.perform(get(ShortUrlController.BASE_PATH
                                 + testShortUrl.getShortUrlPath()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.shortUrl.shortUrlPath")
                               .value(testShortUrl.getShortUrlPath()));
    }

    @Test
    public void findShortUrl_unknownReturnsNotFound() throws Exception {
        when(mockShortUrlService.findByShortUrlPath(TEST_SHORTURLPATH))
            .thenThrow(new NoSuchElementException());

        this.mockMvc.perform(get(ShortUrlController.BASE_PATH
                                 + TEST_SHORTURLPATH))
                    .andExpect(status().isNotFound());
    }


    @Test
    public void createShortUrl_returnsCreated() throws Exception {
        ShortUrl testShortUrl = new ShortUrl("http://www.testcreate.com");

        when(mockShortUrlService
                .createShortUrl(testShortUrl.getRedirectTo()))
            .thenReturn(testShortUrl);

        this.mockMvc.perform(
                post(ShortUrlController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testShortUrl)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl.shortUrlPath")
                    .value(testShortUrl.getShortUrlPath()));
    }

    @Test
    public void createShortUrl_withPathReturnsImmutable() throws Exception {
        ShortUrl testShortUrl = new ShortUrl("http://www.testcreate.com");
        testShortUrl.setShortUrlPath(TEST_SHORTURLPATH);

        when(mockShortUrlService
                .createShortUrl(testShortUrl.getRedirectTo()))
            .thenReturn(testShortUrl);

        this.mockMvc.perform(
                post(ShortUrlController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testShortUrl)))
                .andExpect(status().isConflict());
    }

    @Test
    public void createShortUrl_withInvalidRedirectReturnsBadRequest()
                                                    throws Exception {
        ShortUrl testShortUrl = new ShortUrl("adf;ksie");

        when(mockShortUrlService
                .createShortUrl(testShortUrl.getRedirectTo()))
            .thenThrow(new IllegalArgumentException());

        this.mockMvc.perform(
                post(ShortUrlController.BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testShortUrl)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_deleteShortUrl_returnsOk() throws Exception {
        this.mockMvc.perform(delete(ShortUrlController.BASE_PATH
                                    + TEST_SHORTURLPATH))
                    .andExpect(status().isOk());
    }

    @Test
    public void test_deleteShortUrl_unknownReturnsNotFound() throws Exception {
        doThrow(new NoSuchElementException())
             .when(mockShortUrlService)
             .deleteByShortUrlPath(TEST_SHORTURLPATH);

        this.mockMvc.perform(delete(ShortUrlController.BASE_PATH
                                    + TEST_SHORTURLPATH))
                    .andExpect(status().isNotFound());
    }
}
