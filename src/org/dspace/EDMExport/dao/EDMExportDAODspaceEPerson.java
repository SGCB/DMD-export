package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.eperson.EPerson;
import org.dspace.core.Context;
import org.dspace.EDMExport.bo.EDMExportBOUser;

public class EDMExportDAODspaceEPerson implements EDMExportDAOEperson
{
	private EPerson eperson;
	private EDMExportBOUser boUser;
	private EDMExportDAOBase edmExportDAOBase;
	private Context context;
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	public EDMExportDAODspaceEPerson(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceEPerson");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
		
	public EDMExportBOUser getEperson(String username) throws SQLException, AuthorizeException
	{
			logger.debug("Looking for user " + username.toLowerCase());
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
				boUser.setUsername(eperson.getEmail());
				boUser.setPassword(eperson.getPasswordHash());
				boUser.setAccess(1);
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
	
}
