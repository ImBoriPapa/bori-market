package com.bmarket.tradeservice.utils.link;

import com.bmarket.tradeservice.api.TradeCommandController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class LinkAssem implements RepresentationModelAssembler<TradeCommandController.ResponseCreateTrade, EntityModel<TradeCommandController.ResponseCreateTrade>> {


    @Override
    public CollectionModel toCollectionModel(Iterable entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<TradeCommandController.ResponseCreateTrade> toModel(TradeCommandController.ResponseCreateTrade entity) {
        return null;
    }
}
