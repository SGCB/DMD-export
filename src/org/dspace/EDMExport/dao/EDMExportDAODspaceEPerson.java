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
		boUser = new EDMExportBOUser();
	}
		
	public EDMExportBOUser getEperson(String username)
	{
		try {
			logger.debug("Looking for user " + username.toLowerCase());
			eperson = EPerson.findByEmail(context, username.toLowerCase());
			if (eperson != null && eperson.canLogIn() && !eperson.getRequireCertificate()) {
				boUser.setUsername(eperson.getEmail());
				boUser.setPassword(eperson.getPasswordHash());
				boUser.setAccess(1);
			} else {
				if (eperson == null) logger.debug("User unexistent");
				else if (!eperson.canLogIn()) logger.debug("User can't login");
				else if (eperson.getRequireCertificate()) logger.debug("User require certificate");
				boUser = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (AuthorizeException e) {
			e.printStackTrace();
		}
		return boUser;
	}
	
}
