package com.orange.clara.cloud.servicedbdumper.dbdumper.core;

import com.orange.clara.cloud.servicedbdumper.dbdumper.Credentials;
import com.orange.clara.cloud.servicedbdumper.helper.UrlForge;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseDumpFile;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperCredential;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstance;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 26/11/2015
 */
public class CoreCredentials implements Credentials {

    @Autowired
    @Qualifier(value = "bucketName")
    protected String bucketName;
    @Autowired
    @Qualifier(value = "blobStoreContext")
    protected BlobStoreContext blobStoreContext;

    @Autowired
    @Qualifier(value = "dateFormat")
    private String dateFormat;

    @Autowired
    private UrlForge urlForge;

    @Override
    public List<DbDumperCredential> getCredentials(DbDumperServiceInstance dbDumperServiceInstance) {
        List<DbDumperCredential> dbDumperCredentials = new ArrayList<>();
        List<DatabaseDumpFile> databaseDumpFiles = dbDumperServiceInstance.getDatabaseRef().getDatabaseDumpFiles();
        for (DatabaseDumpFile databaseDumpFile : databaseDumpFiles) {
            dbDumperCredentials.add(new DbDumperCredential(
                    databaseDumpFile.getId(),
                    databaseDumpFile.getCreatedAt(),
                    urlForge.createDownloadLink(databaseDumpFile),
                    urlForge.createShowLink(databaseDumpFile),
                    databaseDumpFile.getFileName()
            ));
        }
        return dbDumperCredentials;
    }
}
