package org.dspace.EDMExport.bo;

public class EDMExportBOCollection
{

	private String name;
	private String handle;
	private int id;
	private int index;
	
	public EDMExportBOCollection()
	{
	}
	
	public EDMExportBOCollection(String name, String handle, int id, int index)
	{
		this.name = name;
		this.handle = handle;
		this.id = id;
		this.index = index;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getHandle()
	{
		return handle;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setHandle(String handle)
	{
		this.handle = handle;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
}
