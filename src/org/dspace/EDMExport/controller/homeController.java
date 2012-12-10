package org.dspace.EDMExport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.bo.EDMExportBOListItems;
import org.dspace.EDMExport.bo.EDMExportBOSearch;
import org.dspace.EDMExport.service.EDMExportServiceListCollections;
import org.dspace.EDMExport.service.EDMExportServiceSearch;

@Controller
@RequestMapping("/")
public class homeController
{
	@Value("${list_coll.pagecount}")
    private String PageCount;
	
	@Value("${list_coll.itemspage}")
    private String ItemsPage;
	
	private EDMExportServiceListCollections edmExportServiceListCollections;
	private EDMExportServiceSearch edmExportServiceSearch;
	
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

	
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="error")
	public String getError(@RequestParam(value="error", required=true) String error, Model model)
	{
		EDMExportBOSearch searchBO = new EDMExportBOSearch();
		model.addAttribute("search", searchBO);
		model.addAttribute("error", 1);
		return "search";
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
	public String post(@ModelAttribute(value="listCollections") EDMExportBOListCollections listCollectionsBO, BindingResult result)
	{
		if (result.hasErrors()) {
			return "home";
		} else {
			return "listItems";
		}
	}
	
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params="pageAction=searchItems")
	public String postSearch(@ModelAttribute(value="search") EDMExportBOSearch searchBO, BindingResult result, Model model)
	{
		if (result.hasErrors()) {
			return "home";
		} else {
			EDMExportBOListItems boListIems = edmExportServiceSearch.getListItems(searchBO);
			if (boListIems.getListItems() == null || boListIems.getListItems().length == 0) {
				model.addAttribute("error", 1);
				return "redirect:home.htm";
			} else {
				model.addAttribute("listItems", boListIems);
				return "listItems";
			}
		}
	}
}
