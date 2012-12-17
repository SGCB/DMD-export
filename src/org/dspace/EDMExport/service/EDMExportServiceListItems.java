package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.dao.EDMExportDAOListItems;


public class EDMExportServiceListItems
{
	protected static Logger logger = Logger.getLogger("edmexport");

	private Map<Integer, EDMExportBOItem> mapItemsSubmit;
	
	private EDMExportDAOListItems daoListItems;
	
	public EDMExportServiceListItems()
	{
		logger.debug("Init EDMExportServiceListItems");
		this.mapItemsSubmit = new ConcurrentHashMap<Integer, EDMExportBOItem>();
	}
	
	public void clearMapItemsSubmit()
	{
		mapItemsSubmit.clear();
	}
	
	
	public EDMExportBOItem searchEDMExportBOItemFromId(EDMExportBOListItems boListItems, int id)
	{
		for (EDMExportBOItem boItem : boListItems.getListItems()) {
			if (boItem.getId() == id) return boItem;
		}
		return null;
	}
	
	public synchronized void processEDMExportBOItemsChecked(EDMExportBOListItems boListItems, String[] checked)
	{
		try {
			for (String idStr : checked) {
				int id = Integer.parseInt(idStr);
				if (!mapItemsSubmit.containsKey(id)) {
					EDMExportBOItem item = searchEDMExportBOItemFromId(boListItems, id);
					addItem(id, item);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception EDMExportServiceListItems.processEDMExportBOItemsChecked" ,e);
		}
	}
	
	public synchronized void processEDMExportBOItemsNoChecked(EDMExportBOListItems boListItems, String[] nochecked)
	{
		try {
			for (String idStr : nochecked) {
				int id = Integer.parseInt(idStr);
				if (mapItemsSubmit.containsKey(id)) {
					EDMExportBOItem item = searchEDMExportBOItemFromId(boListItems, id);
					removeItem(id, item);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception EDMExportServiceListItems.processEDMExportBOItemsNoChecked" ,e);
		}
	}
	
	public synchronized void processEDMExportBOListItems(EDMExportBOListItems boListItems)
	{
		for (EDMExportBOItem item : boListItems.getListItems()) {
			if (mapItemsSubmit.containsKey(item.getId())) {
				item.setChecked(true);
			} else {
				item.setChecked(false);
			}
		}
	}
	
	
	public synchronized boolean containsEDMExportBOItem(int id)
	{
		return mapItemsSubmit.containsKey(id);
	}
	
	
	public Map<Integer, EDMExportBOItem> getMapItemsSubmit()
	{
		return this.mapItemsSubmit;
	}
	
	public synchronized void addItem(int id, EDMExportBOItem item)
	{
		if (!mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.put(id, item);
			item.setChecked(true);
			logger.debug("Added item " + id);
		}
	}
	
	public synchronized void removeItem(int id, EDMExportBOItem item)
	{
		if (mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.remove(id);
			item.setChecked(false);
			logger.debug("Removed item " + id);
		}
	}
	
	public List<String> getListCollections()
	{
		List<String> listCollections = new ArrayList<String>();
		Set<Integer> setIdCollections = new HashSet<Integer>();
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			EDMExportBOListCollections boListCollections = item.getListCollections(); 
			if (boListCollections == null || boListCollections.isEmpty()) daoListItems.getListCollectionsItem(item.getId());
			if (boListCollections != null) {
				for (EDMExportBOCollection coll : boListCollections.getListCollections()) {
					if (!setIdCollections.contains(coll.getId())) {
						listCollections.add(coll.getName() + " (" + coll.getHandle() + ")");
						setIdCollections.add(coll.getId());
					}
				}
			}
		}
		Collections.sort(listCollections);
		return listCollections;
	}
	
	public void setEdmExportDAOListItems(EDMExportDAOListItems daoListItems)
	{
		this.daoListItems = daoListItems;
	}
}
