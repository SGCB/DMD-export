package org.dspace.EDMExport.controller;

import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

public class FilterSearchValidator  implements ConstraintValidator<FilterSearch, String>
{
	
	@Value ("${search.filter.values}")
	private String searchFilterValues;
	
	@Autowired
	private ApplicationContext context;
	
	public String message()
	{
		return context.getMessage("FilterSearch.EDMExportBOSearch.option", null, Locale.getDefault());
	}


	@Override
    public void initialize(final FilterSearch target)
	{
	}
	
	@Override
	public boolean isValid(final String filter, final ConstraintValidatorContext context)
	{
        String[] arr = searchFilterValues.split(",");
        for (String value : arr) {
        	if (filter.equalsIgnoreCase(value)) return true;
        }
        return false;
    }

}
