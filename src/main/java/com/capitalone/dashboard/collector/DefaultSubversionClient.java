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

package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.SubversionRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * SubversionClient implementation that uses SVNKit to fetch information about
 * Subversion repositories.
 */
@Component
public class DefaultSubversionClient implements SubversionClient {
  private static final Log LOG = LogFactory.getLog(DefaultSubversionClient.class);

  private final SubversionSettings settings;

  @Autowired
  public DefaultSubversionClient(SubversionSettings settings) {
    this.settings = settings;
    DAVRepositoryFactory.setup();
  }

  @Override
  public List<Commit> getCommits(SubversionRepo repo, long startRevision) {
    List<Commit> commits = new ArrayList<>();

    for (Object entry : getHistory(repo.getRepoUrl(), startRevision)) {
      SVNLogEntry logEntry = (SVNLogEntry) entry;

      Commit commit = new Commit();
      commit.setTimestamp(System.currentTimeMillis());
      commit.setScmUrl(repo.getRepoUrl());
      commit.setScmRevisionNumber(String.valueOf(logEntry.getRevision()));
      commit.setScmAuthor(logEntry.getAuthor());
      commit.setScmCommitLog(logEntry.getMessage());
      commit.setScmCommitTimestamp(logEntry.getDate().getTime());
      commit.setNumberOfChanges(logEntry.getChangedPaths().size());
      commits.add(commit);
    }

    return commits;
  }

  /**
   * Returns the recent repository revision number for the particular moment
   * in time - the closest one before or at the specified datestamp.
   *
   * @param url          url of subversion repository.
   * @param revisionDate revision date.
   * @return the svn revision number.
   */
  public long getRevisionClosestTo(String url, Date revisionDate) {

    try {
      return getSvnRepository(url).getDatedRevision(revisionDate);
    } catch (SVNException svne) {
      LOG.error("Subversion repo: " + url, svne);
    }

    return 0;
  }

  @SuppressWarnings("unchecked")
  private Collection<SVNLogEntry> getHistory(String url, long startRevision) {
    long endRevision = -1; //HEAD (the latest) revision

    try {
      return getSvnRepository(url).log(
          new String[] {""},
          null,
          startRevision,
          endRevision,
          true,
          true
      );
    } catch (SVNException svne) {
      LOG.error("Subversion repo: " + url, svne);
    }

    return Collections.emptySet();
  }

  private SVNRepository getSvnRepository(String url) throws SVNException {
    SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
        settings.getUsername(), settings.getPassword());
    repository.setAuthenticationManager(authManager);
    return repository;
  }
}
