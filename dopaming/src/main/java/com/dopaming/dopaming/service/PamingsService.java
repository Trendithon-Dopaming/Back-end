package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.PamingSaves;
import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.domain.Steps;
import com.dopaming.dopaming.exception.errorCode.UserErrorCode;
import com.dopaming.dopaming.exception.exception.RestApiException;
import com.dopaming.dopaming.repository.PamingSavesRepsitory;
import com.dopaming.dopaming.repository.PamingsRepsitory;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.responseDto.PamingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PamingsService {

    private final PamingsRepsitory pamingsRepsitory;
    private final UsersRepository usersRepository;
    private final PamingSavesRepsitory pamingSavesRepsitory;

    public PamingsResponse.GetOngoingPamingListDTO getOngoingPamings(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INACTIVE_USER));

        List<Pamings> pamingList = pamingsRepsitory.findAllByUsers(user);

        List<PamingsResponse.GetOngoingPamingDTO> pamingDTOList = pamingList.stream()
                .map(pamings -> {
                    List<Steps> stepsList = pamings.getSteps();

                    List<PamingsResponse.GetStepDTO> stepsDTOList = stepsList.stream()
                            .map(step -> PamingsResponse.GetStepDTO.builder()
                                    .step(step.getStep())
                                    .content(step.getContent())
                                    .success(step.isSuccess())
                                    .build())
                            .collect(Collectors.toList());

                    return PamingsResponse.GetOngoingPamingDTO.builder()
                            .paming_id(pamings.getId())
                            .paming_title(pamings.getPaming_title())
                            .category(pamings.getCategory())
                            .photo_url(pamings.getPhoto_name())
                            .start_date(pamings.getStart_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .end_date(pamings.getEnd_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .remaining_period(getRemainingPeriod(pamings.getEnd_date()))
                            .cleared_step(pamings.getSteps().stream().filter(Steps::isSuccess).count())
                            .unclear_step(pamings.getSteps().stream().filter(step -> !step.isSuccess()).count())
                            .steps(stepsDTOList)
                            .build();
                })
                .collect(Collectors.toList());

        return PamingsResponse.GetOngoingPamingListDTO.builder()
                .pamings(pamingDTOList)
                .build();
    }

    private long getRemainingPeriod(LocalDateTime endDate) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(currentDate, endDate);
        return duration.toDays();
    }

    public Page<PamingsResponse.GetSavedPamingListDTO> getSavedPamings(Long userId, int pageNumber, int pageSize) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INACTIVE_USER));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PamingSaves> pamingSavePage = pamingSavesRepsitory.findAllByUsers(user, pageable);

        List<PamingsResponse.GetSavedPamingDTO> pamingDTOList = pamingSavePage.getContent().stream()
                .map(pamingSave -> {
                    Pamings pamings = pamingSave.getPamings();
                    return PamingsResponse.GetSavedPamingDTO.builder()
                            .paming_id(pamings.getId())
                            .paming_title(pamings.getPaming_title())
                            .photo_url(pamings.getPhoto_name())
                            .start_date(pamings.getStart_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .end_date(pamings.getEnd_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(Collections.singletonList(PamingsResponse.GetSavedPamingListDTO.builder()
                .pamings(pamingDTOList)
                .build()), pageable, pamingSavePage.getTotalElements());
    }
}
