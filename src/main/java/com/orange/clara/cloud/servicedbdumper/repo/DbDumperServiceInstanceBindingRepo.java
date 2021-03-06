package com.orange.clara.cloud.servicedbdumper.repo;

import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstance;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstanceBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p/>
 * Author: Arthur Halet
 * Date: 12/10/2015
 */
@Repository
public interface DbDumperServiceInstanceBindingRepo extends PagingAndSortingRepository<DbDumperServiceInstanceBinding, String> {
    Long deleteByDbDumperServiceInstance(DbDumperServiceInstance dbDumperServiceInstance);
}
