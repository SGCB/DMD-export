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

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Clase pojo con los datos de los tipos para edm, se usa en {@link EDMExportBOItem}
 * Esta clase se valida mediante hibernate 
 *
 */

public class EDMExportBOListTypes
{
	@NotNull
	private List<String> listTypes;
	
	
	public EDMExportBOListTypes()
	{
	}
	
	public EDMExportBOListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
		
	public EDMExportBOListTypes(String[] listTypes)
	{
		this.listTypes = Arrays.asList(listTypes);
	}
	
	public List<String> getListTypes()
	{
		return listTypes;
	}
	
	public void setListTypes(List<String> listTypes)
	{
		this.listTypes = listTypes;
	}
}
