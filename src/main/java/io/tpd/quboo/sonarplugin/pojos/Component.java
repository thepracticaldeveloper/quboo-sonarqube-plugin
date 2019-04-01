package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Component {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("key")
  private String key;
  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("enabled")
  private Boolean enabled;
  @JsonProperty("projectId")
  private Integer projectId;
  @JsonProperty("subProjectId")
  private Integer subProjectId;

  /**
   * @return The id
   */
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  /**
   * @param id The id
   */
  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  public Component withId(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * @return The key
   */
  @JsonProperty("key")
  public String getKey() {
    return key;
  }

  /**
   * @param key The key
   */
  @JsonProperty("key")
  public void setKey(String key) {
    this.key = key;
  }

  public Component withKey(String key) {
    this.key = key;
    return this;
  }

  /**
   * @return The uuid
   */
  @JsonProperty("uuid")
  public String getUuid() {
    return uuid;
  }

  /**
   * @param uuid The uuid
   */
  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Component withUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  /**
   * @return The enabled
   */
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  /**
   * @param enabled The enabled
   */
  @JsonProperty("enabled")
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Component withEnabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * @return The projectId
   */
  @JsonProperty("projectId")
  public Integer getProjectId() {
    return projectId;
  }

  /**
   * @param projectId The projectId
   */
  @JsonProperty("projectId")
  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public Component withProjectId(Integer projectId) {
    this.projectId = projectId;
    return this;
  }

  /**
   * @return The subProjectId
   */
  @JsonProperty("subProjectId")
  public Integer getSubProjectId() {
    return subProjectId;
  }

  /**
   * @param subProjectId The subProjectId
   */
  @JsonProperty("subProjectId")
  public void setSubProjectId(Integer subProjectId) {
    this.subProjectId = subProjectId;
  }

  public Component withSubProjectId(Integer subProjectId) {
    this.subProjectId = subProjectId;
    return this;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
