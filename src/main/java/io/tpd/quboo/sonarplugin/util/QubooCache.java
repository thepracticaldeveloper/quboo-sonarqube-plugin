package io.tpd.quboo.sonarplugin.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.tpd.quboo.sonarplugin.dtos.IssuesWrapper;
import io.tpd.quboo.sonarplugin.dtos.UsersWrapper;
import io.tpd.quboo.sonarplugin.pojos.Issue;
import io.tpd.quboo.sonarplugin.pojos.User;

import java.time.Duration;

/**
 * This cache avoids sending the same users and issues to the server over and over again.
 */
public enum QubooCache {

  INSTANCE;

  private Cache<String, String> usersCache;
  private Cache<String, String> issuesCache;

  QubooCache() {
    usersCache = CacheBuilder.newBuilder().maximumSize(5000).expireAfterWrite(Duration.ofDays(2)).build();
    issuesCache = CacheBuilder.newBuilder().maximumSize(11000).expireAfterWrite(Duration.ofDays(2)).build();
  }

  public void toCache(final UsersWrapper usersWrapper) {
    usersWrapper.getUsers().forEach(user -> usersCache.put(user.getLogin(), user.getName()));
  }

  public void toCache(final IssuesWrapper issuesWrapper) {
    issuesWrapper.getIssues().forEach(issue -> issuesCache.put(issueHash(issue), issue.getKey()));
  }

  public boolean inCache(final User user) {
    return usersCache.getIfPresent(user.getLogin()) != null;
  }

  private String issueHash(final Issue issue) {
    return issue.getKey() + issue.getAssignee() + issue.getStatus() + issue.getResolution() + issue.getAuthor();
  }

  public boolean inCache(final Issue issue) {
    return issuesCache.getIfPresent(issueHash(issue)) != null;
  }

  public void clear() {
    issuesCache.invalidateAll();
    usersCache.invalidateAll();
  }
}
