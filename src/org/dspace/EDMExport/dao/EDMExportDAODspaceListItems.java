package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;


/**
 * 
 * Clase para realizar consultas desde la API de Dspace sobre los ítems {@link Item}
 * Implementa la interfaz para la consulta a los ítems {@link EDMExportDAOListItems}
 * 
 * Obtiene todas los datos del ítem a partir de su id o handle.
 * Obtiene los recursos electrónicos asociados a un ítem.
 * Obtiene las colecciones en las que está asociado el ítem.
 *
 */

public class EDMExportDAODspaceListItems implements EDMExportDAOListItems
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * contexto de Dspace {@link Context}
	 */
	private Context context;
	
	/**
	 * Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	private EDMExportDAOBase edmExportDAOBase;

	
	/**
	 * Constructor donde se incializa el contexto
	 * 
	 * @param edmExportDAOBase Objeto con la obtención del contexto de Dspace {@link EDMExportDAOBase}
	 */
	public EDMExportDAODspaceListItems(EDMExportDAOBase edmExportDAOBase)
	{
		logger.debug("Init EDMExportDAODspaceListItems");
		this.edmExportDAOBase = edmExportDAOBase;
		context = edmExportDAOBase.getContext();
	}

	/**
	 * Obtiene la lista de colecciones {@link EDMExportBOListCollections} asociadas al ítem
	 * 
	 *  @param id entero con el id del ítem {@link Item}
	 *  @return lista de las colecciones {@link EDMExportBOListCollections}
	 */
	@Override
	public EDMExportBOListCollections getListCollectionsItem(int id)
	{
		logger.debug("EDMExportDAODspaceListItems.getListCollectionsItem");
		try {
			Item item = Item.find(context, id);
			Collection[] listDspaceCollection = item.getCollections();
			EDMExportBOCollection[] listCollections = new EDMExportBOCollection[listDspaceCollection.length];
			int i = 0;
			for (Collection coll : listDspaceCollection) {
				listCollections[i] = new EDMExportBOCollection(coll.getName(), coll.getHandle(), coll.getID(), i);
				i++;
			}
			EDMExportBOListCollections edmExportBoListCollections = new EDMExportBOListCollections(listCollections);
			return edmExportBoListCollections;
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getListCollectionsItem", e);
		} catch (Exception e) {
			logger.debug("EDMExportBOListCollections.getListCollectionsItem", e);
		}
		return null;
	}

	/**
	 * Obtiene un objeto Dspace del ítem {@link Item} a partir de su id
	 * 
	 * @param id entero con el id del ítem
	 * @return objeto Dspace del ítem {@link Item}
	 */
	@Override
	public Item getDSPaceItem(int id)
	{
		logger.debug("EDMExportDAODspaceListItems.getDSPaceItem");
		try {
			return Item.find(context, id);
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getDSPaceItem", e);
		}
		return null;
	}
	
	/**
	 * Obtiene un array de los recursos electrónicos (objeto Bundle de Dspace {@link Bundle}) de un ítem
	 * 
	 * @param item objeto Dspace del item {@link Item}
	 * @param type cadena con el tipo de recurso electrónico a recoger
	 * @return array de los recursos electrónicos {@link Bundle}
	 */
	@Override
	public Bundle[] getDSPaceBundleItem(Item item, String type)
	{
		logger.debug("EDMExportDAODspaceListItems.getDSPaceBundleItem");
		try {
			return (type == null)?item.getBundles():item.getBundles(type);
		} catch (SQLException e) {
			logger.debug("EDMExportBOListCollections.getDSPaceBundleItem", e);
		}
		return null;
	}
	
	
	/**
	 * Comprueba si el handle es un objeto Dspace
	 * 
	 * @param handle cadena con el handle del objeto Dspace
	 * @return cierto si es un objeto Dspace
	 */
	@Override
	public boolean checkHandleItemDataBase(String handle) throws SQLException
	{
		return (HandleManager.resolveToObject(context, handle) != null);
	}
	
	
	/**
	 * Obtiene un objeto Dspace del ítem {@link Item} a partir de su handle
	 * 
	 * @param handle string con el handle del ítem
	 * @return objeto Dspace del ítem {@link Item}
	 * @throws SQLException 
	 * @throws IllegalStateException 
	 */
	@Override
	public Item getDSPaceItem(String handle) throws IllegalStateException, SQLException
	{
		return (Item) HandleManager.resolveToObject(context, handle);
	}

}
