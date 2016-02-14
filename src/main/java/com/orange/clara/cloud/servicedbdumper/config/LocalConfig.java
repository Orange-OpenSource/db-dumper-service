package com.orange.clara.cloud.servicedbdumper.config;

import com.orange.clara.cloud.servicedbdumper.filer.Filer;
import com.orange.clara.cloud.servicedbdumper.filer.factory.FactoryFiler;
import com.orange.cloudfoundry.connector.s3.factory.S3ContextBuilder;
import com.orange.cloudfoundry.connector.s3.factory.S3FactoryCreator;
import com.orange.cloudfoundry.connector.s3.service.info.S3ServiceInfo;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

import static org.jclouds.Constants.PROPERTY_RELAX_HOSTNAME;
import static org.jclouds.Constants.PROPERTY_TRUST_ALL_CERTS;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p/>
 * Author: Arthur Halet
 * Date: 03/06/2015
 */
@Configuration
@Profile("local")
public class LocalConfig {


    @Value("${filer.type:GzipDisk}")
    private String filerType;


    @Bean
    public String bucketName() {
        return "myBucket";
    }

    @Bean
    public BlobStoreContext blobStoreContext() {
        S3ServiceInfo riakcsServiceInfo = new S3ServiceInfo("local", "s3", "localhost", 80, "access", "secret", "mybucket");
        Properties storeProviderInitProperties = new Properties();
        storeProviderInitProperties.put(PROPERTY_TRUST_ALL_CERTS, true);
        storeProviderInitProperties.put(PROPERTY_RELAX_HOSTNAME, true);
        storeProviderInitProperties.put(PROPERTY_S3_VIRTUAL_HOST_BUCKETS, false);
        S3FactoryCreator s3FactoryCreator = new S3FactoryCreator();
        S3ContextBuilder s3ContextBuilder = s3FactoryCreator.create(riakcsServiceInfo, null);
        return s3ContextBuilder.getContextBuilder().overrides(storeProviderInitProperties).buildView(BlobStoreContext.class);
    }

    @Bean
    public Filer filer() throws InstantiationException, IllegalAccessException {
        return FactoryFiler.createFiler(this.filerType);
    }


}
