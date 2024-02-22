package com.dopaming.dopaming.controller;

import com.dopaming.dopaming.responseDto.PamingsResponse;
import com.dopaming.dopaming.security.Util;
import com.dopaming.dopaming.service.PamingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pamings")
@Slf4j
public class PamingsController {

    @Value("${jwt.secret}")
    private String secretKey;
    private final Util util;

    private final PamingsService pamingsService;

    /**
     * [GET] /pamings/ongoing
     * 진행 중인 파밍로그 조회
     *
     * @param token 토큰
     * @return
     */
    @GetMapping("/ongoing")
    public ResponseEntity<PamingsResponse.GetOngoingPamingListDTO> getProfileAlarm(@CookieValue(name = "token") String token) {

        Long userId = util.getUserId(token, secretKey);

        PamingsResponse.GetOngoingPamingListDTO pamingList = pamingsService.getOngoingPamings(userId);

        log.info("진행 중인 파밍로그 조회: user={}", userId);

        return new ResponseEntity<>(pamingList, HttpStatus.OK);
    }
}
