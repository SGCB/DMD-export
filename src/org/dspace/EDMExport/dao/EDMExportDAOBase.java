package org.dspace.EDMExport.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dspace.core.Context;


/**
 * 
 * Clase base de acceso a la base de datos de Dspace
 *
 */

public class EDMExportDAOBase
{

	/**
	 * Contexto de Dspace, en él está el acceso a base de datos. Es requerio por la API de Dspace
	 */
	protected Context context = null;
	
	/**
	 * logar eventos de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 * Inicializamos el context. Llamado desde EDMExport-data.xml
	 */
	public void init()
    {
		getContext();
        logger.info("Init EDMExportDAOBase");
    }
	
	
	/**
	 * Constructor para inicializar el context
	 */
	public void EDMExportServiceBase()
	{
		if (context == null) {
	        try {
	            context = new Context();
	            logger.debug("Dspace context created");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
	}
	

	/**
	 * Inicializa el context y lo devuelve
	 * 
	 * @return context: contexto de Dspace {@link Context}
	 */
	protected Context getContext()
	{
		if (context == null) {
			try {
	            context = new Context();
	            logger.debug("Dspace context created");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
			
		}
		return context;
	}
		

}
