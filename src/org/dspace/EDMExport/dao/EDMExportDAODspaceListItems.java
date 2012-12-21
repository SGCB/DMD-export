package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.core.Context;

public class EDMExportDAODspaceListItems implements EDMExportDAOListItems
{
	protected static Logger logger = Logger.getLogger("edmexport");
	private Context context;
	private EDMExportDAOBase edmExportDAOBase;

	public EDMExportDAODspaceListItems(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceListItems");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}

	@Override
	public EDMExportBOListCollections getListCollectionsItem(int id)
	{
		logger.debug("EDMExportDAODspaceListItems.getListCollectionsItem");
		try {
			Item item = Item.find(context, id);
			Collection[] listDspaceCollection = item.getCollections();
			EDMExportBOCollection[] listCollections = new EDMExportBOCollection[listDspaceCollection.length];
			int i = 0;
			for (Collection coll : listDspaceCollection) {
				listCollections[i] = new EDMExportBOCollection(coll.getName(), coll.getHandle(), coll.getID(), i);
				i++;
			}
			EDMExportBOListCollections edmExportBoListCollections = new EDMExportBOListCollections(listCollections);
			return edmExportBoListCollections;
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getListCollectionsItem", e);
		} catch (Exception e) {
			logger.debug("EDMExportBOListCollections.getListCollectionsItem", e);
		}
		return null;
	}

	@Override
	public Item getDSPaceItem(int id)
	{
		logger.debug("EDMExportDAODspaceListItems.getDSPaceItem");
		try {
			return Item.find(context, id);
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getDSPaceItem", e);
		}
		return null;
	}
	
	public Bundle[] getDSPaceBundleItem(Item item, String type)
	{
		logger.debug("EDMExportDAODspaceListItems.getDSPaceBundleItem");
		try {
			return item.getBundles(type);
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getDSPaceBundleItem", e);
		}
		return null;
	}

}
