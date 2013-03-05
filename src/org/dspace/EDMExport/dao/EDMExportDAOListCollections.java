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
