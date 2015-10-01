package com.orange.clara.cloud.controllers;

import com.orange.clara.cloud.dbdump.action.Dumper;
import com.orange.clara.cloud.model.DatabaseRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
@RestController
public class DumperController extends AbstractDbController {

    @Autowired
    @Qualifier(value = "dumper")
    private Dumper dumper;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/dumpdb", method = RequestMethod.GET)
    public String dump(@RequestParam String dbUrl) throws IOException, InterruptedException {
        DatabaseRef databaseRef = this.getDatabaseRefFromUrl(dbUrl, "temp");
        return this.dumper.dump(databaseRef);
    }
}
