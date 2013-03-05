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
