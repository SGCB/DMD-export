package org.dspace.EDMExport.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.dao.EDMExportDAOListCollections;
import org.dspace.content.Collection;

public class EDMExportServiceListCollections
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	private EDMExportBOListCollections boListCollections;
	private EDMExportDAOListCollections daoListCollections;
	
	public EDMExportServiceListCollections()
	{
		logger.debug("Init EDMExportServiceListCollections");
		boListCollections = new EDMExportBOListCollections();
	}
	
	public EDMExportBOListCollections getListCollection()
	{
		Collection[] listCol = daoListCollections.getListCollections();
		if (listCol != null && listCol.length > 0) {
			logger.debug("Num collections: " + listCol.length);
			ArrayList<EDMExportBOCollection> listArrayCols = new ArrayList<EDMExportBOCollection>();
			int i = 0;
			for (Collection colDS : listCol) {
				EDMExportBOCollection col = new EDMExportBOCollection(colDS.getName(), colDS.getHandle(), colDS.getID(), i);
				listArrayCols.add(col);
				i++;
			}
			EDMExportBOCollection[] collectionArray = new EDMExportBOCollection[listArrayCols.size()];
			collectionArray = (EDMExportBOCollection[])listArrayCols.toArray(collectionArray);
			boListCollections.setListCollections(collectionArray);
		} else {
			logger.debug("No collections");
			boListCollections = null;
		}
		return boListCollections;
	}
	
	public void setEdmExportDAOListCollections(EDMExportDAOListCollections daoListCollections)
	{
		this.daoListCollections = daoListCollections;
	}

}
