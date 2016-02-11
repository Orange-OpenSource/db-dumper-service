package com.orange.clara.cloud.servicedbdumper.security;

import com.orange.clara.cloud.servicedbdumper.exception.UserAccessRightException;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstance;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C) 2016 Arthur Halet
 * <p/>
 * This software is distributed under the terms and conditions of the 'MIT'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'http://opensource.org/licenses/MIT'.
 * <p/>
 * Author: Arthur Halet
 * Date: 11/02/2016
 */
@Service
public class CloudFoundryUserAccessRight implements UserAccessRight {

    @Autowired
    @Qualifier("cloudFoundryClientAsUser")
    private CloudFoundryClient cloudFoundryClient;

    @Override
    public Boolean haveAccessToServiceInstance(String serviceInstanceId) throws UserAccessRightException {
        List<CloudService> cloudServices = this.cloudFoundryClient.getServices();
        for (CloudService cloudService : cloudServices) {
            if (cloudService.getMeta().getGuid().toString().equals(serviceInstanceId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean haveAccessToServiceInstance(List<DbDumperServiceInstance> dbDumperServiceInstances) throws UserAccessRightException {
        for (DbDumperServiceInstance dbDumperServiceInstance : dbDumperServiceInstances) {
            if (this.haveAccessToServiceInstance(dbDumperServiceInstance)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean haveAccessToServiceInstance(DbDumperServiceInstance dbDumperServiceInstance) throws UserAccessRightException {
        return this.haveAccessToServiceInstance(dbDumperServiceInstance.getServiceInstanceId());
    }
}
