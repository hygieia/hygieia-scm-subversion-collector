package com.capitalone.dashboard.collector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link SubversionSettings}
 */
public class SubversionSettingsTest {

  private SubversionSettings subject;

  private static final String CRON = "* * * * * *";

  private static final String USERNAME = "username";

  private static final String PASSWORD = "password";

  private static final int COMMIT_THRESHOLD_DAYS = 3;

  @Before
  public void before() {
    subject = new SubversionSettings();
    subject.setCron(CRON);
    subject.setUsername(USERNAME);
    subject.setPassword(PASSWORD);
    subject.setCommitThresholdDays(COMMIT_THRESHOLD_DAYS);
  }

  @Test
  public void testPojoFields() {
    assertEquals(CRON, subject.getCron());
    assertEquals(USERNAME, subject.getUsername());
    assertEquals(PASSWORD, subject.getPassword());
    assertEquals(COMMIT_THRESHOLD_DAYS, subject.getCommitThresholdDays());
  }
}
