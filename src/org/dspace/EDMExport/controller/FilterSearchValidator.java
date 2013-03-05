package org.dspace.EDMExport.controller;

import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;


/**
 * Clase para validar el formulario de búsqueda, necesita el filtro {@link FilterSearch}
 * 
 *
 */

public class FilterSearchValidator  implements ConstraintValidator<FilterSearch, String>
{
	
	/**
	 *  Valores de los campos recogidos de edmexport.properties
	 */
	@Value ("${search.filter.values}")
	private String searchFilterValues;
	
	/**
	 *  Inyección del contexto de la aplicación
	 */
	@Autowired
	private ApplicationContext context;
	
	/**
	 * Mensaje de error si no se valida
	 * 
	 * @return cadena con el mensaje de error
	 */
	public String message()
	{
		return context.getMessage("FilterSearch.EDMExportBOSearch.option", null, Locale.getDefault());
	}


	@Override
    public void initialize(final FilterSearch target)
	{
	}
	
	
	/**
	 * Comprueba que alguno de los elementos esté bien.
	 * 
	 * @param filter cadena con el valor a comprobar
	 * @param context contexto de la validación {@link ConstraintValidatorContext}
	 * @return cierto si el valor existe
	 */
	@Override
	public boolean isValid(final String filter, final ConstraintValidatorContext context)
	{
		if (filter == null || filter.isEmpty()) return true;
        String[] arr = searchFilterValues.split(",");
        for (String value : arr) {
        	if (filter.equalsIgnoreCase(value)) return true;
        }
        return false;
    }

}
