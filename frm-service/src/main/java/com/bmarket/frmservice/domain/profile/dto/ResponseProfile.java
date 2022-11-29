package com.bmarket.frmservice.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProfile {

    private boolean success;
    private Long accountId;
    private String imagePath;
}
