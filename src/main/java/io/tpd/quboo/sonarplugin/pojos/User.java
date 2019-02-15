package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "login",
  "name",
  "active"
})
public class User {

  @JsonProperty("login")
  private String login;
  @JsonProperty("name")
  private String name;
  @JsonProperty("active")
  private boolean active;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("active")
  public boolean isActive() {
    return active;
  }

  @JsonProperty("active")
  public void setActive(final boolean active) {
    this.active = active;
  }

  @JsonProperty("login")
  public String getLogin() {
    return login;
  }

  @JsonProperty("login")
  public void setLogin(String login) {
    this.login = login;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("login", login).append("name", name)
      .append("active", active)
      .append("additionalProperties", additionalProperties).toString();
  }

}
