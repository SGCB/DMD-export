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

package org.dspace.EDMExport.dao;

import org.dspace.content.Collection;
import org.dspace.content.Item;

/**
 * 
 * Interfaz para la búsqueda de colecciones en Dspace
 * Se ha implementado la de la api de Dspace, pero se podrían desarrollar otras con consultas a la base de datos sin
 * pasar por esta api 
 *
 */

public interface EDMExportDAOListCollections
{
	/**
	 * Obtiene todas las colecciones en Dspace
	 * 
	 * @return devuelve un array de colecciones {@link Collection}
	 */
	public Collection[] getListCollections();
	
	/**
	 * Obtiene los ítems pertenecientes a una colección
	 * 
	 * @param id entero con el id de la colección
	 * @return array de ítems {@link Item}
	 */
	public Item[] getItems(int id);
}
