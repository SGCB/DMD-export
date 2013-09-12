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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.app.util.MetadataExposure;
import org.dspace.app.util.Util;
import org.dspace.constants.Constants;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 
 * Clase con la lógica para crear un elemento RDF del esquema EDM a partir de los datos de un POJO {@link EDMExportBOItem}
 * <p>Usa la librería jdom para crear el elemento RDF y todos sus hijos</p>
 * <p>Hereda de la clase {@link EDMExportXML} e implementa el método {@link #processItemElement}</p>
 *
 */
@SuppressWarnings("deprecation")
public class EDMExportXMLItem extends EDMExportXML
{
	
	/**
	 * Url de los namespace necesarios para el esquema EDM
	 */
	private static final String NAMESPACE_URI_DCTERMS = "http://purl.org/dc/terms/";
	private static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
	private static final String NAMESPACE_URI_ENRICHMENT = "http://www.europeana.eu/schemas/edm/enrichment/";
	private static final String NAMESPACE_URI_OWL = "http://www.w3.org/2002/07/owl#";
	private static final String NAMESPACE_URI_WGS84 = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	private static final String NAMESPACE_URI_SKOS = "http://www.w3.org/2004/02/skos/core#";
	private static final String NAMESPACE_URI_ORE = "http://www.openarchives.org/ore/terms/";
	private static final String NAMESPACE_URI_OAI = "http://www.openarchives.org/OAI/2.0/";
	private static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd";

	
	/**
	 * Namespace necesarios para el esquema EDM
	 */
	private Namespace DCTERMS = Namespace.getNamespace("dcterms", NAMESPACE_URI_DCTERMS);
	private Namespace EDM = Namespace.getNamespace("edm", NAMESPACE_URI_EDM);
	private Namespace ENRICHMENT = Namespace.getNamespace("enrichment", NAMESPACE_URI_ENRICHMENT);
	private Namespace OWL = Namespace.getNamespace("owl", NAMESPACE_URI_OWL);
	private Namespace WGS84 = Namespace.getNamespace("wgs84", NAMESPACE_URI_WGS84);
	private Namespace SKOS = Namespace.getNamespace("skos", NAMESPACE_URI_SKOS);
	private Namespace ORE = Namespace.getNamespace("ore", NAMESPACE_URI_ORE);
	private Namespace OAI = Namespace.getNamespace("oai", NAMESPACE_URI_OAI);
	private Namespace RDF = Namespace.getNamespace("rdf", NAMESPACE_URI_RDF);


	/**
	 * Constructor vacío
	 */
	public EDMExportXMLItem()
	{
	}
	
	/**
	 * Constructor con el servicio {@link EDMExportServiceListItems} inyectado
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public EDMExportXMLItem(EDMExportServiceListItems edmExportServiceListItems)
	{
		super(edmExportServiceListItems);
	}
	
	
	/**
	 * Creación del esquema EDM con la lista de los ítems seleccionados
	 * <p>Se usa jdom para crear el documento xml</p>
	 * 
	 * @param edmExportBOFormEDMData POJO {@link EDMExportBOFormEDMData} con los datos del formulario
	 * @return element jdom con el root
	 */
	public Element processXML()
	{		
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
		
		Map<Integer, EDMExportBOItem> mapItemsSubmit = edmExportServiceListItems.getMapItemsSubmit();
		logger.debug("Num items: " + mapItemsSubmit.size());
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			List<Element> listElements = processItemElement(item, rdf_RDF);
			for (Element element : listElements) {
				rdf_RDF.addContent(element);
			}
		}
		return rdf_RDF;
	}
	


	/**
	 * Procesa los datos del ítem para generar los hijos del element rdf del esquema EDM:
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
		
		// recogemos todos los elementos DC
		DCValue[] itemDC = item.getDC(Item.ANY, Item.ANY, Item.ANY);
		for (DCValue dcv : itemDC) {
			logger.debug(dcv.schema+"."+dcv.element+"."+dcv.qualifier+" = "+dcv.value);
		}
		
		// generamos ProvidedCHO
		Element ProvidedCHO = processProvidedCHO(item);
		listElements.add(ProvidedCHO);
		
		// generamos SkosConcept
		List<Element> listSkosConcept = processSkosConcept(item, itemDC); 
		if (listSkosConcept != null && listSkosConcept.size() > 0) {
			for (Element skosConceptElement : listSkosConcept)
				listElements.add(skosConceptElement);
		}
		
		if (origBundles.length > 0) {
			// recogemos los recursos electrónicos del tipo "thumbnail" del ítem
			Bundle[] thumbBundles = edmExportServiceListItems.getDSPaceBundleItem(item, "THUMBNAIL");
			Bitstream[] bitstreams = origBundles[0].getBitstreams();
			if (bitstreams.length > 0) {
				
				// generamos WebResource
				Element[] WebResources = processWebResource(item, origBundles); 
				if (WebResources != null && WebResources.length > 0) {
					for (Element element : WebResources)
						listElements.add(element);
				}
				
				// generamos oreAggregation
				Element oreAggregation = processOreAgreggation(item, origBundles, thumbBundles, bitstreams[0]); 
				if (oreAggregation != null) listElements.add(oreAggregation);
			}
		}
		
		return listElements;
	}
	
	/**
	 * Genera todos los elementos EDM de la clase ProvidedCHO que se pueden mapear desde DC
	 * <p>Para generar los tipos (sólo permitidos TEXT, VIDEO, IMAGE, SOUND, 3D) en el formulario de EDM se asignaron
	 * las palabras que se asociarían a estos tipos, para poder buscarlas en el elemento dc.type y sustituirlas por
	 * los tipos asociados.</p>
	 * 
	 * @param item objeto Item de dspace {@link Item}
	 * @return elemento jdom con la clase ProvidedCHO
	 */
	private Element processProvidedCHO(Item item)
	{
		Element ProvidedCHO = new Element("ProvidedCHO", EDM);
		
		String urlH = handleUrl + item.getHandle();
		DCValue[] identifiers = item.getDC("identifier", "uri", null);
		if (identifiers.length > 0) ProvidedCHO.setAttribute(new Attribute("about", identifiers[0].value, RDF));
		else ProvidedCHO.setAttribute(new Attribute("about", urlH, RDF));

		createElementDCExclusion(item, "contributor", DC, "contributor", new HashSet<String>(Arrays.asList("author")),
				ProvidedCHO, true, RDF);
		
		createElementDC(item, "coverage", DC, "coverage", null, ProvidedCHO, true);
		
		createElementDC(item, "creator", DC, "creator", null, ProvidedCHO, true, RDF);
		createElementDC(item, "creator", DC, "contributor", "author", ProvidedCHO, true, RDF);
		
		createElementDC(item, "date", DC, "date", null, ProvidedCHO, true);
		
		createElementDC(item, "description", DC, "description", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "format", DC, "format", Item.ANY, ProvidedCHO, true);
		
		// createElementDC(item, "identifier", DC, "identifier", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "language", DC, "language", "iso", ProvidedCHO, true);
		createElementDC(item, "language", DC, "language", null, ProvidedCHO, true);
		
		createElementDC(item, "publisher", DC, "publisher", null, ProvidedCHO, false);
		
		// createElementDC(item, "relation", DC, "relation", null, ProvidedCHO, false);
		
		createElementDC(item, "rights", DC, "rights", "holder", ProvidedCHO, true);
		createElementDC(item, "rights", DC, "rights", "uri", ProvidedCHO, true);
		
		createElementDC(item, "source", DC, "source", null, ProvidedCHO, false);
		
		createElementDC(item, "subject", DC, "subject", null, ProvidedCHO, true, RDF);
		createElementDC(item, "subject", DC, "subject", Item.ANY, ProvidedCHO, true, RDF);
		
		createElementDC(item, "title", DC, "title", null, ProvidedCHO, true);
		
		createElementDC(item, "type", DC, "type", null, ProvidedCHO, true);
		
		createElementDC(item, "alternative", DCTERMS, "title", "alternative", ProvidedCHO, true);
		
		createElementDC(item, "created", DCTERMS, "date", "created", ProvidedCHO, true);
		
		createElementDC(item, "extent", DCTERMS, "format", "extent", ProvidedCHO, true);

		createElementDC(item, "issued", DCTERMS, "date", "issued", ProvidedCHO, true);
		
		createElementDC(item, "medium", DCTERMS, "format", "medium", ProvidedCHO, true);
		
		createElementDC(item, "provenance", DCTERMS, "description", "provenance", ProvidedCHO, true);
		
		createElementDC(item, "isPartOf", DCTERMS, "relation", "isPartOf", ProvidedCHO, true);
		
		createElementDC(item, "isPartOf", DCTERMS, "relation", "ispartofseries", ProvidedCHO, true);
		
		createElementDC(item, "hasPart", DCTERMS, "relation", "hasPart", ProvidedCHO, true);
		
		createElementDC(item, "isRequiredBy", DCTERMS, "relation", "isRequiredBy", ProvidedCHO, true);
		
		createElementDC(item, "isReplacedBy", DCTERMS, "relation", "isReplacedBy", ProvidedCHO, true);
		
		createElementDC(item, "isVersionOf", DCTERMS, "relation", "isVersionOf", ProvidedCHO, true);
		
		createElementDC(item, "hasVersion", DCTERMS, "relation", "hasVersion", ProvidedCHO, true);
		
		createElementDC(item, "isFormatOf", DCTERMS, "relation", "isFormatOf", ProvidedCHO, true);
		
		createElementDC(item, "hasFormat", DCTERMS, "relation", "hasFormat", ProvidedCHO, true);
		
		createElementDC(item, "isReferencedBy", DCTERMS, "relation", "isReferencedBy", ProvidedCHO, true);
		
		createElementDC(item, "conformsTo", DCTERMS, "relation", "conformsTo", ProvidedCHO, true);
		
		createElementDC(item, "replaces", DCTERMS, "relation", "replaces", ProvidedCHO, true);
		
		createElementDC(item, "requires", DCTERMS, "relation", "requires", ProvidedCHO, true);
		
		createElementDCExclusion(item, "references", DCTERMS, "relation", 
				new HashSet<String>(Arrays.asList("isPartOf", "ispartofseries", "hasPart", "isRequiredBy", "isReplacedBy"
						, "isVersionOf", "hasVersion", "isFormatOf", "hasFormat", "isReferencedBy", "conformsTo"
						, "replaces", "requires")), ProvidedCHO, true, null);
		
		createElementDC(item, "spatial", DCTERMS, "coverage", "spatial", ProvidedCHO, true);
		
		createElementDC(item, "tableOfContents", DCTERMS, "description", "tableofcontents", ProvidedCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProvidedCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProvidedCHO, true);
		
		/*
		String currentLocation = null;
        try {
            currentLocation = item.getMetadata("edm", "currentLocation", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (currentLocation == null || currentLocation.isEmpty())
        	currentLocation = handleUrl + item.getHandle();
        ProvidedCHO.addContent(new Element("currentLocation", EDM).setText(currentLocation));
		checkElementFilled("currentLocation", EDM);
		*/
		
		ProvidedCHO.addContent(new Element("type", EDM).setText(processEDMType(item, false)));
		
		getOwlSameAs(item, ProvidedCHO);
		
		return ProvidedCHO;
	}
	
	
	/**
	 * Genera todos los elementos EDM de la clase WebResource que se pueden mapear desde DC y los recursos electrónicos
	 * <p>el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM</p>
	 * 
	 * @param item objeto Item de dspace {@link Item}
	 * @param bundles array objetos bundle de dspace {@link Bundle} con los recursos electrónicos de tipo "original"
	 * @return array elementos jdom con la clase WebResource
	 */
	private Element[] processWebResource(Item item, Bundle[] bundles)
	{
		List<Element> listWebResources = new ArrayList<Element>();

		Element WebResource = null;
		
		try {
			WebResource = new Element("WebResource", EDM);
			WebResource.setAttribute(new Attribute("about", handleUrl + item.getHandle() + "#&lt;/edm:isShownAt&gt", RDF));
			fillWebResource(item, WebResource);
			listWebResources.add(WebResource);
			
			for (Bundle bundle: bundles) {
				Bitstream[] bitstreams = bundle.getBitstreams();
				for (Bitstream bitstream: bitstreams) {
					// creamos el elemento WebResource
					WebResource = new Element("WebResource", EDM);
					
					// url del recurso
					String url = edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/" +
							Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
					
					WebResource.setAttribute(new Attribute("about", url, RDF));
					fillWebResource(item, WebResource);
					listWebResources.add(WebResource);
				}
			}
		} catch (Exception e) {
			logger.debug("EDMExportXML.processWebResource", e);
		}
		
		return listWebResources.toArray(new Element[listWebResources.size()]);
	}
	
	
	/**
	 * Rellenar con elementos dc y edm el WebResource
	 * 
	 * @param item objeto Item de Dspace
	 * @param WebResource elemento jdom padre
	 */
	private void fillWebResource(Item item, Element WebResource)
	{
		// creamos el elemento dc.rights
		createElementDC(item, "rights", DC, "rights", null, WebResource, true);
		
		createElementDC(item, "format", DC, "format", "mimetype", WebResource, true);
		
		createElementDC(item, "extend", DCTERMS, "format", "extend", WebResource, true);
		
		createElementDC(item, "issued", DCTERMS, "date", "available", WebResource, true);
		
		// creamos el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM
		String edmRights = null;
        try {
            edmRights = item.getMetadata("edm", "rights", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (edmRights == null || edmRights.isEmpty()) edmRights = this.edmExportBOFormEDMData.getEdmRights();
        WebResource.addContent(new Element("rights", EDM).setText(edmRights));
		checkElementFilled("rights", EDM);
	}
	
	
	/**
	 * Genera todos los elementos EDM de la clase SkosConcept que se pueden mapear desde las autoridades de los elementos DC
	 * <p>Las autoridades han de ser una URL bien formada o un handle que exista en la base de datos de dspace y a partir de
	 * cual se creará la URL desde la que se puede acceder en nuestro dspace</p>
	 * 
	 * @param item pojo dspace Item
	 * @param itemDC array con los elementos DC del ítem
	 * @return lista de elementos jdom con los Concept de Skos
	 */
	private List<Element> processSkosConcept(Item item, DCValue[] itemDC)
	{
		List<Element> listElementsSkosConcept = new ArrayList<Element>();
		
        // recorremos los elementos DC
		for (DCValue dcv : itemDC) {
			String authority;
			//  comprobamos si tiene autoridad
			if (dcv.authority != null && !dcv.authority.isEmpty()) {
				logger.debug("EDMExportXML.processSkosConcept " + dcv.element + "," + dcv.authority);
				// si no es una url válida, o es un handle del dspace se descarta
				authority = checkAuthority(dcv.authority);
				if (authority == null) continue;
				logger.debug("EDMExportXML.processSkosConcept " + authority);
				
				Item itemAuth = getItemFromAuthority(authority);
				// creamos el elemento Concept o Agent para la autoridad
				Element skosConcept = null;
				try {
					skosConcept = ((dcv.element.equals("creator") && dcv.qualifier == null) || (dcv.element.equals("contributor") && dcv.qualifier.equals("author")))?new Element("Agent", EDM):new Element("Concept", SKOS);
					skosConcept.setAttribute(new Attribute("about", authority, RDF));
					Element prefLabel = new Element("prefLabel", SKOS);
					if (dcv.language != null) prefLabel.setAttribute(new Attribute("lang", dcv.language, XML));
					prefLabel.setText(dcv.value);
					skosConcept.addContent(prefLabel);
					if (itemAuth != null) {
						logger.debug("itemAuth handle: " + itemAuth.getHandle());
						DCValue[] elementsTitleAlt = itemAuth.getDC("title", "alternative", dcv.language);
						if (elementsTitleAlt.length > 0) {
							Element altLabel;
							for (DCValue elementDCV : elementsTitleAlt) {
								altLabel = new Element("altLabel", SKOS);
								altLabel.setAttribute(new Attribute("lang", elementDCV.language, XML));
								altLabel.setText(elementDCV.value);
								skosConcept.addContent(altLabel);
							}
						}
						DCValue[] elementsDesc = itemAuth.getDC("description", null, dcv.language);
						if (elementsDesc.length > 0) {
							Element note;
							for (DCValue elementDCV : elementsDesc) {
								note = new Element("note", SKOS);
								note.setAttribute(new Attribute("lang", elementDCV.language, XML));
								note.setText(elementDCV.value);
								skosConcept.addContent(note);
							}
						}
						getOwlSameAs(itemAuth, skosConcept);
					}
					listElementsSkosConcept.add(skosConcept);
					checkElementFilled("Concept", SKOS);
				} catch (Exception e) {
					logger.debug("EDMExportXML.processSkosConcept", e);
				}
			}
		}
		
		return listElementsSkosConcept;
	}
	
	
	/**
	 * Genera todos los elementos EDM de la clase ProvidedCHO que se pueden mapear desde DC y los recursos electrónicos
	 * <p>Para cada uno de los recursos electrónicos crea un elemento hasView. con el pimer recursos electrónico además crea
	 * isShownBy y object.</p>
	 * <p>Las URL se generan a partir del handle del item {@link Item}, de la secuencia del bitstream {@link Bitstream} y
	 * del nombre del recurso</p>
	 * <p>el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM</p>
	 * 
	 * @param item objeto item de dspace {@link Item}
	 * @param origBundles array de {@link Bundle} recursos del tipo "original"
	 * @param thumbBundles array de {@link Bundle} recursos del tipo "thumbnail"
	 * @param bitstream primer recurso electrónico
	 * @return elemento jdom con la clase oreAggregation
	 */
	private Element processOreAgreggation(Item item, Bundle[] origBundles, Bundle[] thumbBundles, Bitstream bitstream)
	{
		
		Element oreAggregation = null;
		
		try {
			// creamos el elemento Aggregation
			oreAggregation = new Element("Aggregation", ORE);
			
			// creamos la url de nuestro item
			String url = handleUrl + item.getHandle();
			oreAggregation.setAttribute(new Attribute("about", url + "#aggregation", RDF));
			
			// creamos el elemento aggregatedCHO
			//createElementDC(item, "aggregatedCHO", EDM, "identifier", null, oreAggregation, false);
			Element aggregatedCHO = new Element("aggregatedCHO", EDM);
			aggregatedCHO.setAttribute("resource", url, RDF);
			oreAggregation.addContent(aggregatedCHO);
			checkElementFilled("aggregatedCHO", EDM);
			
			if (edmExportBOFormEDMData.isEdmUgc()) {
				oreAggregation.addContent(new Element("ugc", EDM).setText("true"));
				checkElementFilled("ugc", EDM);
			}
			
			// creamos el elemento dataProvider
			oreAggregation.addContent(new Element("dataProvider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("dataProvider", EDM);
			
			// url del primer recurso
			String urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/"
					+ Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			// creamos el elemento isShownAt
			oreAggregation.addContent(new Element("isShownAt", EDM).setAttribute("resource", url + "#&lt;/edm:isShownAt&gt", RDF));
			checkElementFilled("isShownAt", EDM);
			
			// creamos el elemento isShownBy
			oreAggregation.addContent(new Element("isShownBy", EDM).setAttribute("resource", urlObject, RDF));
			checkElementFilled("isShownBy", EDM);
			
			// creamos el elemento object
			if (thumbBundles != null && thumbBundles.length > 0) {
                Bitstream[] bitstreamsThumb = thumbBundles[0].getBitstreams();
                if (bitstreamsThumb != null && bitstreamsThumb.length > 0) {
                    String urlObjectThumb = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
                            + item.getHandle() + "/" + bitstreamsThumb[0].getSequenceID() + "/"
                            + Util.encodeBitstreamName(bitstreamsThumb[0].getName(), Constants.DEFAULT_ENCODING);
                    oreAggregation.addContent(new Element("object", EDM).setAttribute("resource", urlObjectThumb, RDF));
                    checkElementFilled("object", EDM);
                }
            }
			
			
			// recorremos todos los recursos
			int i = 0;
			for (Bundle bundle : origBundles) {
				try {
					Bitstream[] bitstreamsOrig = bundle.getBitstreams();
					for (Bitstream bitstream1 : bitstreamsOrig) {
						if (i++ == 0) continue;
						// url del recurso
						urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
								+ item.getHandle() + "/" + bitstream1.getSequenceID() + "/"
								+ Util.encodeBitstreamName(bitstream1.getName(), Constants.DEFAULT_ENCODING);

						// creamos el elemento hasView
						oreAggregation.addContent(new Element("hasView", EDM).setAttribute("resource", urlObject, RDF));
						checkElementFilled("hasView", EDM);
					}
				} catch (Exception ex) {
					logger.debug("EDMExportXML.processOreAgreggation", ex);
				}
			}
			
			// creamos el elemento provider
			oreAggregation.addContent(new Element("provider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("provider", EDM);
			
			// creamos el elemento dc.rights
			createElementDC(item, "rights", DC, "rights", null, oreAggregation, true);
			
			// creamos el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM
			String edmRights = null;
            try {
                edmRights = item.getMetadata("edm", "rights", null, Item.ANY)[0].value;
            } catch (Exception e) {
            }
            if (edmRights == null || edmRights.isEmpty()) edmRights = this.edmExportBOFormEDMData.getEdmRights();
			oreAggregation.addContent(new Element("rights", EDM).setText(edmRights));
			checkElementFilled("rights", EDM);
			
		} catch (Exception e) {
			logger.debug("EDMExportXML.processOreAgreggation", e);
		}
		
		return oreAggregation;
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
        
        boolean found = false;
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
							if (multiValued) {
								edmType.append(typePatternArr[0]).append(',');
								found = true;
							}
							else return typePatternArr[0];
						}
					}
				}
			}
		}
		if (!found) return "TEXT";
		return (edmType.length() > 0)?edmType.toString().substring(0, edmType.length() - 1):edmType.toString();
	}
	
	
	/**
	 * Recoger elementos owl:sameAs del ítem y añadirlos al elemento DOM
	 * 
	 * @param item item objeto item de dspace {@link Item}
	 * @param elementDOMParent elemento DOM al que añadir
	 */
	private void getOwlSameAs(Item item, Element elementDOMParent)
	{
		try {
			if (MetadataExposure.isHidden(null, OWL.getPrefix(), "sameAs", null)) return;
		} catch (SQLException e) {
			logger.debug("EDMExportXML.getOwlSameAs", e);
			return;
		}
		DCValue[] elements = item.getMetadata(OWL.getPrefix(), "sameAs", null, Item.ANY);
		if (elements.length > 0) {
			checkElementFilled("sameAs", OWL);
			for (DCValue element : elements) {
				Element elementDom = new Element("sameAs", OWL).setAttribute("resource", element.value, RDF);
				if (elementDOMParent != null) elementDOMParent.addContent(elementDom);
			}
		}
	}

	
}
