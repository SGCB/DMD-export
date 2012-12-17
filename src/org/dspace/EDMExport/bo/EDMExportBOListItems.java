package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import org.dspace.EDMExport.bo.EDMExportBOItem;

public class EDMExportBOListItems
{
	private List<EDMExportBOItem> listItems;
	
	public EDMExportBOListItems()
	{
	}
	
	
	public EDMExportBOListItems(List<EDMExportBOItem> listItems)
	{
		this.listItems = listItems;
	}
	
	public EDMExportBOListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = Arrays.asList(listItems);
	}
	
	public void setListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = Arrays.asList(listItems);
	}
	
	public void setListItems(List<EDMExportBOItem> listItems)
	{
		this.listItems = listItems;
	}
	
	
	public List<EDMExportBOItem> getListItems()
	{
		return this.listItems;
	}
	
	
	public boolean isEmpty()
	{
		return (listItems == null || listItems.size() == 0);
	}
	
}
