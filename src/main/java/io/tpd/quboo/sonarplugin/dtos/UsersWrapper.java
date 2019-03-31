package io.tpd.quboo.sonarplugin.dtos;

import io.tpd.quboo.sonarplugin.pojos.User;
import io.tpd.quboo.sonarplugin.pojos.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersWrapper {

  private List<User> users;

  public UsersWrapper() {
    this.users = new ArrayList<>();
  }

  public void filterAndAddUsers(final Users users) {
    this.users.addAll(
      users.getUsers()
    );
  }

  public List<User> getUsers() {
    return users;
  }
}
