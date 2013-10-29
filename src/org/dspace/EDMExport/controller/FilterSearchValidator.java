/**
 *  Copyright 2013 Spanish Minister of Education, Culture and Sport
 *  
 *  written by MasMedios
 *  
 *  Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 *  
 *  You may not use this work  except in compliance with the License. You may obtain a copy of the License at:
 *  
 *  http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" basis,
 *  
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 */

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
