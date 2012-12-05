package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.core.Context;

public class EDMExportDAODspaceListCollections implements EDMExportDAOListCollections
{
	private EDMExportDAOBase edmExportDAOBase;
	private Context context;
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	private Collection[] listCol;
	
	public EDMExportDAODspaceListCollections(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceListCollections");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
		
	public Collection[] getListCollections()
	{
		try {
			logger.debug("Looking for list of collections ");
			listCol = Collection.findAll(context);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listCol;
	}
	
}
