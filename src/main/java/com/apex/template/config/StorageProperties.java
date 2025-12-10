package com.apex.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    //     private String location = "/opt/tomcat/webapps/abc"; //for droplet
//    private String location = ""; //for mac
    @Value("${storage.properties.base}")
    private String location;
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}