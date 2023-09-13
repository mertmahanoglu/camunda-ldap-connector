package org.camunda.bpm.run.plugins;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.identity.impl.ldap.LdapIdentityProviderFactory;
import org.camunda.bpm.run.connectors.LdapPropertyConfigurator;
import org.springframework.stereotype.Component;

@Component
public class LdapConnectorPlugin extends AbstractProcessEnginePlugin {
	@Override
	public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

		LdapIdentityProviderFactory ldapIdentityProviderFactory = new LdapIdentityProviderFactory();
		LdapPropertyConfigurator ldapConfigurator = new LdapPropertyConfigurator();

		try {
			ldapIdentityProviderFactory.setLdapConfiguration(ldapConfigurator.getLdapConfiguration());
		} catch (Exception e) {
			e.printStackTrace();
		}
		processEngineConfiguration.setIdentityProviderSessionFactory(ldapIdentityProviderFactory);
	}
}
