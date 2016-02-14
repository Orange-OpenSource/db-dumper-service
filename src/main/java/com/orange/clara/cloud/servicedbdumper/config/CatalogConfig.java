package com.orange.clara.cloud.servicedbdumper.config;

import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.model.Plan;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p/>
 * Author: Arthur Halet
 * Date: 13/10/2015
 */
@Configuration
public class CatalogConfig {

    @Value("#{${use.ssl:false} ? 'https://' : 'http://'}${vcap.application.uris[0]:localhost:8080}")
    private String appUri;

    @Value("${service.definition.id:db-dumper-service}")
    private String serviceDefinitionId;

    private Map<String, Object> sdMetadata = new HashMap<String, Object>();

    @Bean
    public String appUri() {
        return this.appUri;
    }

    @Bean
    public Catalog catalog() {
        return new Catalog(Arrays.asList(
                new ServiceDefinition(
                        this.serviceDefinitionId,
                        this.serviceDefinitionId,
                        "Dump and restore data from your database",
                        true,
                        true,
                        Arrays.asList(
                                new Plan(this.serviceDefinitionId + "-plan-experimental",
                                        "experimental",
                                        "This is a default db-dumper-service plan.  All services are created equally.",
                                        getPlanMetadata(),
                                        true)), //TODO: change it cause set to free
                        Arrays.asList("db-dumper-service", "dump", "restore"),
                        getServiceDefinitionMetadata(),
                        null,
                        null)));
    }

/* Used by Pivotal CF console */

    private Map<String, Object> getServiceDefinitionMetadata() {
        sdMetadata.put("displayName", "db-dumper-service");
        sdMetadata.put("longDescription", "db-dumper-service");
        sdMetadata.put("providerDisplayName", "Orange");
        sdMetadata.put("documentationUrl", "");
        sdMetadata.put("supportUrl", "");
        return sdMetadata;
    }

    private Map<String, Object> getPlanMetadata() {
        Map<String, Object> planMetadata = new HashMap<String, Object>();
        planMetadata.put("costs", getCosts());
        planMetadata.put("bullets", getBullets());
        return planMetadata;
    }

    private List<Map<String, Object>> getCosts() {
        Map<String, Object> costsMap = new HashMap<String, Object>();

        Map<String, Object> amount = new HashMap<String, Object>();
        amount.put("usd", new Double(0.0));

        costsMap.put("amount", amount);
        costsMap.put("unit", "MONTHLY");

        return Arrays.asList(costsMap);
    }

    private List<String> getBullets() {
        return Arrays.asList("db-dumper-service",
                "Unlimited storage",
                "Stored in riakcs");
    }

    @PostConstruct
    public void postConstruct() {
        sdMetadata.put("imageUrl", appUri + "/images/logo.png");
    }
}