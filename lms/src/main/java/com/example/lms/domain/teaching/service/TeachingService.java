package com.example.lms.domain.teaching.service;

import com.example.lms.domain.teaching.dto.TeachingResponseDto;
import com.example.lms.domain.teaching.entity.Teaching;
import com.example.lms.domain.teaching.repository.TeachingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeachingService {

    private final TeachingRepository teachingRepository;

    public Page<TeachingResponseDto> findAllTeaching(int page) {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Page<Teaching> teachingPage = teachingRepository.findPageByInstructorId(id, PageRequest.of(page - 1, 10));

        return teachingPage.map(teaching -> TeachingResponseDto.builder()
                .id(teaching.getId())
                .instructorName(teaching.getInstructor().getName())
                .instructorEmail(teaching.getInstructor().getEmail())
                .courseId(teaching.getCourse().getId())
                .courseTitle(teaching.getCourse().getCourseTitle())
                .courseDescription(teaching.getCourse().getCourseDescription())
                .startDate(teaching.getCourse().getStartDate())
                .endDate(teaching.getCourse().getEndDate())
                .build()
        );
    }

    @Transactional
    public void delete(Long id) {
        Long instructorId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!teachingRepository.existsByInstructorIdAndTeachingId(instructorId, id)) {
            throw new IllegalArgumentException("수업 생성을 한 강사만 삭제를 진행 할 수 있습니다.");
        }
        teachingRepository.deleteById(id);
    }
}
