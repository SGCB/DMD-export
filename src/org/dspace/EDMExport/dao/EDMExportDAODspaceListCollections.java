package org.dspace.EDMExport.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
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
			logger.debug("EDMExportDAODspaceListCollections.getListCollections: Looking for list of collections ");
			listCol = Collection.findAll(context);
		} catch (SQLException e) {
			logger.debug("getListCollections", e);
		}
		return listCol;
	}
	
	public Item[] getItems(int id)
	{
		logger.debug("EDMExportDAODspaceListCollections.getItems");
		try {
			ArrayList<Item> listArrayItems = new ArrayList<Item>();
			ItemIterator iter = Collection.find(context, id).getItems();
			while (iter.hasNext()) {
                Item item = iter.next();
				listArrayItems.add(item);
			}
			Item[] itemsArray = new Item[listArrayItems.size()];
			itemsArray = (Item[])listArrayItems.toArray(itemsArray);
			return itemsArray;
		} catch (SQLException e) {
			logger.debug("getItems", e);
		} catch (Exception e) {
			logger.debug("getItems", e);
		}
		return null;
	}
	
}
