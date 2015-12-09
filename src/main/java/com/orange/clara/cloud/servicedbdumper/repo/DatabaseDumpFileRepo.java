package com.orange.clara.cloud.servicedbdumper.repo;

import com.orange.clara.cloud.servicedbdumper.model.DatabaseDumpFile;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p/>
 * Author: Arthur Halet
 * Date: 03/06/2015
 */
@Repository
public interface DatabaseDumpFileRepo extends
        PagingAndSortingRepository<DatabaseDumpFile, Integer> {
    DatabaseDumpFile findFirstByDatabaseRefOrderByCreatedAtDesc(DatabaseRef databaseRef);

    List<DatabaseDumpFile> findByDatabaseRefOrderByCreatedAtDesc(DatabaseRef databaseRef);

    DatabaseDumpFile findByDatabaseRefAndCreatedAt(DatabaseRef databaseRef, Date date);
}
