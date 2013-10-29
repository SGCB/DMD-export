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

package org.dspace.EDMExport.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.EDMExport.dao.EDMExportDAODspaceListItems;
import org.dspace.EDMExport.dao.EDMExportDAOSearch;
import org.dspace.content.Item;

import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * Clase con la lógica para gestionar la búsqueda de ítems
 * <p>Se obtienen los ítems que a partir del criterio de búsqueda</p>
 *
 */

public class EDMExportServiceSearch
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Variables obtenidas de edmexport.properties
	 */
	@Value("${search.subject.index.solr}")
    private String searchSubject;
	
	@Value("${search.author.index.solr}")
    private String searchAuthor;
	
	@Value("${search.title.index.solr}")
    private String searchTitle;
		
	@Value("${list_items.itemspage}")
    private String searchItemsPage;
	
	@Value("${search.sortby}")
    private String searchSortBy;
	
	@Value("${search.sortorder}")
    private String searchOrder;
	
	/**
	 * POJO {@link EDMExportBOListItems} donde se alamcenarán los items {@link EDMExportBOItem} de la búsqueda
	 */
	private EDMExportBOListItems boListIems;
	
	/**
	 * POJO {@link EDMExportBOSearch} con los datos del formulario de la búsqueda
	 */
	private EDMExportBOSearch searchBO;
	
	/**
	 * DAO {@link EDMExportDAOSearch} para establecer la búsqueda en Solr en Dspace
	 */
	private EDMExportDAOSearch daoSearch;
	
	/**
	 * número de resultados totales de la búsqueda
	 */
	private int hitCount = 0;
	
	/**
	 * Constructor vacío. Inicializa el POJO {@link EDMExportBOListItems} del listado de ítems
	 */
	public EDMExportServiceSearch()
	{
		logger.debug("Init EDMExportServiceSearch");
		boListIems = new EDMExportBOListItems();
	}
	
	/**
	 * Asigna el POJO {@link EDMExportBOSearch} del formulario de búsqueda
	 * @param searchBO {@link EDMExportBOSearch} a asignar
	 */
	public void setSearchBO(EDMExportBOSearch searchBO)
	{
		this.searchBO = searchBO;
	}
	
	/**
	 * Obtiene la lista de ítems a partir de un offset.
	 * <p>Llama a al método getListItems(int offset, int limit) {@link #getListItems(int offset, int limit)}</p>
	 * 
	 * @param offset entero con el offset a partir del que recoger los ítems
	 * @return {@link EDMExportBOListItems} con la lista de ítems
	 */
	public EDMExportBOListItems getListItems(int offset)
	{
		if (hitCount > 0) {
			int searchItemsPageInt = Integer.parseInt(searchItemsPage);
			int limit = (hitCount < searchItemsPageInt)?hitCount:searchItemsPageInt;
			return getListItems(offset, limit);
		} else return getListItems(offset, 0);
	}
	
	/**
	 * Obtiene la lista de ítems a partir de un offset y un límite.
	 * <p>Mediante el DAO {@link EDMExportDAOSearch} consulta en solr los ítems y los añade a la lista {@link EDMExportBOListItems}</p>
	 * 
	 * @param offset entero con el offset a partir del que recoger los ítems
	 * @param limit entero con el límite de los ítems a recoger
	 * @return {@link EDMExportBOListItems} con la lista de ítems
	 */
	public EDMExportBOListItems getListItems(int offset, int limit)
	{
		logger.debug("EDMExportServiceSearch.getListItems");
		if (limit == 0) limit = Integer.parseInt(searchItemsPage);
		Item[] listItems = daoSearch.getListItems(searchBO, searchSubject, searchAuthor, searchTitle, searchSortBy, searchOrder, offset, limit);
		this.hitCount = daoSearch.getHitCount();
		if (listItems != null && listItems.length > 0) {
			logger.debug("Num items: " + listItems.length);
			EDMExportBOItem[] listArrayItems = new EDMExportBOItem[listItems.length];
			int i = 0;
			for (Item itemDS : listItems) {
				try {
					EDMExportBOItem it = EDMExportServiceItemDS2ItemBO.itemDS2ItemBO(itemDS, i);
					listArrayItems[i] = it;
					i++;
				} catch (Exception e) {
					logger.debug("item fail " + i, e);
				}
			}
			boListIems.setListItems(listArrayItems);
		} else {
			this.hitCount = 0;
			logger.debug("No items");
			boListIems.setListItems((List<EDMExportBOItem>) null);
		}
		return boListIems;
	}
	
	/**
	 * Devuelve el número de ítems de la búsqueda
	 * 
	 * @return el número de ítems de la búsqueda
	 */
	public int getHitCount()
	{
		return hitCount;
	}
	
	/**
	 * Asigna el POJO {@link EDMExportBOListItems} con la lista de ítems
	 * 
	 * @param boListItems {@link EDMExportBOListItems}
	 */
	public void setBoListIems(EDMExportBOListItems boListItems)
	{
		this.boListIems = boListItems;
	}
	
	/**
	 * Devuelve el POJO {@link EDMExportBOListItems} con la lista de ítems
	 * 
	 * @return {@link EDMExportBOListItems}
	 */
	public EDMExportBOListItems getBoListItems()
	{
		return boListIems;
	}
	
	/**
	 * Limpia la lista de items {@link EDMExportBOListItems}
	 */
	public void clearBoListItems()
	{
		try {
			if (boListIems.getListItems() != null)
				boListIems.getListItems().clear();
		} catch (Exception e) {
			logger.debug("EDMExportServiceSearch.clearBoListItems", e);
		}
		boListIems.setListItems((List<EDMExportBOItem>) null);
	}
	
	/**
	 * Inyecta el DAO {@link EDMExportDAOSearch} para la búsqueda
	 * 
	 * @param daoSearch DAO {@link EDMExportDAOSearch}
	 */
	public void setEdmExportDAOSearch(EDMExportDAOSearch daoSearch)
	{
		this.daoSearch = daoSearch;
	}


}
