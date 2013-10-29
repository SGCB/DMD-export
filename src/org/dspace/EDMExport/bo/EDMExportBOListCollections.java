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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dspace.EDMExport.bo.EDMExportBOCollection;

/**
 * Clase pojo con los datos de la lista de colecciones {@link EDMExportBOCollection}
 * Se valida con hibernate
 *
 */

public class EDMExportBOListCollections
{
	@NotNull
	@Valid
	private EDMExportBOCollection[] listCollections;
	
	public EDMExportBOListCollections()
	{
	}
	
	public EDMExportBOListCollections(EDMExportBOCollection[] listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public void setListCollections(EDMExportBOCollection[] listCollections)
	{
		this.listCollections = listCollections;
	}
	
	public EDMExportBOCollection[] getListCollections()
	{
		return this.listCollections;
	}
	
	public boolean isEmpty()
	{
		return (listCollections == null || listCollections.length == 0);
	}
	
}
