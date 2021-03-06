package com.orange.clara.cloud.servicedbdumper.task.asynctask;

import com.orange.clara.cloud.servicedbdumper.dbdumper.Deleter;
import com.orange.clara.cloud.servicedbdumper.exception.AsyncTaskException;
import com.orange.clara.cloud.servicedbdumper.model.Job;
import com.orange.clara.cloud.servicedbdumper.model.JobEvent;
import com.orange.clara.cloud.servicedbdumper.repo.JobRepo;
import com.orange.clara.cloud.servicedbdumper.task.job.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

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
public class DeleteDumpTask {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private Deleter deleter;

    @Autowired
    @Qualifier("jobFactory")
    private JobFactory jobFactory;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Future<Boolean> runTask(Integer jobId) throws AsyncTaskException {
        Job job = this.jobRepo.findOne(jobId);
        this.deleter.deleteAll(job.getDbDumperServiceInstance());
        this.jobFactory.createJobDeleteDbDumperServiceInstance(job.getDbDumperServiceInstance());

        job.setJobEvent(JobEvent.FINISHED);
        jobRepo.save(job);
        return new AsyncResult<Boolean>(true);
    }
}
