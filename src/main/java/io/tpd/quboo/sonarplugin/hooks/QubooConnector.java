package io.tpd.quboo.sonarplugin.hooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tpd.quboo.sonarplugin.QubooPlugin;
import io.tpd.quboo.sonarplugin.pojos.Issues;
import io.tpd.quboo.sonarplugin.pojos.Users;
import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import okhttp3.*;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.platform.Server;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Sends stats to the Quboo server
 */
public class QubooConnector implements PostProjectAnalysisTask {

  private final Server server;
  private final Logger log = Loggers.get(getClass());
  private final ObjectMapper mapper;
  private final OkHttpClient http;

  public QubooConnector(final Server server) {
    this.http = new OkHttpClient();
    this.server = server;
    this.mapper = new ObjectMapper();
  }

  @Override
  public void finished(ProjectAnalysis analysis) {
    final String qubooKey = analysis.getScannerContext().getProperties().get(QubooProperties.ACCESS_KEY);
    final String qubooSecret = analysis.getScannerContext().getProperties().get(QubooProperties.SECRET_KEY);
    log.info("Connecting to Quboo with quboo key: " + qubooKey);
    try {
      final Users allUsers = getUsers();
      sendUsersToQuboo(allUsers, qubooKey, qubooSecret);
      final Issues allIssues = getIssues();
      sendIssuesToQuboo(allIssues, qubooKey, qubooSecret);
    } catch (final Exception e) {
      log.error("Error while trying to connect to Quboo", e);
    }
  }

  private void sendIssuesToQuboo(final Issues allIssues, final String qubooKey, final String qubooSecret) throws Exception {
    final Request request = new Request.Builder()
      .url(QubooPlugin.QUBOO_SERVER + "/updater/issues")
      .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
      .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
      .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allIssues)))
      .build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    log.info("Response " + body);
  }

  private Issues getIssues() throws Exception {
    final Request request = new Request.Builder().url(server.getPublicRootUrl() + "/api/issues/search").get().build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    final Issues issues = mapper.readValue(body, Issues.class);
    log.info("There are " + issues.getIssues().size() + " issues");
    return issues;
  }

  private void sendUsersToQuboo(final Users allUsers, final String qubooKey, final String qubooSecret) throws Exception {
    final Request request = new Request.Builder()
      .url(QubooPlugin.QUBOO_SERVER + "/updater/users")
      .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
      .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
      .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allUsers)))
      .build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    log.info("Response " + body);
  }

  private Users getUsers() throws Exception {
    final Request request = new Request.Builder().url(server.getPublicRootUrl() + "/api/users/search").get().build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    final Users users = mapper.readValue(body, Users.class);
    log.info("There are " + users.getUsers().size() + " users");
    return users;
  }

}
