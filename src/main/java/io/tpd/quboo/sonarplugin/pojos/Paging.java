package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paging {

  @JsonProperty("pageIndex")
  private Integer pageIndex;
  @JsonProperty("pageSize")
  private Integer pageSize;
  @JsonProperty("total")
  private Integer total;

  /**
   * @return The pageIndex
   */
  @JsonProperty("pageIndex")
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * @param pageIndex The pageIndex
   */
  @JsonProperty("pageIndex")
  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  public Paging withPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
    return this;
  }

  /**
   * @return The pageSize
   */
  @JsonProperty("pageSize")
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * @param pageSize The pageSize
   */
  @JsonProperty("pageSize")
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Paging withPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

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

  public Paging withTotal(Integer total) {
    this.total = total;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
