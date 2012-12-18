package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

public class EDMExportBOFormEDMData
{

	private List<String> listTypes;
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
