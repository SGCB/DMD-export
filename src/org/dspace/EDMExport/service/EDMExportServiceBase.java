package org.dspace.EDMExport.service;

import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.springframework.beans.factory.annotation.Value;

/**
 * Clase que obtiene algunas propiedades el fichero de configuración de dspace.
 * <p>Mirar el fichero EDMExport-service.xml</p>
 *
 */

public class EDMExportServiceBase
{
	
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Inyectamos el valor del archivo de configuración de dspace
	 */
	@Value("${dspace-config}") private String dspaceConfig;
	
	
	/**
	 * Constructor vacío
	 */
	public EDMExportServiceBase()
	{
	}
	
	/**
	 * Inyectamos el valor del archivo de configuración de dspace
	 * 
	 * @param dspaceConfig cadena con el valor del archivo de configuración de dspace
	 */
	@Value("${dspace-config}")
	public void setDspaceConfig(String dspaceConfig)
	{
		this.dspaceConfig = dspaceConfig;
	}
	

	/**
	 * Inicialización desde EDMExport-service.xml
	 * <p>Se cargan las propiedades del archivo</p>
	 */
	protected void init()
	{
		logger.debug("initConfDspace dspaceConfig: " + dspaceConfig);
		try {
			ConfigurationManager.loadConfig(dspaceConfig);
	    } catch (RuntimeException e) {
	    	throw e;
	    }
	    catch (Exception e) {
	    	logger.error("DSpace has failed to initialize, during stage 2. Error while attempting to read the \nDSpace configuration file (Path: '" + dspaceConfig + "'). \n" + "This has likely occurred because either the file does not exist, or it's permissions \n" + "are set incorrectly, or the path to the configuration file is incorrect. The path to \n" + "the DSpace configuration file is stored in a context variable, 'dspace-config', in \n" + "either the local servlet or global context");
	    	throw new IllegalStateException("\n\nDSpace has failed to initialize, during stage 2. Error while attempting to read the \nDSpace configuration file (Path: '" + dspaceConfig + "'). \n" + "This has likely occurred because either the file does not exist, or it's permissions \n" + "are set incorrectly, or the path to the configuration file is incorrect. The path to \n" + "the DSpace configuration file is stored in a context variable, 'dspace-config', in \n" + "either the local servlet or global context.\n\n", e);
	    }
	}
	
	/**
	 * Obtenemos la propiedad de dspace: "dspace.dir"
	 * 
	 * @return el directorio de dspace
	 */
	public String getDspaceDir()
	{
		return ConfigurationManager.getProperty("dspace.dir");
	}
	
	/**
	 * Obtenemos la propiedad de dspace: "dspace.name"
	 * 
	 * @return el nombre de este dspace
	 */
	public String getDspaceName()
	{
		return ConfigurationManager.getProperty("dspace.name");
	}
	
	/**
	 * Obtenemos la propiedad de dspace: "dspace.baseUrl"
	 * 
	 * @return url base de dspace
	 */
	public String getDspaceBaseUrl()
	{
		return ConfigurationManager.getProperty("dspace.baseUrl");
	}
	
	/**
	 * Obtenemos la propiedad de dspace: "handle.prefix"
	 * 
	 * @return el prefijo para los handle
	 */
	public String getHandlePrefix()
	{
		return ConfigurationManager.getProperty("handle.prefix");
	}
	
	/**
	 * Obtenemos la propiedad de dspace: "handle.canonical.prefix"
	 * 
	 * @return la url de los handle
	 */
	public String getHandleCanonicalPrefix()
	{
		return ConfigurationManager.getProperty("handle.canonical.prefix");
	}
	

}
