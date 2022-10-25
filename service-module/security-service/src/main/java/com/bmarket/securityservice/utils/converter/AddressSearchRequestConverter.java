package com.bmarket.securityservice.utils.converter;

import com.bmarket.securityservice.domain.profile.entity.AddressSearchRange;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressSearchRequestConverter implements Converter<String, AddressSearchRange> {

    @Override
    public AddressSearchRange convert(String addressSearchRange) {
        return AddressSearchRange.valueOf(addressSearchRange);
    }

}
