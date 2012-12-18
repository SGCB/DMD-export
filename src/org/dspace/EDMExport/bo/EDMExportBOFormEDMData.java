package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class EDMExportBOFormEDMData
{
	@NotEmpty
	private List<String> listTypes;
	
	@NotBlank
	private String currentLocation;
	
	
	public EDMExportBOFormEDMData()
	{
	}
	
	public EDMExportBOFormEDMData(String[] listTypes, String currentLocation)
	{
		this.listTypes = Arrays.asList(listTypes);
		this.currentLocation = currentLocation;
	}
	
	public EDMExportBOFormEDMData(List<String> listTypes, String currentLocation)
	{
		this.listTypes = listTypes;
		this.currentLocation = currentLocation;
	}
	
	public List<String> getListTypes()
	{
		return this.listTypes;
	}
	
	public String getCurrentLocation()
	{
		return this.currentLocation;
	}
	
	public void setListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
	
	public void setCurrentLocation(String currentLocation)
	{
		this.currentLocation = currentLocation;
	}
}
