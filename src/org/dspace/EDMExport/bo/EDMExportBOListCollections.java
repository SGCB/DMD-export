package org.dspace.EDMExport.bo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dspace.EDMExport.bo.EDMExportBOCollection;

public class EDMExportBOListCollections
{
	@NotNull
	@Valid
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
