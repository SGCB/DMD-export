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

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOCollection;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.DCValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Clase para transformar objetos Items de Dspace a nuestro modelo POJO de Item
 *
 */

@Service
public class EDMExportServiceItemDS2ItemBO
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * elemento dc del subject, recogido de edmexport.properties 
	 */
    private static String searchSubject;
	
    /**
	 * elemento dc del author, recogido de edmexport.properties 
	 */
    private static String searchAuthor;
	
    /**
	 * elemento dc del type, recogido de edmexport.properties 
	 */
    private static String searchType;
	
    /**
     * Inyección del valor del subject
     * 
     * @param searchSubject cadena con el valor del subject
     */
	@Value("${search.subject}")
	public void setPrivateName(String searchSubject) {
		EDMExportServiceItemDS2ItemBO.searchSubject = searchSubject;
	}
	
	/**
     * Inyección del valor del author
     * 
     * @param searchAuthor cadena con el valor del author
     */
	@Value("${search.author}")
	public void setPrivateAuthor(String searchAuthor) {
		EDMExportServiceItemDS2ItemBO.searchAuthor = searchAuthor;
	}
	
	/**
     * Inyección del valor del type
     * 
     * @param searchType cadena con el valor del type
     */
	@Value("${search.type}")
	public void setPrivateType(String searchType) {
		EDMExportServiceItemDS2ItemBO.searchType = searchType;
	}
	
	
	/**
	 * Transforma un objeto Item de dspace al POJO Item de la aplicación
	 * Recoge los valores necesarios y los almacena en el POJO {@link EDMExportBOItem}
	 * 
	 * @param itemDS objeto Iten de dspace {@link Item} a transformar
	 * @param i índice en el listado
	 * @return objeto con los datos del Item {@link EDMExportBOItem}
	 */
	@SuppressWarnings("deprecation")
	public static EDMExportBOItem itemDS2ItemBO(Item itemDS, int i)
	{
		EDMExportBOItem col = null;
		try {
			// obtener colecciones asociadas al ítem
			Collection[] collections = itemDS.getCollections();
			EDMExportBOCollection[] collectionsBOArr = new EDMExportBOCollection[collections.length];
			int j = 0;
			for (Collection colDS : collections) {
				EDMExportBOCollection colBO = new EDMExportBOCollection(colDS.getName(), colDS.getHandle(), colDS.getID(), j);
				collectionsBOArr[j] = colBO;
				j++;
			}
			EDMExportBOListCollections listCollectionsBO = new EDMExportBOListCollections(collectionsBOArr);
			
			// obtener authors asociadas al ítem
			DCValue[] authors = itemDS.getMetadata(EDMExportServiceItemDS2ItemBO.searchAuthor);
			String[] authorsStr = new String[authors.length];
			j = 0;
			for (DCValue authDCV : authors) {
				authorsStr[j] = authDCV.value;
				j++;
			}
			
			// obtener subjects asociadas al ítem
			DCValue[] subjects = itemDS.getMetadata(EDMExportServiceItemDS2ItemBO.searchSubject);
			String[] subjectsStr = new String[subjects.length];
			j = 0;
			for (DCValue subjectDCV : subjects) {
				subjectsStr[j] = subjectDCV.value;
				j++;
			}
			
			// obtener types asociadas al ítem
			DCValue[] types = itemDS.getMetadata(EDMExportServiceItemDS2ItemBO.searchType);
			String[] typesStr = new String[types.length];
			j = 0;
			for (DCValue typeDCV : types) {
				typesStr[j] = typeDCV.value;
				j++;
			}
			
			col = new EDMExportBOItem(itemDS.getName(), listCollectionsBO, authorsStr, subjectsStr, typesStr, itemDS.getHandle(), itemDS.getID(), i);
		} catch (Exception e) {
			logger.debug("item fail " + i, e);
		}
		return col;
	}
}
