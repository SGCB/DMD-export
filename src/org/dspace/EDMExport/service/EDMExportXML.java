package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOItem;
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

@SuppressWarnings("deprecation")
public class EDMExportXML
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	private static final String NAMESPACE_URI_DCTERMS = "http://purl.org/dc/terms/";
	private static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
	private static final String NAMESPACE_URI_ENRICHMENT = "http://www.europeana.eu/schemas/edm/enrichment/";
	private static final String NAMESPACE_URI_OWL = "http://www.w3.org/2002/07/owl#";
	private static final String NAMESPACE_URI_WGS84 = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	private static final String NAMESPACE_URI_SKOS = "http://www.w3.org/2004/02/skos/core#";
	private static final String NAMESPACE_URI_ORE = "http://www.openarchives.org/ore/terms/";
	private static final String NAMESPACE_URI_OAI = "http://www.openarchives.org/OAI/2.0/";
	private static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String NAMESPACE_URI_DC = "http://purl.org/dc/elements/1.1/";
	private static final String NAMESPACE_URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.w3.org/1999/02/22-rdf-syntax-ns# EDM.xsd";
	
	
	private EDMExportServiceListItems edmExportServiceListItems;
	private EDMExportBOFormEDMData edmExportBOFormEDMData;
	
	private Namespace DCTERMS = Namespace.getNamespace("dcterms", NAMESPACE_URI_DCTERMS);
	private Namespace EDM = Namespace.getNamespace("edm", NAMESPACE_URI_EDM);
	private Namespace ENRICHMENT = Namespace.getNamespace("enrichment", NAMESPACE_URI_ENRICHMENT);
	private Namespace OWL = Namespace.getNamespace("owl", NAMESPACE_URI_OWL);
	private Namespace WGS84 = Namespace.getNamespace("wgs84", NAMESPACE_URI_WGS84);
	private Namespace SKOS = Namespace.getNamespace("skos", NAMESPACE_URI_SKOS);
	private Namespace ORE = Namespace.getNamespace("ore", NAMESPACE_URI_ORE);
	private Namespace OAI = Namespace.getNamespace("oai", NAMESPACE_URI_OAI);
	private Namespace RDF = Namespace.getNamespace("rdf", NAMESPACE_URI_RDF);
	private Namespace DC = Namespace.getNamespace("dc", NAMESPACE_URI_DC);
	private Namespace XSI = Namespace.getNamespace("xsi", NAMESPACE_URI_XSI);
	
	public EDMExportXML()
	{
	}
	
	public EDMExportXML(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
	}
	
	public void setEdmExportServiceListItems(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
	}
	
	public String showEDMXML(EDMExportBOFormEDMData edmExportBOFormEDMData)
	{
		this.edmExportBOFormEDMData = edmExportBOFormEDMData;
		
		Element rdf_RDF = new Element("RDF", "rdf");
		
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
	

	public List<Element> processItemElement(EDMExportBOItem boItem)
	{
		List<Element> listElements = new ArrayList<Element>();
		Item item = edmExportServiceListItems.getDSPaceItem(boItem);
		
		DCValue[] itemDC = item.getDC(Item.ANY, Item.ANY, Item.ANY);
		for (DCValue dcv : itemDC) {
			logger.debug(dcv.schema+"."+dcv.element+"."+dcv.qualifier+" = "+dcv.value);
		}
		
		Element ProviderCHO = processProviderCHO(item);
		listElements.add(ProviderCHO);
		
		Bundle[] origBundles = edmExportServiceListItems.getDSPaceBundleItem(item, "ORIGINAL");
		
		if (origBundles.length > 0) {
			Bitstream[] bitstreams = origBundles[0].getBitstreams();
			if (bitstreams.length > 0) {
				Element WebResource = processWebResource(item, bitstreams[0]); 
				if (WebResource != null) listElements.add(WebResource);
			}
		}
		
		return listElements;
	}
	
	private Element processProviderCHO(Item item)
	{
		Element ProviderCHO = new Element("ProviderCHO", "edm");
		
		DCValue[] identifiers = item.getDC("identifier", "uri", null);
		if (identifiers.length > 0) ProviderCHO.setAttribute(new Attribute("about", identifiers[0].value, RDF));

		createElementDC(item, "contributor", DC, "contributor", Item.ANY, ProviderCHO, true);
		
		createElementDC(item, "coverage", DC, "coverage", null, ProviderCHO, true);
		
		createElementDC(item, "creator", DC, "creator", null, ProviderCHO, true);
		
		createElementDC(item, "date", DC, "date", null, ProviderCHO, true);
		
		createElementDC(item, "description", DC, "description", Item.ANY, ProviderCHO, true);
		
		createElementDC(item, "format", DC, "format", Item.ANY, ProviderCHO, true);
		
		createElementDC(item, "identifier", DC, "identifier", Item.ANY, ProviderCHO, true);
		
		createElementDC(item, "language", DC, "language", "iso", ProviderCHO, true);
		createElementDC(item, "language", DC, "language", null, ProviderCHO, true);
		
		createElementDC(item, "publisher", DC, "publisher", null, ProviderCHO, false);
		
		createElementDC(item, "relation", DC, "relation", null, ProviderCHO, false);
		
		createElementDC(item, "rights", DC, "rights", "holder", ProviderCHO, true);
		createElementDC(item, "rights", DC, "rights", "uri", ProviderCHO, true);
		
		createElementDC(item, "source", DC, "source", null, ProviderCHO, false);
		
		createElementDC(item, "subject", DC, "subject", Item.ANY, ProviderCHO, true);
		
		createElementDC(item, "title", DC, "title", null, ProviderCHO, true);
		
		createElementDC(item, "type", DC, "type", null, ProviderCHO, true);
		
		createElementDC(item, "alternative", DCTERMS, "title", "alternative", ProviderCHO, true);
		
		createElementDC(item, "created", DCTERMS, "date", "created", ProviderCHO, true);
		
		createElementDC(item, "extent", DCTERMS, "format", "extent", ProviderCHO, true);
		
		createElementDC(item, "hasFormat", DCTERMS, "relation", "hasformatof", ProviderCHO, true);
		
		createElementDC(item, "hasPart", DCTERMS, "relation", "haspart", ProviderCHO, true);
		
		createElementDC(item, "hasVersion", DCTERMS, "relation", "hasversion", ProviderCHO, true);
		
		createElementDC(item, "isPartOf", DCTERMS, "relation", "ispartof", ProviderCHO, true);
		createElementDC(item, "isPartOf", DCTERMS, "relation", "ispartofseries", ProviderCHO, true);
		
		createElementDC(item, "isReferencedBy", DCTERMS, "relation", "isreferencedby", ProviderCHO, true);
		
		createElementDC(item, "isReplacedBy", DCTERMS, "relation", "isreplacedby", ProviderCHO, true);
		
		createElementDC(item, "issued", DCTERMS, "date", "issued", ProviderCHO, true);
		
		createElementDC(item, "isVersionOf", DCTERMS, "relation", "isversionof", ProviderCHO, true);
		
		createElementDC(item, "medium", DCTERMS, "format", "medium", ProviderCHO, true);
		
		createElementDC(item, "provenance", DCTERMS, "description", "provenance", ProviderCHO, true);
		
		createElementDC(item, "replaces", DCTERMS, "relation", "replaces", ProviderCHO, true);
		
		createElementDC(item, "requires", DCTERMS, "relation", "requires", ProviderCHO, true);
		
		createElementDC(item, "spatial", DCTERMS, "coverage", "spatial", ProviderCHO, true);
		
		createElementDC(item, "tableOfContents", DCTERMS, "description", "tableofcontents", ProviderCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProviderCHO, true);
		
		createElementDC(item, "temporal", DCTERMS, "coverage", "temporal", ProviderCHO, true);
		
		ProviderCHO.addContent(new Element("currentLocation", EDM).setText(this.edmExportBOFormEDMData.getCurrentLocation()));
		
		ProviderCHO.addContent(new Element("type", EDM).setText(processEDMType(item)));
		
		return ProviderCHO;
	}
	
	
	private Element processWebResource(Item item, Bitstream bitstream)
	{
		Element WebResource = null;
		
		try {
			WebResource = new Element("WebResource", "edm");
			
			String url = this.edmExportBOFormEDMData.getCurrentLocation() + "/retrieve/" + bitstream.getID()
					+ "/" + Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			WebResource.setAttribute(new Attribute("about", url, RDF));
			
			createElementDC(item, "rights", DC, "rights", null, WebResource, true);
		} catch (Exception e) {
			logger.debug("EDMExportXML.processWebResource", e);
		}
		
		return WebResource;
	}
	
	
	private String processEDMType(Item item)
	{
		StringBuilder edmType = new StringBuilder();
		DCValue[] elements = item.getDC("type", null, Item.ANY);
		if (elements.length > 0) {
			for (DCValue element : elements) {
				String value = element.value;
				//logger.debug("dc.type: " + value);
				for (String typeListPatterns : edmExportBOFormEDMData.getListTypes()) {
					String[] typePatternArr = typeListPatterns.split(",");
					logger.debug("dc.type patterns: " + Arrays.toString(typePatternArr));
					for (int i=1; i < typePatternArr.length; i++) {
						if (value.toLowerCase().indexOf(typePatternArr[i].toLowerCase()) >= 0 && edmType.toString().toLowerCase().indexOf(typePatternArr[i].toLowerCase()) < 0) {
							edmType.append(typePatternArr[0]).append(',');
						}
					}
				}
			}
		}
		return (edmType.length() > 0)?edmType.toString().substring(0, edmType.length() - 1):edmType.toString();
	}
	
	private void createElementDC(Item item, String elementEDM, Namespace nameSpace, String elementDC, String qualifier, Element ProviderCHO, boolean repeat)
	{
		DCValue[] elements = item.getDC(elementDC, qualifier, Item.ANY);
		if (elements.length > 0) {
			for (DCValue element : elements) {
				ProviderCHO.addContent(new Element(elementEDM, nameSpace).setText(element.value));
				if (!repeat) break;
			}
		}
	}
	
}
