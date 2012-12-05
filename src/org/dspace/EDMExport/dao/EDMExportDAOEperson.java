package org.dspace.EDMExport.dao;

import org.dspace.EDMExport.bo.EDMExportBOUser;

public interface EDMExportDAOEperson
{
	public EDMExportBOUser getEperson(String username);

}
