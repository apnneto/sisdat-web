package com.frw.base.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author Carlos Santos
 */
@Constraint(validatedBy = CNPJEntityPropertyValidator.class)
@Target({ ElementType.METHOD,  ElementType.FIELD,  ElementType.ANNOTATION_TYPE,  ElementType.CONSTRUCTOR,  ElementType.PARAMETER })
//@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {

    Class<?>[] groups() default {};
    String message() default "{com.frw.parcerias.web.validation.CPF.message}";
    Class<? extends Payload>[] payload() default {};

}
