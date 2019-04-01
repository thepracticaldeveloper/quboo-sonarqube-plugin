package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issues {

  @JsonProperty("total")
  private Integer total;
  @JsonProperty("p")
  private Integer p;
  @JsonProperty("ps")
  private Integer ps;
  @JsonProperty("paging")
  private Paging paging;
  @JsonProperty("issues")
  private List<Issue> issues = new ArrayList<Issue>();
  @JsonProperty("components")
  private List<Component> components = new ArrayList<Component>();

  /**
   * @return The total
   */
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  /**
   * @param total The total
   */
  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  public Issues withTotal(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * @return The p
   */
  @JsonProperty("p")
  public Integer getP() {
    return p;
  }

  /**
   * @param p The p
   */
  @JsonProperty("p")
  public void setP(Integer p) {
    this.p = p;
  }

  public Issues withP(Integer p) {
    this.p = p;
    return this;
  }

  /**
   * @return The ps
   */
  @JsonProperty("ps")
  public Integer getPs() {
    return ps;
  }

  /**
   * @param ps The ps
   */
  @JsonProperty("ps")
  public void setPs(Integer ps) {
    this.ps = ps;
  }

  public Issues withPs(Integer ps) {
    this.ps = ps;
    return this;
  }

  /**
   * @return The paging
   */
  @JsonProperty("paging")
  public Paging getPaging() {
    return paging;
  }

  /**
   * @param paging The paging
   */
  @JsonProperty("paging")
  public void setPaging(Paging paging) {
    this.paging = paging;
  }

  public Issues withPaging(Paging paging) {
    this.paging = paging;
    return this;
  }

  /**
   * @return The issues
   */
  @JsonProperty("issues")
  public List<Issue> getIssues() {
    return issues;
  }

  /**
   * @param issues The issues
   */
  @JsonProperty("issues")
  public void setIssues(List<Issue> issues) {
    this.issues = issues;
  }

  public Issues withIssues(List<Issue> issues) {
    this.issues = issues;
    return this;
  }

  /**
   * @return The components
   */
  @JsonProperty("components")
  public List<Component> getComponents() {
    return components;
  }

  /**
   * @param components The components
   */
  @JsonProperty("components")
  public void setComponents(List<Component> components) {
    this.components = components;
  }

  public Issues withComponents(List<Component> components) {
    this.components = components;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
