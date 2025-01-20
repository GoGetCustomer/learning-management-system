package com.example.lms.common.config;

import com.example.lms.common.auth.dto.CustomUserDetails;
import com.example.lms.common.fixture.InstructorFixture;
import com.example.lms.common.fixture.StudentFixture;
import com.example.lms.domain.user.entity.User;
import com.example.lms.domain.user.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustom> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustom customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User mockUser;
        if (Role.STUDENT.getAuthority().equals(customUser.role())) {
            mockUser = StudentFixture.STUDENT_FIXTURE_1.createStudent();
        } else if (Role.INSTRUCTOR.getAuthority().equals(customUser.role())) {
            mockUser = InstructorFixture.INSTRUCTOR_FIXTURE_1.createInstructor();
        } else {
            throw new IllegalArgumentException("존재하지 않는 권한 : " + customUser.role());
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(mockUser, customUser.id()), null, List.of(new SimpleGrantedAuthority(customUser.role())));
        context.setAuthentication(authentication);

        return context;
    }
}
