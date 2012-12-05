package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.core.Context;

public class EDMExportDAOBase
{

	protected Context context = null;
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	public void init()
    {
		getContext();
        logger.info("Init EDMExportDAOBase");
    }
	
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
