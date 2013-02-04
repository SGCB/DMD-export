package org.dspace.EDMExport.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dspace.EDMExport.bo.EDMExportBOItem;
import org.dspace.app.util.Util;
import org.dspace.constants.Constants;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.jdom.Attribute;
import org.jdom.Element;

@SuppressWarnings("deprecation")
public class EDMExportXMLItem extends EDMExportXML
{
	
	public EDMExportXMLItem()
	{
	}
	
	public EDMExportXMLItem(EDMExportServiceListItems edmExportServiceListItems)
	{
		super(edmExportServiceListItems);
	}

	@Override
	protected List<Element> processItemElement(EDMExportBOItem boItem)
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
			Bundle[] thumbBundles = edmExportServiceListItems.getDSPaceBundleItem(item, "THUMBNAIL");
			Bitstream[] bitstreams = origBundles[0].getBitstreams();
			if (bitstreams.length > 0) {
				Element WebResource = processWebResource(item, bitstreams[0]); 
				if (WebResource != null) listElements.add(WebResource);
				
				List<Element> listSkosConcept = processSkosConcept(itemDC); 
				if (listSkosConcept != null && listSkosConcept.size() > 0) {
					for (Element skosConceptElement : listSkosConcept)
						listElements.add(skosConceptElement);
				}
				Element oreAggregation = processOreAgreggation(item, origBundles, thumbBundles, bitstreams[0]); 
				if (oreAggregation != null) listElements.add(oreAggregation);
			}
		}
		
		return listElements;
	}
	
	private Element processProviderCHO(Item item)
	{
		Element ProviderCHO = new Element("ProviderCHO", EDM);
		
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
		
		String currentLocation = null;
        try {
            currentLocation = item.getMetadata("edm", "currentLocation", null, Item.ANY)[0].value;
        } catch (Exception e) {
        }
        if (currentLocation == null || currentLocation.isEmpty()) currentLocation = this.edmExportBOFormEDMData.getUrlBase() + "/" + item.getHandle();
        ProviderCHO.addContent(new Element("currentLocation", EDM).setText(currentLocation));
		checkElementFilled("currentLocation", EDM);
		
		ProviderCHO.addContent(new Element("type", EDM).setText(processEDMType(item)));
		
		return ProviderCHO;
	}
	
	
	private Element processWebResource(Item item, Bitstream bitstream)
	{
		Element WebResource = null;
		
		try {
			WebResource = new Element("WebResource", EDM);
			
			String url = this.edmExportBOFormEDMData.getUrlBase() + "/bitstreams/"
			+ item.getHandle() + "/" + bitstream.getSequenceID() + "/" + Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			WebResource.setAttribute(new Attribute("about", url, RDF));
			
			createElementDC(item, "rights", DC, "rights", null, WebResource, true);
			
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
	
	
	private List<Element> processSkosConcept(DCValue[] itemDC)
	{
		List<Element> listElementsSkosConcept = new ArrayList<Element>();
		final String REGEX_HANDLE_PATTERN = "^\\d+/\\d+$";
        String prefixUrl = this.edmExportBOFormEDMData.getUrlBase() + "/handle/";
		
		for (DCValue dcv : itemDC) {
			String authority;
			if (dcv.authority != null && !dcv.authority.isEmpty()) {
				if (!isValidURI(dcv.authority)) {
                    try {
                        if (dcv.authority.matches(REGEX_HANDLE_PATTERN) && edmExportServiceListItems.checkHandleItemDataBase(dcv.authority)) {
                            authority = prefixUrl + dcv.authority;
                        } else continue;
                    } catch (Exception e) {
                    	logger.debug("EDMExportXML.processSkosConcept authority", e);
                        continue;
                    }
                } else authority = dcv.authority;
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
	
	
	private Element processOreAgreggation(Item item, Bundle[] origBundles, Bundle[] thumbBundles, Bitstream bitstream)
	{
		
		Element oreAggregation = null;
		
		try {
			oreAggregation = new Element("Aggregation", ORE);
			
			String url = this.edmExportBOFormEDMData.getUrlBase() + "/" + item.getHandle();
			oreAggregation.setAttribute(new Attribute("about", url, RDF));
			
			createElementDC(item, "aggregatedCHO", EDM, "identifier", null, oreAggregation, false);
			
			oreAggregation.addContent(new Element("dataProvider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("dataProvider", EDM);
			
			String urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstreams/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/"
					+ Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			oreAggregation.addContent(new Element("isShownAt", EDM).setText(url));
			checkElementFilled("isShownAt", EDM);
			
			oreAggregation.addContent(new Element("isShownBy", EDM).setText(urlObject));
			checkElementFilled("isShownBy", EDM);
			
			oreAggregation.addContent(new Element("object", EDM).setText(urlObject));
			checkElementFilled("object", EDM);
			
			int i = 0;
			for (Bundle bundle : origBundles) {
				try {
					Bitstream[] bitstreamsOrig = bundle.getBitstreams();
					Bitstream[] bitstreamsThumb = null;
					if (thumbBundles.length > i && thumbBundles[i] != null) bitstreamsThumb = thumbBundles[i].getBitstreams();
					for (Bitstream bitstream1 : bitstreamsOrig) {
						urlObject = this.edmExportBOFormEDMData.getUrlBase() + "/bitstreams/"
								+ item.getHandle() + "/" + bitstream1.getSequenceID() + "/"
								+ Util.encodeBitstreamName(bitstream1.getName(), Constants.DEFAULT_ENCODING);
						String urlThumb = urlObject;
						if (bitstreamsThumb != null) {
							for (Bitstream bitThumb : bitstreamsThumb) {
								if (bitThumb.getSequenceID() == bitstream1.getSequenceID()) {
									urlThumb = this.edmExportBOFormEDMData.getUrlBase() + "/bitstreams/"
											+ item.getHandle() + "/" + bitThumb.getSequenceID() + "/"
											+ Util.encodeBitstreamName(bitThumb.getName(), Constants.DEFAULT_ENCODING);
									break;
								}
							}
						}
						
						oreAggregation.addContent(new Element("hasView", EDM).setText(urlThumb));
						checkElementFilled("hasView", EDM);
					}
				} catch (Exception ex) {
					logger.debug("EDMExportXML.processOreAgreggation", ex);
				}
				i++;
			}
			
			oreAggregation.addContent(new Element("provider", EDM).setText(this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()));
			checkElementFilled("provider", EDM);
			
			createElementDC(item, "rights", DC, "rights", null, oreAggregation, true);
			
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
			
			String urlObject = this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl() + "/bitstreams/"
					+ item.getHandle() + "/" + bitstream.getSequenceID() + "/"
					+ Util.encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
			
			String urlThumb = urlObject;
			if (bitstreamsThumb != null) {
				for (Bitstream bitThumb : bitstreamsThumb) {
					if (bitThumb.getSequenceID() == bitstream.getSequenceID()) {
						urlThumb = this.edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl() + "/bitstreams/"
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
	
	private String processEDMType(Item item)
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
							edmType.append(typePatternArr[0]).append(',');
						}
					}
				}
			}
		}
		return (edmType.length() > 0)?edmType.toString().substring(0, edmType.length() - 1):edmType.toString();
	}
	
	
	private boolean isValidURI(String uriStr)
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
