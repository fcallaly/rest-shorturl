package com.frankc.shorturl.controllers.hateoas;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import com.frankc.shorturl.controllers.ShortUrlController;
import com.frankc.shorturl.controllers.ShortUrlRedirectController;
import com.frankc.shorturl.entities.ShortUrl;

/**
 * Resource definition for hateoas support on shortUrl.
 *
 * @author Frank Callaly
 */
public class ShortUrlResource extends ResourceSupport {

    private ShortUrl shortUrl;

    public ShortUrlResource(final ShortUrl shortUrl) {
        this.shortUrl = shortUrl;

        add(linkTo(methodOn(ShortUrlController.class)
                   .findAllShortUrls()).withRel("all-urls"));
        add(linkTo(methodOn(ShortUrlController.class)
                    .findShortUrl(shortUrl.getShortUrlPath()))
                    .withSelfRel());
        add(linkTo(methodOn(ShortUrlRedirectController.class)
                .redirectByShortUrl(shortUrl.getShortUrlPath()))
                .withRel("exec_redirect"));
    }

    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(final ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }
}
