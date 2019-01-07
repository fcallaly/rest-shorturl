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
package com.frankc.shorturl.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * Domain object for ShortUrl, primarily maps Short URL Paths to Long URLs.
 * 
 * @author Frank Callaly
 */
@Entity
public class ShortUrl {

    private static final int UUID2_FIELD_SIZE = 36;

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, length = UUID2_FIELD_SIZE)
    private String shortUrlPath;

    private String redirectTo;


    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;


    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public ShortUrl() { }

    public ShortUrl(final String redirectTo) {
        this.setRedirectTo(redirectTo);
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getShortUrlPath() {
        return shortUrlPath;
    }

    public void setShortUrlPath(final String shortUrlPath) {
        this.shortUrlPath = shortUrlPath;
    }

    public String getRedirectTo() {
        return this.redirectTo;
    }

    public void setRedirectTo(final String redirectTo) {
        this.redirectTo = redirectTo;
    }

    @ApiModelProperty(hidden = true)
    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
       this.created = created;
    }

    @ApiModelProperty(hidden = true)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return "Id: " + this.getId()
               + ", shortUrl: " + this.getShortUrlPath()
               + ", redirectTo: " + this.getRedirectTo()
               + ", created: " + this.getCreated()
               + ", last updated: " + this.getLastUpdated();
    }
}
