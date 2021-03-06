package com.orange.clara.cloud.servicedbdumper.dbdumper;

import com.orange.clara.cloud.servicedbdumper.dbdumper.core.dbdrivers.MongodbDatabaseDriver;
import com.orange.clara.cloud.servicedbdumper.dbdumper.core.dbdrivers.MysqlDatabaseDriver;
import com.orange.clara.cloud.servicedbdumper.dbdumper.core.dbdrivers.PostgresqlDatabaseDriver;
import com.orange.clara.cloud.servicedbdumper.dbdumper.core.dbdrivers.RedisDatabaseDriver;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseRef;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Copyright (C) 2015 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 03/06/2015
 */
public class DatabaseLocalDumperTest {

    private final String password = "jojo";
    private final String user = "toto";
    private final Integer port = 8080;
    private final String databaseName = "myBase";
    private final String host = "local";
    private final String serviceName = "dbTest";
    private final File dumpBinaries = new File("dumpBinaries");
    private final File restoreBinaries = new File("restoreBinaries");
    private final String dumpFileName = "dump.sql";
    private DatabaseRef databaseRef;
    private MysqlDatabaseDriver mysqlDatabaseDumper;
    private PostgresqlDatabaseDriver postgresqlDatabaseDumper;
    private MongodbDatabaseDriver mongodbDatabaseDumper;
    private RedisDatabaseDriver redisDatabaseDumper;

    @Before
    public void init() {
        databaseRef = new DatabaseRef();

        databaseRef.setUser(user);
        databaseRef.setPassword(password);
        databaseRef.setPort(port);
        databaseRef.setDatabaseName(databaseName);
        databaseRef.setHost(host);
        databaseRef.setName(serviceName);

        this.mysqlDatabaseDumper = new MysqlDatabaseDriver(this.dumpBinaries, this.restoreBinaries);
        mysqlDatabaseDumper.setDatabaseRef(this.databaseRef);

        this.postgresqlDatabaseDumper = new PostgresqlDatabaseDriver(this.dumpBinaries, this.restoreBinaries);
        postgresqlDatabaseDumper.setDatabaseRef(this.databaseRef);

        this.mongodbDatabaseDumper = new MongodbDatabaseDriver(this.dumpBinaries, this.restoreBinaries);
        mongodbDatabaseDumper.setDatabaseRef(this.databaseRef);

        this.redisDatabaseDumper = new RedisDatabaseDriver(this.dumpBinaries);
        redisDatabaseDumper.setDatabaseRef(this.databaseRef);
    }

    @Test
    public void create_command_line_for_dump_postgres() throws Exception {
        String[] expected = String.format(
                "%s --dbname=postgresql://%s:%s@%s:%s/%s --clean --no-owner --no-privileges",
                this.dumpBinaries.getAbsolutePath(),
                this.user,
                this.password,
                this.host,
                this.port,
                this.databaseName
        ).split(" ");
        assertThat(postgresqlDatabaseDumper.getDumpCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_restore_postgres() throws Exception {
        String[] expected = String.format(
                "%s --dbname=postgresql://%s:%s@%s:%s/%s -f -",
                this.restoreBinaries.getAbsolutePath(),
                this.user,
                this.password,
                this.host,
                this.port,
                this.databaseName
        ).split(" ");
        assertThat(postgresqlDatabaseDumper.getRestoreCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_dump_mongodb() throws Exception {
        String[] expected = {
                dumpBinaries.getAbsolutePath(),
                "--host",
                this.host,
                "--port",
                this.port.toString(),
                "--username",
                this.user,
                "--password",
                this.password,
                "--db",
                this.databaseName,
                "--archive"
        };
        assertThat(mongodbDatabaseDumper.getDumpCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_restore_mongodb() throws Exception {
        String[] expected = {
                restoreBinaries.getAbsolutePath(),
                "--host",
                this.host,
                "--port",
                this.port.toString(),
                "--username",
                this.user,
                "--password",
                this.password,
                "--targetDb",
                this.databaseName,
                "--drop",
                "--archive"
        };
        assertThat(mongodbDatabaseDumper.getRestoreCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_dump_redis() throws Exception {
        String[] expected = {
                dumpBinaries.getAbsolutePath(),
                "-s",
                this.host,
                "-p",
                this.port.toString(),
                "-a",
                this.password,
                "dump",
                "-o"
        };
        assertThat(redisDatabaseDumper.getDumpCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_restore_redis() throws Exception {
        String[] expected = {
                dumpBinaries.getAbsolutePath(),
                "-s",
                this.host,
                "-p",
                this.port.toString(),
                "-a",
                this.password,
                "restore",
                "--flushdb",
                "-i"
        };
        assertThat(redisDatabaseDumper.getRestoreCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_dump_mysql() throws Exception {
        String[] expected = {
                dumpBinaries.getAbsolutePath(),
                "--routines",
                "--skip-add-locks",
                "--skip-extended-insert",
                "--single-transaction",
                "--add-drop-table",
                "--skip-comments",
                "--host=" + this.host,
                "--port=" + this.port,
                "--user=" + this.user,
                "--password=" + this.password,
                this.databaseName,
        };
        assertThat(mysqlDatabaseDumper.getDumpCommandLine()).isEqualTo(expected);
    }

    @Test
    public void create_command_line_for_restore_mysql() throws Exception {
        String[] expected = {
                restoreBinaries.getAbsolutePath(),
                "--host=" + this.host,
                "--port=" + this.port,
                "--user=" + this.user,
                "--password=" + this.password,
                this.databaseName,
        };
        assertThat(mysqlDatabaseDumper.getRestoreCommandLine()).isEqualTo(expected);
    }
}