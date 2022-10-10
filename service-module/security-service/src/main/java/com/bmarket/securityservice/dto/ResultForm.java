package com.bmarket.securityservice.dto;

import org.springframework.hateoas.EntityModel;

public abstract class ResultForm extends EntityModel {

    public abstract String getClientId();
}
