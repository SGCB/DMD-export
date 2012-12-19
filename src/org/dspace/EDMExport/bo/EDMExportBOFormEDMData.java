package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class EDMExportBOFormEDMData
{
	@NotEmpty
	private String pageAction;
	
	@NotEmpty
	private List<String> listTypes;
	
	@NotEmpty
	private String currentLocation;
	
	
	public EDMExportBOFormEDMData()
	{
	}
	
	public EDMExportBOFormEDMData(String[] listTypes, String currentLocation, String pageAction)
	{
		this.listTypes = Arrays.asList(listTypes);
		this.currentLocation = currentLocation;
		this.pageAction = pageAction;
	}
	
	public EDMExportBOFormEDMData(List<String> listTypes, String currentLocation, String pageAction)
	{
		this.listTypes = listTypes;
		this.currentLocation = currentLocation;
		this.pageAction = pageAction;
	}
	
	public List<String> getListTypes()
	{
		return this.listTypes;
	}
	
	public String getCurrentLocation()
	{
		return this.currentLocation;
	}
	
	public String getPageAction()
	{
		return this.pageAction;
	}
	
	public void setListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
	
	public void setCurrentLocation(String currentLocation)
	{
		this.currentLocation = currentLocation;
	}
	
	public void setPageAction(String pageAction)
	{
		this.pageAction = pageAction;
	}
	
	public void paddingTypes(String[] edmTypesArr)
	{
		for (int i=0; i < edmTypesArr.length; i++) {
			String type = listTypes.get(i); 
			if (!type.startsWith(edmTypesArr[i])) {
				listTypes.set(i, edmTypesArr[i] + "," + type);
			}
		}
	}
}
