package org.dspace.EDMExport.bo;

import org.dspace.EDMExport.bo.EDMExportBOCollection;

public class EDMExportBOListCollections
{

	private EDMExportBOCollection[] listCollections;
	
	public EDMExportBOListCollections()
	{
	}
	
	public EDMExportBOListCollections(EDMExportBOCollection[] listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public void setListCollections(EDMExportBOCollection[] listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public EDMExportBOCollection[] getListCollections()
	{
		return this.listCollections;
	}
	
	public boolean isEmpty()
	{
		return (listCollections == null || listCollections.length == 0);
	}
	
}
