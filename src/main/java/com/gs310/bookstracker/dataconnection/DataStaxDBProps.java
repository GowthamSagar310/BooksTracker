package com.gs310.bookstracker.dataconnection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.io.File;

// by default, spring data cassandra tries to connect to a local instance.
// but we have specified it to connect to the contact point in secure bundle

@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxDBProps {

    // observe that the property name mention in application.yml is secure-connect-bundle
    // it is converted into camelCase
    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }
}
