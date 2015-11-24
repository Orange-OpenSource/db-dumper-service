package com.orange.clara.cloud.servicedbdumper.dbdumper.running.core;

import com.google.common.io.ByteStreams;
import com.orange.clara.cloud.servicedbdumper.dbdumper.DatabaseDumper;
import com.orange.clara.cloud.servicedbdumper.dbdumper.running.Restorer;
import com.orange.clara.cloud.servicedbdumper.exception.CannotFindDatabaseDumperException;
import com.orange.clara.cloud.servicedbdumper.exception.RestoreCannotFindFile;
import com.orange.clara.cloud.servicedbdumper.exception.RestoreException;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseDumpFile;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p/>
 * Author: Arthur Halet
 * Date: 01/10/2015
 */
public class CoreRestorer extends AbstractCoreDbAction implements Restorer {

    private Logger logger = LoggerFactory.getLogger(CoreRestorer.class);

    @Override
    public void restore(DatabaseRef databaseRefSource, DatabaseRef databaseRefTarget, Date date) throws RestoreException {
        logger.info("Restoring dump file from " + databaseRefSource.getName() + " to " + databaseRefTarget.getName() + " ...");
        DatabaseDumpFile dumpFile = this.findDumpFile(databaseRefSource, date);
        if (dumpFile == null) {
            throw new RestoreCannotFindFile(databaseRefSource);
        }
        String fileName = this.getFileName(dumpFile);
        try {
            DatabaseDumper databaseDumper = findAndCheckDatabaseDumper(databaseRefSource, databaseRefTarget);
            this.runRestore(databaseDumper, fileName);
        } catch (Exception e) {
            throw new RestoreException("An error occurred: " + e.getMessage(), e);
        }
        logger.info("Restoring dump file from " + databaseRefSource.getName() + " to " + databaseRefTarget.getName() + " finished.");
    }

    protected void runRestore(DatabaseDumper databaseDumper, String fileName) throws IOException, InterruptedException {
        Process p = this.runCommandLine(databaseDumper.getRestoreCommandLine());
        OutputStream outputStream = p.getOutputStream();
        BlobStore blobStore = this.blobStoreContext.getBlobStore();
        Blob blob = blobStore.getBlob(this.bucketName, fileName);
        InputStream inputStream = blob.getPayload().openStream();
        ByteStreams.copy(inputStream, outputStream);
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        p.waitFor();
    }

    protected DatabaseDumper findAndCheckDatabaseDumper(DatabaseRef databaseRefSource, DatabaseRef databaseRefTarget) throws CannotFindDatabaseDumperException, RestoreException {
        if (databaseRefSource.getType().equals(databaseRefTarget.getType())) {
            throw new RestoreException("Database " + databaseRefTarget.getName() + " should be a " + databaseRefSource.getType() + " database");
        }
        return dbDumpersFactory.getDatabaseDumper(databaseRefTarget);
    }

    private String getFileName(DatabaseDumpFile databaseDumpFile) {
        return databaseDumpFile.getDatabaseRef().getName() + "/" + databaseDumpFile.getFileName();
    }

    private DatabaseDumpFile findDumpFile(DatabaseRef databaseRefSource, Date date) {
        if (date == null) {
            return this.databaseDumpFileRepo.findFirstByDatabaseRefOrderByCreatedAtDesc(databaseRefSource);
        }
        return this.databaseDumpFileRepo.findByDatabaseRefAndCreatedAt(databaseRefSource, date);
    }

    @Override
    public void restore(DatabaseRef databaseRefSource, DatabaseRef databaseRefTarget) throws RestoreException {
        this.restore(databaseRefSource, databaseRefTarget, null);
    }
}
