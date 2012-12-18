package org.dspace.EDMExport.service;

import java.util.Iterator;
import java.util.Map;

import org.dspace.EDMExport.bo.EDMExportBOItem;
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
	public static final String NAMESPACE_URI_DCTERMS = "http://purl.org/dc/terms/";
	public static final String NAMESPACE_URI_EDM = "http://www.europeana.eu/schemas/edm/";
	public static final String NAMESPACE_URI_ENRICHMENT = "http://www.europeana.eu/schemas/edm/enrichment/";
	public static final String NAMESPACE_URI_OWL = "http://www.w3.org/2002/07/owl#";
	public static final String NAMESPACE_URI_WGS84 = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	public static final String NAMESPACE_URI_SKOS = "http://www.w3.org/2004/02/skos/core#";
	public static final String NAMESPACE_URI_ORE = "http://www.openarchives.org/ore/terms/";
	public static final String NAMESPACE_URI_OAI = "http://www.openarchives.org/OAI/2.0/";
	public static final String NAMESPACE_URI_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String NAMESPACE_URI_DC = "http://purl.org/dc/elements/1.1/";
	public static final String NAMESPACE_URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.w3.org/1999/02/22-rdf-syntax-ns# EDM.xsd";
	
	
	private EDMExportServiceListItems edmExportServiceListItems;
	
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
	
	public String showEDMXML()
	{
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
		rdf_RDF.setAttribute("schemaLocation", "NAMESPACE_URI_SCHEMALOCATION", XSI);
		
		Document doc = new Document(rdf_RDF);
		doc.setRootElement(rdf_RDF);
		
		Map<Integer, EDMExportBOItem> mapItemsSubmit = edmExportServiceListItems.getMapItemsSubmit();
		Iterator<Integer> it1 = mapItemsSubmit.keySet().iterator();
		while(it1.hasNext()) {
			int id = it1.next();
			EDMExportBOItem item = mapItemsSubmit.get(id);
			Element itemElement = processItemElement(item, rdf_RDF);
			rdf_RDF.addContent(itemElement);
		}
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}
	

	public Element processItemElement(EDMExportBOItem boItem, Element rdf_RDF)
	{
		Element itemEDM = new Element("staff");
		Item item = edmExportServiceListItems.getDSPaceItem(boItem);
		
		DCValue[] itemDC = item.getDC(Item.ANY, Item.ANY, Item.ANY);
		
		Element ProviderCHO = processProviderCHO(item);
		rdf_RDF.addContent(ProviderCHO);
		
		return itemEDM;
	}
	
	public Element processProviderCHO(Item item)
	{
		Element ProviderCHO = new Element("ProviderCHO", "edm");
		
		DCValue[] identifiers = item.getDC("identifier", "uri", null);
		if (identifiers.length > 0) ProviderCHO.setAttribute(new Attribute("rdf:about", identifiers[0].value));

		createElementDC(item, "contributor", "dc", "contributor", null, ProviderCHO, true);
		createElementDC(item, "contributor", "dc", "contributor", "*", ProviderCHO, true);
		
		createElementDC(item, "coverage", "dc", "coverage", null, ProviderCHO, true);
		
		createElementDC(item, "creator", "dc", "creator", null, ProviderCHO, true);
		
		createElementDC(item, "date", "dc", "date", null, ProviderCHO, true);
		
		createElementDC(item, "description", "dc", "description", null, ProviderCHO, true);
		createElementDC(item, "description", "dc", "description", "*", ProviderCHO, true);
		
		createElementDC(item, "format", "dc", "format", null, ProviderCHO, true);
		createElementDC(item, "format", "dc", "format", "*", ProviderCHO, true);
		
		createElementDC(item, "identifier", "dc", "identifier", null, ProviderCHO, true);
		createElementDC(item, "identifier", "dc", "identifier", "*", ProviderCHO, true);
		
		createElementDC(item, "language", "dc", "language", "iso", ProviderCHO, true);
		createElementDC(item, "language", "dc", "language", null, ProviderCHO, true);
		
		createElementDC(item, "publisher", "dc", "publisher", null, ProviderCHO, false);
		
		createElementDC(item, "relation", "dc", "relation", null, ProviderCHO, false);
		
		createElementDC(item, "rights", "dc", "rights", "holder", ProviderCHO, true);
		createElementDC(item, "rights", "dc", "rights", "uri", ProviderCHO, true);
		
		createElementDC(item, "source", "dc", "source", null, ProviderCHO, false);
		
		createElementDC(item, "subject", "dc", "subject", "*", ProviderCHO, true);
		createElementDC(item, "subject", "dc", "subject", null, ProviderCHO, true);
		
		createElementDC(item, "title", "dc", "title", null, ProviderCHO, true);
		
		createElementDC(item, "type", "dc", "type", null, ProviderCHO, true);
		
		createElementDC(item, "alternative", "dcterms", "title", "alternative", ProviderCHO, true);
		
		createElementDC(item, "created", "dcterms", "date", "created", ProviderCHO, true);
		
		createElementDC(item, "extent", "dcterms", "format", "extent", ProviderCHO, true);
		
		createElementDC(item, "hasFormat", "dcterms", "relation", "hasformatof", ProviderCHO, true);
		
		createElementDC(item, "hasPart", "dcterms", "relation", "haspart", ProviderCHO, true);
		
		createElementDC(item, "hasVersion", "dcterms", "relation", "hasversion", ProviderCHO, true);
		
		createElementDC(item, "isPartOf", "dcterms", "relation", "ispartof", ProviderCHO, true);
		createElementDC(item, "isPartOf", "dcterms", "relation", "ispartofseries", ProviderCHO, true);
		
		createElementDC(item, "isReferencedBy", "dcterms", "relation", "isreferencedby", ProviderCHO, true);
		
		createElementDC(item, "isReplacedBy", "dcterms", "relation", "isreplacedby", ProviderCHO, true);
		
		createElementDC(item, "issued", "dcterms", "date", "issued", ProviderCHO, true);
		
		createElementDC(item, "isVersionOf", "dcterms", "relation", "isversionof", ProviderCHO, true);
		
		createElementDC(item, "medium", "dcterms", "format", "medium", ProviderCHO, true);
		
		createElementDC(item, "provenance", "dcterms", "description", "provenance", ProviderCHO, true);
		
		createElementDC(item, "replaces", "dcterms", "relation", "replaces", ProviderCHO, true);
		
		createElementDC(item, "requires", "dcterms", "relation", "requires", ProviderCHO, true);
		
		createElementDC(item, "spatial", "dcterms", "coverage", "spatial", ProviderCHO, true);
		
		createElementDC(item, "tableOfContents", "dcterms", "description", "tableofcontents", ProviderCHO, true);
		
		createElementDC(item, "temporal", "dcterms", "coverage", "temporal", ProviderCHO, true);
		
		createElementDC(item, "temporal", "dcterms", "coverage", "temporal", ProviderCHO, true);
		
		return ProviderCHO;
	}
	
	private void createElementDC(Item item, String elementEDM, String nameSpace, String elementDC, String qualifier, Element ProviderCHO, boolean repeat)
	{
		DCValue[] elements = item.getDC(elementDC, qualifier, null);
		if (elements.length > 0) {
			for (DCValue element : elements) {
				ProviderCHO.addContent(new Element(elementEDM, nameSpace).setText(element.value));
				if (!repeat) break;
			}
		}
	}
}
