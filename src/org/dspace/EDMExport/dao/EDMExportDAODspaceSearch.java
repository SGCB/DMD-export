package org.dspace.EDMExport.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.search.DSQuery;
import org.dspace.search.QueryArgs;
import org.dspace.search.QueryResults;

public class EDMExportDAODspaceSearch implements EDMExportDAOSearch
{
	private EDMExportDAOBase edmExportDAOBase;
	private Context context;
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	private Item[] listItems;
	
	public EDMExportDAODspaceSearch(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceSearch");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
		
	public Item[] getListItems(EDMExportBOSearch searchBO, String searchSubject, String searchAuthor, String searchTitle)
	{
		try {
			logger.debug("Looking for list of items ");
			QueryArgs qArgs = new QueryArgs();
			
			String option = searchBO.getOption();
			if (option != null && !option.isEmpty()) {
				if (option.equals("title")) {
					qArgs.setQuery(searchTitle + ":" + searchBO.getTerm());
				} else if (option.equals("subject")) {
					qArgs.setQuery(searchSubject + ":" + searchBO.getTerm());
				} else if (option.equals("author")) {
					qArgs.setQuery(searchAuthor + ":" + searchBO.getTerm());
				}
			} else {
				qArgs.setQuery(searchBO.getTerm());
			}
			logger.debug("Query " + qArgs.getQuery());
			listItems = null;
			ArrayList<Item> listArrayItems = new ArrayList<Item>();
			QueryResults qResults = null;
			qResults = DSQuery.doQuery(context, qArgs);
			logger.debug("List of dspaceobjects " + qResults.getHitHandles().size());
		    for (int i = 0; i < qResults.getHitHandles().size(); i++) {
		    	String myHandle = (String)qResults.getHitHandles().get(i);
		    	DSpaceObject dso = HandleManager.resolveToObject(context, myHandle);
		    	if (dso != null &&  DSpaceObject.find(context, 2, dso.getID()) != null) {
		    		listArrayItems.add((Item) dso);
		    	}
		    }
		    listItems = new Item[listArrayItems.size()];
		    listItems = (Item[])listArrayItems.toArray(listItems);
		    logger.debug("List of items " + listItems.length);
		} catch (Exception e) {
			logger.debug("Exception in getListItems" , e);
		}
		return listItems;
	}

}
