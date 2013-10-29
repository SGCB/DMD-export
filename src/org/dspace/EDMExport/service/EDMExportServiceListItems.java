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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.dao.EDMExportDAODspaceListItems;
import org.dspace.EDMExport.dao.EDMExportDAOListItems;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.handle.HandleManager;

/**
 * 
 * Clase con la lógica para gestionar el listado de los ítems.
 * <p>Se obtienen los ítems que se han seleccionado y sus colecciones asociadas</p>
 *
 */

public class EDMExportServiceListItems
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");

	/**
	 * Diccionario con el id del ítem y el POJO del ítem {@link EDMExportBOItem} con los que se han seleccionados y se enviarán
	 */
	private Map<Integer, EDMExportBOItem> mapItemsSubmit;
	
	/**
	 * obtiene propiedades del archivode configuración de dspace
	 */
	private EDMExportServiceBase edmExportServiceBase;
	
	/**
	 * DAO con el acceso a base de datos para el listado de ítems
	 */
	private EDMExportDAOListItems daoListItems;
	
	
	/**
	 * Constructor vacío, se inicializa el diccionario
	 */
	public EDMExportServiceListItems()
	{
		logger.debug("Init EDMExportServiceListItems");
		this.mapItemsSubmit = new ConcurrentHashMap<Integer, EDMExportBOItem>();
	}
	
	/**
	 * Constructor con la inyección del DAO. Se inicializa el diccionario
	 * 
	 * @param edmExportServiceBase objeto {@link EDMExportServiceBase} inyectado
	 */
	public EDMExportServiceListItems(EDMExportServiceBase edmExportServiceBase)
	{
		logger.debug("Init EDMExportServiceListItems");
		this.edmExportServiceBase = edmExportServiceBase;
		this.mapItemsSubmit = new ConcurrentHashMap<Integer, EDMExportBOItem>();
	}
	
	/**
	 * Vaciamos la el diccionario de ítems a enviar
	 */
	public void clearMapItemsSubmit()
	{
		mapItemsSubmit.clear();
	}
	
	
	/**
	 * Busca en la lista de ítems uno con id específico
	 * 
	 * @param boListItems POJO {@link EDMExportBOListItems} con la lista de ítems
	 * @param id entero con el id del ítem a buscar
	 * @return POJO del ítem {@link EDMExportBOItem}
	 */
	public EDMExportBOItem searchEDMExportBOItemFromId(EDMExportBOListItems boListItems, int id)
	{
		for (EDMExportBOItem boItem : boListItems.getListItems()) {
			if (boItem.getId() == id) return boItem;
		}
		return null;
	}
	
	/**
	 * Añade al diccionario los nuevos ítems seleccionados
	 * 
	 * @param boListItems POJO con la lista de ítems
	 * @param checked array de cadenas con los ids de los nuevos ítems seleccionados
	 */
	public synchronized void processEDMExportBOItemsChecked(EDMExportBOListItems boListItems, String[] checked)
	{
		try {
			for (String idStr : checked) {
				int id = Integer.parseInt(idStr);
				if (!mapItemsSubmit.containsKey(id)) {
					EDMExportBOItem item = searchEDMExportBOItemFromId(boListItems, id);
					addItem(id, item);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception EDMExportServiceListItems.processEDMExportBOItemsChecked" ,e);
		}
	}
	
	/**
	 * Quita del diccionario los nuevos ítems no seleccionados
	 * 
	 * @param boListItems POJO con la lista de ítems
	 * @param nochecked array de cadenas con los ids de los nuevos ítems no seleccionados
	 */
	public synchronized void processEDMExportBOItemsNoChecked(EDMExportBOListItems boListItems, String[] nochecked)
	{
		try {
			for (String idStr : nochecked) {
				int id = Integer.parseInt(idStr);
				if (mapItemsSubmit.containsKey(id)) {
					EDMExportBOItem item = searchEDMExportBOItemFromId(boListItems, id);
					removeItem(id, item);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception EDMExportServiceListItems.processEDMExportBOItemsNoChecked" ,e);
		}
	}
	
	/**
	 * Procesa toda la lista de ítems para marcar como válidos los que están en el diccionario
	 * 
	 * @param boListItems POJO {@link EDMExportBOListItems} con la lista de ítems
	 */
	public synchronized void processEDMExportBOListItems(EDMExportBOListItems boListItems)
	{
		for (EDMExportBOItem item : boListItems.getListItems()) {
			if (mapItemsSubmit.containsKey(item.getId())) {
				item.setChecked(true);
			} else {
				item.setChecked(false);
			}
		}
	}
	
	
	/**
	 * Comprueba si existe un ítem en el diccionario de los ítems a enviar
	 * 
	 * @param id entero con el id del ítem
	 * @return cierto si existe
	 */
	public synchronized boolean containsEDMExportBOItem(int id)
	{
		return mapItemsSubmit.containsKey(id);
	}
	
	
	/**
	 * Devuelve el diccionario de ítems a enviar
	 * 
	 * @return diccionario de ítems a enviar
	 */
	public Map<Integer, EDMExportBOItem> getMapItemsSubmit()
	{
		return this.mapItemsSubmit;
	}
	
	/**
	 * Añade un POJO {@link EDMExportBOItem} de ítem al diccionario de ítems a enviar y lo marca como seleccionado
	 * @param id entero con el id del ítem
	 * @param item POJO {@link EDMExportBOItem} del ítem a añadir
	 */
	public synchronized void addItem(int id, EDMExportBOItem item)
	{
		if (!mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.put(id, item);
			item.setChecked(true);
			logger.debug("Added item " + id);
		}
	}
	
	/**
	 * Quita un POJO {@link EDMExportBOItem} de ítem al diccionario de ítems a enviar y lo marca como no seleccionado
	 * @param id entero con el id del ítem
	 * @param item POJO {@link EDMExportBOItem} del ítem a quitar
	 */
	public synchronized void removeItem(int id, EDMExportBOItem item)
	{
		if (mapItemsSubmit.containsKey(id)) {
			mapItemsSubmit.remove(id);
			item.setChecked(false);
			logger.debug("Removed item " + id);
		}
	}
	
	
	/**
	 * Obtiene las colecciones de un ítem
	 * 
	 * @return lista de cadenas con los ids de las colecciones
	 */
	public List<String> getListCollections()
	{
		logger.debug("EDMExportServiceListItems.getListCollections");
		List<String> listCollections = new ArrayList<String>();
		Set<Integer> setIdCollections = new HashSet<Integer>();
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			EDMExportBOListCollections boListCollections = item.getListCollections(); 
			if (boListCollections == null || boListCollections.isEmpty()) daoListItems.getListCollectionsItem(item.getId());
			if (boListCollections != null) {
				for (EDMExportBOCollection coll : boListCollections.getListCollections()) {
					if (!setIdCollections.contains(coll.getId())) {
						listCollections.add(coll.getName() + " (" + coll.getHandle() + ")");
						setIdCollections.add(coll.getId());
					}
				}
			}
		}
		Collections.sort(listCollections);
		return listCollections;
	}
	
	/**
	 * Comprueba si existe el handel en dspace
	 * 
	 * @param handle cadena con el handle a comprobar
	 * @return cierto si existe
	 * @throws Exception
	 */
	public boolean checkHandleItemDataBase(String handle) throws Exception
	{
		return daoListItems.checkHandleItemDataBase(handle);
	}
	
	
	/**
	 * Obtiene un objeto Item de dspace {@link Item} a partir del POJO {@link EDMExportBOItem} del item y su id
	 * 
	 * @param boItem POJO del Item
	 * @return objeto Item de dspace
	 */
	public Item getDSPaceItem(EDMExportBOItem boItem)
	{
		return daoListItems.getDSPaceItem(boItem.getId());
	}
	
	/**
	 * Obtiene un objeto Dspace del ítem {@link Item} a partir de su handle
	 * 
	 * @param handle string con el handle del ítem
	 * @return objeto Dspace del ítem {@link Item}
	 * @throws Exception 
	 */
	public Item getDSPaceItem(String handle) throws Exception
	{
		return daoListItems.getDSPaceItem(handle);
	}
	
	/**
	 * Obtiene un array con los recursos electrónicos de cierto tipo asociado a un ítem
	 * 
	 * @param item objeto Itm de dspace
	 * @param type cadena con el tipo de recursos
	 * @return array de objetos Bundle de dspace que representan los recursos electrónicos
	 */
	public Bundle[] getDSPaceBundleItem(Item item, String type)
	{
		return daoListItems.getDSPaceBundleItem(item, type);
	}
	
	
	/**
	 * Inyección del DAO del listado de ítems
	 * 
	 * @param daoListItems DAO {@link EDMExportDAODspaceListItems} de lista de ítems
	 */
	public void setEdmExportDAOListItems(EDMExportDAOListItems daoListItems)
	{
		this.daoListItems = daoListItems;
	}
	
	/**
	 * Obtiene el objeto que devuelve propiedades del archivo de configuración de dspace
	 * 
	 * @return objeto {@link EDMExportServiceBase} con las propiedades de dspace
	 */
	public EDMExportServiceBase getEDMExportServiceBase()
	{
		return edmExportServiceBase;
	}
}
