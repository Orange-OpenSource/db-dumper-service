package com.orange.clara.cloud.servicedbdumper.security.useraccess;

import com.orange.clara.cloud.servicedbdumper.exception.UserAccessRightException;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstance;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 11/02/2016
 */
public interface UserAccessRight {

    Boolean haveAccessToServiceInstance(String serviceInstanceId) throws UserAccessRightException;

    Boolean haveAccessToServiceInstance(DatabaseRef databaseRef) throws UserAccessRightException;

    Boolean haveAccessToServiceInstance(DbDumperServiceInstance dbDumperServiceInstance) throws UserAccessRightException;
}
