package org.dspace.EDMExport.bo;

public class EDMExportBOItem
{
	
	private String title;
	private EDMExportBOListCollections listCollections;
	private String[] author;
	private String[] subject;
	private String[] type;
	private String handle;
	private int id;
	private int index;
	
	public EDMExportBOItem()
	{
	}
	
	public EDMExportBOItem(String title, EDMExportBOListCollections listCollections, String[] author, String[] subject, String[] type, String handle, int id, int index)
	{
		this.title = title;
		this.listCollections = listCollections;
		this.author = author;
		this.subject = subject;
		this.type = type;
		this.handle = handle;
		this.id = id;
		this.index = index;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public EDMExportBOListCollections getListCollections()
	{
		return listCollections;
	}
	
	public String[] getAuthor()
	{
		return author;
	}
	
	public String[] getSubject()
	{
		return subject;
	}
	
	public String[] getType()
	{
		return type;
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
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setAuthor(String[] author)
	{
		this.author = author;
	}
	
	public void setCollection(EDMExportBOListCollections listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public void setSubject(String[] subject)
	{
		this.subject = subject;
	}
	
	public void setType(String[] type)
	{
		this.type = type;
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
