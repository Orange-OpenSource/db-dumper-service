package com.orange.clara.cloud.servicedbdumper.dbdumper.running.core;

import org.junit.Test;

import java.io.BufferedReader;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Copyright (C) 2015 Orange
 * <p/>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p/>
 * Author: Arthur Halet
 * Date: 14/09/2015
 */
public class AbstractCoreDbActionTest {

    @Test
    public void runs_commandline_and_returns_output() throws Exception {
        //given
        //a commandline
        String[] commandLine = {"java", "-version"};

        //when
        AbstractCoreDbAction cmdLineRunner = new AbstractCoreDbAction() {
        };

        //then
        //command output is available:
        Process p = cmdLineRunner.runCommandLine(commandLine);
        BufferedReader output = cmdLineRunner.getOutput(p);
        BufferedReader error = cmdLineRunner.getError(p);
        String outputLine = "";
        String line = "";

        while ((line = output.readLine()) != null) {
            outputLine += line;
        }
        while ((line = error.readLine()) != null) {
            outputLine += line;
        }
        p.waitFor();


        assertThat(outputLine).contains("version");
    }
}