package com.example.lms.domain.course.entity;

import com.example.lms.domain.instructor.entity.Instructor;
import com.example.lms.domain.teaching.entity.Teaching;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.example.lms.common.fixture.CourseFixture.COURSE_FIXTURE_1;
import static com.example.lms.common.fixture.CourseFixture.COURSE_FIXTURE_2;
import static com.example.lms.common.fixture.InstructorFixture.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseTest {
    @Test
    void testBelongsToInstructor() {
        // given
        Instructor instructor1 = INSTRUCTOR_FIXTURE_1.createInstructor();
        Instructor instructor2 = INSTRUCTOR_FIXTURE_2.createInstructor();
        Course course1 = COURSE_FIXTURE_1.createCourse();
        Course course2 = COURSE_FIXTURE_2.createCourse();

        //when
        Teaching teaching1 = Teaching.of(instructor1, course1);

        Teaching teaching2 = Teaching.of(instructor2,course2);

        // then
        assertTrue(course1.belongsToInstructor(instructor1)); // instructor1이 포함됨
        assertTrue(course2.belongsToInstructor(instructor2)); // instructor2도 포함됨

        Instructor otherInstructor = INSTRUCTOR_FIXTURE_3.createInstructor();
        assertFalse(course1.belongsToInstructor(otherInstructor));
    }
}
