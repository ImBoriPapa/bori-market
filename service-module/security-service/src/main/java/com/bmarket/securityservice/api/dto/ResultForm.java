package com.bmarket.securityservice.api.dto;

import org.springframework.hateoas.EntityModel;

public abstract class ResultForm extends EntityModel {

    public abstract String getClientId();
}
