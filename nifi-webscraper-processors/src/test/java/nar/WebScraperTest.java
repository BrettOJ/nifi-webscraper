/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package webscrape.konjur.nifi.processors.webscraper;

import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.List;
import java.util.logging.Logger;

@TestInstance(Lifecycle.PER_CLASS) 
public class WebScraperTest {

    private TestRunner testRunner;

    // IMPORTANT : make sure these variables are set correctly for your system
    private final String URL_TO_TEST = "http://blog.konjur.cloud";
    private final String CSS_SELECTOR = "body > app-root > main > div > div.left-side > h1";
    private final String CHROME_DRIVER_FILEPATH = "/home/brettoj/chromedriver/chromedriver";
    private final String EXPECTED_DATA = "Hello, konjur-blog";
       
    @BeforeAll
    public void init() {
        testRunner = TestRunners.newTestRunner(WebScraper.class);
        
    }

    @Test
    public void testProcessor() {
        // Add properites
        testRunner.setProperty(WebScraper.CHROME_DRIVER_PROPERTY, CHROME_DRIVER_FILEPATH);
        testRunner.setProperty(WebScraper.URL_PROPERTY, URL_TO_TEST);
        testRunner.setProperty(WebScraper.DATA_ELEMENT_PROPERTY, CSS_SELECTOR);

        testRunner.setRunSchedule(60);
        testRunner.run();

        testRunner.assertTransferCount(WebScraper.SUCCESS_RELATIONSHIP, 1);

        List<MockFlowFile> results = testRunner.getFlowFilesForRelationship(WebScraper.SUCCESS_RELATIONSHIP);
        MockFlowFile result = results.get(0);

        result.assertAttributeEquals("webscraper.data", EXPECTED_DATA);
        result.assertAttributeEquals("webscraper.url", URL_TO_TEST);
        }
    }
