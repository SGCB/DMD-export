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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


/**
 * 
 * Interfaz que dice que la validación es de la clase {@link FilterSearchValidator}, por campos y en tiempo de ejecución
 *
 */

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
