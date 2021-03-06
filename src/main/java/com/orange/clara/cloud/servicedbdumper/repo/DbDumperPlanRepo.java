package com.orange.clara.cloud.servicedbdumper.repo;

import com.orange.clara.cloud.servicedbdumper.model.DbDumperPlan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 21/02/2016
 */
@Repository
public interface DbDumperPlanRepo extends
        PagingAndSortingRepository<DbDumperPlan, String> {
}
