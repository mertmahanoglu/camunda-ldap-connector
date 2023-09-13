package org.camunda.bpm.run.connectors;

import org.camunda.bpm.identity.impl.ldap.LdapConfiguration;
import org.camunda.bpm.run.helpers.DecodeHelper;
import org.json.JSONObject;
public class LdapPropertyConfigurator {


	public LdapConfiguration getLdapConfiguration() throws Exception {

		try {
			JSONObject ldapYmlJson = DecodeHelper.GetYamlProperties(System.getenv("CAMUNDA_HOME") + "/configuration/production.yml","ldap");;
			LdapConfiguration configuration = new LdapConfiguration();

			configuration.setServerUrl(ldapYmlJson.getString("server-url"));
			configuration.setManagerDn(ldapYmlJson.getString("manager-dn"));
			configuration.setManagerPassword(DecodeHelper.DecryptText(ldapYmlJson.getString("manager-password")));
			configuration.setBaseDn(ldapYmlJson.getString("base-dn"));
			configuration.setUseSsl(ldapYmlJson.getBoolean("useSsl"));
			configuration.setUserSearchBase(ldapYmlJson.getString("user-search-base"));
			configuration.setUserSearchFilter(ldapYmlJson.getString("user-search-filter"));
			configuration.setUserIdAttribute(ldapYmlJson.getString("user-id-attribute"));
			configuration.setUserFirstnameAttribute(ldapYmlJson.getString("user-firstname-attribute"));
			configuration.setUserLastnameAttribute(ldapYmlJson.getString("user-lastname-attribute"));
			configuration.setUserEmailAttribute(ldapYmlJson.getString("user-email-ttribute"));
			configuration.setUserPasswordAttribute(ldapYmlJson.getString("user-password-attribute"));
			configuration.setGroupSearchBase(ldapYmlJson.getString("group-search-base"));
			configuration.setGroupSearchFilter(ldapYmlJson.getString("group-search-filter"));
			configuration.setGroupIdAttribute(ldapYmlJson.getString("group-id-attribute"));
			configuration.setGroupNameAttribute(ldapYmlJson.getString("group-name-attribute"));
			configuration.setGroupMemberAttribute(ldapYmlJson.getString("group-member-attribute"));
			configuration.setSortControlSupported(ldapYmlJson.getBoolean("sort-control-supported"));

			return configuration;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}
}
