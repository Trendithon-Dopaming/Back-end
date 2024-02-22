package com.dopaming.dopaming.controller;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.service.PamingsService;
import com.dopaming.dopaming.responseDto.PamingsResponse;
import com.dopaming.dopaming.security.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pamings")
@Slf4j
public class PamingsController {

    @Value("${jwt.secret}")
    private String secretKey;
    private final Util util;

    private final PamingsService pamingsService;

    //@Autowired
    //private PamingsService pamingsService;

    @GetMapping
    public ResponseEntity<List<Pamings>> getAllPamings(@CookieValue(name = "token") String token) {
        Long userId = util.getUserId(token, secretKey);
        List<Pamings> userPamings = pamingsService.getAllPamings(userId);
        return new ResponseEntity<>(userPamings, HttpStatus.OK);
    }

    @GetMapping("/{paming_id}")
    public ResponseEntity<Pamings> getPamingById(@PathVariable("paming_id") Long paming_id,
                                                 @CookieValue(name = "token") String token) {
        Long userId = util.getUserId(token, secretKey);
        Pamings pamings = pamingsService.getPamingById(paming_id);
        if (pamings != null && pamings.getUsers().equals(userId)) {
            return new ResponseEntity<>(pamings, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pamings")
    public ResponseEntity<Pamings> createPaming(@RequestBody Pamings pamings,
                                                @CookieValue(name = "token") String token) {
        Long userId = util.getUserId(token, secretKey);
        Users users = new Users();
        pamings.setUsers(users);
        Pamings createdPaming = pamingsService.createPaming(pamings);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaming);
    }

    @PutMapping("/{paming_id}")
    public ResponseEntity<Pamings> updatePaming(@PathVariable("paming_id") Long paming_id,
                                                @RequestBody Pamings updatedPaming,
                                                @CookieValue(name = "token") String token) {
        Long userId = util.getUserId(token, secretKey);
        Pamings pamings = pamingsService.getPamingById(paming_id);
        if (pamings != null && pamings.getUsers().equals(userId)) {
            Pamings updatedPamings = pamingsService.updatePaming(paming_id, updatedPaming);
            return ResponseEntity.ok(updatedPamings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        /**
         * [GET] /pamings/ongoing
         * 진행 중인 파밍로그 조회
         *
         * @param token 토큰
         * @return
         */
        @GetMapping("/ongoing")
        public ResponseEntity<PamingsResponse.GetOngoingPamingListDTO> getOngoing (@CookieValue(name = "token") String
        token){

            Long userId = util.getUserId(token, secretKey);

            PamingsResponse.GetOngoingPamingListDTO pamingList = pamingsService.getOngoingPamings(userId);

            log.info("진행 중인 파밍로그 조회: user={}", userId);

            return new ResponseEntity<>(pamingList, HttpStatus.OK);
        }

        /**
         * [GET] /pamings/saved
         * 저장 팜 조회
         *
         * @param token 토큰
         * @return
         */
        @GetMapping("/saved")
        public ResponseEntity<Page<PamingsResponse.GetSavedPamingListDTO>> getSaved (@CookieValue(name = "token") String
        token,
        @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "12") int pageSize){

            Long userId = util.getUserId(token, secretKey);

            Page<PamingsResponse.GetSavedPamingListDTO> pamingList = pamingsService.getSavedPamings(userId, pageNumber, pageSize);

            log.info("저장 팜 조회: user={}, pageNumber={}, pageSize={}", userId, pageNumber, pageSize);

            return new ResponseEntity<>(pamingList, HttpStatus.OK);
        }

    }
