package org.dspace.EDMExport.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.EDMExport.bo.EDMExportBOUser;
import org.dspace.EDMExport.service.EDMExportServiceListCollections;
import org.dspace.EDMExport.service.EDMExportServiceListItems;
import org.dspace.EDMExport.service.EDMExportServiceSearch;
import org.dspace.EDMExport.service.EDMExportXML;

@Controller
@RequestMapping("/")
public class homeController
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	@Value("${list_coll.pagecount}")
    private String PageCount;
	
	@Value("${list_coll.itemspage}")
    private String ItemsPage;
	
	@Value("${list_items.itemspage}")
    private String listItemsPage;
	
	@Value("${selectedItems.Edmtype}")
	private String edmTypes;
		
	private EDMExportServiceListCollections edmExportServiceListCollections;
	private EDMExportServiceSearch edmExportServiceSearch;
	private EDMExportServiceListItems edmExportServiceListItems;
	private EDMExportXML edmExportXml;
	
	private int pageTotal = 0;
	private int hitCount = 0;
	private int listItemsPageInt;
	
	@Autowired
	private EDMExportBOUser edmExportBOUser;
	
	
	@Autowired
	public void setEdmExportServiceListCollections(EDMExportServiceListCollections edmExportServiceListCollections)
	{
		this.edmExportServiceListCollections = edmExportServiceListCollections;

	}
	
	@Autowired
	public void setEdmExportServiceSearch(EDMExportServiceSearch edmExportServiceSearch)
	{
		this.edmExportServiceSearch = edmExportServiceSearch;

	}

	@Autowired
	public void setEdmExportServiceListItems(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;

	}
	
	@Autowired
	public void setEDMExportXML(EDMExportXML edmExportXml)
	{
		this.edmExportXml = edmExportXml;

	}

	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="error=search")
	public String getError(@RequestParam(value="error", required=true) String error, Model model)
	{
		logger.debug("homeController.getError");
		EDMExportBOSearch searchBO = new EDMExportBOSearch();
		model.addAttribute("search", searchBO);
		model.addAttribute("error", 1);
		return returnView("search", model);
	}
	
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","checked","nochecked"})
	public @ResponseBody Integer getItemsCheck(@RequestParam(value="referer", required=true) String referer, 
		@RequestParam(value="checked", required=true) String checkedStr, @RequestParam(value="nochecked", required=true) String nocheckedStr, Model model)
	{
		logger.debug("homeController.getItemsCheck");
		ObjectMapper mapper = new ObjectMapper();
		ObjectMapper mapperNo = new ObjectMapper();
		try {
			String[] checked = mapper.readValue(checkedStr, new TypeReference<String[]>(){});
			String[] nochecked = mapperNo.readValue(nocheckedStr, new TypeReference<String[]>(){});
			logger.debug("referer: " + referer + " ; checked: " + checked.length + " ; nochecked: " + nochecked.length);
			for (int i=0; i < checked.length; i++)
			logger.debug("referer: " + referer + " ; checked: " + checked[i]);
			EDMExportBOListItems boListItems = (referer.equals("listCollections"))?edmExportServiceListCollections.getBoListItems():edmExportServiceSearch.getBoListItems();
			edmExportServiceListItems.processEDMExportBOItemsChecked(boListItems, checked);
			edmExportServiceListItems.processEDMExportBOItemsNoChecked(boListItems, nochecked);
			logger.debug("Items checked: " + edmExportServiceListItems.getMapItemsSubmit().size());
		} catch (JsonParseException e) {
		    logger.debug("JsonParseException", e);
		} catch (IOException e) {
			logger.debug("IOException", e);
		}
		return Integer.valueOf(edmExportServiceListItems.getMapItemsSubmit().size());
	}
	
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","page"})
	public String getItemsPage(@RequestParam(value="referer", required=true) String referer, @RequestParam(value="page", required=true) String page, Model model)
	{
		logger.debug("homeController.getItemsPage");
		int pageInt = Integer.parseInt(page);
		if (pageInt < 0 || pageInt > pageTotal) return "redirect:home.htm";
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListItems = edmExportServiceListCollections.getListItems((pageInt - 1) * listItemsPageInt);
			if (boListItems.isEmpty()) {
				if (pageInt > 1) {
					model.addAttribute("referer", "listCollections");
					model.addAttribute("page", 1);
				} else model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				logger.debug("Showing items " + boListItems.getListItems().size());
				model.addAttribute("referer", "listCollections");
				model.addAttribute("numItemsChecked", edmExportServiceListItems.getMapItemsSubmit().size());
				model.addAttribute("listItemsBO", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_page", pageInt - 1);
				return returnView("listItems", model);
			}
		} else {
			EDMExportBOListItems boListItems = edmExportServiceSearch.getListItems((pageInt - 1) * listItemsPageInt);
			if (boListItems.isEmpty()) {
				if (pageInt > 1) {
					model.addAttribute("referer", "search");
					model.addAttribute("page", 1);
				} else model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				logger.debug("Showing items " + boListItems.getListItems().size());
				model.addAttribute("referer", "search");
				model.addAttribute("numItemsChecked", edmExportServiceListItems.getMapItemsSubmit().size());
				model.addAttribute("listItemsBO", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_page", pageInt - 1);
				return returnView("listItems", model);
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="referer")
	public String getShowAll(@RequestParam(value="referer", required=true) String referer, Model model)
	{
		logger.debug("homeController.getShowAll");
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListItems = edmExportServiceListCollections.getListItems(0, hitCount);
			if (boListItems.isEmpty()) {
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				model.addAttribute("referer", "listCollections");
				model.addAttribute("numItemsChecked", edmExportServiceListItems.getMapItemsSubmit().size());
				model.addAttribute("listItemsBO", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", hitCount);
				return returnView("listItems", model);
			}
		} else {
			EDMExportBOListItems boListItems = edmExportServiceSearch.getListItems(0, hitCount);
			if (boListItems.isEmpty()) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				model.addAttribute("referer", "search");
				model.addAttribute("numItemsChecked", edmExportServiceListItems.getMapItemsSubmit().size());
				model.addAttribute("listItemsBO", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", hitCount);
				return returnView("listItems", model);
			}
		}
	}
	
	
	@RequestMapping(value = "/selectedItems.htm", method = RequestMethod.GET)
	public String getXML(Model model)
	{
		logger.debug("homeController.getXML");
		String[] edmTypesArr = edmTypes.split(",");
		EDMExportBOFormEDMData edmExportBOFormEDMData = new EDMExportBOFormEDMData(edmTypesArr, edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()
				, "", edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl(), "xml");
		List<String> listCollections = edmExportServiceListItems.getListCollections();
		model.addAttribute("FormEDMData", edmExportBOFormEDMData);
		model.addAttribute("listCollections", listCollections);
		model.addAttribute("listCollectionsCount", listCollections.size());
		model.addAttribute("selectedItemsCount", edmExportServiceListItems.getMapItemsSubmit().size());
		return returnView("selectedItems", model);
	}
	
	@RequestMapping(value = "/viewXml.htm", method = RequestMethod.GET)
	public String getViewXML(@ModelAttribute(value="FormEDMData") EDMExportBOFormEDMData edmExportBOFormEDMData,
			BindingResult result, Model model, HttpServletRequest request)
	{
		logger.debug("homeController.getViewXML");
		Map<String,?> map = RequestContextUtils.getInputFlashMap(request);
		if (map == null) {
			logger.debug("No FlashMap");
			return "redirect:selectedItems.htm";
		}
		if (result.hasErrors()) {
			logErrorValid(result);
			return "redirect:selectedItems.htm";
		} else {
			edmExportXml.setEdmExportServiceListItems(edmExportServiceListItems);
			String edmXML = edmExportXml.showEDMXML(edmExportBOFormEDMData);
			logger.debug(edmXML);
			model.addAttribute("edmXML", edmXML);
			model.addAttribute("listElementsFilled", edmExportXml.getListElementsFilled());
			return returnView("viewXml", model);
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String get(@RequestParam(value="tab", required=false) String tab, Model model)
	{
		logger.debug("homeController.get");
		getBOUserFromAuth();

		if (tab == null || tab.isEmpty() || tab.equals("list")) {
			EDMExportBOListCollections listCollectionsBO = edmExportServiceListCollections.getListCollection();
			model.addAttribute("listCollections", listCollectionsBO);
			model.addAttribute("PageCount", PageCount);
			model.addAttribute("ItemsPage", ItemsPage);
			return returnView("home", model);
		} else {
			EDMExportBOSearch searchBO = new EDMExportBOSearch();
			model.addAttribute("search", searchBO);
			return returnView("search", model);
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/getFile.htm", method = RequestMethod.POST)
	public HttpEntity<byte[]> postExport(@ModelAttribute(value="FormEDMData") @Valid EDMExportBOFormEDMData edmExportBOFormEDMData, 
			BindingResult result)
	{
		logger.debug("homeController.postExport");
		try {
			if (result.hasErrors()) {
				logErrorValid(result);
			} else {
				edmExportXml.setEdmExportServiceListItems(edmExportServiceListItems);
				String EDMXml = edmExportXml.showEDMXML(edmExportBOFormEDMData);
				if (EDMXml != null && !EDMXml.isEmpty()) {
					return getHttpEntityFromXml(EDMXml);
				}
			}
		} catch (Exception e) {
			logger.debug("homeController.postExport", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/getFile.htm", method = RequestMethod.POST, params="pageAction=exportView")
	public HttpEntity<byte[]> postExportView(@RequestParam("EDMXml") String EDMXml)
	{
		logger.debug("homeController.postExportView");
		try {
			if (EDMXml != null && !EDMXml.isEmpty()) {
				return getHttpEntityFromXml(EDMXml);
			}
		} catch (Exception e) {
			logger.debug("homeController.postExportView", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/selectedItems.htm", method = RequestMethod.POST, params="pageAction=xml")
	public String postXml(@ModelAttribute(value="FormEDMData") @Valid EDMExportBOFormEDMData boFormData, 
			BindingResult result, Model model, final RedirectAttributes redirectAttributes)
	{
		logger.debug("homeController.postXml ");
		if (result.hasErrors()) {
			logErrorValid(result);
			List<String> listCollections = edmExportServiceListItems.getListCollections();
			model.addAttribute("FormEDMData", boFormData);
			model.addAttribute("listCollections", listCollections);
			model.addAttribute("listCollectionsCount", listCollections.size());
			model.addAttribute("selectedItemsCount", edmExportServiceListItems.getMapItemsSubmit().size());
			return returnView("selectedItems", model);
		} else {
			boFormData.paddingTypes(edmTypes.split(","));
			redirectAttributes.addFlashAttribute("FormEDMData", boFormData);
			if (result.hasErrors()) {
				logErrorValid(result);
				List<String> listCollections = edmExportServiceListItems.getListCollections();
				model.addAttribute("FormEDMData", boFormData);
				model.addAttribute("listCollections", listCollections);
				model.addAttribute("listCollectionsCount", listCollections.size());
				model.addAttribute("selectedItemsCount", edmExportServiceListItems.getMapItemsSubmit().size());
				return returnView("selectedItems", model);
			}
			edmExportXml.clear();
			return "redirect:viewXml.htm";
		}
	}
	
	@RequestMapping(value = "/selectedItems.htm", method = RequestMethod.POST, params="referer")
	public String postListItems(@ModelAttribute(value="listItems") EDMExportBOListItems boListItems, BindingResult result, Model model)
	{
		logger.debug("homeController.postListItems");
		if (result.hasErrors()) {
			logErrorValid(result);
			return "redirect:home.htm";
		} else {
			String[] edmTypesArr = edmTypes.split(",");
			EDMExportBOFormEDMData edmExportBOFormEDMData = new EDMExportBOFormEDMData(edmTypesArr, edmExportServiceListItems.getEDMExportServiceBase().getDspaceName()
					, "", edmExportServiceListItems.getEDMExportServiceBase().getDspaceBaseUrl(), "xml");
			edmExportServiceListItems.processEDMExportBOListItems(boListItems);
			List<String> listCollections = edmExportServiceListItems.getListCollections();
			model.addAttribute("FormEDMData", edmExportBOFormEDMData);
			model.addAttribute("listCollections", listCollections);
			model.addAttribute("listCollectionsCount", listCollections.size());
			model.addAttribute("selectedItemsCount", edmExportServiceListItems.getMapItemsSubmit().size());
			return returnView("selectedItems", model);
		}
	}
	
	
	private HttpEntity<byte[]> getHttpEntityFromXml(String xml) throws UnsupportedEncodingException
	{
		byte[] EDMXmlBytes;
		EDMXmlBytes = xml.getBytes("UTF-8");
		HttpHeaders header = new HttpHeaders();
	    header.setContentType(new MediaType("text", "xml"));
	    header.set("Content-Disposition", "attachment; filename=EDMXml.xml");
	    header.setContentLength(EDMXmlBytes.length);
	    return new HttpEntity<byte[]>(EDMXmlBytes, header);
	}
	 
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params="pageAction=listColls")
	public String postListCollections(@ModelAttribute(value="listCollections") @Valid EDMExportBOListCollections listCollectionsBO, BindingResult result, Model model)
	{
		logger.debug("homeController.postListCollections");
		if (result.hasErrors()) {
			logErrorValid(result);
			return "redirect:home.htm";
		} else {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			edmExportServiceListCollections.setBoListCollections(listCollectionsBO);
			edmExportServiceListCollections.clearBoListItems();
			edmExportServiceListItems.clearMapItemsSubmit();
			EDMExportBOListItems boListIems = edmExportServiceListCollections.getListItems(0, listItemsPageInt);
			if (boListIems.isEmpty()) {
				return "redirect:home.htm";
			} else {
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItemsBO", boListIems);
				hitCount = edmExportServiceListCollections.getHitCount();
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", 1);
				pageTotal = getPageTotal(hitCount, listItemsPageInt);
				model.addAttribute("pageTotal", pageTotal);
				if (pageTotal > 1) model.addAttribute("next_page", 2);
				return returnView("listItems", model);
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params="pageAction=searchItems")
	public String postSearch(@ModelAttribute(value="search") @Valid EDMExportBOSearch searchBO, BindingResult result, Model model)
	{
		logger.debug("homeController.postSearch");
		if (result.hasErrors()) {
			logErrorValid(result);
			return returnView("search", model);
		} else {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			edmExportServiceSearch.setSearchBO(searchBO);
			edmExportServiceSearch.clearBoListItems();
			edmExportServiceListItems.clearMapItemsSubmit();
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(0, listItemsPageInt);
			if (boListIems.isEmpty()) {
				model.addAttribute("error", "search");
				return "redirect:home.htm";
			} else {
				model.addAttribute("referer", "search");
				model.addAttribute("listItemsBO", boListIems);
				hitCount = edmExportServiceSearch.getHitCount();
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", 1);
				pageTotal = getPageTotal(hitCount, listItemsPageInt);
				model.addAttribute("pageTotal", pageTotal);
				if (pageTotal > 1) model.addAttribute("next_page", 2);
				return returnView("listItems", model);
			}
		}
	}
	
	
	private void getBOUserFromAuth()
	{
		if (edmExportBOUser.getUsername() != null && !edmExportBOUser.getUsername().isEmpty()) return;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null) {
		    Authentication authentication = securityContext.getAuthentication();
		    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
		    	Object principal = authentication.getPrincipal();
		    	if (principal instanceof UserDetails) {
		    		edmExportBOUser.setUsername(((UserDetails) principal).getUsername());
		    		edmExportBOUser.setPassword(((UserDetails) principal).getPassword());
		    		edmExportBOUser.setAccess(1);
		    	} else if (principal instanceof java.lang.String) {
		    		edmExportBOUser.setUsername(principal.toString());
		    	}
		    }
		}
	}
	
	
	private String returnView(String view, Model model)
	{
		addCommonObjects2Model(model);
		return view;
	}
	
	
	private void addCommonObjects2Model(Model model)
	{
		model.addAttribute("edmExportBOUser", edmExportBOUser);
	}
	
	
	private void logErrorValid(BindingResult result)
	{
		for (Object object : result.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                logger.debug( fieldError.toString() );
            }
        }
	}
	
	private int getPageTotal(int hitCount, int listItemsPageInt)
	{
		if (hitCount < listItemsPageInt) return 1;
		else {
			int pageTotal = hitCount / listItemsPageInt;
			if (hitCount % listItemsPageInt > 0) pageTotal++;
			return pageTotal;
		}
	}
}
