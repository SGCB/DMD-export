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

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Clase pojo con los datos de una colección
 * Se valida con hibernate y javax
 *
 */

public class EDMExportBOCollection
{
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String handle;
	
	@Min(1)
	private int id;
	
	@Min(0)
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
