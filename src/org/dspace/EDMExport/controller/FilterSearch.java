package org.dspace.EDMExport.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Documented
@Constraint(validatedBy = FilterSearchValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface FilterSearch
{
	
	public abstract String message() default "Error search filter";
	
	public abstract Class[] groups() default {};
	public abstract Class[] payload() default {};

}