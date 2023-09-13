# camunda-ldap-connector

This project developed with Spring Boot and Camunda Run. The goal of the project is making LDAP connection with encrypted data in production.yml file. 

# How to use it?

After downloading the project, use command down below at root directory or use your IDE to clean and install the project.
```
mvn clean install
```
When the command is finished running or jar created, you should place ldap-connector-0.0.1.jar to your_camunda_directory/configuration/userlib like down below.
![image](https://github.com/mertmahanoglu/camunda-ldap-connector/assets/72344057/aaf3ad4a-2fb5-4c86-b823-912a7435a9bf)

You need to add jackson-dataformat, spring-security-crypto and org.json jars to userlib too. 
You can find these jars from here :
 https://mvnrepository.com/artifact/org.json/json/20230618
 https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml/2.15.2
 https://mvnrepository.com/artifact/org.springframework.security/spring-security-crypto/6.1.3

When jars are placed, we need to configure our LDAP info in production.yml. *You can find example production.yml file in repo.*
*!!!WARNING: We are replacing default yaml structure in production.yml. Our ldap tag is not in camunda.bpm.run. You need to make ldap tag out of camunda tag.*

After configuring our ldap informations, we need to add our plugin to process-engine-plugins in production.yml file.
![image](https://github.com/mertmahanoglu/camunda-ldap-connector/assets/72344057/22f052c0-4624-44d8-8c0e-f9725e27955a)

Also, project gets production.yml file path from environment variable. We need to set environment variable like this:
![image](https://github.com/mertmahanoglu/camunda-ldap-connector/assets/72344057/57d8a678-d47f-4a9b-8c77-687743e13c03)

And it's done! You can use your ldap with encrypted information.

# How it works?

We are preparing LdapConfiguration at LdapPropertyConfigurator.java. Getting our ldap values from production.yml with DecodeHelper.GetYamlProperties and setting our values to LdapConfiguration. 
```Java
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
```
We're setting this LdapConfiguration at LdapConnectorPlugin to connect Camunda to LDAP.

```Java
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
```

Also we need to configure admin user and we're doing this at BpmAdminAuthorizator.java. Using @Bean to make this code run as Camunda runs and grant authorization to our user. It is getting administrator-user-name tag value from production.yml and decrypts value.
```Java
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
```
