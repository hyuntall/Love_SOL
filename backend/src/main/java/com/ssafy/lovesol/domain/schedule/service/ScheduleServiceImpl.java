package com.ssafy.lovesol.domain.schedule.service;

import com.ssafy.lovesol.domain.couple.entity.Couple;
import com.ssafy.lovesol.domain.couple.repository.CoupleRepository;
import com.ssafy.lovesol.domain.schedule.dto.request.CreateScheduleRequestDto;
import com.ssafy.lovesol.domain.schedule.entity.Schedule;
import com.ssafy.lovesol.domain.schedule.entity.ScheduleType;
import com.ssafy.lovesol.domain.schedule.repository.ScheduleRepository;
import com.ssafy.lovesol.domain.user.entity.User;
import com.ssafy.lovesol.domain.user.repository.UserRepository;
import com.ssafy.lovesol.global.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final CoupleRepository coupleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public Long CreateSchedule(Long coupleId, CreateScheduleRequestDto createScheduleRequestDto ,
                               HttpServletRequest request
                               ) {
        log.info("ScheduleServiceImpl_CreateSchedule | 일정 작성");
        Couple couple = coupleRepository.findById(coupleId).get();
        String loginId = jwtService.extractUserLoginIdFromAccessToken(request.getHeader("Authorization").split(" ")[1]);
        User user = userRepository.findByLoginId(loginId).get();
        Schedule schedule = createScheduleRequestDto.toScheduleEntity(couple, getScheduleType(couple, user, createScheduleRequestDto.getScheduleType()));
        return scheduleRepository.save(schedule).getScheduleId();
    }

}
