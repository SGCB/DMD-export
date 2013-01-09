package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.dao.EDMExportDAOListCollections;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Value;

public class EDMExportServiceListCollections
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	
	@Value("${list_items.itemspage}")
    private String listItemsPage;
	private int listItemsPageInt;
	
	private EDMExportBOListCollections boListCollections;
	private EDMExportDAOListCollections daoListCollections;
	private EDMExportBOListItems boListIems;
	
	private int hitCount = 0;
	
	public EDMExportServiceListCollections()
	{
		logger.debug("Init EDMExportServiceListCollections");
		boListCollections = new EDMExportBOListCollections();
		boListIems = new EDMExportBOListItems();
	}
	
	public void setBoListCollections(EDMExportBOListCollections boListCollections)
	{
		this.boListCollections = boListCollections;
	}
	
	public EDMExportBOListCollections getListCollection()
	{
		logger.debug("EDMExportServiceListCollections.getListCollection");
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
			logger.debug("Num collections: " + boListCollections.getListCollections().length);
		} else {
			logger.debug("No collections");
			boListCollections = null;
		}
		return boListCollections;
	}
	
	public EDMExportBOListItems getListItems(int offset)
	{
		if (hitCount > 0) {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			int limit = (hitCount < listItemsPageInt)?hitCount:listItemsPageInt;
			return getListItems(offset, limit);
		} else return getListItems(offset, listItemsPageInt);
	}
	
	public EDMExportBOListItems getListItems(int offset, int limit)
	{
		logger.debug("EDMExportServiceListCollections.getListItems");
		listItemsPageInt = Integer.parseInt(listItemsPage);
		if (boListIems.isEmpty()) {
			Set<Item> setItems = new HashSet<Item>();
			
			logger.debug("Num selected coll: " + this.boListCollections.getListCollections().length);
			for (EDMExportBOCollection boColl : this.boListCollections.getListCollections()) {
				Item[] listItemsCol = daoListCollections.getItems(boColl.getId());
				if (listItemsCol != null) {
					for (Item it : listItemsCol) {
						if (!setItems.contains(it)) {
							setItems.add(it);
						}
					}
				}
			}
		
			Item[] listItems = setItems.toArray(new Item[0]);
			if (listItems != null && listItems.length > 0) {
				logger.debug("Num items: " + listItems.length);
				EDMExportBOItem[] listArrayItems = new EDMExportBOItem[listItems.length];
				int i = 0;
				for (Item itemDS : listItems) {
					try {
						EDMExportBOItem col = EDMExportServiceItemDS2ItemBO.itemDS2ItemBO(itemDS, i);
						listArrayItems[i] = col;
						i++;
					} catch (Exception e) {
						logger.debug("item fail " + i, e);
					}
				}
				hitCount = listArrayItems.length;
				logger.debug("Num final items: " + hitCount);
				boListIems.setListItems(listArrayItems);
			} else {
				hitCount = 0;
				logger.debug("No items");
				boListIems.setListItems((List<EDMExportBOItem>) null);
			}
		}
		if (hitCount <= listItemsPageInt) return boListIems;
		if (offset > hitCount) offset = 0;
		EDMExportBOListItems boListIemsPage = new EDMExportBOListItems();
		if (limit > hitCount) limit = hitCount;
		logger.debug("EDMExportServiceListCollections.getListItems Items from "+ offset + " qty " + limit);
		EDMExportBOItem[] listItemsPage = new EDMExportBOItem[limit];
		for (int i = 0; i < limit; i++) {
			listItemsPage[i] = boListIems.getListItems().get(offset + i);
		}
		boListIemsPage.setListItems(listItemsPage);
		return boListIemsPage;
	}
	
	
	public EDMExportBOListItems getBoListItems()
	{
		return boListIems;
	}
	
	public void setBoListIems(EDMExportBOListItems boListItems)
	{
		this.boListIems = boListItems;
	}
	
	public void clearBoListItems()
	{
		boListIems.setListItems((List<EDMExportBOItem>) null);
	}
	
	public int getHitCount()
	{
		return hitCount;
	}
	
	public void setEdmExportDAOListCollections(EDMExportDAOListCollections daoListCollections)
	{
		this.daoListCollections = daoListCollections;
	}

}
