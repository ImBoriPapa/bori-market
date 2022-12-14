package com.bmarket.securityservice.utils;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 기본 링크 생성
 *
 * @param
 */
@Component
public class LinkProvider {


    private WebMvcLinkBuilder getController(Class<?> className) {
        return WebMvcLinkBuilder.linkTo(className);
    }
    /**
     * 기본 C,R,U,D 링크 생성
     */
    public List<Link> createCrudLink(Class<?> className, Object ids, String resourcesName) {
        WebMvcLinkBuilder builder = getController(className);
        Link link1 = builder.withRel("POST   : " + resourcesName + " 생성");
        Link link2 = builder.withRel("GET   : " + resourcesName + "  전체 조회");
        Link link3 = builder.slash(ids).withRel("GET : " + resourcesName + " 상세 조회");
        Link link4 = builder.slash(ids).withRel("PUT : " + resourcesName + " 수정");
        Link link5 = builder.slash(ids).withRel("DELETE: " + resourcesName + " 삭제");
        return List.of(link1, link2, link3, link4, link5);
    }

    public Link createOneLink(Class<?> className, Object resourceName, String rel) {
        return getController(className).slash(resourceName).withRel(rel);
    }

    public List<Link> createPageLink(Class<?> className, Integer pageNumber, Integer size, Long totalCount) {
        WebMvcLinkBuilder builder = getController(className);

        String next = "?page=" + (pageNumber + 1);
        String previous = "?page=" + (pageNumber - 1);
        Link nextLink = builder.slash(next).withRel("GET : 다음 페이지");
        Link previousLink = builder.slash(previous).withRel("GET : 이전 페이지");

        if (pageNumber <= 0) {
            return List.of(nextLink);
        }

        if (pageNumber < (totalCount / size)) {
            return List.of(previousLink, nextLink);
        }

        return List.of(previousLink);
    }
}
