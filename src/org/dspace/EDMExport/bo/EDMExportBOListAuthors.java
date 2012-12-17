package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

public class EDMExportBOListAuthors
{
	
	private List<String> listAuthors;
	
	public EDMExportBOListAuthors()
	{
	}
	
	public EDMExportBOListAuthors(List<String> listAuthors)
	{
		this.listAuthors = listAuthors;
	}
	
	public EDMExportBOListAuthors(String[] listAuthors)
	{
		this.listAuthors = Arrays.asList(listAuthors);
	}
	
	public List<String> getListAuthors()
	{
		return listAuthors;
	}
	
	public void setListAuthors(List<String> listAuthors)
	{
		this.listAuthors = listAuthors;
	}

}
