package org.dspace.EDMExport.service;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.dspace.EDMExport.bo.EDMExportBOFormEDMData;

public class EDMExportServiceXML
{
	
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * 
	 */
	private static String fileSeparator = System.getProperty("file.separator");
	
	/**
	 * POJO {@link EDMExportServiceListItems} con la lista de los ítems con los que generar el EDM
	 */
	private EDMExportServiceListItems edmExportServiceListItems;
	
	/**
	 * POJO {@link EDMExportBOFormEDMData} con los datos del formulario EDM
	 */
	private EDMExportBOFormEDMData edmExportBOFormEDMData;
	
	/**
	 * 
	 */
	private EDMExportXML edmExportXML;
	
	/**
	 * Constructor vacío
	 */
	public EDMExportServiceXML()
	{
	}
	
	/**
	 * Constructor con el servicio {@link EDMExportServiceListItems} inyectado
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public EDMExportServiceXML(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
	}
	
	/**
	 * Inyección del servicio {@link EDMExportServiceListItems}
	 * 
	 * @param edmExportServiceListItems {@link EDMExportServiceListItems}
	 */
	public void setEdmExportServiceListItems(EDMExportServiceListItems edmExportServiceListItems)
	{
		this.edmExportServiceListItems = edmExportServiceListItems;
	}
	
	
	/**
	 * Comprueba los formatos disponibles en el war
	 * 
	 * @param formatsXml cadena con los formatos separados por coma en edmexport.properties
	 * @param path ruta física del edmexport desplegado
	 * 
	 * @return array de cadenas con los formatos soportados
	 */
	public String[] getFormatsXml(String formatsXml, String path)
	{
		String[] xmlFormatsArr = formatsXml.split(",");
		Set<String> setXmlFormats = new HashSet<String>(Arrays.asList(xmlFormatsArr));
		List<String> listXmlFormats = new ArrayList<String>();
		String suffix = "ExportXMLItem.class";
		StringBuilder sb = new StringBuilder(path).append(fileSeparator).append("WEB-INF").append(fileSeparator).append("classes").
				append(fileSeparator).append("org").append(fileSeparator).append("dspace").append(fileSeparator).
				append("EDMExport").append(fileSeparator).append("service").append(fileSeparator);
        String dirXml = sb.toString();
        File dir = new File(dirXml);
        String[] files = dir.list(new SuffixFileFilter(suffix));
        for (int i = 0; i < files.length; i++) {
            String exportXml = files[i].replaceFirst(suffix, "");
            File fileExportXml = new File(dirXml + files[i]);
            if (fileExportXml.canRead() && setXmlFormats.contains(exportXml)) {
            	listXmlFormats.add(exportXml);
            }
        }
		return listXmlFormats.toArray(new String[listXmlFormats.size()]);
	}
	
	/**
	 * Creación del esquema EDM con la lista de los ítems seleccionados
	 * <p>Se usa jdom para crear el documento xml</p>
	 * 
	 * @param edmExportBOFormEDMData POJO {@link EDMExportBOFormEDMData} con los datos del formulario
	 * @param pat ruta física del edmexport desplegado
	 * @return cadena con el contenido xml
	 */
	public String showEDMXML(EDMExportBOFormEDMData edmExportBOFormEDMData, String path)
	{
		this.edmExportBOFormEDMData = edmExportBOFormEDMData;
		String formatXml = edmExportBOFormEDMData.getXmlFormat();
		logger.debug("Formato: " + formatXml);
		StringBuilder sb = new StringBuilder(path).append(fileSeparator).append("WEB-INF").append(fileSeparator).append("classes").
				append(fileSeparator).append("org").append(fileSeparator).append("dspace").append(fileSeparator).
				append("EDMExport").append(fileSeparator).append("service").append(fileSeparator).append(formatXml).append("ExportXMLItem.class");
        String formatClassStr = sb.toString();
        File formatClassFile = new File(formatClassStr);
        if (formatClassFile.canRead()) {
        	String formatClassName = "org.dspace.EDMExport.service." + formatXml + "ExportXMLItem";
        	try {
                Class<?> formatClass = Class.forName(formatClassName);
                Constructor<?> ctor = formatClass.getDeclaredConstructor(new Class[]{EDMExportServiceListItems.class});
                edmExportXML = (EDMExportXML) ctor.newInstance(edmExportServiceListItems);
                return edmExportXML.showEDMXML(edmExportBOFormEDMData);
            } catch (ClassNotFoundException e) {
            	logger.debug("EDMExportServiceXML.showEDMXML", e);
            } catch (InstantiationException e) {
            	logger.debug("EDMExportServiceXML.showEDMXML", e);
            } catch (IllegalAccessException e) {
            	logger.debug("EDMExportServiceXML.showEDMXML", e);
            } catch (InvocationTargetException e) {
            	logger.debug("EDMExportServiceXML.showEDMXML", e);
            } catch (NoSuchMethodException e) {
            	logger.debug("EDMExportServiceXML.showEDMXML", e);
            }
        }
		return "";
	}
	
	/**
	 * Devuelve la lista de los elementos EDM únicos creados en el xml
	 * 
	 * @return una lista de cadenas con los elementos EDM
	 */
	public List<String> getListElementsFilled()
	{
		return edmExportXML.getListElementsFilled();
	}
	
	/**
	 * Limpiar la lista de elementos edm únicos y el conjunto
	 */
	public void clear()
	{
		if (edmExportXML != null) edmExportXML.clear();
	}
	
}
