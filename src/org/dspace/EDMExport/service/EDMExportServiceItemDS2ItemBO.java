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


@Service
public class EDMExportServiceItemDS2ItemBO
{
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
    private static String searchSubject;
	
    private static String searchAuthor;
	
    private static String searchType;
	
	@Value("${search.subject}")
	public void setPrivateName(String searchSubject) {
		EDMExportServiceItemDS2ItemBO.searchSubject = searchSubject;
	}
	
	@Value("${search.author}")
	public void setPrivateAuthor(String searchAuthor) {
		EDMExportServiceItemDS2ItemBO.searchAuthor = searchAuthor;
	}
	
	@Value("${search.type}")
	public void setPrivateType(String searchType) {
		EDMExportServiceItemDS2ItemBO.searchType = searchType;
	}
	
	
	@SuppressWarnings("deprecation")
	public static EDMExportBOItem itemDS2ItemBO(Item itemDS, int i)
	{
		EDMExportBOItem col = null;
		try {
			Collection[] collections = itemDS.getCollections();
			EDMExportBOCollection[] collectionsBOArr = new EDMExportBOCollection[collections.length];
			int j = 0;
			for (Collection colDS : collections) {
				EDMExportBOCollection colBO = new EDMExportBOCollection(colDS.getName(), colDS.getHandle(), colDS.getID(), j);
				collectionsBOArr[j] = colBO;
				j++;
			}
			EDMExportBOListCollections listCollectionsBO = new EDMExportBOListCollections(collectionsBOArr);
			
			DCValue[] authors = itemDS.getMetadata(EDMExportServiceItemDS2ItemBO.searchAuthor);
			String[] authorsStr = new String[authors.length];
			j = 0;
			for (DCValue authDCV : authors) {
				authorsStr[j] = authDCV.value;
				j++;
			}
			
			DCValue[] subjects = itemDS.getMetadata(EDMExportServiceItemDS2ItemBO.searchSubject);
			String[] subjectsStr = new String[subjects.length];
			j = 0;
			for (DCValue subjectDCV : subjects) {
				subjectsStr[j] = subjectDCV.value;
				j++;
			}
			
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
