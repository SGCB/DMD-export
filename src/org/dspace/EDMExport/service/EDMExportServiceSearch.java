package org.dspace.EDMExport.service;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.EDMExport.dao.EDMExportDAOSearch;
import org.dspace.content.Item;

import org.springframework.beans.factory.annotation.Value;

public class EDMExportServiceSearch
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	@Value("${search.subject.index.solr}")
    private String searchSubject;
	
	@Value("${search.author.index.solr}")
    private String searchAuthor;
	
	@Value("${search.title.index.solr}")
    private String searchTitle;
		
	@Value("${list_items.itemspage}")
    private String searchItemsPage;
	
	@Value("${search.sortby}")
    private String searchSortBy;
	
	@Value("${search.sortorder}")
    private String searchOrder;
	
		
	private EDMExportBOListItems boListIems;
	private EDMExportBOSearch searchBO;
	private EDMExportDAOSearch daoSearch;
	private int hitCount = 0;
	
	public EDMExportServiceSearch()
	{
		logger.debug("Init EDMExportServiceSearch");
		boListIems = new EDMExportBOListItems();
	}
	
	public void setSearchBO(EDMExportBOSearch searchBO)
	{
		this.searchBO = searchBO;
	}
	
	public EDMExportBOListItems getListItems(int offset)
	{
		if (hitCount > 0) {
			int searchItemsPageInt = Integer.parseInt(searchItemsPage);
			int limit = (hitCount < searchItemsPageInt)?hitCount:searchItemsPageInt;
			return getListItems(offset, limit);
		} else return getListItems(offset, 0);
	}
	
	public EDMExportBOListItems getListItems(int offset, int limit)
	{
		if (limit == 0) limit = Integer.parseInt(searchItemsPage);
		Item[] listItems = daoSearch.getListItems(searchBO, searchSubject, searchAuthor, searchTitle, searchSortBy, searchOrder, offset, limit);
		this.hitCount = daoSearch.getHitCount();
		if (listItems != null && listItems.length > 0) {
			logger.debug("Num items: " + listItems.length);
			EDMExportBOItem[] listArrayItems = new EDMExportBOItem[listItems.length];
			int i = 0;
			for (Item itemDS : listItems) {
				try {
					EDMExportBOItem it = EDMExportServiceItemDS2ItemBO.itemDS2ItemBO(itemDS, i);
					listArrayItems[i] = it;
					i++;
				} catch (Exception e) {
					logger.debug("item fail " + i, e);
				}
			}
			boListIems.setListItems(listArrayItems);
		} else {
			this.hitCount = 0;
			logger.debug("No items");
			boListIems.setListItems(null);
		}
		return boListIems;
	}
	
	public int getHitCount()
	{
		return hitCount;
	}
	
	public void setBoListIems(EDMExportBOListItems boListItems)
	{
		this.boListIems = boListItems;
	}
	
	public EDMExportBOListItems getBoListItems()
	{
		return boListIems;
	}
	
	public void clearBoListItems()
	{
		boListIems.setListItems(null);
	}
	
	
	public void setEdmExportDAOSearch(EDMExportDAOSearch daoSearch)
	{
		this.daoSearch = daoSearch;
	}


}
