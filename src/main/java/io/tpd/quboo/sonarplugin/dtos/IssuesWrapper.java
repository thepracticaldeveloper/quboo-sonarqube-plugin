package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.Issue;
import io.tpd.quboo.sonarplugin.pojos.Issues;

import java.util.ArrayList;
import java.util.List;

public class IssuesWrapper {

  private List<Issue> issues;

  public IssuesWrapper() {
    this.issues = new ArrayList<>();
  }

  public void filterAndAddIssues(final Issues issues) {
    this.issues.addAll(
      issues.getIssues()
    );
  }

  public List<Issue> getIssues() {
    return issues;
  }
}
