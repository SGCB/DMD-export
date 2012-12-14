package org.dspace.EDMExport.controller;


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
		EDMExportBOSearch searchBO = new EDMExportBOSearch();
		model.addAttribute("search", searchBO);
		model.addAttribute("error", 1);
		return "search";
	}
	
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"checked","nochecked"})
	public @ResponseBody Integer getItemsCheck(@RequestParam(value="checked", required=true) String checked, @RequestParam(value="nochecked", required=true) String nochecked, Model model)
	{
		return Integer.valueOf(1);
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","page"})
	public String getItemsPage(@RequestParam(value="referer", required=true) String referer, @RequestParam(value="page", required=true) String page, Model model)
	{
		int pageInt = Integer.parseInt(page);
		if (pageInt < 0 || pageInt > pageTotal) return "home";
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListIems = edmExportServiceListCollections.getListItems(pageInt * listItemsPageInt);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				return "home";
			} else {
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListIems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_pag", pageInt - 1);
				return "listItems";
			}
		} else {
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(pageInt * listItemsPageInt);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "home";
			} else {
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListIems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				model.addAttribute("page", page);
				model.addAttribute("pageTotal", pageTotal);
				if (pageInt < pageTotal) model.addAttribute("next_page", pageInt + 1);
				if (pageInt > 1) model.addAttribute("prev_pag", pageInt - 1);
				return "listItems";
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="referer")
	public String getShowAll(@RequestParam(value="referer", required=true) String referer, Model model)
	{
		if (referer.equals("listCollections")) {
			EDMExportBOListItems boListIems = edmExportServiceListCollections.getListItems(0);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				return "home";
			} else {
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListIems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				return "listItems";
			}
		} else {
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(0);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "home";
			} else {
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListIems);
				model.addAttribute("hitCount", hitCount);
				model.addAttribute("listItemsPage", (hitCount < listItemsPageInt)?hitCount:listItemsPage);
				return "listItems";
			}
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String get(@RequestParam(value="tab", required=false) String tab, Model model)
	{
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
		
		if (result.hasErrors()) {
			return "home";
		} else {
			edmExportServiceListCollections.setBoListCollections(listCollectionsBO);
			EDMExportBOListItems boListIems = edmExportServiceListCollections.getListItems(0);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				return "home";
			} else {
				model.addAttribute("referer", "listCollections");
				model.addAttribute("listItems", boListIems);
				hitCount = edmExportServiceListCollections.getHitCount();
				listItemsPageInt = Integer.parseInt(listItemsPage);
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
	public String postSearch(@ModelAttribute(value="search") EDMExportBOSearch searchBO, BindingResult result, Model model)
	{
		if (result.hasErrors()) {
			return "home";
		} else {
			edmExportServiceSearch.setSearchBO(searchBO);
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(0);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				model.addAttribute("referer", "search");
				model.addAttribute("listItems", boListIems);
				hitCount = edmExportServiceSearch.getHitCount();
				listItemsPageInt = Integer.parseInt(listItemsPage);
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
