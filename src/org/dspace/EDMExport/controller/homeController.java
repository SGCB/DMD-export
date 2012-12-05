package org.dspace.EDMExport.controller;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springmodules.validation.commons.DefaultBeanValidator;

import org.dspace.EDMExport.bo.EDMExportBOListCollections;
import org.dspace.EDMExport.service.EDMExportServiceListCollections;

@Controller
@RequestMapping("/")
public class homeController
{
	@Value("${list_coll.pagecount}")
    private String PageCount;
	
	@Value("${list_coll.itemspage}")
    private String ItemsPage;
	
	private DefaultBeanValidator validator = null;
	private EDMExportServiceListCollections edmExportServiceListCollections;
	
	public DefaultBeanValidator getBeanValidator()
	{
		return validator;
    }
	
	@Autowired
	public void setBeanValidator(DefaultBeanValidator validator)
	{
		this.validator = validator;

	}
	
	@Autowired
	public void setEdmExportServiceListCollections(EDMExportServiceListCollections edmExportServiceListCollections)
	{
		this.edmExportServiceListCollections = edmExportServiceListCollections;

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
			return "search";
		}
	}
	 
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST)
	public String post(@ModelAttribute(value="listCollections") EDMExportBOListCollections listCollectionsBO, BindingResult result)
	{
		if (result.hasErrors()) {
			return "registration";
		} else {
			return "success";
		}
	}
}
