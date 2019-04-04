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

/**
 * CollectorItem extension to store the subversion url.
 */
public class SubversionRepo extends CollectorItem {
  public static final String URL = "url";
  public static final String LATEST_REV = "rev";

  public String getRepoUrl() {
    return (String) getOptions().get(URL);
  }

  public void setRepoUrl(String instanceUrl) {
    getOptions().put(URL, instanceUrl);
  }

  public long getLatestRevision() {
    Object latestRev = getOptions().get(LATEST_REV);
    return latestRev == null ? 0 : (long) latestRev;
  }

  public void setLatestRev(long latestRev) {
    getOptions().put(LATEST_REV, latestRev);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SubversionRepo subversionRepo = (SubversionRepo) o;

    return getRepoUrl().equals(subversionRepo.getRepoUrl());
  }

  @Override
  public int hashCode() {
    return getRepoUrl().hashCode();
  }
}
