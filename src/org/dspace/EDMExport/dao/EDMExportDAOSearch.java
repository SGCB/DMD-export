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
