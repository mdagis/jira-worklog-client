package com.mdagis.jira.worklog.client;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkLogClient {

    private final String username;
    private final String password;
    private final String jiraUrl;

    public WorkLogClient(String username, String password, String jiraUrl) {
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
    }

    public List<WorkLogItem> fetchWorkLogs(String issueKey) {

        DocumentContext jsonContext = JsonPath.parse(fetchJSONWorkLogs(issueKey));
        int worklogListSize = jsonContext.read("$['worklogs'].length()");

        List<WorkLogItem> returnedWorklogList = new ArrayList<>();
        for (int i = 0; i < worklogListSize; i++) {
            long timeSpentSeconds = jsonContext.read("$['worklogs'][" + i + "]['timeSpentSeconds']", Long.class);

            String stringStartedDate = jsonContext.read("$['worklogs'][" + i + "]['started']"); 
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            LocalDateTime startedDate = formatter.parseLocalDateTime(stringStartedDate);

            String author = jsonContext.read("$['worklogs'][" + i + "]['author']['name']");
            
            WorkLogItem wli = new WorkLogItem(author, timeSpentSeconds / 60, startedDate);
            returnedWorklogList.add(wli);
        }

        return returnedWorklogList;
    }

    private String fetchJSONWorkLogs(String issueKey) {
        
        Client client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        WebTarget webTarget = client
                .target(jiraUrl)
                .path("rest").path("api").path("2").path("issue").path(issueKey).path("worklog");

        return webTarget.request(MediaType.APPLICATION_JSON).get(String.class);
    }

}
