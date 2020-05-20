package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.User;
import io.tpd.quboo.sonarplugin.pojos.Users;
import io.tpd.quboo.sonarplugin.util.QubooCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tpd.quboo.sonarplugin.QubooPlugin.QUBOO_API_VERSION;

public class UsersWrapper {

  private List<User> users;
  private String version = QUBOO_API_VERSION;
  private String sonarVersion;

  public UsersWrapper() {
    this.users = new ArrayList<>();
  }

  public void filterAndAddUsers(final Users users, final String sonarVersion) {
    this.users.addAll(
      users.getUsers().stream().filter(user -> !QubooCache.INSTANCE.inCache(user))
        .collect(Collectors.toList())
    );
    this.sonarVersion = sonarVersion;
  }

  public List<User> getUsers() {
    return users;
  }

  public String getVersion() {
    return version;
  }

  public String getSonarVersion() {
    return sonarVersion;
  }
}
