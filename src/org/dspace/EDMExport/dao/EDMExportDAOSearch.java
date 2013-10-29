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

import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.content.Item;

/**
 * 
 * Interfaz para la búsqueda de ítems en Dspace en Solr
 * Se ha implementado la de la api de Dspace, pero se podrían desarrollar otras con consultas a la base de datos sin
 * pasar por esta api 
 *
 */
public interface EDMExportDAOSearch
{
	/**
	 * Consulta a Solr para recuperar los ítems de la búsqueda por title, subject, author o en todos los campos.
	 * Se usa la implementación de la api de Dspace para búsqueda en Solr 
	 * 
	 * @param searchBO datos de la búsqueda {@link EDMExportBOSearch}
	 * @param searchSubject cadena con el subject a buscar
	 * @param searchAuthor cadena con el author a buscar
	 * @param searchTitle cadena con el title a buscar
	 * @param searchSortBy cadena con el campo con el que ordenar
	 * @param searchOrder cadena con el tipo de ordenación: ascendente o descendente
	 * @param offset entero con el offset a partir del que devolver ítems
	 * @param searchItemsPage entero con la página a partir del que devolver ítems
	 * 
	 * @return array con los objetos Item de Dspace {@link Item}
	 */
	Item[] getListItems(EDMExportBOSearch searchBO, String searchSubject, String searchAuthor, String searchTitle, String searchSortBy, String searchOrder, int offset, int searchItemsPage);
	
	/**
	 * Devuelve el número de resultados
	 * 
	 * @return número de resultados
	 */
	int getHitCount();
}
