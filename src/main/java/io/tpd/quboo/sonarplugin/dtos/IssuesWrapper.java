package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.Issue;
import io.tpd.quboo.sonarplugin.pojos.Issues;
import io.tpd.quboo.sonarplugin.util.QubooCache;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tpd.quboo.sonarplugin.QubooPlugin.QUBOO_API_VERSION;

public class IssuesWrapper {

  private static final Logger log = Loggers.get(IssuesWrapper.class);

  private List<Issue> issues;
  private String version = QUBOO_API_VERSION;
  private String sonarVersion;

  public IssuesWrapper() {
    this.issues = new ArrayList<>();
  }

  public void filterAndAddIssues(final Issues issues,
                                 final List<String> selectedProjects,
                                 final List<String> excludedProjects,
                                 final List<String> selectedUsers,
                                 final String sonarVersion) {
    log.info("Quboo project filters - Included: {} | Excluded: {}",
      selectedProjects, excludedProjects);
    log.info("Quboo user filter - Included: {}",
      selectedUsers);
    this.issues.addAll(
      issues.getIssues().stream()
        .filter(issue -> issue.getProject() == null ||
          selectedProjects == null || selectedProjects.isEmpty() ||
          selectedProjects.contains(issue.getProject())
        )
        .filter(issue -> issue.getProject() == null ||
          (selectedProjects != null && !selectedProjects.isEmpty()) ||
          excludedProjects == null || excludedProjects.isEmpty() ||
          !excludedProjects.contains(issue.getProject())
        )
        .filter(issue -> issue.getAssignee() == null ||
          selectedUsers == null || selectedUsers.isEmpty() ||
          selectedUsers.contains(issue.getAssignee()))
        .filter(issue -> !QubooCache.INSTANCE.inCache(issue))
        .collect(Collectors.toList())
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
