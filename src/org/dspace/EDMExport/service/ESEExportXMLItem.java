package org.dspace.EDMExport.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.app.util.Util;
import org.dspace.content.DCValue;
import org.dspace.core.Constants;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * 
 * Clase con la lógica para crear un elemento RDF del esquema ESE a partir de los datos de un POJO {@link EDMExportBOItem}
 * <p>Usa la librería jdom para crear el elemento RDF y todos sus hijos</p>
 * <p>Hereda de la clase {@link EDMExportXML} e implementa el método {@link #processItemElement}</p>
 *
 */
@SuppressWarnings("deprecation")
public class ESEExportXMLItem extends EDMExportXML
{
	
	protected static final String NAMESPACE_URI_EUROPEANA = "http://www.europeana.eu/schemas/ese/";
	protected static final String NAMESPACE_URI_DCTERMS = "http://purl.org/dc/terms/";
	protected static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.europeana.eu/schemas/ese/ http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd";

	protected Namespace EUROPEANA = Namespace.getNamespace("europeana", NAMESPACE_URI_EUROPEANA);
	protected Namespace DCTERMS = Namespace.getNamespace("dcterms", NAMESPACE_URI_DCTERMS);
	
	
	/**
	 * Constructor con el servicio {@link EDMExportServiceListItems} inyectado
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public ESEExportXMLItem(EDMExportServiceListItems edmExportServiceListItems)
	{
		super(edmExportServiceListItems);
	}

	
	/**
	 * Creación del esquema ESE con la lista de los ítems seleccionados
	 * <p>Se usa jdom para crear el documento xml</p>
	 * 
	 * @param edmExportBOFormEDMData POJO {@link EDMExportBOFormEDMData} con los datos del formulario
	 * @return element jdom con el root
	 */
	public Element processXML()
	{
		Element metadata = new Element("metadata", XSI);
		
		metadata.addNamespaceDeclaration(XSI);
		metadata.addNamespaceDeclaration(DCTERMS);
		metadata.addNamespaceDeclaration(DC);
		metadata.setAttribute("schemaLocation", NAMESPACE_URI_SCHEMALOCATION, XSI);
		
		Map<Integer, EDMExportBOItem> mapItemsSubmit = edmExportServiceListItems.getMapItemsSubmit();
		logger.debug("Num items: " + mapItemsSubmit.size());
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			Element europeana_record = new Element("record", EUROPEANA);
			europeana_record.addNamespaceDeclaration(EUROPEANA);
			europeana_record.addNamespaceDeclaration(DC);
			metadata.addContent(europeana_record);
			List<Element> listElements = processItemElement(item, europeana_record);
			for (Element element : listElements) {
				europeana_record.addContent(element);
			}
		}
		return metadata;
	}
	
	/**
	 * Procesa los datos del ítem para generar los hijos del element rdf del esquema ESE:
	 * <p>ProvidedCHO, WebResource, SkosConcept y oreAggregation</p>
	 * 
	 * @param boItem POJO {@link EDMExportBOItem} con los datos del ítem
	 * @return lista de elementos jdom con las clases de EDM ProvidedCHO, WebResource, SkosConcept y oreAggregation
	 */
	@Override
	protected List<Element> processItemElement(EDMExportBOItem boItem, Element parentElement)
	{
		List<Element> listElements = new ArrayList<Element>();
		
		// obtenemos el objeto de dspace del ítem a partir del POJO
		Item item = edmExportServiceListItems.getDSPaceItem(boItem);
		
		// sólo se quieren items con contenido digital
		// recogemos los recursos electrónicos del tipo "original" del ítem
		Bundle[] origBundles = edmExportServiceListItems.getDSPaceBundleItem(item, "ORIGINAL");
		if (origBundles.length == 0) return listElements;
		
		createElementDC(item, "title", DC, "title", Item.ANY, parentElement, true);
		createElementDCExclusion(item, "contributor", DC, "contributor", new HashSet<String>(Arrays.asList("author")), parentElement, true);
		createElementDC(item, "creator", DC, "contributor", "author", parentElement, true);
		createElementDC(item, "creator", DC, "creator", Item.ANY, parentElement, true);
		createElementDC(item, "subject", DC, "subject", Item.ANY, parentElement, true);
		createElementDCExclusion(item, "description", DC, "description", new HashSet<String>(Arrays.asList("provenance")), parentElement, true);
		createElementDC(item, "publisher", DC, "publisher", Item.ANY, parentElement, true);
		createElementDC(item, "date", DC, "date", Item.ANY, parentElement, true);
		createElementDC(item, "format", DC, "format", Item.ANY, parentElement, true);
		createElementDC(item, "identifier", DC, "identifier", Item.ANY, parentElement, true);
		createElementDC(item, "source", DC, "source", Item.ANY, parentElement, true);
		createElementDC(item, "language", DC, "language", Item.ANY, parentElement, true);
		createElementDC(item, "relation", DC, "relation", Item.ANY, parentElement, true);
		createElementDC(item, "coverage", DC, "coverage", Item.ANY, parentElement, true);
		createElementDC(item, "rights", DC, "rights", Item.ANY, parentElement, true);
		createElementDC(item, "type", DC, "type", Item.ANY, parentElement, true);
		
		processEuropeanaElements(item, parentElement);
		
		return listElements;
	}
	
	
	/**
	 * Procesar los elementos europeana de ESE
	 *  
	 * @param item
	 * @param parenElement
	 */
	private void processEuropeanaElements(Item item, Element parenElement)
	{
		try {
			String thumbnail_url = null;
			String baseUrl = edmExportBOFormEDMData.getUrlBase();
			Bundle[] origBundles = item.getBundles("ORIGINAL");
			Bundle[] thumbBundles = item.getBundles("THUMBNAIL");
			if (thumbBundles.length > 0) {
				Bitstream[] bitstreams = thumbBundles[0].getBitstreams();
				if (bitstreams.length > 0) {
					Bitstream tb = bitstreams[0];
					try{
						thumbnail_url = baseUrl + "/retrieve/" + tb.getID() + "/" + 
						Util.encodeBitstreamName(tb.getName(), Constants.DEFAULT_ENCODING);
	                } catch(Exception e) {
						logger.debug("ESEExportXMLItem.processEuropeanaElements ", e);
	                }
				}
			}
			
			if (thumbnail_url != null) {
				parenElement.addContent(new Element("object", EUROPEANA).setText(thumbnail_url));
				checkElementFilled("object", EUROPEANA);
			}
			
			parenElement.addContent(new Element("provider", EUROPEANA).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("provider", EUROPEANA);
			
			parenElement.addContent(new Element("type", EUROPEANA).setText(processESEType(item, false)));
			
			String edmRights = this.edmExportBOFormEDMData.getEdmRights();
			parenElement.addContent(new Element("rights", EUROPEANA).setText(edmRights));
			checkElementFilled("rights", EUROPEANA);
			
			parenElement.addContent(new Element("dataProvider", EUROPEANA).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("dataProvider", EUROPEANA);
			
			String url = baseUrl + "/bitstream/" + item.getHandle();
			
			boolean isShown = false;

			for (Bundle bundle : origBundles) {
				Bitstream[] bitstreams = bundle.getBitstreams();
				if (bitstreams.length > 0) {
					Bitstream bitstream1 = bitstreams[0];
					String urlObject = url + "/" + bitstream1.getSequenceID() + "/"
                            + Util.encodeBitstreamName(bitstream1.getName(), Constants.DEFAULT_ENCODING);

					parenElement.addContent(new Element("isShownAt", EUROPEANA).setText(urlObject));
					checkElementFilled("isShownAt", EUROPEANA);
					isShown = true;

					for (Bitstream bt : bitstreams) {
						urlObject = url + "/" + bt.getSequenceID() + "/"
	                            + Util.encodeBitstreamName(bt.getName(), Constants.DEFAULT_ENCODING);

						parenElement.addContent(new Element("isShownBy", EUROPEANA).setText(urlObject));
						checkElementFilled("isShownBy", EUROPEANA);
						isShown = true;
					}
				}
			}
			
			if (!isShown) {
				parenElement.addContent(new Element("isShownAt", EUROPEANA).setText(handleUrl + item.getHandle()));
				checkElementFilled("isShownAt", EUROPEANA);
			}

		} catch (SQLException e) {
			logger.debug("ESEExportXMLItem.processEuropeanaElements ", e);
		} catch (UnsupportedEncodingException e) {
			logger.debug("ESEExportXMLItem.processEuropeanaElements ", e);
		}
		
	}
	
	
	
	/**
	 * Para generar los tipos (sólo permitidos TEXT, VIDEO, IMAGE, SOUND, 3D) en el formulario de ESE se asignaron
	 * las palabras que se asociarían a estos tipos, para poder buscarlas en el elemento dc.type y sustituirlas por
	 * los tipos asociados.
	 * 
	 * @param item objeto item de dspace {@link Item}
	 * @param booleano para recoger más de un valor
	 * @return cadena con los tipos encontrados
	 */
	protected String processESEType(Item item, boolean multiValued)
	{
		String edmTypeElement = null;
        try {
            edmTypeElement = item.getMetadata("europeana", "type", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (edmTypeElement != null && !edmTypeElement.isEmpty()) return edmTypeElement;
        
		StringBuilder edmType = new StringBuilder();
		DCValue[] elements = item.getDC("type", null, Item.ANY);
		if (elements.length > 0) {
			checkElementFilled("type", EUROPEANA);
			for (DCValue element : elements) {
				String value = element.value;
				//logger.debug("dc.type: " + value);
				for (String typeListPatterns : edmExportBOFormEDMData.getListTypes()) {
					String[] typePatternArr = typeListPatterns.split(",");
					logger.debug("dc.type patterns: " + Arrays.toString(typePatternArr));
					for (int i=0; i < typePatternArr.length; i++) {
						//logger.debug(value.toLowerCase() + " , " + typePatternArr[i].toLowerCase() + " ; " + edmType.toString().toLowerCase() + " , " + typePatternArr[0].toLowerCase());
						if (value.toLowerCase().indexOf(typePatternArr[i].toLowerCase()) >= 0 && edmType.toString().toLowerCase().indexOf(typePatternArr[0].toLowerCase()) < 0) {
							if (multiValued) edmType.append(typePatternArr[0]).append(',');
							else return typePatternArr[0];
						}
					}
				}
			}
		}
		return (edmType.length() > 0)?edmType.toString().substring(0, edmType.length() - 1):edmType.toString();
	}




}
