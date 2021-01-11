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
import io.tpd.quboo.sonarplugin.util.QubooCache;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.platform.Server;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static io.tpd.quboo.sonarplugin.settings.QubooProperties.DEFAULT_ACCESS_KEY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Sends stats to the Quboo server after an analysis
 */
public class QubooConnector implements PostProjectAnalysisTask {

  private final Server server;
  private final Logger log = Loggers.get(getClass());
  private final ObjectMapper mapper;
  private OkHttpClient http;
  private List<String> selectedProjects;
  private List<String> rejectedProjects;

  public QubooConnector(final Server server) {
    this.http = new HttpClients().getQubooTrustedOkHttpClient();
    this.server = server;
    this.mapper = new ObjectMapper();
  }

  void setHttp(final OkHttpClient http) {
    this.http = http;
  }

  public String getDescription() {
    return "Send statistics to Quboo after an analysis";
  }

  @Override
  public void finished(ProjectAnalysis analysis) {
    final String qubooKey = analysis.getScannerContext().getProperties().get(QubooProperties.ACCESS_KEY);
    final String qubooSecret = analysis.getScannerContext().getProperties().get(QubooProperties.SECRET_KEY);
    final String token = analysis.getScannerContext().getProperties().get(QubooProperties.TOKEN_KEY);
    this.selectedProjects = extractProjectList(analysis.getScannerContext().getProperties().get(QubooProperties.SELECTED_PROJECTS_KEY));
    this.rejectedProjects = extractProjectList(analysis.getScannerContext().getProperties().get(QubooProperties.REJECTED_PROJECTS_KEY));
    if (!isEmpty(token)) {
      log.info("A token will be used to connect to SonarQube server");
    }
    if (!isEmpty(qubooKey) && !DEFAULT_ACCESS_KEY.equals(qubooKey)) {
      log.info("Connecting to Quboo with quboo key: " + qubooKey);
      try {
        final UsersWrapper allUsers = getUsers(token);
        sendUsersToQuboo(allUsers, qubooKey, qubooSecret);
        final IssuesWrapper allIssues = getIssues(token);
        sendIssuesToQuboo(allIssues, qubooKey, qubooSecret);
      } catch (final Exception e) {
        log.error("Error while trying to connect to Quboo", e);
      }
    } else {
      log.info("Quboo plugin is disabled. Enter your access and secret keys to enable it.");
    }
  }

  private List<String> extractProjectList(final String commaSeparatedProjects) {
    if (StringUtils.isBlank(commaSeparatedProjects)) {
      return new ArrayList<>();
    } else {
      List<String> projects = new ArrayList<>();
      String[] projectArray = commaSeparatedProjects.trim().split(",");
      for (String p : projectArray) {
        projects.add(p.trim()); // in case of spaces between commas
      }
      return projects;
    }
  }

  private void sendIssuesToQuboo(final IssuesWrapper allIssues, final String qubooKey, final String qubooSecret) throws Exception {
    if (!allIssues.getIssues().isEmpty()) {
      final Request request = new Request.Builder()
        .url(QubooPlugin.QUBOO_SERVER + "/updater/issues")
        .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
        .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
        .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allIssues)))
        .build();
      log.info("Sending {} issues to Quboo, url: {}", allIssues.getIssues().size(), request.url().toString());
      final Response response = http.newCall(request).execute();
      final String body = response.body().string();
      log.info("Response " + response.code() + " | " + body);
      if (response.isSuccessful()) {
        QubooCache.INSTANCE.toCache(allIssues);
      }
    } else {
      log.info("No new issues to send to Quboo. Skipping...");
    }
  }

  private IssuesWrapper getIssues(final String token) {
    int pageNumber = 1;
    boolean moreData = true;
    IssuesWrapper wrapper = new IssuesWrapper();
    while (moreData) {
      try {
        final Request.Builder request = new Request.Builder()
          .url(server.getPublicRootUrl() + "/api/issues/search?assigned=true&ps=200&p=" + pageNumber).get();
        addAuthorizationIfNeeded(request, token);
        final Request r = request.build();
        log.info("Quboo plugin getting issues from {}", r.url().toString());
        final Response response = http.newCall(r).execute();
        if (response.isSuccessful()) {
          final String body = response.body().string();
          final Issues issues = mapper.readValue(body, Issues.class);
          log.info("Quboo plugin got {} issues", issues.getIssues().size());
          wrapper.filterAndAddIssues(issues, selectedProjects, rejectedProjects, server.getVersion());
          moreData = moreData(issues.getPaging(), issues.getIssues().size());
          pageNumber++;
          if (pageNumber > 50) { // there is a hard limit in Sonar API that doesn't allow querying more than 10K results
            log.info("Reached max number of issues that can be fetched. Skipping remaining ones...");
            break;
          }
        } else {
          log.error("Aborting issues fetch. Got an error from server: {}. Request: {}", response.message(), r.url().toString());
          break;
        }
      } catch (final Exception e) {
        log.error("Quboo could not fetch issues from the server: " + e.getMessage());
        break;
      }
    }
    return wrapper;
  }

  private void sendUsersToQuboo(final UsersWrapper allUsers, final String qubooKey, final String qubooSecret) throws Exception {
    if (!allUsers.getUsers().isEmpty()) {
      final Request request = new Request.Builder()
        .url(QubooPlugin.QUBOO_SERVER + "/updater/users")
        .header(QubooPlugin.QUBOO_HEADER_ACCESS_KEY, qubooKey)
        .header(QubooPlugin.QUBOO_HEADER_SECRET_KEY, qubooSecret)
        .post(RequestBody.create(MediaType.get("application/json"), mapper.writeValueAsString(allUsers)))
        .build();
      log.info("Sending {} users to Quboo, url: {}", allUsers.getUsers().size(), request.url().toString());
      final Response response = http.newCall(request).execute();
      final String body = response.body().string();
      log.info("Response " + response.code() + " | " + body);
      if (response.isSuccessful()) {
        QubooCache.INSTANCE.toCache(allUsers);
      }
    } else {
      log.info("No new users to send to Quboo. Skipping...");
    }
  }

  private UsersWrapper getUsers(final String token) {
    int pageNumber = 1;
    boolean moreData = true;
    UsersWrapper wrapper = new UsersWrapper();
    while (moreData) {
      try {
        final Request.Builder request = new Request.Builder()
          .url(server.getPublicRootUrl() + "/api/users/search?ps=200&p=" + pageNumber)
          .get();
        addAuthorizationIfNeeded(request, token);
        final Request r = request.build();
        log.info("Quboo plugin getting users from {}", r.url().toString());
        final Response response = http.newCall(r).execute();
        if (response.isSuccessful()) {
          final String body = response.body().string();
          final Users users = mapper.readValue(body, Users.class);
          log.info("Quboo plugin got {} users", users.getUsers().size());
          wrapper.filterAndAddUsers(users, server.getVersion());
          moreData = moreData(users.getPaging(), users.getUsers().size());
          pageNumber++;
          if (pageNumber > 50) { // there is a hard limit in Sonar API that doesn't allow querying more than 10K results
            log.info("Reached max number of users that can be fetched. Skipping remaining ones...");
            break;
          }
        } else {
          log.error("Aborting users fetch. Got an error from server: {}. Request: {}", response.message(), r.url().toString());
          break;
        }
      } catch (final Exception e) {
        log.error("Quboo could not fetch users from the server: " + e.getMessage());
        break;
      }
    }
    return wrapper;
  }

  private boolean moreData(final Paging paging, final int elementsInPage) {
    return elementsInPage == paging.getPageSize() && paging.getTotal() > paging.getPageSize() * paging.getPageIndex();
  }

  private Request.Builder addAuthorizationIfNeeded(final Request.Builder requestBuilder, final String token) {
    if (!isEmpty(token)) {
      final String headerValue = "Basic " + Base64.getEncoder().encodeToString((token + ":").getBytes());
      requestBuilder.header("Authorization", headerValue);
      log.info("Adding Authorization header to request with token ****{}", token.substring(3 * token.length() / 4));
    }
    return requestBuilder;
  }

}
