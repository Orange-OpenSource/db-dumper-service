package com.orange.clara.cloud.servicedbdumper.dbdumper.running.core;

import com.orange.clara.cloud.servicedbdumper.dbdumper.DatabaseDumper;
import com.orange.clara.cloud.servicedbdumper.dbdumper.running.Dumper;
import com.orange.clara.cloud.servicedbdumper.exception.DumpException;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseDumpFile;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p/>
 * Author: Arthur Halet
 * Date: 09/09/2015
 */
public class CoreDumper extends AbstractCoreDbAction implements Dumper {

    private Logger logger = LoggerFactory.getLogger(CoreDumper.class);


    @Override
    @Transactional
    public void dump(DatabaseRef databaseRef) throws DumpException {
        String fileName = this.generateFileName();
        try {
            DatabaseDumper databaseDumper = dbDumpersFactory.getDatabaseDumper(databaseRef);
            logger.info("Dumping database '" + databaseRef.getName() + "' with " + databaseRef.getType() + " binary ...");
            this.runDump(databaseDumper, databaseRef.getName() + "/" + fileName);
            this.createDatabaseDumpFile(databaseRef, fileName);
        } catch (Exception e) {
            this.logOutputFromProcess();
            e.printStackTrace();
            throw new DumpException("An error occurred: " + e.getMessage() + this.getErrorMessageFromProcess(), e);
        }
        logger.info("Dumping database '" + databaseRef.getName() + "' with " + databaseRef.getType() + " binary finished.");
    }

    private void runDump(DatabaseDumper databaseDumper, String fileName) throws IOException, InterruptedException {
        Process p = this.runCommandLine(databaseDumper.getDumpCommandLine());
        this.filer.store(p.getInputStream(), fileName);
    }

    private void createDatabaseDumpFile(DatabaseRef databaseRef, String fileName) {
        SimpleDateFormat form = new SimpleDateFormat(this.dateFormat);
        Date today = new Date();
        try {
            today = form.parse(form.format(new Date()));
        } catch (ParseException e) { // should have no error
        }
        if (this.databaseDumpFileRepo.findByDatabaseRefAndCreatedAt(databaseRef, today) == null) {
            this.databaseDumpFileRepo.save(new DatabaseDumpFile(fileName, databaseRef));
        }
    }
}
