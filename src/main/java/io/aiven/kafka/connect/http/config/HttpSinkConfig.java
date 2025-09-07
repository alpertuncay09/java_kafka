package io.aiven.kafka.connect.http.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.connect.errors.ConnectException;



























public final class HttpSinkConfig
  extends AbstractConfig
{
  private static final String CONNECTION_GROUP = "Connection";
  private static final String HTTP_URL_CONFIG = "http.url";
  private static final String HTTP_PROXY_HOST = "http.proxy.host";
  private static final String HTTP_PROXY_PORT = "http.proxy.port";
  private static final String HTTP_SSL_TRUST_ALL_CERTIFICATES = "http.ssl.trust.all.certs";
  private static final String HTTP_AUTHORIZATION_TYPE_CONFIG = "http.authorization.type";
  private static final String HTTP_HEADERS_AUTHORIZATION_CONFIG = "http.headers.authorization";
  private static final String HTTP_HEADERS_CONTENT_TYPE_CONFIG = "http.headers.content.type";
  private static final String HTTP_HEADERS_ADDITIONAL = "http.headers.additional";
  private static final String HTTP_HEADERS_ADDITIONAL_DELIMITER = ":";
  public static final String KAFKA_RETRY_BACKOFF_MS_CONFIG = "kafka.retry.backoff.ms";
  private static final String OAUTH2_ACCESS_TOKEN_URL_CONFIG = "oauth2.access.token.url";
  private static final String OAUTH2_GRANT_TYPE_PROP_CONFIG = "oauth2.request.grant.type.property";
  private static final String OAUTH2_GRANT_TYPE_CONFIG = "oauth2.grant.type";
  private static final String OAUTH2_CLIENT_ID_PROP_CONFIG = "oauth2.request.client.id.property";
  private static final String OAUTH2_CLIENT_ID_CONFIG = "oauth2.client.id";
  private static final String OAUTH2_CLIENT_SECRET_PROP_CONFIG = "oauth2.request.client.secret.property";
  private static final String OAUTH2_CLIENT_SECRET_CONFIG = "oauth2.client.secret";
  private static final String OAUTH2_CLIENT_AUTHORIZATION_MODE_CONFIG = "oauth2.client.authorization.mode";
  private static final String OAUTH2_CLIENT_SCOPE_CONFIG = "oauth2.client.scope";
  private static final String OAUTH2_RESPONSE_TOKEN_PROPERTY_CONFIG = "oauth2.response.token.property";
  private static final String BATCHING_GROUP = "Batching";
  private static final String BATCHING_ENABLED_CONFIG = "batching.enabled";
  private static final String BATCH_MAX_SIZE_CONFIG = "batch.max.size";
  private static final String BATCH_PREFIX_CONFIG = "batch.prefix";
  private static final String BATCH_PREFIX_DEFAULT = "";
  private static final String BATCH_SUFFIX_CONFIG = "batch.suffix";
  private static final String BATCH_SUFFIX_DEFAULT = "\n";
  private static final String BATCH_SEPARATOR_CONFIG = "batch.separator";
  private static final String BATCH_SEPARATOR_DEFAULT = "\n";
  private static final String DELIVERY_GROUP = "Delivery";
  private static final String MAX_RETRIES_CONFIG = "max.retries";
  private static final String RETRY_BACKOFF_MS_CONFIG = "retry.backoff.ms";
  private static final String TIMEOUT_GROUP = "Timeout";
  private static final String HTTP_TIMEOUT_CONFIG = "http.timeout";
  public static final String NAME_CONFIG = "name";
  private static final String ERRORS_GROUP = "Errors Handling";
  private static final String ERRORS_TOLERANCE = "errors.tolerance";
  
  public static ConfigDef configDef() {
    ConfigDef configDef = new ConfigDef();
    addConnectionConfigGroup(configDef);
    addBatchingConfigGroup(configDef);
    addRetriesConfigGroup(configDef);
    addTimeoutConfigGroup(configDef);
    addErrorsConfigGroup(configDef);
    return configDef;
  }
  
  private static void addConnectionConfigGroup(ConfigDef configDef) {
    int groupCounter = 0;
    configDef.define("http.url", ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, new UrlValidator(), ConfigDef.Importance.HIGH, "The URL to send data to.", "Connection", groupCounter++, ConfigDef.Width.LONG, "http.url");










    
    configDef.define("http.proxy.host", ConfigDef.Type.STRING, null, new NonBlankStringValidator(true), ConfigDef.Importance.LOW, "Proxy hostname", "Connection", groupCounter++, ConfigDef.Width.LONG, "http.proxy.host");










    
    configDef.define("http.proxy.port", ConfigDef.Type.INT, 

        
        Integer.valueOf(-1), 
        (ConfigDef.Validator)ConfigDef.Range.between(Integer.valueOf(-1), Integer.valueOf(65535)), ConfigDef.Importance.LOW, "Proxy port", "Connection", groupCounter++, ConfigDef.Width.SHORT, "http.proxy.port");






    
    configDef.define("http.ssl.trust.all.certs", ConfigDef.Type.BOOLEAN, 

        
        Boolean.valueOf(false), ConfigDef.Importance.LOW, "Disable hostname verification. Not recommended for production environments.", "Connection", groupCounter++, ConfigDef.Width.SHORT, "http.ssl.trust.all.certs");







    
    configDef.define("http.authorization.type", ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, new ConfigDef.Validator()
        {


          
          @SuppressFBWarnings({"NP_LOAD_OF_KNOWN_NULL_VALUE"})
          public void ensureValid(String name, Object value)
          {
            if (value == null) {
              throw new ConfigException("http.authorization.type", value);
            }
            assert value instanceof String;
            String valueStr = (String)value;
            if (!AuthorizationType.NAMES.contains(valueStr)) {
              throw new ConfigException("http.authorization.type", valueStr, "supported values are: " + AuthorizationType.NAMES);
            }
          }



          
          public String toString() {
            return AuthorizationType.NAMES.toString();
          }
        }ConfigDef.Importance.HIGH, "The HTTP authorization type.", "Connection", groupCounter++, ConfigDef.Width.SHORT, "http.authorization.type", List.of("http.headers.authorization"), 
        FixedSetRecommender.ofSupportedValues(AuthorizationType.NAMES));

    
    configDef.define("http.headers.authorization", ConfigDef.Type.PASSWORD, null, ConfigDef.Importance.MEDIUM, "The static content of Authorization header. Must be set along with 'static' authorization type.", "Connection", groupCounter++, ConfigDef.Width.MEDIUM, "http.headers.authorization", new ConfigDef.Recommender()
        {









          
          public List<Object> validValues(String name, Map<String, Object> parsedConfig)
          {
            return List.of();
          }

          
          public boolean visible(String name, Map<String, Object> parsedConfig) {
            return AuthorizationType.STATIC.name.equalsIgnoreCase((String)parsedConfig
                .get("http.authorization.type"));
          }
        });
    
    configDef.define("http.headers.content.type", ConfigDef.Type.STRING, null, new NonBlankStringValidator(true), ConfigDef.Importance.LOW, "The value of Content-Type that will be send with each request. Must be non-blank.", "Connection", groupCounter++, ConfigDef.Width.MEDIUM, "http.headers.content.type");











    
    configDef.define("http.headers.additional", ConfigDef.Type.LIST, Collections.EMPTY_LIST, new KeyValuePairListValidator(":"), ConfigDef.Importance.LOW, "Additional headers to forward in the http request in the format header:value separated by a comma, headers are case-insensitive and no duplicate headers are allowed.", "Connection", groupCounter++, ConfigDef.Width.MEDIUM, "http.headers.additional");












    
    configDef.define("oauth2.access.token.url", ConfigDef.Type.STRING, null, new UrlValidator(true), ConfigDef.Importance.HIGH, "The URL to be used for fetching an access token. Client Credentials is the only supported grant type.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.access.token.url", 










        
        List.of("oauth2.request.grant.type.property", "oauth2.grant.type", "oauth2.request.client.id.property", "oauth2.client.id", "oauth2.request.client.secret.property", "oauth2.client.secret", "oauth2.client.authorization.mode", "oauth2.client.scope", "oauth2.response.token.property"));



    
    configDef.define("oauth2.request.grant.type.property", ConfigDef.Type.STRING, "grant_type", (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {

          
          public String toString()
          {
            return "OAuth2 grant type key";
          }
        }, 




        
        ConfigDef.Importance.HIGH, "The grant type Key used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.request.grant.type.property", List.of("oauth2.grant.type"));
    
    configDef.define("oauth2.grant.type", ConfigDef.Type.STRING, "client_credentials", (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {


          
          public String toString()
          {
            return "OAuth2 grant type";
          }
        },  ConfigDef.Importance.HIGH, "The grant type used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.grant.type");






    
    configDef.define("oauth2.request.client.id.property", ConfigDef.Type.STRING, "client_id", (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {

          
          public String toString()
          {
            return "OAuth2 client id Key";
          }
        }, 




        
        ConfigDef.Importance.HIGH, "The client id Key used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.request.client.id.property", List.of("oauth2.client.id"));
    
    configDef.define("oauth2.client.id", ConfigDef.Type.STRING, null, (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {


          
          public String toString()
          {
            return "OAuth2 client id";
          }
        }, 





        
        ConfigDef.Importance.HIGH, "The client id used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.client.id", List.of("oauth2.access.token.url", "oauth2.client.secret", "oauth2.client.authorization.mode", "oauth2.client.scope", "oauth2.response.token.property"));


    
    configDef.define("oauth2.request.client.secret.property", ConfigDef.Type.STRING, "client_secret", ConfigDef.Importance.HIGH, "The secret Key used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.request.client.secret.property", 






        
        List.of("oauth2.client.secret"));
    
    configDef.define("oauth2.client.secret", ConfigDef.Type.PASSWORD, null, ConfigDef.Importance.HIGH, "The secret used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.client.secret", 








        
        List.of("oauth2.access.token.url", "oauth2.client.id", "oauth2.client.authorization.mode", "oauth2.client.scope", "oauth2.response.token.property"));


    
    configDef.define("oauth2.client.authorization.mode", ConfigDef.Type.STRING, OAuth2AuthorizationMode.HEADER

        
        .name(), new ConfigDef.Validator()
        {
          public void ensureValid(String name, Object value)
          {
            if (value == null) {
              throw new ConfigException(name, null, "can't be null");
            }
            if (!(value instanceof String)) {
              throw new ConfigException(name, value, "must be string");
            }
            
            if (!OAuth2AuthorizationMode.OAUTH2_AUTHORIZATION_MODES.contains(value.toString().toUpperCase())) {
              throw new ConfigException("oauth2.client.authorization.mode", value, "supported values are: " + OAuth2AuthorizationMode.OAUTH2_AUTHORIZATION_MODES);
            }
          }



          
          public String toString() {
            return String.join(",", (Iterable)OAuth2AuthorizationMode.OAUTH2_AUTHORIZATION_MODES);
          }
        }ConfigDef.Importance.MEDIUM, "Specifies how to encode ``client_id`` and ``client_secret`` in the OAuth2 authorization request. If set to ``header``, the credentials are encoded as an ``Authorization: Basic <base-64 encoded client_id:client_secret>`` HTTP header. If set to ``url``, then ``client_id`` and ``client_secret`` are sent as URL encoded parameters. Default is ``header``.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.client.authorization.mode", List.of("oauth2.access.token.url", "oauth2.client.id", "oauth2.client.secret", "oauth2.client.scope", "oauth2.response.token.property"));

    
    configDef.define("oauth2.client.scope", ConfigDef.Type.STRING, null, (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {


          
          public String toString()
          {
            return "OAuth2 client scope";
          }
        }, 





        
        ConfigDef.Importance.LOW, "The scope used for fetching an access token.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.client.scope", List.of("oauth2.access.token.url", "oauth2.client.id", "oauth2.client.secret", "oauth2.client.authorization.mode", "oauth2.response.token.property"));

    
    configDef.define("oauth2.response.token.property", ConfigDef.Type.STRING, "access_token", (ConfigDef.Validator)new ConfigDef.NonEmptyStringWithoutControlChars()
        {


          
          public String toString()
          {
            return "OAuth2 response token";
          }
        }, 






        
        ConfigDef.Importance.LOW, "The name of the JSON property containing the access token returned by the OAuth2 provider. Default value is ``access_token``.", "Connection", groupCounter++, ConfigDef.Width.LONG, "oauth2.response.token.property", List.of("oauth2.access.token.url", "oauth2.client.id", "oauth2.client.secret", "oauth2.client.authorization.mode", "oauth2.client.scope"));
  }


  
  private static void addBatchingConfigGroup(ConfigDef configDef) {
    int groupCounter = 0;
    configDef.define("batching.enabled", ConfigDef.Type.BOOLEAN, 

        
        Boolean.valueOf(false), ConfigDef.Importance.HIGH, "Whether to enable batching multiple records in a single HTTP request.", "Batching", groupCounter++, ConfigDef.Width.SHORT, "batching.enabled");







    
    configDef.define("batch.max.size", ConfigDef.Type.INT, 

        
        Integer.valueOf(500), 
        (ConfigDef.Validator)ConfigDef.Range.between(Integer.valueOf(1), Integer.valueOf(1000000)), ConfigDef.Importance.MEDIUM, "The maximum size of a record batch to be sent in a single HTTP request.", "Batching", groupCounter++, ConfigDef.Width.MEDIUM, "Batching");







    
    configDef.define("batch.prefix", ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, "Prefix added to record batches. Written once before the first record of a batch. Defaults to \"\" and may contain escape sequences like ``\\n``.", "Batching", groupCounter++, ConfigDef.Width.MEDIUM, "Batching");















    
    configDef.define("batch.suffix", ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, "Suffix added to record batches. Written once after the last record of a batch. Defaults to \"\\n\" (for backwards compatibility) and may contain escape sequences.", "Batching", groupCounter++, ConfigDef.Width.MEDIUM, "Batching");











    
    configDef.define("batch.separator", ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, "Separator for records in a batch. Defaults to \"\\n\" and may contain escape sequences.", "Batching", groupCounter++, ConfigDef.Width.MEDIUM, "Batching");
  }










  
  private static void addRetriesConfigGroup(ConfigDef configDef) {
    int groupCounter = 0;
    configDef.define("kafka.retry.backoff.ms", ConfigDef.Type.LONG, null, new ConfigDef.Validator()
        {
          static final long MAXIMUM_BACKOFF_POLICY = 86400000L;





          
          public void ensureValid(String name, Object value) {
            if (Objects.isNull(value)) {
              return;
            }
            assert value instanceof Long;
            Long longValue = (Long)value;
            if (longValue.longValue() < 0L)
              throw new ConfigException(name, value, "Value must be at least 0"); 
            if (longValue.longValue() > 86400000L) {
              throw new ConfigException(name, value, "Value must be no more than 86400000 (24 hours)");
            }
          }


          
          public String toString() {
            return String.join(",", List.of("null", "[0, 86400000]"));
          }
        }ConfigDef.Importance.MEDIUM, "The retry backoff in milliseconds. This config is used to notify Kafka Connect to retry delivering a message batch or performing recovery in case of transient failures.", "Delivery", groupCounter++, ConfigDef.Width.NONE, "kafka.retry.backoff.ms");








    
    configDef.define("max.retries", ConfigDef.Type.INT, 

        
        Integer.valueOf(1), 
        (ConfigDef.Validator)ConfigDef.Range.atLeast(Integer.valueOf(0)), ConfigDef.Importance.MEDIUM, "The maximum number of times to retry on errors when sending a batch before failing the task.", "Delivery", groupCounter++, ConfigDef.Width.SHORT, "max.retries");






    
    configDef.define("retry.backoff.ms", ConfigDef.Type.INT, 

        
        Integer.valueOf(3000), 
        (ConfigDef.Validator)ConfigDef.Range.atLeast(Integer.valueOf(0)), ConfigDef.Importance.MEDIUM, "The time in milliseconds to wait following an error before a retry attempt is made.", "Delivery", groupCounter++, ConfigDef.Width.SHORT, "retry.backoff.ms");
  }







  
  @SuppressFBWarnings({"DLS_DEAD_LOCAL_STORE"})
  private static void addTimeoutConfigGroup(ConfigDef configDef) {
    int groupCounter = 0;
    configDef.define("http.timeout", ConfigDef.Type.INT, 

        
        Integer.valueOf(30), 
        (ConfigDef.Validator)ConfigDef.Range.atLeast(Integer.valueOf(1)), ConfigDef.Importance.LOW, "HTTP Response timeout (seconds). Default is 30 seconds.", "Timeout", groupCounter++, ConfigDef.Width.SHORT, "http.timeout");
  }







  
  @SuppressFBWarnings({"DLS_DEAD_LOCAL_STORE"})
  private static void addErrorsConfigGroup(ConfigDef configDef) {
    int groupCounter = 0;
    configDef.define("errors.tolerance", ConfigDef.Type.STRING, null, ConfigDef.Importance.LOW, "Optional errors.tolerance setting. Defaults to \"none\".", "Errors Handling", groupCounter++, ConfigDef.Width.SHORT, "http.timeout");
  }











  
  public HttpSinkConfig(Map<String, String> properties) {
    super(configDef(), properties);
    validate();
  }
  
  private void validate() {
    AuthorizationType authorizationType = authorizationType();
    switch (authorizationType) {
      case STATIC:
        if (headerAuthorization() == null || headerAuthorization().isBlank()) {
          throw new ConfigException("http.headers.authorization", 
              
              getPassword("http.headers.authorization"), "Must be present when http.authorization.type = " + authorizationType);
        }
        break;
      
      case OAUTH2:
        validateOAuth2Configuration();
        break;
      case NONE:
        if (headerAuthorization() != null && !headerAuthorization().isBlank()) {
          throw new ConfigException("http.headers.authorization", 
              
              getPassword("http.headers.authorization"), "Must not be present when http.authorization.type != " + AuthorizationType.STATIC);
        }
        break;
    } 






    
    if (batchingEnabled() && errorsTolerance().equalsIgnoreCase("all")) {
      throw new ConfigException("Cannot use errors.tolerance when batching is enabled");
    }
    
    if ((hasProxy() && getInt("http.proxy.port").intValue() == -1) || (!hasProxy() && getInt("http.proxy.port").intValue() > -1)) {
      throw new ConfigException("Proxy host and port must be defined together");
    }
  }
  
  private void validateOAuth2Configuration() {
    Stream.<String>of(new String[] { "oauth2.access.token.url", "oauth2.request.grant.type.property", "oauth2.grant.type", "oauth2.request.client.id.property", "oauth2.client.id", "oauth2.request.client.secret.property"
        
        }).filter(configKey -> (getString(configKey) == null || getString(configKey).isBlank()))
      .findFirst()
      .ifPresent(missingConfiguration -> {
          throw new ConfigException(missingConfiguration, getString(missingConfiguration), "Must be present when http.headers.authorization = " + AuthorizationType.OAUTH2);
        });



    
    if (oauth2ClientSecret() == null || oauth2ClientSecret().value().isEmpty()) {
      throw new ConfigException("oauth2.client.secret", oauth2ClientSecret(), "Must be present when http.headers.authorization = " + AuthorizationType.OAUTH2);
    }
  }

  
  public final URI httpUri() {
    return toURI("http.url");
  }
  
  public final Long kafkaRetryBackoffMs() {
    return getLong("kafka.retry.backoff.ms");
  }
  
  public Map<String, String> getAdditionalHeaders() {
    return (Map<String, String>)getList("http.headers.additional").stream()
      .map(s -> s.split(":"))
      .collect(Collectors.toMap(h -> h[0], h -> h[1]));
  }
  
  public AuthorizationType authorizationType() {
    return AuthorizationType.forName(getString("http.authorization.type"));
  }
  
  public final String headerAuthorization() {
    Password authPasswd = getPassword("http.headers.authorization");
    return (authPasswd != null) ? authPasswd.value() : null;
  }
  
  public final String headerContentType() {
    return getString("http.headers.content.type");
  }
  
  public final boolean batchingEnabled() {
    return getBoolean("batching.enabled").booleanValue();
  }
  
  public final int batchMaxSize() {
    return getInt("batch.max.size").intValue();
  }

  
  private final String errorsTolerance() {
    return (getString("errors.tolerance") != null) ? getString("errors.tolerance") : "";
  }


  
  private String getOriginalString(String key, String defaultValue) {
    get(key);
    return (String)originalsStrings().getOrDefault(key, defaultValue);
  }
  
  public final String batchPrefix() {
    return getOriginalString("batch.prefix", "");
  }
  
  public final String batchSuffix() {
    return getOriginalString("batch.suffix", "\n");
  }
  
  public final String batchSeparator() {
    return getOriginalString("batch.separator", "\n");
  }
  
  public int maxRetries() {
    return getInt("max.retries").intValue();
  }
  
  public int retryBackoffMs() {
    return getInt("retry.backoff.ms").intValue();
  }
  
  public int httpTimeout() {
    return getInt("http.timeout").intValue();
  }
  
  public final String connectorName() {
    return (String)originalsStrings().get("name");
  }
  
  public final URI oauth2AccessTokenUri() {
    return toURI("oauth2.access.token.url");
  }
  
  private URI toURI(String propertyName) {
    try {
      return (new URL(getString(propertyName))).toURI();
    } catch (MalformedURLException|java.net.URISyntaxException e) {
      throw new ConnectException(String.format("Could not retrieve proper URI from %s", new Object[] { propertyName }), e);
    } 
  }
  
  public final String oauth2GrantTypeProperty() {
    return getString("oauth2.request.grant.type.property");
  }
  
  public final String oauth2GrantType() {
    return getString("oauth2.grant.type");
  }
  
  public final String oauth2ClientIdProperty() {
    return getString("oauth2.request.client.id.property");
  }
  
  public final String oauth2ClientId() {
    return getString("oauth2.client.id");
  }
  
  public final String oauth2ClientSecretProperty() {
    return getString("oauth2.request.client.secret.property");
  }
  
  public final Password oauth2ClientSecret() {
    return getPassword("oauth2.client.secret");
  }
  
  public final OAuth2AuthorizationMode oauth2AuthorizationMode() {
    return OAuth2AuthorizationMode.valueOf(getString("oauth2.client.authorization.mode").toUpperCase());
  }
  
  public final String oauth2ClientScope() {
    return getString("oauth2.client.scope");
  }
  
  public final String oauth2ResponseTokenProperty() {
    return getString("oauth2.response.token.property");
  }
  
  public final boolean hasProxy() {
    return (getString("http.proxy.host") != null);
  }
  
  public final InetSocketAddress proxy() {
    return new InetSocketAddress(getString("http.proxy.host"), getInt("http.proxy.port").intValue());
  }
  
  public final boolean sslTrustAllCertificates() {
    return getBoolean("http.ssl.trust.all.certs").booleanValue();
  }
  
  public static void main(String... args) {
    System.out.println("=========================================");
    System.out.println("HTTP Sink connector Configuration Options");
    System.out.println("=========================================");
    System.out.println();
    System.out.println(configDef().toEnrichedRst());
  }
}