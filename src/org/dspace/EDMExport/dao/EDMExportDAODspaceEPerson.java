package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.core.Context;
import org.dspace.EDMExport.bo.EDMExportBOUser;

/**
 * 
 * Clase para realizar consultas desde la API de Dspace sobre los usuarios {@link EPerson}
 * Implementa la interfaz para la consulta a los usuarios {@link EDMExportDAOEperson}
 * 
 * Se comprueba que un usuario exista en la base de datos de Dspace, tenga permisos
 * para valdiarse y que pertenezca al grupo suministrado.
 *
 */

public class EDMExportDAODspaceEPerson implements EDMExportDAOEperson
{
	/**
	 * Objeto EPerson de Dspace {@link EPerson} 
	 */
	private EPerson eperson;
	
	/**
	 * Objeto con los datos del usuario {@link EDMExportBOUser}
	 */
	private EDMExportBOUser boUser;
	
	/**
	 * Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	private EDMExportDAOBase edmExportDAOBase;
	
	/**
	 * contexto de Dspace {@link Context}
	 */
	private Context context;
	
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	
	/**
	 * Constructor donde inicializamos contexto
	 * 
	 * @param edmExportDAOBase Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	public EDMExportDAODspaceEPerson(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceEPerson");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
	
	
	/**
	 * 
	 * Se obtienen los datos del usuario a partir del login
	 * 
	 * @param username cadena con el login del usuario
	 * @return objeto con los datos del usuario {@link EDMExportBOUser}
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
	
	public EDMExportBOUser getEperson(String username) throws SQLException, AuthorizeException
	{
		boUser = null;
		try {
			boUser = getEperson(username, -1);
		} catch (SQLException se) {
			throw new SQLException(se.getMessage());
		} catch (Exception e) {
			throw new AuthorizeException(e.getMessage());
		}
		return boUser;
	}
		
	
	/**
	 * 
	 * Se obtienen los datos del usuario a partir del login y del grupo al que ha de pertenecer
	 * 
	 * @param username cadena con el login del usuario
	 * @param groupID entero con el id del grupo al que ha de pertenecer
	 * @return objeto con los datos del usuario {@link EDMExportBOUser}
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
	public EDMExportBOUser getEperson(String username, int groupID) throws SQLException, AuthorizeException
	{
		logger.debug("EDMExportDAODspaceEPerson.getEperson Looking for user " + username.toLowerCase() + " in group " + groupID);
		boUser = null;
		try {
			boUser = new EDMExportBOUser();
			eperson = EPerson.findByEmail(context, username.toLowerCase());
		} catch (SQLException se) {
			logger.debug(se.getMessage());
			throw new SQLException(se.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new AuthorizeException(e.getMessage());
		}
		if (eperson != null && eperson.canLogIn() && !eperson.getRequireCertificate()) {
			logger.debug("User canLogin and not RequireCertificate");
			// si no hay grupo, recogemos los datos
			if (groupID < 0) {
				boUser.setUsername(eperson.getEmail());
				boUser.setPassword(eperson.getPasswordHash());
				boUser.setAccess(1);
			// comprobamos que pertenezca al grupo
			} else {
				try {
					if (isAdmin(eperson, groupID)) {
						logger.debug("Member of group " + groupID);
						boUser.setUsername(eperson.getEmail());
						boUser.setPassword(eperson.getPasswordHash());
						boUser.setAccess(1);
					} else {
						throw new AuthorizeException("No member of group " + groupID);
					}
				} catch (SQLException se) {
					logger.debug(se.getMessage());
					throw new SQLException(se.getMessage());
				} catch (Exception e) {
					logger.debug(e.getMessage());
					throw new AuthorizeException(e.getMessage());
				}
			}
		// no posee los requisitos adecuados para validarse el usuario suministrado
		} else {
			String mess = "";
			if (eperson == null) {
				mess = "User unexistent";
			}
			else if (!eperson.canLogIn()) {
				mess = "User can't login";
			}
			else if (eperson.getRequireCertificate()) {
				mess = "User require certificate";
			} else mess = "Unknown error";
			logger.debug(mess);
			boUser = null;
			throw new AuthorizeException(mess);
		}
		return boUser;
	}
	
	/**
	 * comprobar si el usuario de Dspace pertenece al grupo
	 * 
	 * @param eperson objeto Dspace con el usuario {@link EPerson}
	 * @param groupID entero con el id del grupo al que ha de pertenecer
	 * @return cierto si pertenece al grupo
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
	private boolean isAdmin(EPerson eperson, int groupID) throws SQLException, AuthorizeException
	{
		try {
			Group group = Group.find(context, groupID);
			if (group.isMember(eperson)) return true;
			else logger.debug(eperson.getEmail() + " no member of " + group.getName());
		} catch (SQLException se) {
			logger.debug(se.getMessage());
			throw new SQLException(se.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new AuthorizeException(e.getMessage());
		}
		return false;
	}
	
}
