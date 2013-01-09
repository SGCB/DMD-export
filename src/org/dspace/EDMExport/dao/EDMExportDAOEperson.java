package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.dspace.EDMExport.bo.EDMExportBOUser;
import org.dspace.authorize.AuthorizeException;

public interface EDMExportDAOEperson
{
	public EDMExportBOUser getEperson(String username) throws SQLException, AuthorizeException;
	
	public EDMExportBOUser getEperson(String username, int groupID) throws SQLException, AuthorizeException;

}
