package com.capitalone.dashboard.collector;


import com.capitalone.dashboard.CommitBuilder;
import com.capitalone.dashboard.Sandbox;
import com.capitalone.dashboard.TestOptions;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.SubversionRepo;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNURL;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DefaultSubversionClient}.
 */
public class DefaultSubversionClientTest {

  private DefaultSubversionClient subject;

  @Before
  public void before() {
    SubversionSettings subversionSettings = new SubversionSettings();
    subversionSettings.setCron("* * * * * *");
    subversionSettings.setUsername("username");
    subversionSettings.setPassword("password");
    subversionSettings.setCommitThresholdDays(3);
    subject = new DefaultSubversionClient(subversionSettings);
  }

  @Test
  public void testGetCommits() throws Exception {
    TestOptions options = TestOptions.getInstance();
    Sandbox sandbox = Sandbox.createWithCleanup("DefaultSubversionClientTest.testGetCommits", options);
    SVNURL url = sandbox.createSvnRepository();
    final CommitBuilder commitBuilder1 = new CommitBuilder(url);
    commitBuilder1.addFile("file");
    commitBuilder1.setCommitMessage("Commit 1");
    commitBuilder1.commit();

    final CommitBuilder commitBuilder2 = new CommitBuilder(url);
    commitBuilder2.delete("file");
    commitBuilder2.setCommitMessage("Commit 2");
    commitBuilder2.commit();
    SubversionRepo subversionRepo = new SubversionRepo();
    subversionRepo.setRepoUrl(url.toString());
    List<Commit> commits = subject.getCommits(subversionRepo, 1l);
    assertEquals(2, commits.size());
    assertEquals("Commit 1", commits.get(0).getScmCommitLog());
    assertEquals("Commit 2", commits.get(1).getScmCommitLog());
  }
}
