package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class EDMExportBOFormEDMData
{
	@NotEmpty
	private String pageAction;
	
	@NotEmpty
	private List<String> listTypes;
	
	@NotEmpty
	private String currentLocation;
	
	@URL
	private String edmRights;
	
	@URL
	private String urlBase;
	
	
	public EDMExportBOFormEDMData()
	{
	}
	
	public EDMExportBOFormEDMData(String[] listTypes, String currentLocation, String edmRights, String urlBase, String pageAction)
	{
		this.listTypes = Arrays.asList(listTypes);
		this.currentLocation = currentLocation;
		this.pageAction = pageAction;
		this.edmRights = edmRights;
		this.urlBase = urlBase;
	}
	
	public EDMExportBOFormEDMData(List<String> listTypes, String currentLocation, String edmRights, String urlBase, String pageAction)
	{
		this.listTypes = listTypes;
		this.currentLocation = currentLocation;
		this.pageAction = pageAction;
		this.edmRights = edmRights;
		this.urlBase = urlBase;
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
	
	public String getEdmRights()
	{
		return this.edmRights;
	}
	
	public String getUrlBase()
	{
		return this.urlBase;
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
	
	public void setEdmRights(String edmRights)
	{
		this.edmRights = edmRights;
	}
	
	public void setUrlBase(String urlBase)
	{
		this.urlBase = urlBase;
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
