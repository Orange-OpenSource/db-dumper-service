package com.orange.clara.cloud.servicedbdumper.helper;

import com.orange.clara.cloud.servicedbdumper.model.DatabaseDumpFile;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperPlan;
import com.orange.clara.cloud.servicedbdumper.model.DbDumperServiceInstance;

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
public class CalculateQuota {

    public static Long calculateFullQuota(DatabaseRef databaseRef) {
        Long size = 0L;
        for (DbDumperServiceInstance dbDumperServiceInstance : databaseRef.getDbDumperServiceInstances()) {
            DbDumperPlan dbDumperPlan = dbDumperServiceInstance.getDbDumperPlan();
            if (dbDumperPlan.getSize() == null) {
                return null;
            }
            size += dbDumperPlan.getSize();
        }
        return size;
    }

    public static Float calculateFullPrice(DatabaseRef databaseRef) {
        Float price = 0.0F;
        for (DbDumperServiceInstance dbDumperServiceInstance : databaseRef.getDbDumperServiceInstances()) {
            DbDumperPlan dbDumperPlan = dbDumperServiceInstance.getDbDumperPlan();
            price += dbDumperPlan.getCost();
        }
        return price;
    }

    public static Long calculateQuotaUsed(DbDumperServiceInstance dbDumperServiceInstance) {
        return calculateQuotaUsed(dbDumperServiceInstance.getDatabaseRef());
    }

    public static Long calculateQuotaUsed(DatabaseRef databaseRef) {
        Long fullQuota = calculateFullQuota(databaseRef);
        if (fullQuota == null) {
            return null;
        }
        return fullQuota - calculateDumpFullSize(databaseRef);
    }

    public static Long calculateQuotaUsedInPercent(DbDumperServiceInstance dbDumperServiceInstance) {
        return calculateQuotaUsedInPercent(dbDumperServiceInstance.getDatabaseRef());
    }

    public static Long calculateQuotaUsedInPercent(DatabaseRef databaseRef) {
        Long fullQuota = calculateFullQuota(databaseRef);
        if (fullQuota == null) {
            return 0L;
        }
        return calculateDumpFullSize(databaseRef) * 100 / fullQuota;
    }

    public static Long calculateDumpFullSize(DbDumperServiceInstance dbDumperServiceInstance) {
        return calculateDumpFullSize(dbDumperServiceInstance.getDatabaseRef());
    }

    public static Long calculateDumpFullSize(DatabaseRef databaseRef) {
        Long size = 0L;
        for (DatabaseDumpFile databaseDumpFile : databaseRef.getDatabaseDumpFiles()) {
            size += databaseDumpFile.getSize();
        }
        return size;
    }
}
