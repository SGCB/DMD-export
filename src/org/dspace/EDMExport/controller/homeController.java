package org.dspace.EDMExport.controller;


import java.io.IOException;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.EDMExport.service.EDMExportServiceListCollections;
import org.dspace.EDMExport.service.EDMExportServiceListItems;
import org.dspace.EDMExport.service.EDMExportServiceSearch;

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
	
	private EDMExportServiceListCollections edmExportServiceListCollections;
	private EDMExportServiceSearch edmExportServiceSearch;
	private EDMExportServiceListItems edmExportServiceListItems;
	
	private int pageTotal = 0;
	private int hitCount = 0;
	private int listItemsPageInt;
	
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

	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="error")
	public String getError(@RequestParam(value="error", required=true) String error, Model model)
	{
		logger.debug("homeController.getError");
		EDMExportBOSearch searchBO = new EDMExportBOSearch();
		model.addAttribute("search", searchBO);
		model.addAttribute("error", 1);
		return "search";
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
			//for (int i=0; i < checked.length; i++)
			//logger.debug("referer: " + referer + " ; checked: " + checked[i]);
			EDMExportBOListItems boListItems = (referer.equals("listCollections"))?edmExportServiceListCollections.getBoListItems():edmExportServiceSearch.getBoListItems();
			edmExportServiceListItems.processEDMExportBOItemsChecked(boListItems, checked);
			edmExportServiceListItems.processEDMExportBOItemsNoChecked(boListItems, nochecked);
			logger.debug("Items checked: " + edmExportServiceListItems.getMapItemsSubmit().size());
		} catch (JsonParseException e) {
		    logger.debug("JsonParseException", e);
		} catch (IOException e) {
			logger.debug("IOException", e);
		}
		return Integer.valueOf(1);
	}
	
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","page"})
	public String getItemsPage(@RequestParam(value="referer", required=true) String referer, @RequestParam(value="page", required=true) String page, Model model)
	{
		logger.debug("homeController.getItemsPage");
		int pageInt = Integer.parseInt(page);
		if (pageInt < 0 || pageInt > pageTotal) return "redirect:home.htm";
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListItems = edmExportServiceListCollections.getListItems((pageInt - 1) * listItemsPageInt);
			if (boListItems.getListItems() == null || boListItems.getListItems().length == 0) {
				return "home";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				logger.debug("Showing items " + boListItems.getListItems().length);
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_page", pageInt - 1);
				return "listItems";
			}
		} else {
			EDMExportBOListItems boListItems = edmExportServiceSearch.getListItems((pageInt - 1) * listItemsPageInt);
			if (boListItems.getListItems() == null || boListItems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				logger.debug("Showing items " + boListItems.getListItems().length);
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_page", pageInt - 1);
				return "listItems";
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="referer")
	public String getShowAll(@RequestParam(value="referer", required=true) String referer, Model model)
	{
		logger.debug("homeController.getShowAll");
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListItems = edmExportServiceListCollections.getListItems(0, hitCount);
			if (boListItems.getListItems() == null || boListItems.getListItems().length == 0) {
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", hitCount);
				return "listItems";
			}
		} else {
			EDMExportBOListItems boListItems = edmExportServiceSearch.getListItems(0, hitCount);
			if (boListItems.getListItems() == null || boListItems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				edmExportServiceListItems.processEDMExportBOListItems(boListItems);
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListItems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", hitCount);
				return "listItems";
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String get(@RequestParam(value="tab", required=false) String tab, Model model)
	{
		logger.debug("homeController.get");
		if (tab == null || tab.isEmpty() || tab.equals("list")) {
			EDMExportBOListCollections listCollectionsBO = edmExportServiceListCollections.getListCollection();
			model.addAttribute("listCollections", listCollectionsBO);
			model.addAttribute("PageCount", PageCount);
			model.addAttribute("ItemsPage", ItemsPage);
			return "home";
		} else {
			EDMExportBOSearch searchBO = new EDMExportBOSearch();
			model.addAttribute("search", searchBO);
			return "search";
		}
	}
	 
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params="pageAction=listColls")
	public String post(@ModelAttribute(value="listCollections") EDMExportBOListCollections listCollectionsBO, BindingResult result, Model model)
	{
		logger.debug("homeController.post");
		if (result.hasErrors()) {
			return "home";
		} else {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			edmExportServiceListCollections.setBoListCollections(listCollectionsBO);
			edmExportServiceListCollections.clearBoListItems();
			edmExportServiceListItems.clearMapItemsSubmit();
			EDMExportBOListItems boListIems = edmExportServiceListCollections.getListItems(0, listItemsPageInt);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				return "home";
			} else {
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListIems);
				hitCount = edmExportServiceListCollections.getHitCount();
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", 1);
				pageTotal = getPageTotal(hitCount, listItemsPageInt);
				model.addAttribute("pageTotal", pageTotal);
				if (pageTotal > 1) model.addAttribute("next_page", 2);
				return "listItems";
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params="pageAction=searchItems")
	public String postSearch(@ModelAttribute(value="search") @Valid EDMExportBOSearch searchBO, BindingResult result, Model model)
	{
		logger.debug("homeController.postSearch");
		if (result.hasErrors()) {
			return "home";
		} else {
			listItemsPageInt = Integer.parseInt(listItemsPage);
			edmExportServiceSearch.setSearchBO(searchBO);
			edmExportServiceSearch.clearBoListItems();
			edmExportServiceListItems.clearMapItemsSubmit();
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(0, listItemsPageInt);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListIems);
				hitCount = edmExportServiceSearch.getHitCount();
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", 1);
				pageTotal = getPageTotal(hitCount, listItemsPageInt);
				model.addAttribute("pageTotal", pageTotal);
				if (pageTotal > 1) model.addAttribute("next_page", 2);
				return "listItems";
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
