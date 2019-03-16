# jira-worklog-client

A simple java jira client to retrive issue worklogs

Sample usage:
~~~~
WorkLogClient client = new WorkLogClient(username, password, jiraUrl);
List<WorkLogItem> worklogs = client.fetchWorkLogs(issueKey);
~~~~
