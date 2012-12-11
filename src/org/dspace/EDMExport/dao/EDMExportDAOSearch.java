package org.dspace.EDMExport.dao;

import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.content.Item;

public interface EDMExportDAOSearch
{
	Item[] getListItems(EDMExportBOSearch searchBO, String searchSubject, String searchAuthor, String searchTitle, String searchSortBy, String searchOrder, int offset, int searchItemsPage);
	
	int getHitCount();
}
