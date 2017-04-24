package com.mabsisa.portal.web;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

	private static final int TOMCAT_THREAD_COUNT = 100;
	private static final int ACCEPT_COUNT = 1000;
	private static final int BUFFER_SIZE = 2048;
	public static final int CONNECTION_LINGER = -1;
	public static final int CONNECTION_TIMEOUT = 5000;
	public static final int KEEP_ALIVE_TIMEOUT = 1;
	public static final int MAX_KEEP_ALIVE_REQUESTS = 1;
	public static final int ACCEPTOR_THREAD_COUNT = 1;
	public static final int MAX_CONNECTIONS = 100000;
	public static final String ENABLE_LOOKUPS = "false";
	public static final String COMPRESSION = "off";
	public static final boolean SOCKET_SO_KEEP_ALIVE = false;

	@Override
	public void run(String... args) throws Exception {

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory tomcat() {
		TomcatEmbeddedServletContainerFactory myFactory = new TomcatEmbeddedServletContainerFactory();
		return myFactory;
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
				tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {

					@Override
					public void customize(Connector connector) {

						connector.setAttribute("maxThreads", TOMCAT_THREAD_COUNT);
						connector.setAttribute("acceptCount", ACCEPT_COUNT);
						connector.setAttribute("bufferSize", BUFFER_SIZE);
						connector.setAttribute("connectionLinger", CONNECTION_LINGER);
						connector.setAttribute("connectionTimeout", CONNECTION_TIMEOUT);
						connector.setAttribute("keepAliveTimeout", KEEP_ALIVE_TIMEOUT);
						connector.setAttribute("maxKeepAliveRequests", MAX_KEEP_ALIVE_REQUESTS);
						connector.setAttribute("acceptorThreadCount", ACCEPTOR_THREAD_COUNT);
						connector.setAttribute("maxConnections", MAX_CONNECTIONS);
						connector.setAttribute("enableLookups", ENABLE_LOOKUPS);
						connector.setAttribute("compression", COMPRESSION);
						connector.setAttribute("socket.soKeepAlive", SOCKET_SO_KEEP_ALIVE);

					}
				});
			}
		};
	}

}
