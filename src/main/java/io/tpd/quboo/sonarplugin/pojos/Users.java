package io.tpd.quboo.sonarplugin.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

  @JsonProperty("paging")
  private Paging paging;
  @JsonProperty("users")
  private List<User> users = null;

  @JsonProperty("paging")
  public Paging getPaging() {
    return paging;
  }

  @JsonProperty("paging")
  public void setPaging(Paging paging) {
    this.paging = paging;
  }

  @JsonProperty("users")
  public List<User> getUsers() {
    return users;
  }

  @JsonProperty("users")
  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("paging", paging).append("users", users).toString();
  }

}
