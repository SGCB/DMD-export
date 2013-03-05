package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.dao.EDMExportDAOListCollections;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Value;

/**
 * Clase con la lógica para gestionar las colecciones.
 * <p>Se obtienen todas las colecciones y los ítems pertenecientes a una colección</p>
 *
 */
public class EDMExportServiceListCollections
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Ítems por página, obtenido de edmexport.porperties
	 */
	@Value("${list_items.itemspage}")
    private String listItemsPage;
	
	/**
	 * El valor entero de listItemsPage
	 */
	private int listItemsPageInt;
	
	/**
	 * POJO {@link EDMExportBOListCollections} con lista de objetos POJO colecciones {@link EDMExportBOCollection}
	 */
	private EDMExportBOListCollections boListCollections;
	
	/**
	 * objeto con el acceso a base de datos de dspace para las colecciones
	 */
	private EDMExportDAOListCollections daoListCollections;
	
	/**
	 * POJO {@link EDMExportBOListItems} con lista de objetos POJO ítem {@link EDMExportBOItem} 
	 */
	private EDMExportBOListItems boListIems;
	
	/**
	 * número de ítems
	 */
	private int hitCount = 0;
	
	/**
	 * Constructor con la inicialización de los POJO
	 */
	public EDMExportServiceListCollections()
	{
		logger.debug("Init EDMExportServiceListCollections");
		boListCollections = new EDMExportBOListCollections();
		boListIems = new EDMExportBOListItems();
	}
	
	/**
	 * Asignación del POJO de la lista de colecciones
	 * 
	 * @param boListCollections POJO con la lista de colecciones {@link EDMExportBOListCollections}
	 */
	public void setBoListCollections(EDMExportBOListCollections boListCollections)
	{
		this.boListCollections = boListCollections;
	}
	
	/**
	 * Obtiene todas las colecciones de dspace {@link Collection} y las almacenamos en el POJO de lista de colecciones {@link EDMExportBOListCollections}
	 * 
	 * @return POJO de lista de colecciones {@link EDMExportBOListCollections}
	 */
	public EDMExportBOListCollections getListCollection()
	{
		logger.debug("EDMExportServiceListCollections.getListCollection");
		Collection[] listCol = daoListCollections.getListCollections();
		if (listCol != null && listCol.length > 0) {
			logger.debug("Num collections: " + listCol.length);
			ArrayList<EDMExportBOCollection> listArrayCols = new ArrayList<EDMExportBOCollection>();
			int i = 0;
			for (Collection colDS : listCol) {
				EDMExportBOCollection col = new EDMExportBOCollection(colDS.getName(), colDS.getHandle(), colDS.getID(), i);
				listArrayCols.add(col);
				i++;
			}
			EDMExportBOCollection[] collectionArray = new EDMExportBOCollection[listArrayCols.size()];
			collectionArray = (EDMExportBOCollection[])listArrayCols.toArray(collectionArray);
			boListCollections.setListCollections(collectionArray);
			logger.debug("Num collections: " + boListCollections.getListCollections().length);
		} else {
			logger.debug("No collections");
			boListCollections = null;
		}
		return boListCollections;
	}
	
	/**
	 * Obtiene del DAO de colecciones {@link EDMExportBOListItems} los ítems a partir de un offset y hasta un límite
	 *  
	 * @param offset entero con el offset a partir del que obtener ítems
	 * @return POJO con el listado de ítems {@link EDMExportBOListItems}
	 */
	public EDMExportBOListItems getListItems(int offset)
	{
		if (hitCount > 0) {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			int limit = (hitCount < listItemsPageInt)?hitCount:listItemsPageInt;
			return getListItems(offset, limit);
		} else return getListItems(offset, listItemsPageInt);
	}
	
	/**
	 * Se obtienen los ítems del POJO de colecciones {@link EDMExportBOListCollections}
	 * <p>Si el POJO del listado de ítems {@link EDMExportBOListItems} está vacío se busca en base de datos, si no se obtienen del listado</p>
	 * 
	 * @param offset offset a apartir del que obtener los ítems
	 * @param limit límite de ítems a devolver
	 * @return POJO del listado de ítems {@link EDMExportBOListItems}
	 */
	public EDMExportBOListItems getListItems(int offset, int limit)
	{
		logger.debug("EDMExportServiceListCollections.getListItems");
		listItemsPageInt = Integer.parseInt(listItemsPage);
		// No tenemos aún ítems, lo buscamos de base de datos
		if (boListIems.isEmpty()) {
			Set<Item> setItems = new HashSet<Item>();
			
			// para cada una de las colecciones se obtienen sus ítems
			logger.debug("Num selected coll: " + this.boListCollections.getListCollections().length);
			for (EDMExportBOCollection boColl : this.boListCollections.getListCollections()) {
				Item[] listItemsCol = daoListCollections.getItems(boColl.getId());
				if (listItemsCol != null) {
					for (Item it : listItemsCol) {
						if (!setItems.contains(it)) {
							setItems.add(it);
						}
					}
				}
			}
		
			// se convierten los objetos ítem de dspace al POJO ítem de la aplicación
			Item[] listItems = setItems.toArray(new Item[0]);
			if (listItems != null && listItems.length > 0) {
				logger.debug("Num items: " + listItems.length);
				EDMExportBOItem[] listArrayItems = new EDMExportBOItem[listItems.length];
				int i = 0;
				for (Item itemDS : listItems) {
					try {
						EDMExportBOItem col = EDMExportServiceItemDS2ItemBO.itemDS2ItemBO(itemDS, i);
						listArrayItems[i] = col;
						i++;
					} catch (Exception e) {
						logger.debug("item fail " + i, e);
					}
				}
				hitCount = listArrayItems.length;
				logger.debug("Num final items: " + hitCount);
				boListIems.setListItems(listArrayItems);
			} else {
				hitCount = 0;
				logger.debug("No items");
				boListIems.setListItems((List<EDMExportBOItem>) null);
			}
		}
		// el total de ítems es inferior al límite
		if (hitCount <= listItemsPageInt) return boListIems;
		if (offset > hitCount) offset = 0;
		EDMExportBOListItems boListIemsPage = new EDMExportBOListItems();
		if (limit > hitCount) limit = hitCount;
		logger.debug("EDMExportServiceListCollections.getListItems Items from "+ offset + " qty " + limit);
		// recogemos a partir del offset y hasta el límite
		EDMExportBOItem[] listItemsPage = new EDMExportBOItem[limit];
		for (int i = 0; i < limit; i++) {
			listItemsPage[i] = boListIems.getListItems().get(offset + i);
		}
		boListIemsPage.setListItems(listItemsPage);
		return boListIemsPage;
	}
	
	
	/**
	 * Obtiene el POJO con todo los ítems {@link EDMExportBOListItems}
	 * 
	 * @return POJO con la lista de ítems {@link EDMExportBOListItems}
	 */
	public EDMExportBOListItems getBoListItems()
	{
		return boListIems;
	}
	
	/**
	 * Asigna el POJO con la lista de ítems {@link EDMExportBOListItems}
	 * 
	 * @param boListItems POJO con la lista de ítems {@link EDMExportBOListItems}
	 */
	public void setBoListIems(EDMExportBOListItems boListItems)
	{
		this.boListIems = boListItems;
	}
	
	/**
	 * Limpia el POJO con la lista de ítems {@link EDMExportBOListItems}
	 */
	public void clearBoListItems()
	{
		try {
			if (boListIems.getListItems() != null)
				boListIems.getListItems().clear();
		} catch (Exception e) {
			logger.debug("EDMExportServiceListCollections.clearBoListItems", e);
		}
		boListIems.setListItems((List<EDMExportBOItem>) null);
	}
	
	/**
	 * Devuelve número de ítems
	 * @return número de ítems
	 */
	public int getHitCount()
	{
		return hitCount;
	}
	
	/**
	 * Inyección del DAO para colecciones {@link EDMExportDAOListCollections}
	 * 
	 * @param daoListCollections DAO para colecciones {@link EDMExportDAOListCollections}
	 */
	public void setEdmExportDAOListCollections(EDMExportDAOListCollections daoListCollections)
	{
		this.daoListCollections = daoListCollections;
	}

}
