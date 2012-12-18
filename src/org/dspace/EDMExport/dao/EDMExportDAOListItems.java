package org.dspace.EDMExport.dao;

import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.content.Bundle;
import org.dspace.content.Item;

public interface EDMExportDAOListItems
{
	public EDMExportBOListCollections getListCollectionsItem(int id);

	public Item getDSPaceItem(int id);

	public Bundle[] getDSPaceBundleItem(Item item, String type);
}
