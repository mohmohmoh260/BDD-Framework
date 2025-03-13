PRE-REQUISITE:
1) Install and set these into environment variable (Java 8, JAVA_HOME)(Android SDK, ANDROID_HOME)(Maven, MAVEN_HOME)

Run the project buy running the Run.bat and use below command line:
notes: replace XXX with the tag to run


mvn clean test -D"cucumber.filter.tags=@XXX"

mvn clean test -D"cucumber.filter.tags=@XXX or @XXX"


Jenkins setup
1. Download Jenkins .war file and paste it into the project directory
2. Open Run.bat use this command to run the Jenkins server: java -jar jenkins.war
3. Browse http://localhost:8080
4. username "admin"
5. password in folder C:\Users\<user>\.jenkins\secrets\initialAdminPassword
6. set JENKINS_HOME in variable environment

additional steps for first time setup (continue after step 2 above)
3. Copy the password (keys) from cmd
4. Browse http://localhost:8080 and enter the password key
5. Select the mandatory/default plugins