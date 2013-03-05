package org.dspace.EDMExport.bo;


/**
 * Clase pojo con los datos del usuario
 *
 */

public class EDMExportBOUser
{

	private String username;
	private String password;
	private Integer access;
	
	
	public EDMExportBOUser()
	{
	}
	  
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public Integer getAccess()
	{
		return access;
	}
	
	public void setAccess(Integer access)
	{
		this.access = access;
	}
	
}
