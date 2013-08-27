package org.dspace.EDMExport.bo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 * Clase pojo con los datos del formulario de los elementos EDM
 * Se valida con hibernate
 *
 */

public class EDMExportBOFormEDMData
{
	@NotEmpty
	private String pageAction;
	
	@NotEmpty
	private List<String> listTypes;
	
	@NotEmpty
	private List<String> listXmlFormats;
	
	@NotEmpty
	private String xmlFormat;
	
	@NotEmpty
	private String title;
	
	@URL
	private String edmRights;
	
	@URL
	private String urlBase;
	
	@NotNull
	private boolean edmUgc = false;
	
	
	public EDMExportBOFormEDMData()
	{
	}
	
	public EDMExportBOFormEDMData(String[] listXmlFormats, String[] listTypes, String title, String edmRights, String urlBase, String pageAction)
	{
		this.listXmlFormats = Arrays.asList(listXmlFormats);
		this.listTypes = Arrays.asList(listTypes);
		this.title = title;
		this.pageAction = pageAction;
		this.edmRights = edmRights;
		this.urlBase = urlBase;
	}
	
	public EDMExportBOFormEDMData(List<String> listXmlFormats, List<String> listTypes, String title, String edmRights, String urlBase, String pageAction)
	{
		this.listTypes = listTypes;
		this.listXmlFormats = listXmlFormats;
		this.title = title;
		this.pageAction = pageAction;
		this.edmRights = edmRights;
		this.urlBase = urlBase;
	}
	
	public List<String> getListTypes()
	{
		return this.listTypes;
	}
	
	public List<String> getListXmlFormats()
	{
		return this.listXmlFormats;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getXmlFormat()
	{
		return this.xmlFormat;
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

	public boolean isEdmUgc()
	{
		return this.edmUgc;
	}
	
	public void setListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
	
	public void setListXmlFormats(List<String> listXmlFormats)
	{
		this.listXmlFormats = listXmlFormats;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setXmlFormat(String xmlFormat)
	{
		this.xmlFormat = xmlFormat;
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
	
	public void setEdmUgc(boolean edmUgc)
	{
		this.edmUgc = edmUgc;
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
