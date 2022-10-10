package com.bmarket.securityservice.utils;

import com.bmarket.securityservice.dto.ResultForm;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


import java.net.URI;
import java.util.List;

/**
 * 기본 링크 생성
 * @param <T>
 */
@Component
public class LinkProvider<T> {
    /**
     * Controller 클래스로 resource 링크 생성
     * @param target
     * @return
     */
    private  WebMvcLinkBuilder getResourcesLink(Class<T> target) {
        WebMvcLinkBuilder resource = WebMvcLinkBuilder.linkTo(target);
        return resource;
    }

    /**
     * resource, clientId 로 LOCATION URI 생성
     * @param target
     * @return
     */
    public  URI getLocationUri(Class<T> target, ResultForm form){
       return getResourcesLink(target).slash(form.getClientId()).toUri();
    }

    /**
     * 기본 링크 생성 : findOne, list, update, delete
     * @param target
     * @param form
     * @return
     */
    public List getLinks(Class<T> target, ResultForm form) {
        WebMvcLinkBuilder resource = getResourcesLink(target);
        Link findOne = resource.slash(form.getClientId()).withRel("GET : 단건 조회");
        Link list = resource.withSelfRel().withRel("GET : 전체 조회");
        Link update = resource.slash(form.getClientId()).withRel("PUT : 수정");
        Link delete = resource.slash(form.getClientId()).withRel("DELETE : 삭제");
        return List.of(findOne, list, update, delete);
    }
}
