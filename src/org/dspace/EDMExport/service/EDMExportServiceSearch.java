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
		
	private EDMExportBOListItems boListIems;
	private EDMExportDAOSearch daoSearch;
	
	public EDMExportServiceSearch()
	{
		logger.debug("Init EDMExportServiceSearch");
		boListIems = new EDMExportBOListItems();
	}
	
	
	public EDMExportBOListItems getListItems(EDMExportBOSearch searchBO)
	{
		Item[] listItems = daoSearch.getListItems(searchBO, searchSubject, searchAuthor, searchTitle);
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
			boListIems.setListItems(listArrayItems);
		} else {
			logger.debug("No items");
			boListIems.setListItems(null);
		}
		return boListIems;
	}
	
	
	public void setEdmExportDAOSearch(EDMExportDAOSearch daoSearch)
	{
		this.daoSearch = daoSearch;
	}


}
