package org.dspace.EDMExport.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.app.util.Util;
import org.dspace.constants.Constants;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.jdom.Attribute;
import org.jdom.Element;

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
	 * Constructor vacío
	 */
	public EDMExportXMLItem()
	{
	}
	
	/**
	 * Constructor que inyecta el servicio del lista de ítems {@link EDMExportServiceListItems}
	 * <p>Llama al constructor de la clase padre</p>
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public EDMExportXMLItem(EDMExportServiceListItems edmExportServiceListItems)
	{
		super(edmExportServiceListItems);
	}

	/**
	 * Procesa los datos del ítem para generar los hijos del element rdf del esquema EDM:
	 * <p>ProvidedCHO, WebResource, SkosConcept y oreAggregation</p>
	 * 
	 * @param boItem POJO {@link EDMExportBOItem} con los datos del ítem
	 * @return lista de elementos jdom con las clases de EDM ProvidedCHO, WebResource, SkosConcept y oreAggregation
	 */
	@Override
	protected List<Element> processItemElement(EDMExportBOItem boItem)
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
		List<Element> listSkosConcept = processSkosConcept(itemDC); 
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
				Element WebResource = processWebResource(item, bitstreams[0]); 
				if (WebResource != null) listElements.add(WebResource);
				
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
		
		DCValue[] identifiers = item.getDC("identifier", "uri", null);
		if (identifiers.length > 0) ProvidedCHO.setAttribute(new Attribute("about", identifiers[0].value, RDF));

		createElementDC(item, "contributor", DC, "contributor", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "coverage", DC, "coverage", null, ProvidedCHO, true);
		
		createElementDC(item, "creator", DC, "creator", null, ProvidedCHO, true);
		
		createElementDC(item, "date", DC, "date", null, ProvidedCHO, true);
		
		createElementDC(item, "description", DC, "description", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "format", DC, "format", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "identifier", DC, "identifier", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "language", DC, "language", "iso", ProvidedCHO, true);
		createElementDC(item, "language", DC, "language", null, ProvidedCHO, true);
		
		createElementDC(item, "publisher", DC, "publisher", null, ProvidedCHO, false);
		
		createElementDC(item, "relation", DC, "relation", null, ProvidedCHO, false);
		
		createElementDC(item, "rights", DC, "rights", "holder", ProvidedCHO, true);
		createElementDC(item, "rights", DC, "rights", "uri", ProvidedCHO, true);
		
		createElementDC(item, "source", DC, "source", null, ProvidedCHO, false);
		
		createElementDC(item, "subject", DC, "subject", Item.ANY, ProvidedCHO, true);
		
		createElementDC(item, "title", DC, "title", null, ProvidedCHO, true);
		
		createElementDC(item, "type", DC, "type", null, ProvidedCHO, true);
		
		createElementDC(item, "alternative", DCTERMS, "title", "alternative", ProvidedCHO, true);
		
		createElementDC(item, "created", DCTERMS, "date", "created", ProvidedCHO, true);
		
		createElementDC(item, "extent", DCTERMS, "format", "extent", ProvidedCHO, true);
		
		createElementDC(item, "hasFormat", DCTERMS, "relation", "hasformatof", ProvidedCHO, true);
		
		createElementDC(item, "hasPart", DCTERMS, "relation", "haspart", ProvidedCHO, true);
		
		createElementDC(item, "hasVersion", DCTERMS, "relation", "hasversion", ProvidedCHO, true);
		
		createElementDC(item, "isPartOf", DCTERMS, "relation", "ispartof", ProvidedCHO, true);
		createElementDC(item, "isPartOf", DCTERMS, "relation", "ispartofseries", ProvidedCHO, true);
		
		createElementDC(item, "isReferencedBy", DCTERMS, "relation", "isreferencedby", ProvidedCHO, true);
		
		createElementDC(item, "isReplacedBy", DCTERMS, "relation", "isreplacedby", ProvidedCHO, true);
		
		createElementDC(item, "issued", DCTERMS, "date", "issued", ProvidedCHO, true);
		
		createElementDC(item, "isVersionOf", DCTERMS, "relation", "isversionof", ProvidedCHO, true);
		
		createElementDC(item, "medium", DCTERMS, "format", "medium", ProvidedCHO, true);
		
		createElementDC(item, "provenance", DCTERMS, "description", "provenance", ProvidedCHO, true);
		
		createElementDC(item, "replaces", DCTERMS, "relation", "replaces", ProvidedCHO, true);
		
		createElementDC(item, "requires", DCTERMS, "relation", "requires", ProvidedCHO, true);
		
		createElementDC(item, "spatial", DCTERMS, "coverage", "spatial", ProvidedCHO, true);
		
		createElementDC(item, "tableOfContents", DCTERMS, "description", "tableofcontents", ProvidedCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProvidedCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProvidedCHO, true);
		
		String currentLocation = null;
        try {
            currentLocation = item.getMetadata("edm", "currentLocation", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (currentLocation == null || currentLocation.isEmpty()) currentLocation = this.edmExportBOFormEDMData.getUrlBase() + "/" + item.getHandle();
        ProvidedCHO.addContent(new Element("currentLocation", EDM).setText(currentLocation));
		checkElementFilled("currentLocation", EDM);
		
		ProvidedCHO.addContent(new Element("type", EDM).setText(processEDMType(item, false)));
		
		return ProvidedCHO;
	}
	
	
	/**
	 * Genera todos los elementos EDM de la clase WebResource que se pueden mapear desde DC y el primer recurso electrónico
	 * <p>el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM</p>
	 * 
	 * @param item objeto Item de dspace {@link Item}
	 * @param bitstream objeto bitstream de dspace {@link Bitstream} con el primer recurso electrónico de tipo "original"
	 * @return elemento jdom con la clase WebResource
	 */
	private Element processWebResource(Item item, Bitstream bitstream)
	{
		Element WebResource = null;
		
		try {
			// creamos el elemento Aggregation
			WebResource = new Element("WebResource", EDM);
			
			// url del primer recurso
			String url = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
			+ item.getHandle() + "/" + bitstream.getSequenceID() + "/" + Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			WebResource.setAttribute(new Attribute("about", url, RDF));
			
			// creamos el elemento dc.rights
			createElementDC(item, "rights", DC, "rights", null, WebResource, true);
			
			// creamos el elemento edm.rights, buscamos si existe un edm.rights, si no lo cogemos del formulario EDM
			String edmRights = null;
            try {
                edmRights = item.getMetadata("edm", "rights", null, Item.ANY)[0].value;
            } catch (Exception e) {
            }
            if (edmRights == null || edmRights.isEmpty()) edmRights = this.edmExportBOFormEDMData.getEdmRights();
            WebResource.addContent(new Element("rights", EDM).setText(edmRights));
			checkElementFilled("rights", EDM);
		} catch (Exception e) {
			logger.debug("EDMExportXML.processWebResource", e);
		}
		
		return WebResource;
	}
	
	
	/**
	 * Genera todos los elementos EDM de la clase SkosConcept que se pueden mapear desde las autoridades de los elementos DC
	 * <p>Las autoridades han de ser una URL bien formada o un handle que exista en la base de datos de dspace y a partir de
	 * cual se creará la URL desde la que se puede acceder en nuestro dspace</p>
	 * 
	 * @param itemDC array con los elementos DC del ítem
	 * @return lista de elementos jdom con los Concept de Skos
	 */
	private List<Element> processSkosConcept(DCValue[] itemDC)
	{
		List<Element> listElementsSkosConcept = new ArrayList<Element>();
		final String REGEX_HANDLE_PATTERN = "^\\d+/\\d+$";
		final String REGEX_HANDLE_VOCAB_PATTERN = "^.+_(\\d+_\\d+)$";
        Pattern patternHandleVocab = Pattern.compile(REGEX_HANDLE_VOCAB_PATTERN);
        String prefixUrl = this.edmExportBOFormEDMData.getUrlBase() + "/handle/";
		
        // recorremos los elementos DC
		for (DCValue dcv : itemDC) {
			String authority;
			//  comprobamos si tiene autoridad
			if (dcv.authority != null && !dcv.authority.isEmpty()) {
				logger.debug("EDMExportXML.processSkosConcept " + dcv.element + "," + dcv.authority);
				// si no es una url válida, o es un handle del dspace o se descarta
				if (!isValidURI(dcv.authority)) {
                    try {
                    	Matcher matcherHandleVocab = patternHandleVocab.matcher(dcv.authority);
                        if (matcherHandleVocab.find()) dcv.authority = ((String) matcherHandleVocab.group(1)).replace('_', '/');
                    	// comprobamos que es un handle y que existe en nuestro dspace
                        if (dcv.authority.matches(REGEX_HANDLE_PATTERN) && edmExportServiceListItems.checkHandleItemDataBase(dcv.authority)) {
                            authority = prefixUrl + dcv.authority;
                        } else continue;
                    } catch (Exception e) {
                    	logger.debug("EDMExportXML.processSkosConcept authority", e);
                        continue;
                    }
                    // es una url válida
                } else authority = dcv.authority;
				logger.debug("EDMExportXML.processSkosConcept " + authority);
				
				// creamos el elemento Concept para la autoridad
				Element skosConcept = null;
				try {
					skosConcept = new Element("Concept", SKOS);
					skosConcept.setAttribute(new Attribute("about", authority, RDF));
					Element prefLabel = new Element("prefLabel", SKOS);
					if (dcv.language != null) prefLabel.setAttribute(new Attribute("lang", dcv.language, XML));
					prefLabel.setText(dcv.value);
					skosConcept.addContent(prefLabel);
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
			String url = this.edmExportBOFormEDMData.getUrlBase() + "/handle/" + item.getHandle();
			oreAggregation.setAttribute(new Attribute("about", url, RDF));
			
			// creamos el elemento aggregatedCHO
			createElementDC(item, "aggregatedCHO", EDM, "identifier", null, oreAggregation, false);
			
			// creamos el elemento dataProvider
			oreAggregation.addContent(new Element("dataProvider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("dataProvider", EDM);
			
			// url del primer recurso
			String urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/"
					+ Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			// creamos el elemento isShownAt
			oreAggregation.addContent(new Element("isShownAt", EDM).setText(url));
			checkElementFilled("isShownAt", EDM);
			
			// creamos el elemento isShownBy
			oreAggregation.addContent(new Element("isShownBy", EDM).setText(urlObject));
			checkElementFilled("isShownBy", EDM);
			
			// creamos el elemento object
			oreAggregation.addContent(new Element("object", EDM).setText(urlObject));
			checkElementFilled("object", EDM);
			
			// recorremos todos los recursos
			int i = 0;
			for (Bundle bundle : origBundles) {
				try {
					Bitstream[] bitstreamsOrig = bundle.getBitstreams();
					Bitstream[] bitstreamsThumb = null;
					if (thumbBundles.length > i && thumbBundles[i] != null) bitstreamsThumb = thumbBundles[i].getBitstreams();
					for (Bitstream bitstream1 : bitstreamsOrig) {
						// url del recurso
						urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
								+ item.getHandle() + "/" + bitstream1.getSequenceID() + "/"
								+ Util.encodeBitstreamName(bitstream1.getName(), Constants.DEFAULT_ENCODING);
						// comprobar que tiene thumbnail o nos quedamos con la original
						String urlThumb = urlObject;
						if (bitstreamsThumb != null) {
							for (Bitstream bitThumb : bitstreamsThumb) {
								if (bitThumb.getSequenceID() == bitstream1.getSequenceID()) {
									urlThumb = this.edmExportBOFormEDMData.getUrlBase() + "/bitstream/"
											+ item.getHandle() + "/" + bitThumb.getSequenceID() + "/"
											+ Util.encodeBitstreamName(bitThumb.getName(), Constants.DEFAULT_ENCODING);
									break;
								}
							}
						}
						
						// creamos el elemento hasView
						oreAggregation.addContent(new Element("hasView", EDM).setText(urlThumb));
						checkElementFilled("hasView", EDM);
					}
				} catch (Exception ex) {
					logger.debug("EDMExportXML.processOreAgreggation", ex);
				}
				i++;
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
	
	/*
	private List<Element> processOreAgreggation(Item item, Bundle[] origBundles, Bundle[] thumbBundles)
	{
		List<Element> listElementsOreAggregation = new ArrayList<Element>();
		
		int i = 0;
		for (Bundle bundle : origBundles) {
			Bitstream[] bitstreamsOrig = bundle.getBitstreams();
			Bitstream[] bitstreamsThumb = null;
			if (thumbBundles.length > i && thumbBundles[i] != null) bitstreamsThumb = thumbBundles[i].getBitstreams();
			int j = 0;
			for (Bitstream bitstream : bitstreamsOrig) {
				Element elementOreAggregation = null;
				if ((elementOreAggregation = processElementOreAgreggation(item, bundle, bitstream, bitstreamsThumb, j)) != null)
					listElementsOreAggregation.add(elementOreAggregation);
				j++;
			}
			i++;
		}
		
		return listElementsOreAggregation;
	}
	
	private Element processElementOreAgreggation(Item item, Bundle origBundle, Bitstream bitstream,
			Bitstream[] bitstreamsThumb, int index)
	{
		Element oreAggregation = null;
		
		try {
			oreAggregation = new Element("Aggregation", ORE);
			
			String url = this.edmExportBOFormEDMData.getUrlBase() + "/" + item.getHandle();
			oreAggregation.setAttribute(new Attribute("about", url, RDF));
			
			createElementDC(item, "aggregatedCHO", EDM, "identifier", null, oreAggregation, false);
			
			oreAggregation.addContent(new Element("dataProvider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			
			String urlObject = this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl() + "/bitstream/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/"
					+ Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			String urlThumb = urlObject;
			if (bitstreamsThumb != null) {
				for (Bitstream bitThumb : bitstreamsThumb) {
					if (bitThumb.getSequenceID() == bitstream.getSequenceID()) {
						urlThumb = this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl() + "/bitstream/"
								+ item.getHandle() + "/" + bitThumb.getSequenceID() + "/"
								+ Util.encodeBitstreamName(bitThumb.getName(), Constants.DEFAULT_ENCODING);
						break;
					}
				}
			}
			
			oreAggregation.addContent(new Element("hasView", EDM).setText(urlThumb));
			checkElementFilled("hasView", EDM);
			
			oreAggregation.addContent(new Element("isShownAt", EDM).setText(url));
			checkElementFilled("isShownAt", EDM);
			
			oreAggregation.addContent(new Element("isShownBy", EDM).setText(urlObject));
			checkElementFilled("isShownBy", EDM);
			
			oreAggregation.addContent(new Element("object", EDM).setText(urlObject));
			checkElementFilled("object", EDM);
			
			oreAggregation.addContent(new Element("provider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("provider", EDM);
			
			createElementDC(item, "rights", DC, "rights", null, oreAggregation, true);
			
			oreAggregation.addContent(new Element("rights", EDM).setText(this.edmExportBOFormEDMData.getEdmRights()));
			checkElementFilled("rights", EDM);
		} catch (Exception e) {
			logger.debug("EDMExportXML.processOreAgreggation", e);
		}
		
		return oreAggregation;
	}
	*/
	
	
}
