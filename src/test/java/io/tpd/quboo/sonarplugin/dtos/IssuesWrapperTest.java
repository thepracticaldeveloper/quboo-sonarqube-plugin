package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.Issue;
import io.tpd.quboo.sonarplugin.pojos.Issues;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IssuesWrapperTest {

  private IssuesWrapper w;

  @Before
  public void setup() {
    w = new IssuesWrapper();
  }

  @Test
  public void issueWithoutProjectIncluded() {
    Issues issues = buildIssuesWithProjects(null, null);
    List<String> selected = toList("project1");
    List<String> rejected = toList("project2");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).hasSize(2);
  }

  @Test
  public void issueWithNoMatchIncluded() {
    Issues issues = buildIssuesWithProjects("project3");
    List<String> selected = toList();
    List<String> rejected = toList("project2");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void issueWithSelectedMatchIncluded() {
    Issues issues = buildIssuesWithProjects("project1");
    List<String> selected = toList("project1");
    List<String> rejected = toList("project3");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void issueWithNoSelectedMatchExcluded() {
    Issues issues = buildIssuesWithProjects("project2");
    List<String> selected = toList("project1");
    List<String> rejected = toList();
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).isEmpty();
  }

  @Test
  public void issueWithDoubleMatchNotExcluded() {
    Issues issues = buildIssuesWithProjects("project1");
    List<String> selected = toList("project1");
    List<String> rejected = toList("project1");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void allIssuesIncludedWithEmptyFilters() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3");
    List<String> selected = toList();
    List<String> rejected = toList();
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).hasSize(4);
  }

  @Test
  public void multipleExcluded() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3");
    List<String> selected = toList();
    List<String> rejected = toList("project1", "project3");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).extracting("project").containsExactlyInAnyOrder(null, "project2");
  }

  @Test
  public void bothFilters1() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3", "project5");
    List<String> selected = toList("project2");
    List<String> rejected = toList("project1", "project3");
    w.filterAndAddIssues(issues, selected, rejected, null, null);

    assertThat(w.getIssues()).extracting("project").containsExactlyInAnyOrder(null, "project2");
  }

  @Test
  public void issueWithSelectedUserList() {
    Issues issues = buildIssuesWithAssignees("johndoe", "pepe");
    List<String> selectedUsers = toList("pepe");
    w.filterAndAddIssues(issues, null, null, selectedUsers, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void issueWithAllSelectedUsersList() {
    Issues issues = buildIssuesWithAssignees("johndoe", "pepe");
    List<String> selectedUsers = toList("pepe", "johndoe");
    w.filterAndAddIssues(issues, null, null, selectedUsers, null);

    assertThat(w.getIssues()).hasSize(2);
  }

  private List<String> toList(String... s) {
    List<String> selected = new ArrayList<>();
    if(s != null) {
      selected.addAll(Arrays.asList(s));
    }
    return selected;
  }

  private Issues buildIssuesWithProjects(String... projects) {
    Issues issues = new Issues();
    issues.setIssues(new ArrayList<>());
    for (String project : projects) {
      Issue i = new Issue();
      i.setProject(project);
      issues.getIssues().add(i);
    }
    return issues;
  }

  private Issues buildIssuesWithAssignees(String... assignees) {
    Issues issues = new Issues();
    issues.setIssues(new ArrayList<>());
    for (String assignee : assignees) {
      Issue i = new Issue();
      i.setAssignee(assignee);
      issues.getIssues().add(i);
    }
    return issues;
  }
}
