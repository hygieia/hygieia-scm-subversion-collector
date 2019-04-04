/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.capitalone.dashboard.model;


import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SubversionRepo}
 */
public class SubversionRepoTest {

  private SubversionRepo subject;

  private SubversionRepo subjectEq;

  private SubversionRepo subjectNotEq;

  private static final Long LATEST_REV = 12345l;

  private static final String REPO_URL = "http://some.repo.url";

  private static final Collector COLLECTOR = new Collector();

  private static final ObjectId COLLECTOR_ID = new ObjectId();

  private static final String DESCRIPTION = "An arbitrary description string";

  private static final Boolean ENABLED = Boolean.TRUE;

  private static final String ENVIRONMENT = "env";

  private static final ObjectId ID = new ObjectId();

  private static final Long LAST_UPDATED = 123456l;

  private static final String NICE_NAME = "aNiceName";

  private static final Boolean PUSHED = Boolean.FALSE;


  @Before
  public void before() {
    subject = new SubversionRepo();
    subject.setLatestRev(LATEST_REV);
    subject.setRepoUrl(REPO_URL);
    subject.setCollector(COLLECTOR);
    subject.setCollectorId(COLLECTOR_ID);
    subject.setDescription(DESCRIPTION);
    subject.setEnabled(ENABLED);
    subject.setEnvironment(ENVIRONMENT);
    subject.setId(ID);
    subject.setLastUpdated(LAST_UPDATED);
    subject.setNiceName(NICE_NAME);
    subject.setPushed(PUSHED);

    subjectEq = new SubversionRepo();
    subjectEq.setLatestRev(LATEST_REV);
    subjectEq.setRepoUrl(REPO_URL);
    subjectEq.setCollector(COLLECTOR);
    subjectEq.setCollectorId(COLLECTOR_ID);
    subjectEq.setDescription(DESCRIPTION);
    subjectEq.setEnabled(ENABLED);
    subjectEq.setEnvironment(ENVIRONMENT);
    subjectEq.setId(ID);
    subjectEq.setLastUpdated(LAST_UPDATED);
    subjectEq.setNiceName(NICE_NAME);
    subjectEq.setPushed(PUSHED);

    subjectNotEq = new SubversionRepo();
    subjectNotEq.setRepoUrl("http://not.the.same.url");
  }

  @Test
  public void testPojoFields() {
    assertEquals(LATEST_REV.longValue(), subject.getLatestRevision());
    assertEquals(REPO_URL, subject.getRepoUrl());
  }

  @Test
  public void testHashCode() {
    assertEquals(subject.hashCode(), subjectEq.hashCode());
    assertNotEquals(subject.hashCode(), subjectNotEq.hashCode());
  }

  @Test
  public void testEquals() {
    assertTrue(subject.equals(subjectEq));
    assertFalse(subject.equals(subjectNotEq));
    assertFalse(subject.equals(null));
  }
}
