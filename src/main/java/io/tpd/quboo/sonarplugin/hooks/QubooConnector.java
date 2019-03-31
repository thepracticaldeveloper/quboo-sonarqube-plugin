package io.tpd.quboo.sonarplugin.hooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tpd.quboo.sonarplugin.QubooPlugin;
import io.tpd.quboo.sonarplugin.dtos.IssuesWrapper;
import io.tpd.quboo.sonarplugin.dtos.UsersWrapper;
import io.tpd.quboo.sonarplugin.http.HttpClients;
import io.tpd.quboo.sonarplugin.pojos.Issues;
import io.tpd.quboo.sonarplugin.pojos.Paging;
import io.tpd.quboo.sonarplugin.pojos.Users;
import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import okhttp3.*;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.platform.Server;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Sends stats to the Quboo server after an analysis meeting these conditions:
 * 1. On a 'master' branch
 * 2. No more than 5 times a day
 * 3. Issues created before today
 */
public class QubooConnector implements PostProjectAnalysisTask {

  private final Server server;
  private final Logger log = Loggers.get(getClass());
  private final ObjectMapper mapper;
  private final OkHttpClient http;

  public QubooConnector(final Server server) {
    this.http = HttpClients.getUnsafeOkHttpClient();
    this.server = server;
    this.mapper = new ObjectMapper();
  }

  @Override
  public void finished(ProjectAnalysis analysis) {
    final String qubooKey = analysis.getScannerContext().getProperties().get(QubooProperties.ACCESS_KEY);
    final String qubooSecret = analysis.getScannerContext().getProperties().get(QubooProperties.SECRET_KEY);
    log.info("Connecting to Quboo with quboo key: " + qubooKey);
    try {
      final UsersWrapper allUsers = getUsers();
      sendUsersToQuboo(allUsers, qubooKey, qubooSecret);
      final IssuesWrapper allIssues = getIssues();
      sendIssuesToQuboo(allIssues, qubooKey, qubooSecret);
    } catch (final Exception e) {
      log.error("Error while trying to connect to Quboo", e);
    }
  }

  private void sendIssuesToQuboo(final IssuesWrapper allIssues, final String qubooKey, final String qubooSecret) throws Exception {
    final Request request = new Request.Builder()
      .url(QubooPlugin.QUBOO_SERVER + "/updater/issues")
      .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
      .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
      .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allIssues)))
      .build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    log.info("Response " + response.code() + " | " + body);
  }

  private IssuesWrapper getIssues() throws Exception {
    int pageNumber = 1;
    boolean moreData = true;
    IssuesWrapper wrapper = new IssuesWrapper();
    while (moreData) {
      final Request request = new Request.Builder().url(server.getPublicRootUrl() + "/api/issues/search?assigned=true&ps=200&p=" + pageNumber).get().build();
      final Response response = http.newCall(request).execute();
      final String body = response.body().string();
      final Issues issues = mapper.readValue(body, Issues.class);
      wrapper.filterAndAddIssues(issues);
      moreData = moreData(issues.getPaging(), issues.getIssues().size());
      pageNumber++;
    }
    log.info("There are " + wrapper.getIssues().size() + " issues");
    return wrapper;
  }

  private void sendUsersToQuboo(final UsersWrapper allUsers, final String qubooKey, final String qubooSecret) throws Exception {
    final Request request = new Request.Builder()
      .url(QubooPlugin.QUBOO_SERVER + "/updater/users")
      .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
      .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
      .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allUsers)))
      .build();
    final Response response = http.newCall(request).execute();
    final String body = response.body().string();
    log.info("Response " + response.code() + " | " + body);
  }

  private UsersWrapper getUsers() {
    int pageNumber = 1;
    boolean moreData = true;
    UsersWrapper wrapper = new UsersWrapper();
    while (moreData) {
      try {
        final Request request = new Request.Builder().url(server.getPublicRootUrl() + "/api/users/search?ps=200&p=" + pageNumber).get().build();
        final Response response = http.newCall(request).execute();
        final String body = response.body().string();
        final Users users = mapper.readValue(body, Users.class);
        wrapper.filterAndAddUsers(users);
        moreData = moreData(users.getPaging(), users.getUsers().size());
        pageNumber++;
      } catch (final Exception e) {
        log.error("Quboo could not fetch data from the server: " + e.getMessage());
        break;
      }
    }
    log.info("There are " + wrapper.getUsers().size() + " users");
    return wrapper;
  }

  private boolean moreData(final Paging paging, final int elementsInPage) {
    return elementsInPage == paging.getPageSize() && paging.getTotal() > paging.getPageSize() * paging.getPageIndex();
  }

}
