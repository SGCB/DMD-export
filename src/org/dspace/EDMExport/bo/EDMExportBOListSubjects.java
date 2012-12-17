package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

public class EDMExportBOListSubjects
{
	private List<String> listSubjects;
	
	public EDMExportBOListSubjects()
	{
	}
	
	public EDMExportBOListSubjects(List<String> listSubjects)
	{
		this.listSubjects = listSubjects;
	}
	
	public EDMExportBOListSubjects(String[] listSubjects)
	{
		this.listSubjects = Arrays.asList(listSubjects);
	}
	
	public List<String> getListSubjects()
	{
		return listSubjects;
	}
	
	public void setListSubjects(List<String> listSubjects)
	{
		this.listSubjects = listSubjects;
	}
}
