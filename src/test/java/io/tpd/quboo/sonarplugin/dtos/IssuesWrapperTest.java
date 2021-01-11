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
    List<String> selected = projects("project1");
    List<String> rejected = projects("project2");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).hasSize(2);
  }

  @Test
  public void issueWithNoMatchIncluded() {
    Issues issues = buildIssuesWithProjects("project3");
    List<String> selected = projects();
    List<String> rejected = projects("project2");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void issueWithSelectedMatchIncluded() {
    Issues issues = buildIssuesWithProjects("project1");
    List<String> selected = projects("project1");
    List<String> rejected = projects("project3");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void issueWithNoSelectedMatchExcluded() {
    Issues issues = buildIssuesWithProjects("project2");
    List<String> selected = projects("project1");
    List<String> rejected = projects();
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).isEmpty();
  }

  @Test
  public void issueWithDoubleMatchNotExcluded() {
    Issues issues = buildIssuesWithProjects("project1");
    List<String> selected = projects("project1");
    List<String> rejected = projects("project1");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).hasSize(1);
  }

  @Test
  public void allIssuesIncludedWithEmptyFilters() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3");
    List<String> selected = projects();
    List<String> rejected = projects();
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).hasSize(4);
  }

  @Test
  public void multipleExcluded() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3");
    List<String> selected = projects();
    List<String> rejected = projects("project1", "project3");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).extracting("project").containsExactlyInAnyOrder(null, "project2");
  }

  @Test
  public void bothFilters1() {
    Issues issues = buildIssuesWithProjects("project1", null, "project2", "project3", "project5");
    List<String> selected = projects("project2");
    List<String> rejected = projects("project1", "project3");
    w.filterAndAddIssues(issues, selected, rejected, null);

    assertThat(w.getIssues()).extracting("project").containsExactlyInAnyOrder(null, "project2");
  }

  private List<String> projects(String... s) {
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
}
