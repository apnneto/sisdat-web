/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author juliano
 */
@Constraint(validatedBy = CNPJEntityPropertyValidator.class)
@Target({ ElementType.METHOD,  ElementType.FIELD,  ElementType.ANNOTATION_TYPE,  ElementType.CONSTRUCTOR,  ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
//@ValidationInputMask(pattern="99999999999999")
public @interface CNPJ {

    Class<?>[] groups() default {};
    String message() default "{com.frw.parcerias.web.validation.CNPJ.message}";
    Class<? extends Payload>[] payload() default {};

}
