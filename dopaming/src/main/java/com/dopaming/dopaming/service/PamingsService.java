package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.domain.Users;
import com.dopaming.dopaming.domain.Steps;
import com.dopaming.dopaming.exception.ErrorStatus;
import com.dopaming.dopaming.exception.GeneralException;
import com.dopaming.dopaming.repository.PamingsRepsitory;
import com.dopaming.dopaming.repository.UsersRepository;
import com.dopaming.dopaming.responseDto.PamingsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PamingsService {

    private final PamingsRepsitory pamingsRepsitory;
    private final UsersRepository usersRepository;

    public PamingsResponse.GetOngoingPamingListDTO getOngoingPamings(Long userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Pamings> pamingList = pamingsRepsitory.findAllByUsers(user);

        List<String[]> stepsContentArray = pamingList.stream()
                .map(pamings -> pamings.getSteps().stream()
                        .sorted(Comparator.comparingInt(Steps::getStep))
                        .map(Steps::getContent)
                        .toArray(String[]::new))
                .toList();

        List<PamingsResponse.GetOngoingPamingDTO> pamingDTOList = pamingList.stream()
                .map(pamings -> PamingsResponse.GetOngoingPamingDTO.builder()
                        .paming_title(pamings.getPaming_title())
                        .start_date(pamings.getStart_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .end_date(pamings.getEnd_date().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .remaining_period(getRemainingPeriod(pamings.getEnd_date()))
                        .cleared_step(pamings.getSteps().stream().filter(Steps::isSuccess).count())
                        .unclear_step(pamings.getSteps().stream().filter(step -> !step.isSuccess()).count())
                        .step_contents(stepsContentArray.get(pamingList.indexOf(pamings)))
                        .build())
                .toList();

        return PamingsResponse.GetOngoingPamingListDTO.builder()
                .pamings(pamingDTOList)
                .build();
    }

    private long getRemainingPeriod(LocalDateTime endDate) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(currentDate, endDate);
        return duration.toDays();
    }
}
