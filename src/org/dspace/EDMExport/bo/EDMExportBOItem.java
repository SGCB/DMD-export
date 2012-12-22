package org.dspace.EDMExport.bo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class EDMExportBOItem
{
	@NotEmpty
	private String title;
	
	@NotNull
	@Valid
	private EDMExportBOListCollections listCollections;
	
	@NotNull
	@Valid
	private EDMExportBOListAuthors author;
	
	@NotNull
	@Valid
	private EDMExportBOListSubjects subject;
	
	@NotNull
	@Valid
	private EDMExportBOListTypes type;
	
	@NotEmpty
	private String handle;
	
	@Min(1)
	private int id;
	
	@Min(0)
	private int index;
	
	
	private boolean checked;
	
	public EDMExportBOItem()
	{
	}
	
	public EDMExportBOItem(String title, EDMExportBOListCollections listCollections, String[] author, String[] subject, String[] type, String handle, int id, int index)
	{
		this.title = title;
		this.listCollections = listCollections;
		this.author = new EDMExportBOListAuthors(author);
		this.subject = new EDMExportBOListSubjects(subject);
		this.type = new EDMExportBOListTypes(type);
		this.handle = handle;
		this.id = id;
		this.index = index;
		this.checked = false;
	}
	
	
	public EDMExportBOItem(String title, EDMExportBOListCollections listCollections, List<String> author, List<String> subject, List<String> type, String handle, int id, int index)
	{
		this.title = title;
		this.listCollections = listCollections;
		this.author = new EDMExportBOListAuthors(author);
		this.subject = new EDMExportBOListSubjects(subject);
		this.type = new EDMExportBOListTypes(type);
		this.handle = handle;
		this.id = id;
		this.index = index;
		this.checked = false;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public EDMExportBOListCollections getListCollections()
	{
		return listCollections;
	}
	
	public EDMExportBOListAuthors getAuthor()
	{
		return author;
	}
	
	public List<String> getListAuthors()
	{
		return author.getListAuthors();
	}
	
	public EDMExportBOListSubjects getSubject()
	{
		return subject;
	}
	
	public List<String> getListSubjects()
	{
		return subject.getListSubjects();
	}
	
	public EDMExportBOListTypes getType()
	{
		return type;
	}
	
	public List<String> getListTypes()
	{
		return type.getListTypes();
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
	
	public boolean getChecked()
	{
		return checked;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setAuthor(EDMExportBOListAuthors author)
	{
		this.author = author;
	}
	
	public void setListAuthors(List<String> listAuthors)
	{
		this.author.setListAuthors(listAuthors);
	}
	
	public void setListCollection(EDMExportBOListCollections listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public void setSubject(EDMExportBOListSubjects subject)
	{
		this.subject = subject;
	}
	
	public void setListSubjects(ArrayList<String> listSubjects)
	{
		this.subject.setListSubjects(listSubjects);
	}
	
	public void setType(EDMExportBOListTypes type)
	{
		this.type = type;
	}
	
	public void setListTypes(ArrayList<String> listTypes)
	{
		this.type.setListTypes(listTypes);
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
	
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

}
