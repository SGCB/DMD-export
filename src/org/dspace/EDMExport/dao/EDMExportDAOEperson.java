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

import org.dspace.EDMExport.bo.EDMExportBOUser;
import org.dspace.authorize.AuthorizeException;


/**
 * 
 * Interfaz para la búsqueda de usuarios en Dspace
 * Se ha implementado la de la api de Dspace, pero se podrían desarrollar otras con consultas a la base de datos sin
 * pasar por esta api 
 *
 */

public interface EDMExportDAOEperson
{
	/**
	 * Se obtienen los datos del usuario a partir del login
	 * 
	 * @param username cadena con el login del usuario
	 * @return objeto con los datos del usuario {@link EDMExportBOUser}
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
	public EDMExportBOUser getEperson(String username) throws SQLException, AuthorizeException;
	
	
	/**
	 * Se obtienen los datos del usuario a partir del login y del grupo al que ha de pertenecer
	 * 
	 * @param username cadena con el login del usuario
	 * @param groupID entero con el id del grupo al que ha de pertenecer
	 * @return objeto con los datos del usuario {@link EDMExportBOUser}
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
	public EDMExportBOUser getEperson(String username, int groupID) throws SQLException, AuthorizeException;

}
