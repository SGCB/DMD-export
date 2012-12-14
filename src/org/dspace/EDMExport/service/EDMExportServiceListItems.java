package org.dspace.EDMExport.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListItems;


public class EDMExportServiceListItems
{
	protected static Logger logger = Logger.getLogger("edmexport");

	private Map<Integer, EDMExportBOItem> mapItemsSubmit;
	
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
}
