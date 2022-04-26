package com.springBatch.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AgeValidator.class)
public @interface Age {
	
	int lower() default 18;
	int upper() default 65;
	String message() default "Age should be between 18-65";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
