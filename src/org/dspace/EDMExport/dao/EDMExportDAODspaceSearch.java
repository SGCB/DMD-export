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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.search.DSQuery;
import org.dspace.search.QueryArgs;
import org.dspace.search.QueryResults;
import org.dspace.sort.SortOption;


/**
 * 
 * Clase para realizar consultas desde la API de Dspace sobre la búsqueda de ítems {@link Item} en Solr
 * Implementa la interfaz para la búsqueda de ítems {@link EDMExportDAOSearch}
 * 
 * Obtiene todas los datos del ítem a partir de su id o handle.
 * Obtiene los recursos electrónicos asociados a un ítem.
 * Obtiene las colecciones en las que está asociado el ítem.
 *
 */

public class EDMExportDAODspaceSearch implements EDMExportDAOSearch
{
	/**
	 * Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	private EDMExportDAOBase edmExportDAOBase;
	
	/**
	 * contexto de Dspace {@link Context}
	 */
	private Context context;
	
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Array de items {@link Item} obtenidos de la búsqueda
	 */
	private Item[] listItems;
	
	/**
	 * número de ítems encontrados
	 */
	private int hitCount;
	
	
	/**
	 * Constructor donde se incializa el contexto
	 * 
	 * @param edmExportDAOBase Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	public EDMExportDAODspaceSearch(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceSearch");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
		
	
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
	public Item[] getListItems(EDMExportBOSearch searchBO, String searchSubject, String searchAuthor, String searchTitle, String searchSortBy, String searchOrder, int offset, int searchItemsPage)
	{
		try {
			logger.debug("EDMExportDAODspaceSearch.getListItems : Looking for list of items ");
			// Construimos la consulta a Solr
			// Primero el paginado
			QueryArgs qArgs = new QueryArgs();
			qArgs.setStart(offset);
			qArgs.setPageSize(searchItemsPage);
			qArgs.setSortOption(new SortOption(1, searchSortBy));
			if ("ASC".equalsIgnoreCase(searchOrder)) {
		        qArgs.setSortOrder("ASC");
		    } else {
		        qArgs.setSortOrder("DESC");
		    }
			// Podemos buscar por title, subject, author o en todos los campos
			String query = "";
			String option = searchBO.getOption();
			if (option != null && !option.isEmpty()) {
				if (option.equals("title")) {
					query = searchTitle + ":" + searchBO.getTerm();
				} else if (option.equals("subject")) {
					query = searchSubject + ":" + searchBO.getTerm();
				} else if (option.equals("author")) {
					query = searchAuthor + ":" + searchBO.getTerm();
				}
			} else {
				query = searchBO.getTerm();
			}
			// buscamos sólo ítems
			query += " AND search.resourcetype:2";
			qArgs.setQuery(query);
			logger.debug("Query " + qArgs.getQuery());
			listItems = null;
			ArrayList<Item> listArrayItems = new ArrayList<Item>();
			QueryResults qResults = null;
			// lanzamos consulta
			qResults = DSQuery.doQuery(context, qArgs);
			this.hitCount = qResults.getHitCount();
			logger.debug("List of dspaceobjects " + qResults.getHitHandles().size());
			// almacenamos los ítems con handle válido y de tipo ítem
		    for (int i = 0; i < qResults.getHitHandles().size(); i++) {
		    	String myHandle = (String)qResults.getHitHandles().get(i);
		    	DSpaceObject dso = HandleManager.resolveToObject(context, myHandle);
		    	if (dso != null &&  DSpaceObject.find(context, 2, dso.getID()) != null) {
		    		listArrayItems.add((Item) dso);
		    	}
		    }
		    listItems = new Item[listArrayItems.size()];
		    listItems = (Item[])listArrayItems.toArray(listItems);
		    logger.debug("List of items " + listItems.length);
		} catch (Exception e) {
			logger.debug("Exception in getListItems" , e);
		}
		return listItems;
	}

	/**
	 * Devuelve el número de resultados
	 * 
	 * @return número de resultados
	 */
	public int getHitCount()
	{
		return hitCount;
	}

}
