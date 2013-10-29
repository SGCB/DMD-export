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

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.Context;

/**
 * 
 * Clase para realizar consultas desde la API de Dspace sobre las colecciones {@link Collection}
 * Implementa la interfaz para la consulta a las colecciones {@link EDMExportDAOListCollections}
 * 
 * Obtiene todas las colecciones existentes en Dspace y los ítems de colecciones específicas.
 *
 */


public class EDMExportDAODspaceListCollections implements EDMExportDAOListCollections
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
	 * Array con las colecciones {@link Collection}
	 */
	private Collection[] listCol;
	
	
	/**
	 * Constructor donde se incializa el contexto
	 * 
	 * @param edmExportDAOBase Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	public EDMExportDAODspaceListCollections(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceListCollections");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}
	
	/**
	 * Obtiene todas las colecciones en Dspace
	 * 
	 * @return devuelve un array de colecciones {@link Collection}
	 */
	public Collection[] getListCollections()
	{
		try {
			logger.debug("EDMExportDAODspaceListCollections.getListCollections: Looking for list of collections ");
			listCol = Collection.findAll(context);
		} catch (SQLException e) {
			logger.debug("getListCollections", e);
		}
		return listCol;
	}
	
	/**
	 * Obtiene los ítems pertenecientes a una colección
	 * 
	 * @param id entero con el id de la colección
	 * @return array de ítems {@link Item}
	 */
	public Item[] getItems(int id)
	{
		logger.debug("EDMExportDAODspaceListCollections.getItems");
		try {
			ArrayList<Item> listArrayItems = new ArrayList<Item>();
			ItemIterator iter = Collection.find(context, id).getItems();
			while (iter.hasNext()) {
                Item item = iter.next();
				listArrayItems.add(item);
			}
			Item[] itemsArray = new Item[listArrayItems.size()];
			itemsArray = (Item[])listArrayItems.toArray(itemsArray);
			return itemsArray;
		} catch (SQLException e) {
			logger.debug("getItems", e);
		} catch (Exception e) {
			logger.debug("getItems", e);
		}
		return null;
	}
	
}
