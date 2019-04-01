package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @JsonProperty("login")
  private String login;
  @JsonProperty("name")
  private String name;
  @JsonProperty("active")
  private boolean active;

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

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("login", login).append("name", name)
      .append("active", active).toString();
  }

}
