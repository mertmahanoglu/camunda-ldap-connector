package org.camunda.bpm.run.connectors;

import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.camunda.bpm.run.helpers.DecodeHelper;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BpmAdminAuthorizator {
    @Bean
    public static AdministratorAuthorizationPlugin setAdminUser() {
        AdministratorAuthorizationPlugin plugin = new AdministratorAuthorizationPlugin();
        JSONObject adminAuthJson = null;
		try {
			adminAuthJson = DecodeHelper.GetYamlProperties(System.getenv("CAMUNDA_HOME") + "/configuration/production.yml", "admin-auth");
		} catch (Exception e) {
			e.printStackTrace();
		}
        plugin.setAdministratorUserName(DecodeHelper.DecryptText(adminAuthJson.getString("administrator-user-name")));
        return plugin;
    }
}
