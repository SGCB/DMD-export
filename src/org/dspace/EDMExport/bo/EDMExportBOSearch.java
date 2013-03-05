package org.dspace.EDMExport.bo;

import org.dspace.EDMExport.controller.FilterSearch;
import org.dspace.EDMExport.controller.FilterSearchValidator;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Clase pojo con los datos de la búsqueda.
 * Esta clase se valida mediante hibernate y nuestra clase {@link FilterSearchValidator}
 *
 */

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
