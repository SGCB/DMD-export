/**
 *  Copyright 2013 Spanish Minister of Education, Culture and Sport
 *  
 *  written by MasMedios
 *  
 *  Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 *  
 *  You may not use this work  except in compliance with the License. You may obtain a copy of the License at:
 *  
 *  http://ec.europa.eu/idabc/servlets/Docbb6d.pdf?id=31979
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" basis,
 *  
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package org.dspace.EDMExport.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;


import org.dspace.EDMExport.bo.EDMExportBOUser;
import org.dspace.EDMExport.dao.EDMExportDAOEperson;

/**
 * Clase que gestiona la autenticación de los usuarios en la aplicación
 * 
 *
 */
public class EDMExportAuthenticationManager implements AuthenticationManager
{
	
	/**
	 * Logs de EDMExport
	 */
	protected static Logger logger = Logger.getLogger("edmexport");
	
	/**
	 *  Variable recogida de edmexport.properties, indica el grupo de usuarios válido para entrar
	 */
	@Value("${auth.groupid}")
    private String groupIDStr;
		
	/**
	 * codificar el password
	 */
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	
	/**
	 * acceso a bbdd para validar usuario {@link EDMExportDAOEperson}
	 */
	private EDMExportDAOEperson daoEperson;
	
	/**
	 * datos del usuario {@link EDMExportBOUser}
	 */
	private EDMExportBOUser eperson = null;

	/**
	 * Redefinimos el método para autenticarse
	 * 
	 * @param auth objeto de Spring de Authentication {@link Authentication}
	 * @return UsernamePasswordAuthenticationToken {@link Authentication}
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException
	{
		logger.debug("Performing EDMExport authentication");
				
		try {
			// Buscar usuario con login y grupo o sólo con login
			if (groupIDStr != null && !groupIDStr.isEmpty()) {
				eperson = daoEperson.getEperson(auth.getName(), Integer.parseInt(groupIDStr));
			} else eperson = daoEperson.getEperson(auth.getName());
		} catch (Exception e) {
			logger.error("User "+ auth.getName() + " does not exists! " + e.getMessage() + "," + e.toString(), e);
			//SecurityContextHolder.getContext().setAuthentication(null);
			throw new BadCredentialsException("User does not exists!");
		}
		
		// Validamos el password
		if (!passwordEncoder.isPasswordValid(eperson.getPassword(), (String) auth.getCredentials(), null)) {
			logger.error("Wrong password!" + eperson.getPassword() + " " + (String) auth.getCredentials());
			throw new BadCredentialsException("Wrong password!");
		}
		
		// Comprobamos que el login no se igual que el password, poco seguridad
		if (auth.getName().equals(auth.getCredentials())) {
			logger.debug("Entered username and password are the same!");
			throw new BadCredentialsException("Entered username and password are the same!");
		} else {
			logger.debug("User details are good and ready to go");
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), getAuthorities(eperson.getAccess()));
		}
	}
	
	/**
	 * Autoridades de los permisos que se obtiene al validarse, mirar EDMExport-security.xml
	 * 
	 * @param access entero con el tipo de acceso requerido
	 * @return Collection<SimpleGrantedAuthority>
	 */
	public Collection<SimpleGrantedAuthority> getAuthorities(Integer access)
	{
		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(2);
		logger.debug("Grant ROLE_USER to this user");
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if ( access.compareTo(1) == 0) {
			logger.debug("Grant ROLE_ADMIN to this user");
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
	 
		return authList;
	}
	
	/**
	 * Inyectamos el objeto de la consulta de usuario en bbdd, mirar EDMExport-security.xml
	 * @param daoEperson
	 */
	@Required
	public void setEdmExportDAOEperson(EDMExportDAOEperson daoEperson)
	{
    	this.daoEperson = daoEperson;
	}


}
