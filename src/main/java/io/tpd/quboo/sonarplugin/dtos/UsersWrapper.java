package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.User;
import io.tpd.quboo.sonarplugin.pojos.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersWrapper {

  private List<User> users;

  public UsersWrapper() {
    this.users = new ArrayList<>();
  }

  public void filterAndAddUsers(final Users users) {
    this.users.addAll(
      users.getUsers().stream()
        .filter(User::isActive)
        .collect(Collectors.toList())
    );
  }

  public List<User> getUsers() {
    return users;
  }
}
