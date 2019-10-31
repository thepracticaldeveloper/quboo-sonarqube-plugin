package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

  @JsonProperty("key")
  private String key;
  @JsonProperty("rule")
  private String rule;
  @JsonProperty("severity")
  private String severity;
  @JsonProperty("componentId")
  private Integer componentId;
  @JsonProperty("project")
  private String project;
  @JsonProperty("resolution")
  private String resolution;
  @JsonProperty("status")
  private String status;
  @JsonProperty("debt")
  private String debt;
  @JsonProperty("author")
  private String author;
  @JsonProperty("creationDate")
  private String creationDate;
  @JsonProperty("updateDate")
  private String updateDate;
  @JsonProperty("closeDate")
  private String closeDate;
  @JsonProperty("assignee")
  private String assignee;
  @JsonProperty("type")
  private String type;
  @JsonProperty("tags")
  private List<String> tags;

  @JsonProperty("key")
  public String getKey() {
    return key;
  }

  @JsonProperty("key")
  public void setKey(String key) {
    this.key = key;
  }

  public Issue withKey(String key) {
    this.key = key;
    return this;
  }

  @JsonProperty("rule")
  public String getRule() {
    return rule;
  }

  @JsonProperty("rule")
  public void setRule(String rule) {
    this.rule = rule;
  }

  public Issue withRule(String rule) {
    this.rule = rule;
    return this;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public Issue withSeverity(String severity) {
    this.severity = severity;
    return this;
  }

  @JsonProperty("componentId")
  public Integer getComponentId() {
    return componentId;
  }

  @JsonProperty("componentId")
  public void setComponentId(Integer componentId) {
    this.componentId = componentId;
  }

  public Issue withComponentId(Integer componentId) {
    this.componentId = componentId;
    return this;
  }

  @JsonProperty("project")
  public String getProject() {
    return project;
  }

  @JsonProperty("project")
  public void setProject(String project) {
    this.project = project;
  }

  public Issue withProject(String project) {
    this.project = project;
    return this;
  }

  @JsonProperty("resolution")
  public String getResolution() {
    return resolution;
  }

  @JsonProperty("resolution")
  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public Issue withResolution(String resolution) {
    this.resolution = resolution;
    return this;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  public Issue withStatus(String status) {
    this.status = status;
    return this;
  }

  @JsonProperty("debt")
  public String getDebt() {
    return debt;
  }

  @JsonProperty("debt")
  public void setDebt(String debt) {
    this.debt = debt;
  }

  public Issue withDebt(String debt) {
    this.debt = debt;
    return this;
  }

  @JsonProperty("author")
  public String getAuthor() {
    return author;
  }

  @JsonProperty("author")
  public void setAuthor(String author) {
    this.author = author;
  }

  public Issue withAuthor(String author) {
    this.author = author;
    return this;
  }

  @JsonProperty("creationDate")
  public String getCreationDate() {
    return creationDate;
  }

  @JsonProperty("creationDate")
  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public Issue withCreationDate(String creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  @JsonProperty("updateDate")
  public String getUpdateDate() {
    return updateDate;
  }

  @JsonProperty("updateDate")
  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public Issue withUpdateDate(String updateDate) {
    this.updateDate = updateDate;
    return this;
  }

  @JsonProperty("closeDate")
  public String getCloseDate() {
    return closeDate;
  }

  @JsonProperty("closeDate")
  public void setCloseDate(String closeDate) {
    this.closeDate = closeDate;
  }

  public Issue withCloseDate(String closeDate) {
    this.closeDate = closeDate;
    return this;
  }

  @JsonProperty("assignee")
  public String getAssignee() {
    return assignee;
  }

  @JsonProperty("assignee")
  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public Issue withAssignee(String assignee) {
    this.assignee = assignee;
    return this;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  public Issue withType(String type) {
    this.type = type;
    return this;
  }

  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  @JsonProperty("tags")
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Issue withTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Issue issue = (Issue) o;

    return new EqualsBuilder()
      .append(key, issue.key)
      .append(assignee, issue.assignee)
      .append(componentId, issue.componentId)
      .append(status, issue.status)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(assignee)
      .append(key)
      .append(componentId)
      .append(status)
      .toHashCode();
  }
}
