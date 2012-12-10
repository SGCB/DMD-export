package org.dspace.EDMExport.bo;

public class EDMExportBOSearch
{
	private String term;
	private String option;
	
	
	public EDMExportBOSearch()
	{
	}
	
	public EDMExportBOSearch(String term, String option)
	{
		this.term = term;
		this.option = option;
	}
	
	public void setTerm(String term)
	{
		this.term = term;
	}
	
	public String getTerm()
	{
		return this.term;
	}
	
	public void setOption(String option)
	{
		this.option = option;
	}
	
	public String getOption()
	{
		return this.option;
	}
}
