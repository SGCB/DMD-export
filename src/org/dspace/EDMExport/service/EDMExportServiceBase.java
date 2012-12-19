package org.dspace.EDMExport.service;

import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.springframework.beans.factory.annotation.Value;


public class EDMExportServiceBase
{
	
	protected static Logger logger = Logger.getLogger("edmexport");
	
	@Value("${dspace-config}") private String dspaceConfig;
	
	
	public EDMExportServiceBase()
	{
	}
	
	@Value("${dspace-config}")
	public void setDspaceConfig(String dspaceConfig)
	{
		this.dspaceConfig = dspaceConfig;
	}
	

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
	
	public String getDspaceDir()
	{
		return ConfigurationManager.getProperty("dspace.dir");
	}
	
	public String getDspaceName()
	{
		return ConfigurationManager.getProperty("dspace.name");
	}
	

}
