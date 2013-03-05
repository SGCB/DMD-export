package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Clase pojo con los datos de la lista de autores, se usa en {@link EDMExportBOItem}
 * Se valida con hibernate
 *
 */

public class EDMExportBOListAuthors
{
	@NotNull
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
