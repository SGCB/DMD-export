/**
 *  Copyright 2013 Spanish Minister of Education, Culture and Sport
 *  
 *  written by MasMedios
 *  
 *  Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 *  
 *  You may not use this work  except in compliance with the License. You may obtain a copy of the License at:
 *  
 *  http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" basis,
 *  
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package org.dspace.EDMExport.bo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Clase pojo con los datos de un ítem
 * Se valida con hibernate y javax
 * Usa {@link EDMExportBOListCollections}, {@link EDMExportBOListAuthors}, {@link EDMExportBOListSubjects}, {@link EDMExportBOListTypes}
 *
 */

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
