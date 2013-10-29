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
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dspace.EDMExport.bo.EDMExportBOItem;

/**
 * Clase pojo con los datos de la lista de ítems {@link EDMExportBOItem}
 * Se valida con hibernate
 *
 */

public class EDMExportBOListItems
{
	@NotNull
	@Valid
	private List<EDMExportBOItem> listItems;
	
	public EDMExportBOListItems()
	{
	}
	
	
	public EDMExportBOListItems(List<EDMExportBOItem> listItems)
	{
		this.listItems = listItems;
	}
	
	public EDMExportBOListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = new ArrayList<EDMExportBOItem>(Arrays.asList(listItems));
	}
	
	public void setListItems(EDMExportBOItem[] listItems)
	{
		this.listItems = new ArrayList<EDMExportBOItem>(Arrays.asList(listItems));
	}
	
	public void setListItems(List<EDMExportBOItem> listItems)
	{
		this.listItems = listItems;
	}
	
	
	public List<EDMExportBOItem> getListItems()
	{
		return this.listItems;
	}
	
	
	public boolean isEmpty()
	{
		return (listItems == null || listItems.size() == 0);
	}
	
}
