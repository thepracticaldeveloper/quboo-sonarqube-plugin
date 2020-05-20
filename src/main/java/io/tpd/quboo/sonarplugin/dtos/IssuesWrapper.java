package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.Issue;
import io.tpd.quboo.sonarplugin.pojos.Issues;
import io.tpd.quboo.sonarplugin.util.QubooCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tpd.quboo.sonarplugin.QubooPlugin.QUBOO_API_VERSION;

public class IssuesWrapper {

  private List<Issue> issues;
  private String version = QUBOO_API_VERSION;
  private String sonarVersion;

  public IssuesWrapper() {
    this.issues = new ArrayList<>();
  }

  public void filterAndAddIssues(final Issues issues, final String sonarVersion) {
    this.issues.addAll(
      issues.getIssues().stream().filter(issue -> !QubooCache.INSTANCE.inCache(issue)).collect(Collectors.toList())
    );
    this.sonarVersion = sonarVersion;
  }

  public List<Issue> getIssues() {
    return issues;
  }

  public String getVersion() {
    return version;
  }

  public String getSonarVersion() {
    return sonarVersion;
  }
}
