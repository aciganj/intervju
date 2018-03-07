# intervju

Build the project with: 

`mvn install dockerfile:build`

Run the container with:

`docker run -p 8080:8080 aciganj/intervju`

Application has GUI on localhost:8080. 
Refresh button retrieves (this action doesn't make twitter api calls) all users from the database which are following the predefined user(BEST_Zagreb).

You can submit another handle to monitor.

Use localhost:8080/h2 to access and add users to database. The application only contains three users on startup.
