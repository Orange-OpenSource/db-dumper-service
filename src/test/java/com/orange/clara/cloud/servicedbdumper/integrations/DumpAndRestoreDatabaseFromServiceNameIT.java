package com.orange.clara.cloud.servicedbdumper.integrations;

import com.orange.clara.cloud.servicedbdumper.Application;
import com.orange.clara.cloud.servicedbdumper.integrations.config.FakeCloudFoundryClientConfig;
import com.orange.clara.cloud.servicedbdumper.model.DatabaseType;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 08/04/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration({Application.class, FakeCloudFoundryClientConfig.class})
@WebIntegrationTest(randomPort = true)
@ActiveProfiles({"local", "cloud", "integrationrealcf"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DumpAndRestoreDatabaseFromServiceNameIT extends AbstractIntegrationWithRealCfClientTest {

    @Override
    public String getDbParamsForDump(DatabaseType databaseType) {
        return this.databaseAccessMap.get(databaseType).getServiceSourceInstanceName();

    }

    @Override
    public String getDbParamsForRestore(DatabaseType databaseType) {
        return this.databaseAccessMap.get(databaseType).getServiceTargetInstanceName();
    }
}
