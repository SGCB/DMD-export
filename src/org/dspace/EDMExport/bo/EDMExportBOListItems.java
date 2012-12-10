package org.dspace.EDMExport.bo;

import org.dspace.EDMExport.bo.EDMExportBOItem;

public class EDMExportBOListItems
{
	private EDMExportBOItem[] listItems;
	
	public EDMExportBOListItems()
	{
	}
	
	public EDMExportBOListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = listItems;
	}
	
	public void setListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = listItems;
	}
	
	public EDMExportBOItem[] getListItems()
	{
		return this.listItems;
	}
}
