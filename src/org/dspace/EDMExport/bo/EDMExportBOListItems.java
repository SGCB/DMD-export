package org.dspace.EDMExport.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dspace.EDMExport.bo.EDMExportBOItem;

public class EDMExportBOListItems
{
	@NotNull
	@Valid
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
		this.listItems = new ArrayList<EDMExportBOItem>(Arrays.asList(listItems));
	}
	
	public void setListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = new ArrayList<EDMExportBOItem>(Arrays.asList(listItems));
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
