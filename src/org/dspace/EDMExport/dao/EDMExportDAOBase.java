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

package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.core.Context;


/**
 * 
 * Clase base de acceso a la base de datos de Dspace
 *
 */

public class EDMExportDAOBase
{

	/**
	 * Contexto de Dspace, en él está el acceso a base de datos. Es requerio por la API de Dspace
	 */
	protected Context context = null;
	
	/**
	 * logar eventos de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Inicializamos el context. Llamado desde EDMExport-data.xml
	 */
	public void init()
    {
		getContext();
        logger.info("Init EDMExportDAOBase");
    }
	
	
	/**
	 * Constructor para inicializar el context
	 */
	public void EDMExportServiceBase()
	{
		if (context == null) {
	        try {
	            context = new Context();
	            logger.debug("Dspace context created");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
	}
	

	/**
	 * Inicializa el context y lo devuelve
	 * 
	 * @return context: contexto de Dspace {@link Context}
	 */
	protected Context getContext()
	{
		if (context == null) {
			try {
	            context = new Context();
	            logger.debug("Dspace context created");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
			
		}
		return context;
	}
		

}
