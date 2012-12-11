package org.dspace.EDMExport.dao;

import org.dspace.content.Collection;
import org.dspace.content.Item;

public interface EDMExportDAOListCollections
{
	public Collection[] getListCollections();
	
	public Item[] getItems(int id);
}
