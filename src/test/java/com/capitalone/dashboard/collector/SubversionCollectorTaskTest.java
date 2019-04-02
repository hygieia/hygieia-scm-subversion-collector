package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.CommitBuilder;
import com.capitalone.dashboard.Sandbox;
import com.capitalone.dashboard.TestOptions;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.SubversionRepo;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.CommitRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.SubversionRepoRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;
import org.tmatesoft.svn.core.SVNURL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit tests for {@link SubversionCollectorTask}.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubversionCollectorTaskTest {

  @Mock
  private TaskScheduler taskScheduler;
  @Mock
  private BaseCollectorRepository<Collector> collectorRepository;
  @Mock
  private SubversionRepoRepository subversionRepoRepository;
  @Mock
  private CommitRepository commitRepository;
  @Mock
  private SubversionClient subversionClient;
  @Mock
  private SubversionSettings subversionSettings;
  @Mock
  private ComponentRepository dbComponentRepository;


  private SubversionCollectorTask subject;


  @Before
  public void before() {
    subject = new SubversionCollectorTask(
        taskScheduler,
        collectorRepository,
        subversionRepoRepository,
        commitRepository,
        dbComponentRepository,
        subversionClient,
        subversionSettings
    );
  }

  @Test
  public void testGetCollector() {
    Collector collector = subject.getCollector();
    assertEquals("Subversion", collector.getName());
    assertEquals(CollectorType.SCM, collector.getCollectorType());
    assertEquals(Boolean.TRUE, collector.isEnabled());
    assertEquals(Boolean.TRUE, collector.isOnline());
    assertEquals(0, collector.getErrors().size());
    assertEquals("", collector.getUniqueFields().get("url"));
  }

  @Test
  public void testPojoFields() {
    assertEquals(collectorRepository, subject.getCollectorRepository());
    when(subversionSettings.getCron()).thenReturn("* * * * * *");
    assertEquals("* * * * * *", subject.getCron());
  }

  @Test
  public void testCollect() throws Exception {
    ObjectId collectorId = ObjectId.get();

    //clean()
    List<Component> components = new ArrayList<>();
    Component testComponent = new Component();
    List<CollectorItem> collectorItemList = new ArrayList<>();
    CollectorItem collectorItem = new CollectorItem();
    collectorItem.setCollectorId(collectorId);
    collectorItemList.add(collectorItem);
    testComponent.getCollectorItems().put(CollectorType.SCM, collectorItemList);
    components.add(testComponent);
    when(dbComponentRepository.findAll()).thenReturn(components);
    Set<ObjectId> svnId = new HashSet<>();
    svnId.add(collectorId);
    List<SubversionRepo> repos = new ArrayList<>();
    repos.add(mock(SubversionRepo.class));
    when(subversionRepoRepository.findByCollectorIdIn(svnId)).thenReturn(repos);

    //enabledRepos()
    SubversionRepo mockRepository = mock(SubversionRepo.class);
    List<SubversionRepo> mockRepos = new ArrayList<>();
    mockRepos.add(mockRepository);
    Collector collector = subject.getCollector();
    collector.setId(collectorId);
    when(subversionRepoRepository.findEnabledSubversionRepos(collectorId)).thenReturn(mockRepos);

    //startRevision()
    when(subversionSettings.getCommitThresholdDays()).thenReturn(2);
    when(subversionClient.getRevisionClosestTo(any(), any())).thenReturn(2l);

    when(subversionClient.getCommits(mockRepository, 2)).thenReturn(generateCommits());

    subject.collect(collector);
    verify(subversionRepoRepository, times(2)).save(mockRepository);
  }

  private List<Commit> generateCommits() throws Exception {
    TestOptions options = TestOptions.getInstance();
    Sandbox sandbox = Sandbox.createWithCleanup("SubversionCollectorTaskTest.generateCommits", options);
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
    SubversionSettings subversionSettings = new SubversionSettings();
    subversionSettings.setCron("* * * * * *");
    subversionSettings.setUsername("username");
    subversionSettings.setPassword("password");
    subversionSettings.setCommitThresholdDays(3);
    DefaultSubversionClient localSubversionClient = new DefaultSubversionClient(subversionSettings);
    return localSubversionClient.getCommits(subversionRepo, 1l);
  }

}
