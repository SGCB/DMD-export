package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Clase pojo con los datos de los tipos para edm, se usa en {@link EDMExportBOItem}
 * Esta clase se valida mediante hibernate 
 *
 */

public class EDMExportBOListTypes
{
	@NotNull
	private List<String> listTypes;
	
	
	public EDMExportBOListTypes()
	{
	}
	
	public EDMExportBOListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
		
	public EDMExportBOListTypes(String[] listTypes)
	{
		this.listTypes = Arrays.asList(listTypes);
	}
	
	public List<String> getListTypes()
	{
		return listTypes;
	}
	
	public void setListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
}
