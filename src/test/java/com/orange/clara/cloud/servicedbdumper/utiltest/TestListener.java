package com.orange.clara.cloud.servicedbdumper.utiltest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 20/05/2016
 */
public class TestListener extends RunListener {
    protected static final String DIRECTORY_REPORTS = "reports";
    protected static final String FILE_FORMAT = "dd-MM-yyyy_HHmm";
    protected Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void testRunFinished(Result result) throws Exception {
        List<ReportIntegration> reportIntegrations = ReportManager.getAllReports();
        for (ReportIntegration reportIntegration : reportIntegrations) {
            this.formatReport(reportIntegration);
        }
        this.createReportFile();
        super.testRunFinished(result);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        logger.warn(description.toString());
        super.testIgnored(description);
    }

    private void formatReport(ReportIntegration reportIntegration) {
        String title = "---------" + reportIntegration.getName() + "---------";
        logger.info("\u001b[0;34m{}\u001B[0;0m", title);
        if (reportIntegration.isSkipped()) {
            logger.info("\u001b[1;36mResult: {}\u001B[0;0m", "\u001B[1;33mSkipped");
            logger.info("\u001b[1;36mSkip reason\u001B[0;0m: {}", reportIntegration.getSkippedReason());
            logger.info(this.createSeparator(title.length()));
            return;
        }
        if (reportIntegration.isFailed()) {
            logger.info("\u001b[1;36mResult: {}\u001B[0;0m", "\u001B[0;31mFailed");
            logger.info(this.createSeparator(title.length()));
            return;
        }
        logger.info("\u001b[1;36mResult\u001B[0;0m: {}", "\u001B[0;32mPassed\u001B[0;0m");
        if (reportIntegration.getPopulateFakeDataTime() != 0L) {
            logger.info("\u001b[1;36mCreate fake data duration\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getPopulateFakeDataTime()));

        }
        logger.info("\u001b[1;36mFake data file size (in bytes)\u001B[0;0m: {}", reportIntegration.getFakeDataFileSize());
        logger.info("\u001b[1;36mPopulate fake data duration\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getPopulateToDatabaseTime()));
        logger.info("\u001b[1;36mDump database source duration\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getDumpDatabaseSourceTime()));
        logger.info("\u001b[1;36mRestore database source to database target duration\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getRestoreDatabaseSourceToTargetTime()));
        logger.info("\u001b[1;36mDump database target duration\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getDumpDatabaseTargetTime()));
        logger.info("\u001b[1;36mDiff duration against source and target databases\u001B[0;0m: {}", humanize.Humanize.duration(reportIntegration.getDiffTime()));
        logger.info(this.createSeparator(title.length()));

    }

    private String createSeparator(int length) {
        String separator = "";
        for (int i = 0; i < length; i++) {
            separator += "-";
        }
        return separator + "\n";
    }

    private void createReportFile() throws IOException, URISyntaxException {
        Date d = new Date();
        SimpleDateFormat form = new SimpleDateFormat(FILE_FORMAT);
        File jsonFile = new File(DIRECTORY_REPORTS + File.separator + form.format(d) + ".json");

        File dir = jsonFile.getParentFile();
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, ReportManager.getAllReports());
    }
}
