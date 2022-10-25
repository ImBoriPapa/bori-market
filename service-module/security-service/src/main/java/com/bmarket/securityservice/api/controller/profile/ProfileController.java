package com.bmarket.securityservice.api.controller.profile;
import com.bmarket.securityservice.domain.profile.profileService.ProfileCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileCommandService profileCommandService;

    @GetMapping(value = "/profile/{clientId}")
    public void getProfile(@PathVariable String clientId, @RequestPart MultipartFile image) {

    }

    @PatchMapping("/profile/clientId")
    public void patch(@PathVariable String clientId) {

    }


}
