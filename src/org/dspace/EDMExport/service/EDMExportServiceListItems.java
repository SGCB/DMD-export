package org.dspace.EDMExport.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOItem;


public class EDMExportServiceListItems
{
	protected static Logger logger = Logger.getLogger("edmexport");

	private Map<Integer, EDMExportBOItem> mapItemsSubmit;
	
	public EDMExportServiceListItems()
	{
		logger.debug("Init EDMExportServiceListItems");
		this.mapItemsSubmit = new ConcurrentHashMap<Integer, EDMExportBOItem>();
	}
	
	
	public Map<Integer, EDMExportBOItem> getMapItemsSubmit()
	{
		return this.mapItemsSubmit;
	}
	
	public synchronized void addItem(int id, EDMExportBOItem item)
	{
		if (!mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.put(id, item);
		}
	}
	
	public synchronized void removeItem(int id)
	{
		if (mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.remove(id);
		}
	}
}
