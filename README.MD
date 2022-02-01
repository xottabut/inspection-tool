This project is a demo project created for presenting Java agents API. Three modules are present:

1. application - the main application we want to inspect.
2. inspection-agent - the agent that we can "connect" to the java process.
3. inspection-application - the application that is able to connect to already running java process and attach the agent
   to it.

NOTE: JAVA_HOME variable is used to find the tools.jar in the locally installed JDK. This should be specified in your
environment or corresponding pom.xml file to be modified.
