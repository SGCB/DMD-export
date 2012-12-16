package org.dspace.EDMExport.bo;

import org.dspace.EDMExport.controller.FilterSearch;
import org.hibernate.validator.constraints.NotEmpty;

public class EDMExportBOSearch
{
	@NotEmpty
	private String term;
	@FilterSearch
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
