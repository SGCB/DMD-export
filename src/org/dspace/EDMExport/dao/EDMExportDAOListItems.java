package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.content.Bundle;
import org.dspace.content.Item;

/**
 * 
 * Interfaz para la búsqueda de ítems en Dspace
 * Se ha implementado la de la api de Dspace, pero se podrían desarrollar otras con consultas a la base de datos sin
 * pasar por esta api 
 *
 */
public interface EDMExportDAOListItems
{
	/**
	 * Obtiene la lista de colecciones {@link EDMExportBOListCollections} asociadas al ítem
	 * 
	 *  @param id entero con el id del ítem {@link Item}
	 *  @return lista de las colecciones {@link EDMExportBOListCollections}
	 */
	public EDMExportBOListCollections getListCollectionsItem(int id);

	
	/**
	 * Obtiene un objeto Dspace del ítem {@link Item} a partir de su id
	 * 
	 * @param id entero con el id del ítem
	 * @return objeto Dspace del ítem {@link Item}
	 */
	public Item getDSPaceItem(int id);

	/**
	 * Obtiene un array de los recursos electrónicos (objeto Bundle de Dspace {@link Bundle}) de un ítem
	 * 
	 * @param item objeto Dspace del item {@link Item}
	 * @param type cadena con el tipo de recurso electrónico a recoger
	 * @return array de los recursos electrónicos {@link Bundle}
	 */
	public Bundle[] getDSPaceBundleItem(Item item, String type);
	
	
	/**
	 * Comprueba si el handle es un objeto Dspace
	 * 
	 * @param handle cadena con el handle del objeto Dspace
	 * @return cierto si es un objeto Dspace
	 */
	public boolean checkHandleItemDataBase(String handle) throws SQLException;
	
	/**
	 * Obtiene un objeto Dspace del ítem {@link Item} a partir de su handle
	 * 
	 * @param handle string con el handle del ítem
	 * @return objeto Dspace del ítem {@link Item}
	 */
	public Item getDSPaceItem(String handle) throws IllegalStateException, SQLException;
}
