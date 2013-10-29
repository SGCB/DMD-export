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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;

import org.apache.log4j.Logger;

/**
 * Clase para gestionar la interfaz web de la autenticación a EDMExport
 * Es un controller que mapea la url con path /auth
 *
 */

@Controller
@RequestMapping("/auth")
public class loginController
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Método que mapea la dirección de login.htm mediante método GET
	 * 
	 * @param error parámetro GET opcional que nos indica si ha habido error, se mapea a un boolean
	 * @param model modelo de Spring de la petición.
	 * @return una cadena con la vista a mostrar.
	 */
	
    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public String show(@RequestParam(value="error", required=false) boolean error, 
    		   ModelMap model)
    {
    	logger.debug("Received request to show login page");
    	
    	if (error) {
    		model.put("error", "You have entered an invalid username or password!");
    	}
    	return "login";
    	
    }

}
