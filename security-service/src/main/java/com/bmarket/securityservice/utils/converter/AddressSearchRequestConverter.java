package com.bmarket.securityservice.utils.converter;

import com.bmarket.securityservice.domain.address.AddressRange;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressSearchRequestConverter implements Converter<String, AddressRange> {

    @Override
    public AddressRange convert(String addressSearchRange) {
        return AddressRange.valueOf(addressSearchRange);
    }

}
