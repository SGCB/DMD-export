package org.dspace.EDMExport.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.app.util.MetadataExposure;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 * Clase abstracta para gestionar la creación de los ítems seleccionados en esquema EDM con formato xml
 * <p>Para cada uno de los ítems se llama a procesar el ítem para generar los elementos EDM</p>
 *
 */
@SuppressWarnings("deprecation")
public abstract class EDMExportXML
{
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Url de los namespace necesarios para el esquema EDM
	 */
	protected static final String NAMESPACE_URI_DCTERMS = "http://purl.org/dc/terms/";
	protected static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
	protected static final String NAMESPACE_URI_ENRICHMENT = "http://www.europeana.eu/schemas/edm/enrichment/";
	protected static final String NAMESPACE_URI_OWL = "http://www.w3.org/2002/07/owl#";
	protected static final String NAMESPACE_URI_WGS84 = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	protected static final String NAMESPACE_URI_SKOS = "http://www.w3.org/2004/02/skos/core#";
	protected static final String NAMESPACE_URI_ORE = "http://www.openarchives.org/ore/terms/";
	protected static final String NAMESPACE_URI_OAI = "http://www.openarchives.org/OAI/2.0/";
	protected static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String NAMESPACE_URI_DC = "http://purl.org/dc/elements/1.1/";
	protected static final String NAMESPACE_URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	protected static final String NAMESPACE_URI_XML = "http://www.w3.org/XML/1998/namespace";
	protected static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd";
	
	/**
	 * POJO {@link EDMExportServiceListItems} con la lista de los ítems con los que generar el EDM
	 */
	protected EDMExportServiceListItems edmExportServiceListItems;
	
	/**
	 * POJO {@link EDMExportBOFormEDMData} con los datos del formulario EDM
	 */
	protected EDMExportBOFormEDMData edmExportBOFormEDMData;
	
	/**
	 * Lista de elementos edm distintos creados en el xml para usar luego en la búsqueda sobre el xml
	 */
	protected List<String> listElementsFilled = new ArrayList<String>();
	
	/**
	 * Conjunto de elementos edm creados en el xml
	 */
	protected Set<String> setElementsFilled = new HashSet<String>();
	
	/**
	 * Cadena con la url del handle
	 */
	protected String handleUrl;
	
	/**
	 * Cadena con el prefijo de los handle
	 */
	protected String handlePrefix;
	
	/**
	 * Namespace necesarios para el esquema EDM
	 */
	protected Namespace DCTERMS = Namespace.getNamespace("dcterms", NAMESPACE_URI_DCTERMS);
	protected Namespace EDM = Namespace.getNamespace("edm", NAMESPACE_URI_EDM);
	protected Namespace ENRICHMENT = Namespace.getNamespace("enrichment", NAMESPACE_URI_ENRICHMENT);
	protected Namespace OWL = Namespace.getNamespace("owl", NAMESPACE_URI_OWL);
	protected Namespace WGS84 = Namespace.getNamespace("wgs84", NAMESPACE_URI_WGS84);
	protected Namespace SKOS = Namespace.getNamespace("skos", NAMESPACE_URI_SKOS);
	protected Namespace ORE = Namespace.getNamespace("ore", NAMESPACE_URI_ORE);
	protected Namespace OAI = Namespace.getNamespace("oai", NAMESPACE_URI_OAI);
	protected Namespace RDF = Namespace.getNamespace("rdf", NAMESPACE_URI_RDF);
	protected Namespace DC = Namespace.getNamespace("dc", NAMESPACE_URI_DC);
	protected Namespace XSI = Namespace.getNamespace("xsi", NAMESPACE_URI_XSI);
	protected Namespace XML = Namespace.getNamespace("xml", NAMESPACE_URI_XML);
	
	/**
	 * Prefijo para los handle por defecto
	 */
	private String handlePrefixDefault = "123456789";
	
	/**
	 * Constructor vacío
	 */
	public EDMExportXML()
	{
	}
	
	/**
	 * Constructor con el servicio {@link EDMExportServiceListItems} inyectado
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public EDMExportXML(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
		checkHandleUrl();
	}
	
	/**
	 * Inyección del servicio {@link EDMExportServiceListItems}
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public void setEdmExportServiceListItems(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
		checkHandleUrl();
	}
	
	/**
	 * Se comprueba la url de los handle
	 */
	private void checkHandleUrl()
	{
		handleUrl = null;
		handlePrefix = edmExportServiceListItems.getEDMExportServiceBase().getHandlePrefix();
		if (!handlePrefix.equals(handlePrefixDefault)) {
			String handleCanonicalPrefix = edmExportServiceListItems.getEDMExportServiceBase().getHandleCanonicalPrefix();
			if (isValidURI(handleCanonicalPrefix)) handleUrl = handleCanonicalPrefix + ((!handleCanonicalPrefix.endsWith("/"))?"/":"");
		}
	}
	
	/**
	 * Creación del esquema EDM con la lista de los ítems seleccionados
	 * <p>Se usa jdom para crear el documento xml</p>
	 * 
	 * @param edmExportBOFormEDMData POJO {@link EDMExportBOFormEDMData} con los datos del formulario
	 * @return cadena con el contenido xml
	 */
	public String showEDMXML(EDMExportBOFormEDMData edmExportBOFormEDMData)
	{
		this.edmExportBOFormEDMData = edmExportBOFormEDMData;
		
		if (handleUrl == null)
			handleUrl = this.edmExportBOFormEDMData.getUrlBase() + ((!this.edmExportBOFormEDMData.getUrlBase().endsWith("/"))?"/handle/":"handle/");
		
		//Element rdf_RDF = new Element("RDF", "rdf");
		Element rdf_RDF = new Element("RDF", RDF);
		
		rdf_RDF.addNamespaceDeclaration(DCTERMS);
		
		rdf_RDF.addNamespaceDeclaration(EDM);
		
		rdf_RDF.addNamespaceDeclaration(ENRICHMENT);
		
		rdf_RDF.addNamespaceDeclaration(OWL);
		
		rdf_RDF.addNamespaceDeclaration(WGS84);
		
		rdf_RDF.addNamespaceDeclaration(SKOS);
		
		rdf_RDF.addNamespaceDeclaration(ORE);
		
		rdf_RDF.addNamespaceDeclaration(OAI);
		
		rdf_RDF.addNamespaceDeclaration(RDF);
		
		rdf_RDF.addNamespaceDeclaration(DC);
		
		rdf_RDF.addNamespaceDeclaration(XSI);
		rdf_RDF.setAttribute("schemaLocation", NAMESPACE_URI_SCHEMALOCATION, XSI);
		
		Document doc = new Document(rdf_RDF);
		
		Map<Integer, EDMExportBOItem> mapItemsSubmit = edmExportServiceListItems.getMapItemsSubmit();
		logger.debug("Num items: " + mapItemsSubmit.size());
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			List<Element> listElements = processItemElement(item);
			for (Element element : listElements) {
				rdf_RDF.addContent(element);
			}
		}
		doc.setContent(rdf_RDF);
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		//logger.debug(xmlOutput.outputString(doc));
		return xmlOutput.outputString(doc);
	}
	

	/**
	 * Método abstracto para implementar la creación de un elemento rdf para un ítem
	 * 
	 * @param boItem POJO {@link EDMExportBOItem} con los datos del ítem
	 * @return una lista de elementos jdom para añadir al documento jdom
	 */
	protected abstract List<Element> processItemElement(EDMExportBOItem boItem);
	
	
	/**
	 * Creación de un elemento dc para añadir a la clase EDM ProvidedCHO
	 * <p>Actúa en las clases que heradena de ésta</p>
	 * 
	 * @param item objeto Item de dspace {@link Item} para obtener sus elementos dc
	 * @param elementEDM elemento EDM al que irán los datos
	 * @param nameSpace namespace del nuevo elemento EDM
	 * @param elementDC elemento DC del que se sacarán los datos
	 * @param qualifier calificador del elemento DC del que se sacarán los datos
	 * @param ProvidedCHO elemento jdom con la clase EDM ProvidedCHO
	 * @param repeat indica si se ha de buscar más elementos DC con ese nombre y calificador en el ítem
	 * @param resource indica si la autoridad se ha de añadir como resource
	 */
	protected Object[] createElementDC(Item item, String elementEDM, Namespace nameSpace, String elementDC, String qualifier
			, Element ProvidedCHO, boolean repeat, boolean resource)
	{
		if (qualifier != null && qualifier.equals(Item.ANY)) return null;
		Object[] elementsDom = createElementDC(item, elementEDM, nameSpace, elementDC, qualifier, ProvidedCHO, repeat);
		if (resource && elementsDom != null && elementsDom.length > 0) {
			for (int i=0; i < elementsDom.length; i+=2) {
				Element elementDom = (Element) elementsDom[i];
				DCValue elementDCV = (DCValue) elementsDom[i + 1];
				if (elementDCV.authority != null && !elementDCV.authority.isEmpty()) {
					String authority = checkAuthority(elementDCV.authority);
					if (authority == null) continue;
					elementDom.setAttribute("resource", authority, RDF);
				}
			}
		}
		return null;
	}
	
	/**
	 * Creación de un elemento dc para añadir a la clase EDM ProvidedCHO
	 * <p>Actúa en las clases que heradena de ésta</p>
	 * 
	 * @param item objeto Item de dspace {@link Item} para obtener sus elementos dc
	 * @param elementEDM elemento EDM al que irán los datos
	 * @param nameSpace namespace del nuevo elemento EDM
	 * @param elementDC elemento DC del que se sacarán los datos
	 * @param qualifier calificador del elemento DC del que se sacarán los datos
	 * @param ProvidedCHO elemento jdom con la clase EDM ProvidedCHO
	 * @param repeat indica si se ha de buscar más elementos DC con ese nombre y calificador en el ítem
	 * 
	 * @return array con pares de elementos Dom y DCValue
	 */
	protected Object[] createElementDC(Item item, String elementEDM, Namespace nameSpace, String elementDC, String qualifier
			, Element ProvidedCHO, boolean repeat)
	{
		try {
			if (MetadataExposure.isHidden(null, DC.getPrefix(), elementDC, qualifier)) return null;
		} catch (SQLException e) {
			logger.debug("EDMExportXML.createElementDC", e);
			return null;
		}
		ArrayList<Object> elementsDom = null;
		DCValue[] elements = item.getDC(elementDC, qualifier, Item.ANY);
		if (elements.length > 0) {
			elementsDom = new ArrayList<Object>();
			checkElementFilled(elementEDM, nameSpace);
			for (DCValue element : elements) {
				if (qualifier != null && qualifier.equals(Item.ANY)) {
					try {
						if (MetadataExposure.isHidden(null, DC.getPrefix(), element.element, element.qualifier)) continue;
					} catch (SQLException e) {
						logger.debug("EDMExportXML.createElementDC", e);
						continue;
					}
				}
				Element elementDom = new Element(elementEDM, nameSpace).setText(element.value);
				ProvidedCHO.addContent(elementDom);
				elementsDom.add(elementDom);
				elementsDom.add(element);
				if (!repeat) break;
			}
		}
		return (elementsDom != null)?elementsDom.toArray(new Object[elementsDom.size()]):null;
	}
	
	/**
	 * Creación de un elemento dc para añadir a la clase EDM ProvidedCHO
	 * <p>Actúa en las clases que heradena de ésta</p>
	 * 
	 * @param item objeto Item de dspace {@link Item} para obtener sus elementos dc
	 * @param elementEDM elemento EDM al que irán los datos
	 * @param nameSpace namespace del nuevo elemento EDM
	 * @param elementDC elemento DC del que se sacarán los datos
	 * @param noQualifier calificadores del elemento DC del que no se sacarán los datos
	 * @param ProvidedCHO elemento jdom con la clase EDM ProvidedCHO
	 * @param repeat indica si se ha de buscar más elementos DC con ese nombre y calificador en el ítem
	 * 
	 * @return eúltimo elemento Dom creado
	 */
	protected Element createElementDCExclusion(Item item, String elementEDM, Namespace nameSpace, String elementDC, Set<String> noQualifier, Element ProvidedCHO, boolean repeat)
	{
		Element elementDom = null;
		DCValue[] elements = item.getDC(elementDC, Item.ANY, Item.ANY);
		if (elements.length > 0) {
			checkElementFilled(elementEDM, nameSpace);
			for (DCValue element : elements) {
				if (noQualifier.contains(element.qualifier)) continue;
				try {
					if (MetadataExposure.isHidden(null, DC.getPrefix(), element.element, element.qualifier)) continue;
				} catch (SQLException e) {
					logger.debug("EDMExportXML.createElementDCExclusion", e);
					continue;
				}
				elementDom = new Element(elementEDM, nameSpace).setText(element.value);
				ProvidedCHO.addContent(elementDom);
				if (!repeat) break;
			}
		}
		return elementDom;
	}
	
	/**
	 * Comprueba si el elemento EDM ya está en la lista de elementos, sino lo añade
	 * 
	 * @param elementEDM cadena con el elemento EDM a comprobar
	 * @param nameSpace namespace del elemento
	 */
	protected void checkElementFilled(String elementEDM, Namespace nameSpace)
	{
		String elementName = nameSpace.getPrefix() + ":" + elementEDM;
		if (!setElementsFilled.contains(elementName)) {
			setElementsFilled.add(elementName);
			listElementsFilled.add(elementName);
		}
	}
	
	/**
	 * Devuelve la lista de los elementos EDM únicos creados en el xml
	 * 
	 * @return una lista de cadenas con los elementos EDM
	 */
	public List<String> getListElementsFilled()
	{
		return listElementsFilled;
	}
	
	/**
	 * Asigna la lista de los elementos EDM únicos creados en el xml
	 * 
	 * @param listElementsFilled lista de cadenas con los elementos EDM
	 */
	public void setListElementsFilled(List<String> listElementsFilled)
	{
		this.listElementsFilled = listElementsFilled;
	}
	
	/**
	 * Limpiar la lista de elementos edm únicos y el conjunto
	 */
	public void clear()
	{
		listElementsFilled.clear();
		setElementsFilled.clear();
	}
	
	
	/**
	 * Para generar los tipos (sólo permitidos TEXT, VIDEO, IMAGE, SOUND, 3D) en el formulario de EDM se asignaron
	 * las palabras que se asociarían a estos tipos, para poder buscarlas en el elemento dc.type y sustituirlas por
	 * los tipos asociados.
	 * 
	 * @param item objeto item de dspace {@link Item}
	 * @param booleano para recoger más de un valor
	 * @return cadena con los tipos encontrados
	 */
	protected String processEDMType(Item item, boolean multiValued)
	{
		String edmTypeElement = null;
        try {
            edmTypeElement = item.getMetadata("edm", "type", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (edmTypeElement != null && !edmTypeElement.isEmpty()) return edmTypeElement;
        
		StringBuilder edmType = new StringBuilder();
		DCValue[] elements = item.getDC("type", null, Item.ANY);
		if (elements.length > 0) {
			checkElementFilled("type", EDM);
			for (DCValue element : elements) {
				String value = element.value;
				//logger.debug("dc.type: " + value);
				for (String typeListPatterns : edmExportBOFormEDMData.getListTypes()) {
					String[] typePatternArr = typeListPatterns.split(",");
					logger.debug("dc.type patterns: " + Arrays.toString(typePatternArr));
					for (int i=1; i < typePatternArr.length; i++) {
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
	
	
	/**
	 * Comprueba si una autoridad es válida: o es una url correcta o un handle existente y se construye la url
	 * 
	 * @param authority la autoridad a comprobar
	 * @return la autoridad válida o un null
	 */
	protected String checkAuthority(String authority)
	{
		final String REGEX_HANDLE_PATTERN = "^\\d+/\\d+$";
		final String REGEX_HANDLE_VOCAB_PATTERN = "^.+_(\\d+_\\d+)$";
        Pattern patternHandleVocab = Pattern.compile(REGEX_HANDLE_VOCAB_PATTERN);

        if (!isValidURI(authority)) {
            try {
            	Matcher matcherHandleVocab = patternHandleVocab.matcher(authority);
                if (matcherHandleVocab.find()) authority = ((String) matcherHandleVocab.group(1)).replace('_', '/');
            	// comprobamos que es un handle y que existe en nuestro dspace
                if (authority.matches(REGEX_HANDLE_PATTERN) && edmExportServiceListItems.checkHandleItemDataBase(authority)) {
                    authority = handleUrl + authority;
                    return authority;
                } else return null;
            } catch (Exception e) {
            	logger.debug("EDMExportXML.checkAuthority authority", e);
            	return null;
            }
            // es una url válida
        } else return authority;
	}
	
	/**
	 * Comprueba que una cadena sea un URL válido
	 * 
	 * @param uriStr cadena a comprobar
	 * @return cierto si es válida
	 */
	protected boolean isValidURI(String uriStr)
    {
        try {
            URI uri = new URI(uriStr);
            uri.toURL();
            return true;
        } catch (URISyntaxException e) {
            return false;
        } catch (MalformedURLException e) {
        	return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
    }
	
}
