package org.dspace.EDMExport.controller;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
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


import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Clase que gestiona todas las peticiones web cuando ya estamos validados.
 * Es un controller que mapea las url con ruta /
 *
 */

@Controller
@RequestMapping("/")
public class homeController
{
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 *  Variables capturadas del fichero de configuración WebContent/WEB-INF/classes/edmexport.properties
	 */
	
	@Value("${list_coll.pagecount}")
    private String PageCount;
	
	@Value("${list_coll.itemspage}")
    private String ItemsPage;
	
	@Value("${list_items.itemspage}")
    private String listItemsPage;
	
	@Value("${selectedItems.Edmtype}")
	private String edmTypes;
		
	/**
	 * Listado de colecciones {@link EDMExportServiceListCollections}
	 */
	private EDMExportServiceListCollections edmExportServiceListCollections;
	
	/**
	 * Búsqueda e ítems {@link EDMExportServiceSearch}
	 */
	private EDMExportServiceSearch edmExportServiceSearch;
	
	/**
	 * Listados de ítems {@link EDMExportServiceListItems}
	 */
	private EDMExportServiceListItems edmExportServiceListItems;
	
	/**
	 * Edm xml {@link EDMExportXML}
	 */
	private EDMExportXML edmExportXml;
	
	
	/**
	 * páginas totales a listar
	 */
	private int pageTotal = 0;
	
	/**
	 * número total de elementos a recogidos
	 */
	private int hitCount = 0;
	
	/**
	 * número de ítems por página
	 */
	private int listItemsPageInt;
	
	/**
	 * Objeto de datos de usuario inyectado {@link EDMExportBOUser}
	 */
	@Autowired
	private EDMExportBOUser edmExportBOUser;
	
	/**
	 * Objeto de lógica de la lista de colecciones inyectado
	 * 
	 * @param edmExportServiceListCollections lógica listado de colecciones {@link EDMExportServiceListCollections}
	 */
	@Autowired
	public void setEdmExportServiceListCollections(EDMExportServiceListCollections edmExportServiceListCollections)
	{
		this.edmExportServiceListCollections = edmExportServiceListCollections;

	}
	
	/**
	 * Objeto de lógica de búsqueda inyectado
	 * 
	 * @param edmExportServiceSearch lógica de la búsqueda {@link EDMExportServiceSearch}
	 */
	@Autowired
	public void setEdmExportServiceSearch(EDMExportServiceSearch edmExportServiceSearch)
	{
		this.edmExportServiceSearch = edmExportServiceSearch;

	}

	/**
	 * Objeto de lógica de listado de ítems inyectado
	 * 
	 * @param edmExportServiceListItems lógica de listado de ítems {@link EDMExportServiceListItems}
	 */
	@Autowired
	public void setEdmExportServiceListItems(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;

	}
	
	/**
	 * Objeto de lógica de exportar a xml inyectado
	 * 
	 * @param edmExportXml lógica de EDM en xml {@link EDMExportXML}
	 */
	@Autowired
	public void setEDMExportXML(EDMExportXML edmExportXml)
	{
		this.edmExportXml = edmExportXml;

	}
	
	
	
	/**
	 * Método para el listado de los ítems que nos aumenta el número que podemos unir al enviarlos por POST
	 * 
	 * @param dataBinder objeto WebDataBinder con el objeto a unir a nuestra variable
	 */
	@InitBinder("listItems")
    public void initBinder(WebDataBinder dataBinder)
	{
		logger.debug("homeController.initBinder size: " + dataBinder.getAutoGrowCollectionLimit());
		int newSize = edmExportServiceListItems.getMapItemsSubmit().size() + 1;
		if (dataBinder.getAutoGrowCollectionLimit() < newSize) {
			dataBinder.setAutoGrowCollectionLimit(newSize);
			logger.debug("homeController.resizeAutoGrowCollectionLimit new size: " + newSize);
		}
    }
	
	
	// CONTROLLERS PARA MÉTODO GET
	
	
	/**
	 * Controller para la url /home.htm con método GET y con parámetro "error" obligatorio igual a "search".
	 * Pasamos a la vista un error y el objeto de la búsqueda para regenerar el formulario de la búsqueda con datos. 
	 * 
	 * @param error cadena con el valor del parámetro error
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params="error=search")
	public String getError(@RequestParam(value="error", required=true) String error, Model model)
	{
		logger.debug("homeController.getError");
		EDMExportBOSearch searchBO = new EDMExportBOSearch();
		model.addAttribute("search", searchBO);
		model.addAttribute("error", 1);
		return returnView("search", model);
	}
	
	/**
	 * Controller para la url /home.htm con método GET y con parámetro "referer","checked","nochecked" obligatorios.
	 * Devuelve un entero y no una vista porque este método se llama desde una petición Ajax
	 * 
	 * @param referer cadena con el valor del parámetro referer. Puede venir de la búsqueda o del listado de colecciones
	 * @param checkedStr cadena en Json con el valor del parámetro checked
	 * @param nocheckedStr cadena en Json  con el valor del parámetro nochecked
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return devuelve un entero con el número de ítems seleccionados
	 */
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","checked","nochecked"})
	public @ResponseBody Integer getItemsCheck(@RequestParam(value="referer", required=true) String referer, 
		@RequestParam(value="checked", required=true) String checkedStr, @RequestParam(value="nochecked", required=true) String nocheckedStr, Model model)
	{
		logger.debug("homeController.getItemsCheck");
		ObjectMapper mapper = new ObjectMapper();
		ObjectMapper mapperNo = new ObjectMapper();
		try {
			// decodificamos el json
			String[] checked = mapper.readValue(checkedStr, new TypeReference<String[]>(){});
			String[] nochecked = mapperNo.readValue(nocheckedStr, new TypeReference<String[]>(){});
			logger.debug("referer: " + referer + " ; checked: " + checked.length + " ; nochecked: " + nochecked.length);
			for (int i=0; i < checked.length; i++)
			logger.debug("referer: " + referer + " ; checked: " + checked[i]);
			// recogemos los ítems con origen el listado de colecciones o desde la búsqueda
			EDMExportBOListItems boListItems = (referer.equals("listCollections"))?edmExportServiceListCollections.getBoListItems():edmExportServiceSearch.getBoListItems();
			// guardamos los ítems BO seleccionados y quitamos el resto
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
	
	
	/**
	 * Controller para la url /home.htm con método GET y con parámetros "referer" y "page" obligatorios.
	 * Se muestran los ítems de la página pasada como parámetro.
	 * Si hay algún error se redirecciona a home.htm, si todo está bien devuelve la cadena de la vista a renderizar y mostrar
	 * 
	 * @param referer cadena con el valor del parámetro referer. Nos dice si viene del listado de colecciones o de la búsqueda.
	 * @param page cadena con el valor del parámetro page
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET, params={"referer","page"})
	public String getItemsPage(@RequestParam(value="referer", required=true) String referer, @RequestParam(value="page", required=true) String page, Model model)
	{
		logger.debug("homeController.getItemsPage");
		int pageInt = Integer.parseInt(page);
		if (pageInt < 0 || pageInt > pageTotal) return "redirect:home.htm";
		
		// se viene de la lista de colecciones
		if (referer.equals("listCollections")) {
			// recogemos los ítems de la página
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
			// se viene de la búsqueda
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
	
	/**
	 * Controller para la url /home.htm con método GET y con parámetro "referer" obligatorio.
	 * Se muestran todos los ítems de las colecciones seleccionadas o del criterio de búsqueda aplicado, sin paginado.
	 * Si hay algún error se redirecciona a home.htm, si todo está bien devuelve la cadena de la vista a renderizar y mostrar
	 * 
	 * @param referer cadena con el valor del parámetro referer. Nos dice si viene del listado de colecciones o de la búsqueda.
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	/**
	 * Controller para la url /selectedItems.htm con método GET y sin parámetros.
	 * Nos muestra el formulario con la configuración de elementos EDM para mostrar el xml de los ítems seleccionados
	 * 
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	/**
	 * Controller para la url /viewXml.htm con método GET y sin parámetros.
	 * Se recogen los datos del formulario con la configuración de elementos EDM para mostrar el xml de los ítems seleccionados.
	 * Si hay errores se redirige a la lista de ítems seleccionados, si no se muestra el xml con los datos en EDM.
	 * Si el xml a mostrar excede los 2MB y no se comprime con el servlet se comprime el contenido y se envía también.
	 * 
	 * @param edmExportBOFormEDMData objeto que recoge los datos pasados del formulario {@link EDMExportBOFormEDMData}
	 * @param result objeto con el que se une la petición y se valida {@link BindingResult}
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @param request objeto de la petición http para recoger los datos del objeto flash (como la sesión pero dura una petición)
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
			String edmXMLEncoded = "";
			String encoding = request.getHeader("Content-Encoding");
			if (edmXML.length() > 2000000 && (encoding == null || (encoding != null && encoding.isEmpty()))) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				BufferedWriter writer = null;
				Base64OutputStream b64os = null;
				GZIPOutputStream gzip = null;
				try {
					/*writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8"));
					writer.write(edmXML);
					byte[] encoded = Base64.encodeBase64(output.toByteArray());
					edmXMLEncoded = new String(encoded);
					*/
					b64os = new Base64OutputStream(output);
		            gzip = new GZIPOutputStream(b64os);
		            gzip.write(edmXML.getBytes("UTF-8"));
					edmXMLEncoded = output.toString();
				} catch (IOException e) {
					logger.debug("IOException", e);
				} finally {
					try {
						if (writer != null) writer.close();
						if (output != null) output.close();
						if (gzip != null) gzip.close();
						if (b64os != null) b64os.close();
					} catch (IOException e) {
						logger.debug("IOException", e);
					}
					
				}
			}
			model.addAttribute("edmXML", edmXML);
			model.addAttribute("edmXMLEncoded", edmXMLEncoded);
			model.addAttribute("listElementsFilled", edmExportXml.getListElementsFilled());
			return returnView("viewXml", model);
		}
	}
	
	
	/**
	 * Controller para la url /home.htm con método GET y con parámetro "tab" opcional.
	 * Dependiendo de la pestaña seleccionada se mostrará el listado de colecciones o la búsqueda.
	 * 
	 * @param tab cadena con la pestaña de la página a mostrar
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
			model.addAttribute("tab", "list");
			return returnView("home", model);
		} else {
			EDMExportBOSearch searchBO = new EDMExportBOSearch();
			model.addAttribute("search", searchBO);
			model.addAttribute("tab", tab);
			return returnView("search", model);
		}
	}
	
	
	// CONTROLLERS PARA MÉTODO POST
	
	
	/**
	 * Controller para la url /home.htm con método POST y con parámetro "referer","checked","nochecked" obligatorios.
	 * Devuelve un entero y no una vista porque este método se llama desde una petición Ajax
	 * 
	 * @param referer cadena con el valor del parámetro referer. Puede venir de la búsqueda o del listado de colecciones
	 * @param checkedStr cadena en Json con el valor del parámetro checked
	 * @param nocheckedStr cadena en Json  con el valor del parámetro nochecked
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return devuelve un entero con el número de ítems seleccionados
	 */
	@RequestMapping(value = "/home.htm", method = RequestMethod.POST, params={"referer","checked","nochecked"})
	public @ResponseBody Integer getItemsCheckPost(@RequestParam(value="referer", required=true) String referer, 
		@RequestParam(value="checked", required=true) String checkedStr, @RequestParam(value="nochecked", required=true) String nocheckedStr, Model model)
	{
		logger.debug("homeController.getItemsCheckPost");
		ObjectMapper mapper = new ObjectMapper();
		ObjectMapper mapperNo = new ObjectMapper();
		try {
			// decodificamos el json
			String[] checked = mapper.readValue(checkedStr, new TypeReference<String[]>(){});
			String[] nochecked = mapperNo.readValue(nocheckedStr, new TypeReference<String[]>(){});
			logger.debug("referer: " + referer + " ; checked: " + checked.length + " ; nochecked: " + nochecked.length);
			for (int i=0; i < checked.length; i++)
			logger.debug("referer: " + referer + " ; checked: " + checked[i]);
			// recogemos los ítems con origen el listado de colecciones o desde la búsqueda
			EDMExportBOListItems boListItems = (referer.equals("listCollections"))?edmExportServiceListCollections.getBoListItems():edmExportServiceSearch.getBoListItems();
			// guardamos los ítems BO seleccionados y quitamos el resto
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
	
	/**
	 * Controller para la url /getFile.htm con método POST.
	 * Devuelve el fichero del EDM en xml. Viene de la página del formulario EDM.
	 * 
	 * @param edmExportBOFormEDMData objeto con los datos del formulario de configuración de elementos de EDM {@link EDMExportBOFormEDMData}
	 * @param result objeto con el que se une la petición
	 * @return objeto HttpEntity {@link HttpEntity} con el array de bytes del document xml de EDM
	 */
	
	@RequestMapping(value = "/getFile.htm", method = RequestMethod.POST, params="pageAction=xml")
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
	
	/**
	 * Controller para la url /getFile.htm con método POST.
	 * Devuelve el fichero del EDM en xml. Viene de la página del formulario que muestra el xml.
	 * 
	 * @param EDMXml cadena con el contenido xml
	 * @param edmXMLEncoded cadena con el contenido xml codificado bas64 y comprimido gzip
	 * @return objeto HttpEntity {@link HttpEntity} con el array de bytes del document xml de EDM
	 */
	@RequestMapping(value = "/getFile.htm", method = RequestMethod.POST, params="pageAction=exportView")
	public HttpEntity<byte[]> postExportView(@RequestParam("EDMXml") String EDMXml, @RequestParam("edmXMLEncoded") String edmXMLEncoded)
	{
		logger.debug("homeController.postExportView");
		try {
			if (edmXMLEncoded != null && !edmXMLEncoded.isEmpty()) {
				BufferedInputStream reader = null;
				try {
					logger.debug("edmXMLEncoded: " + edmXMLEncoded);
					byte[] compressedData = Base64.decodeBase64(edmXMLEncoded.getBytes());
					reader = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(compressedData)));
					EDMXml = IOUtils.toString(reader, "UTF-8");
				} catch (Exception e) {
					logger.debug("homeController.postExportView", e);
				} finally {
					 if (reader != null) reader.close();
				}
			}
			if (EDMXml != null && !EDMXml.isEmpty()) {
				return getHttpEntityFromXml(EDMXml);
			}
		} catch (Exception e) {
			logger.debug("homeController.postExportView", e);
		}
		return null;
	}
	
	
	/**
	 * Controller para la url /selectedItems.htm con método POST y parámetro "pageAction" obligatorio con valor "xml"
	 * Si hay un error se vuelve a la lista de ítem seleccionados, si no se muestra el contenido xml en un formulario.
	 * Los contenidos del formulario de elementos EDM se pasan en un objeto flash.
	 * 
	 * @param boFormData objeto con los datos del formulario de elementos EDM {@link EDMExportBOFormEDMData}
	 * @param result objeto con el que se une la petición y se valida {@link BindingResult}
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @param redirectAttributes objeto para devolver un objeto flash con los datos del formulario {@link RedirectAttributes}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	/**
	 * Controller para la url /selectedItems.htm con método POST y parámetro "referer" obligatorio.
	 * Recoge los ítems seleccionados para procesarlos.
	 * Si hay un error redirige al listado de colecciones, si no al formulario de elementos EDM
	 * 
	 * @param boListItems objeto con los datos de los ítems pasados en la petición {@link EDMExportBOListItems}
	 * @param result objeto con el que se une la petición {@link BindingResult}
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	
	/**
	 * Método para generar un objeto HttpEntity con un array de bytes a partir del contenido xml
	 * 
	 * @param xml cadena con el contenido xml
	 * @return objeto HttpEntity {@link HttpEntity}
	 * @throws UnsupportedEncodingException
	 */
	private HttpEntity<byte[]> getHttpEntityFromXml(String xml) throws UnsupportedEncodingException
	{
		byte[] EDMXmlBytes;
		EDMXmlBytes = xml.getBytes("UTF-8");
		HttpHeaders header = new HttpHeaders();
	    header.setContentType(new MediaType("text", "xml", Charset.forName("UTF-8")));
	    header.set("Content-Disposition", "attachment; filename=EDMXml.xml");
	    header.setContentLength(EDMXmlBytes.length);
	    return new HttpEntity<byte[]>(EDMXmlBytes, header);
	}
	 
	
	/**
	 * Controller para la url /home.htm con método POST y parámetro "pageAction" obligatorio y valor "listColls".
	 * Recoge las colecciones seleccionadas y los primeros ítems a mostrar en su listado.
	 * Si hay un error redirige al listado de colecciones, si no muestra el listado de ítems con paginado.
	 * 
	 * @param listCollectionsBO objeto con las colecciones seleccionadas {@link EDMExportBOListCollections}
	 * @param result objeto con el que se une la petición {@link BindingResult}
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	
	/**
	 * Controller para la url /home.htm con método POST y parámetro "pageAction" obligatorio y valor "searchItems".
	 * Busca y recoge los primeros ítems a mostrar en su listado.
	 * Si hay un error redirige al formulario de búsqueda, si no muestra el listado de ítems con paginado.
	 * 
	 * @param searchBO objeto con los datos del formulario {@link EDMExportBOSearch}
	 * @param result objeto con el que se une la petición {@link BindingResult}
	 * @param model objeto de Spring Model con la petición {@link Model}
	 * @return cadena con la vista a renderizar y mostrar
	 */
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
	
	
	/**
	 * Método para recoger los datos del usuario del contexto de seguridad y almacenarlos en el objeto {@link EDMExportBOUser}
	 * Así se podrá mostrar el nombre del usuario en toda la web
	 */
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
	
	/**
	 * Método para añadir atributos comunes a las vistas
	 * 
	 * @param view cadena con el nombre de la vista
	 * @param model objeto Spring con el modelo para la vista {@link Model}
	 * @return cadena con la vista
	 */
	private String returnView(String view, Model model)
	{
		addCommonObjects2Model(model);
		return view;
	}
	
	/**
	 * Método para añadir atributos comunes al modelo
	 * 
	 * @param model objeto Spring con el modelo para la vista {@link Model}
	 */
	private void addCommonObjects2Model(Model model)
	{
		model.addAttribute("edmExportBOUser", edmExportBOUser);
	}
	
	
	/**
	 * Método para logar los errores del objeto que une los datos de la petición
	 * 
	 * @param result objeto que une la petición {@link BindingResult}
	 */
	private void logErrorValid(BindingResult result)
	{
		for (Object object : result.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                logger.debug( fieldError.toString() );
            }
        }
	}
	
	/**
	 * Método para devolver el número total de páginas en el paginado
	 * 
	 * @param hitCount número de resultados
	 * @param listItemsPageInt resultados por página
	 * @return número de páginas totales
	 */
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
