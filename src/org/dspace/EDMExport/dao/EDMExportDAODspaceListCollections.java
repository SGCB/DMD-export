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
