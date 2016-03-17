package com.orange.clara.cloud.servicedbdumper.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 12/10/2015
 */
@Entity
public class DbDumperServiceInstance {
    @Id
    private String serviceInstanceId;
    private String planId;
    private String organizationGuid;
    private String spaceGuid;
    private String dashboardUrl;

    @ManyToOne
    @JoinColumn(name = "db_dumper_plan_id")
    private DbDumperPlan dbDumperPlan;

    @ManyToOne
    @JoinColumn(name = "database_ref_id")
    private DatabaseRef databaseRef;

    @OneToMany(mappedBy = "dbDumperServiceInstance")
    private List<DbDumperServiceInstanceBinding> dbDumperServiceInstanceBindings;

    public DbDumperServiceInstance() {
        this.dbDumperServiceInstanceBindings = new ArrayList<>();
    }

    public DbDumperServiceInstance(String serviceInstanceId, String planId, String organizationGuid, String spaceGuid, String dashboardUrl, DbDumperPlan dbDumperPlan) {
        this();
        this.serviceInstanceId = serviceInstanceId;
        this.planId = planId;
        this.organizationGuid = organizationGuid;
        this.spaceGuid = spaceGuid;
        this.dashboardUrl = dashboardUrl;
        this.dbDumperPlan = dbDumperPlan;
    }

    public DbDumperServiceInstance(String serviceInstanceId, String planId, String organizationGuid, String spaceGuid, String dashboardUrl, DbDumperPlan dbDumperPlan, DatabaseRef databaseRef) {
        this(serviceInstanceId, planId, organizationGuid, spaceGuid, dashboardUrl, dbDumperPlan);
        this.setDatabaseRef(databaseRef);
    }

    public void addDbDumperServiceInstanceBinding(DbDumperServiceInstanceBinding dbDumperServiceInstanceBinding) {
        if (this.dbDumperServiceInstanceBindings.contains(dbDumperServiceInstanceBinding)) {
            return;
        }
        this.dbDumperServiceInstanceBindings.add(dbDumperServiceInstanceBinding);
    }

    public void removeDbDumperServiceInstanceBinding(DbDumperServiceInstanceBinding dbDumperServiceInstanceBinding) {
        if (!this.dbDumperServiceInstanceBindings.contains(dbDumperServiceInstanceBinding)) {
            return;
        }
        this.dbDumperServiceInstanceBindings.remove(dbDumperServiceInstanceBinding);
    }

    public String getServiceInstanceId() {
        return this.serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getPlanId() {
        return this.planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOrganizationGuid() {
        return this.organizationGuid;
    }

    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    public String getSpaceGuid() {
        return this.spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    public String getDashboardUrl() {
        return this.dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public DatabaseRef getDatabaseRef() {
        return databaseRef;
    }

    public void setDatabaseRef(DatabaseRef databaseRef) {
        this.databaseRef = databaseRef;
        this.databaseRef.addDbDumperServiceInstance(this);
    }

    public DbDumperPlan getDbDumperPlan() {
        return dbDumperPlan;
    }

    public void setDbDumperPlan(DbDumperPlan dbDumperPlan) {
        this.dbDumperPlan = dbDumperPlan;
    }

    public List<DbDumperServiceInstanceBinding> getDbDumperServiceInstanceBindings() {
        return dbDumperServiceInstanceBindings;
    }

    public void setDbDumperServiceInstanceBindings(List<DbDumperServiceInstanceBinding> dbDumperServiceInstanceBindings) {
        this.dbDumperServiceInstanceBindings = dbDumperServiceInstanceBindings;
    }
}
