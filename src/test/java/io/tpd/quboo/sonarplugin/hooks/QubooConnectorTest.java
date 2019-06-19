package io.tpd.quboo.sonarplugin.hooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tpd.quboo.sonarplugin.pojos.*;
import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.platform.Server;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QubooConnectorTest {

  private static final String SERVER_URL = "http://server";
  private static final String SERVER_VERSION = "6.7";
  private static final String ACCESS_KEY_VALUE = "dummy-access-key";
  private static final String SECRET_KEY_VALUE = "dummy-secret-key";
  private static final String TOKEN_VALUE = "dummy-token";

  private QubooConnector qubooConnector;

  @Mock
  private OkHttpClient http;
  @Mock
  private Call call;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Server server;
  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private PostProjectAnalysisTask.ProjectAnalysis projectAnalysis;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Before
  public void setup() {
    qubooConnector = new QubooConnector(server);
    qubooConnector.setHttp(http);
    // common mock setup
    given(server.getPublicRootUrl()).willReturn(SERVER_URL);
    given(server.getVersion()).willReturn(SERVER_VERSION);
    given(http.newCall(any())).willReturn(call);
  }

  @Test
  public void testHappyFlow() throws IOException {
    // Given
    given(projectAnalysis.getScannerContext().getProperties()).willReturn(fullPropertiesMap());
    final Response okResponse1 = build200Response("OK", "text/plain");
    final Response okResponse2 = build200Response("OK", "text/plain");
    final Response usersResponse1 = build200Response(OBJECT_MAPPER.writeValueAsString(generateUsersPage1()), "application/json");
    final Response usersResponse2 = build200Response(OBJECT_MAPPER.writeValueAsString(generateUsersPage2()), "application/json");
    final Response issuesResponse1 = build200Response(OBJECT_MAPPER.writeValueAsString(generateIssuesPage1()), "application/json");
    final Response issuesResponse2 = build200Response(OBJECT_MAPPER.writeValueAsString(generateIssuesPage2()), "application/json");
    given(call.execute())
      .willReturn(usersResponse1) // get users from Sonar (page 1)
      .willReturn(usersResponse2) // get users from Sonar (page 2)
      .willReturn(okResponse1) // send users to Quboo
      .willReturn(issuesResponse1) // get issues from Sonar (page 1)
      .willReturn(issuesResponse2) // get issues from Sonar (page 2)
      .willReturn(okResponse2); // send issues to Quboo

    qubooConnector.finished(projectAnalysis);

    final ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
    verify(http, times(6)).newCall(requestArgumentCaptor.capture());
    verifyNoMoreInteractions(http);

    final List<Request> requests = requestArgumentCaptor.getAllValues();
    final Request getUsersPage1 = requests.get(0);
    final Request getUsersPage2 = requests.get(1);
    final Request sendUsers = requests.get(2);
    final Request getIssuesPage1 = requests.get(3);
    final Request getIssuesPage2 = requests.get(4);
    final Request sendIssues = requests.get(5);

    assertThat(getUsersPage1.headers().names()).containsOnly("Authorization");
    assertThat(getUsersPage1.url()).matches(url -> url.queryParameter("p").equals("1"));
    assertThat(getUsersPage2.url()).matches(url -> url.queryParameter("p").equals("2"));
    assertThat(sendUsers.headers().names()).containsExactlyInAnyOrder("x-quboo-access-key", "x-quboo-secret-key");
    assertThat(sendUsers.body().contentLength()).isNotZero();
    assertThat(getIssuesPage1.headers().names()).containsOnly("Authorization");
    assertThat(getIssuesPage1.url()).matches(url -> url.queryParameter("p").equals("1"));
    assertThat(getIssuesPage2.url()).matches(url -> url.queryParameter("p").equals("2"));
    assertThat(sendIssues.headers().names()).containsExactlyInAnyOrder("x-quboo-access-key", "x-quboo-secret-key");
    assertThat(sendIssues.body().contentLength()).isNotZero();
  }

  private Response build200Response(final String body, final String contentType) {
    final Response.Builder responseBuilder = new Response.Builder();
    return responseBuilder.body(ResponseBody.create(MediaType.get(contentType), body))
      .protocol(Protocol.HTTP_2).code(200).message("OK").request(dummyRequest()).build();
  }

  private Issues generateIssuesPage1() {
    final Issue issue1 = new Issue().withAssignee("player1").withResolution("open").withDebt("1h")
      .withRule("InsufficientCoverage").withKey("issue-1").withProject("project").withAuthor("author")
      .withSeverity("major").withStatus("open");
    final Issue issue2 = new Issue().withAssignee("player2").withResolution("closed").withDebt("1h")
      .withRule("InsufficientCoverage").withKey("issue-2").withProject("project").withAuthor("author")
      .withSeverity("major").withStatus("fixed");
    final Paging paging = new Paging().withPageIndex(1).withPageSize(2).withTotal(3);
    return new Issues().withIssues(Arrays.asList(issue1, issue2)).withPaging(paging);
  }

  private Issues generateIssuesPage2() {
    final Issue issue1 = new Issue().withAssignee("player1").withResolution("open").withDebt("1h")
      .withRule("InsufficientCoverage").withKey("issue-3").withProject("project").withAuthor("author")
      .withSeverity("major").withStatus("open");
    final Paging paging = new Paging().withPageIndex(2).withPageSize(2).withTotal(3);
    return new Issues().withIssues(Collections.singletonList(issue1)).withPaging(paging);
  }

  private Users generateUsersPage1() {
    final User user1 = user("login1", "Player 1", true);
    final User user2 = user("login2", "Player 2", true);
    final Users users = new Users();
    users.setUsers(Arrays.asList(user1, user2));
    final Paging paging = new Paging().withPageIndex(1).withPageSize(2).withTotal(3);
    users.setPaging(paging);
    return users;
  }

  private Users generateUsersPage2() {
    final User user3 = user("login3", "Player 3", false);
    final Users users = new Users();
    users.setUsers(Collections.singletonList(user3));
    final Paging paging = new Paging().withPageIndex(2).withPageSize(2).withTotal(3);
    users.setPaging(paging);
    return users;
  }

  private Map<String, String> fullPropertiesMap() {
    final Map<String, String> map = new HashMap<>();
    map.put(QubooProperties.ACCESS_KEY, ACCESS_KEY_VALUE);
    map.put(QubooProperties.SECRET_KEY, SECRET_KEY_VALUE);
    map.put(QubooProperties.TOKEN_KEY, TOKEN_VALUE);
    return map;
  }

  private Request dummyRequest() {
    final Request.Builder requestBuilder = new Request.Builder();
    requestBuilder.url("https://api.quboo.io/issues");
    return requestBuilder.build();
  }

  private User user(final String login, final String name, final boolean active) {
    final User user = new User();
    user.setLogin(login);
    user.setActive(active);
    user.setName(name);
    return user;
  }

}
