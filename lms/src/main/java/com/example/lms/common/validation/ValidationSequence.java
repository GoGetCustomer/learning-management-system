package com.example.lms.common.validation;

import jakarta.validation.GroupSequence;

import static com.example.lms.common.validation.ValidationGroups.*;

@GroupSequence({
        NotBlankGroup.class,
        NotNullGroup.class,
        PatternGroup.class,
        SizeGroup.class,
        EmailGroup.class
})
public interface ValidationSequence {
}
