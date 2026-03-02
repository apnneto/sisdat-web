/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frw.base.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Framework
 */
@Target({ ElementType.METHOD,  ElementType.FIELD,  ElementType.ANNOTATION_TYPE,  ElementType.CONSTRUCTOR,  ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationInputMask {

    boolean acceptsNegative() default false;
    String centsSeparator() default "";
    int maxDecimalDigits() default 0;
    int maxDigits() default 0;
    String pattern() default "";
    String thousandsSeparator() default "";
}
