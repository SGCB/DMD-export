package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

@SuppressWarnings("deprecation")
public abstract class EDMExportXML
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
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
	protected static final String NAMESPACE_URI_SCHEMALOCATION = "http://www.w3.org/1999/02/22-rdf-syntax-ns# EDM.xsd";
	
	
	protected EDMExportServiceListItems edmExportServiceListItems;
	protected EDMExportBOFormEDMData edmExportBOFormEDMData;
	
	protected List<String> listElementsFilled = new ArrayList<String>();
	protected Set<String> setElementsFilled = new HashSet<String>();
	
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
	

	protected abstract List<Element> processItemElement(EDMExportBOItem boItem);
	
	
	protected void createElementDC(Item item, String elementEDM, Namespace nameSpace, String elementDC, String qualifier, Element ProviderCHO, boolean repeat)
	{
		DCValue[] elements = item.getDC(elementDC, qualifier, Item.ANY);
		if (elements.length > 0) {
			checkElementFilled(elementEDM, nameSpace);
			for (DCValue element : elements) {
				ProviderCHO.addContent(new Element(elementEDM, nameSpace).setText(element.value));
				if (!repeat) break;
			}
		}
	}
	
	protected void checkElementFilled(String elementEDM, Namespace nameSpace)
	{
		String elementName = nameSpace.getPrefix() + ":" + elementEDM;
		if (!setElementsFilled.contains(elementName)) {
			setElementsFilled.add(elementName);
			listElementsFilled.add(elementName);
		}
	}
	
	public List<String> getListElementsFilled()
	{
		return listElementsFilled;
	}
	
	public void setListElementsFilled(List<String> listElementsFilled)
	{
		this.listElementsFilled = listElementsFilled;
	}
	
	public void clear()
	{
		listElementsFilled.clear();
		setElementsFilled.clear();
	}
	
}
