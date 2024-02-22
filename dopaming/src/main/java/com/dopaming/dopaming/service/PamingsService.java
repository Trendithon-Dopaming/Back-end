package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.repository.PamingsRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.dopaming.dopaming.domain.PamingSaves;
import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.domain.Steps;
import com.dopaming.dopaming.exception.errorCode.UserErrorCode;
import com.dopaming.dopaming.exception.exception.RestApiException;
import com.dopaming.dopaming.repository.PamingSavesRepsitory;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.responseDto.PamingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PamingsService {

    private final PamingsRepository pamingsRepository;
    private final UsersRepository usersRepository;
    private final PamingSavesRepsitory pamingSavesRepsitory;

    public List<Pamings> getAllPamings(Long userId) {
        return pamingsRepository.findAll();
    }

    public Pamings getPamingById(Long id) {
        return pamingsRepository.findById(id).orElse(null);
    }

    public Pamings createPaming(Pamings pamings) {
        return pamingsRepository.save(pamings);
    }

    public Pamings updatePaming(Long id, Pamings updatePaming) {
        Optional<Pamings> pamingsOptional = pamingsRepository.findById(id);
        if (pamingsOptional.isPresent()) {
            Pamings pamings = pamingsOptional.get();
            pamings.setPaming_title(updatePaming.getPaming_title());
            pamings.setStart_date(updatePaming.getStart_date());
            pamings.setEnd_date(updatePaming.getEnd_date());
            pamings.setInfo(updatePaming.getInfo());
            pamings.setPub_priv(updatePaming.isPub_priv());
            pamings.setCategory(updatePaming.getCategory());
            pamings.setRegion(updatePaming.getRegion());

            return pamingsRepository.save(pamings);
        }

        return null;
    }

    public PamingsResponse.GetOngoingPamingListDTO getOngoingPamings(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INACTIVE_USER));

        List<Pamings> pamingList = pamingsRepository.findAllByUsers(user);
        LocalDateTime currentDate = LocalDateTime.now();

        List<Pamings> ongoingPamings = pamingList.stream()
                .filter(paming -> (paming.getStart_date().isBefore(currentDate) || paming.getStart_date().isEqual(currentDate)) &&
                                (paming.getEnd_date().isAfter(currentDate) || paming.getEnd_date().isEqual(currentDate))
                )
                .toList();

        List<PamingsResponse.GetOngoingPamingDTO> pamingDTOList = ongoingPamings.stream()
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

    public PamingsResponse.GetSavedPamingListDTO getExpiredPamingsV1(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.INACTIVE_USER));
        List<Pamings> pamingList = pamingsRepository.findAllByUsers(user);
        LocalDateTime currentDate = LocalDateTime.now();
        List<Pamings> expiredPamings = pamingList.stream().
                filter(paming -> paming.getEnd_date().isBefore(currentDate))
                .sorted(Comparator.comparing(Pamings::getEnd_date).reversed())
                .limit(3)
                .toList();
        List<PamingsResponse.GetSavedPamingDTO> expiredPamingDTO = expiredPamings.stream()
                .map(paming -> PamingsResponse.GetSavedPamingDTO.builder()
                        .paming_id(paming.getId())
                        .paming_title(paming.getPaming_title())
                        .photo_url(paming.getPhoto_name())
                        .start_date(paming.getStart_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .end_date(paming.getEnd_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .build())
                .collect(Collectors.toList());
        return PamingsResponse.GetSavedPamingListDTO.builder().pamings(expiredPamingDTO).build();
    }

    public PamingsResponse.GetOngoingPamingListDTO getExpiredPamingsV2(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.INACTIVE_USER));

        List<Pamings> pamingList = pamingsRepository.findAllByUsers(user);
        LocalDateTime currentDate = LocalDateTime.now();

        List<Pamings> expiredPamings = pamingList.stream().
                filter(paming -> paming.getEnd_date().isBefore(currentDate))
                .sorted(Comparator.comparing(Pamings::getEnd_date).reversed())
                .toList();

        List<PamingsResponse.GetOngoingPamingDTO> pamingDTOList = expiredPamings.stream()
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
}
