package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;


/**
 * 
 * Clase pojo con las materias, se usa en {@link EDMExportBOItem}
 * Esta clase se valida mediante hibernate 
 *
 */

public class EDMExportBOListSubjects
{
	@NotNull
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
