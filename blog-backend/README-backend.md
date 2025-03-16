# Start Project in Dev-Mode

- Open the Terminal
- Make sure you are in the folder **blog-backend** and run:

  ```
  mvn quarkus:dev
  ```

# Create Container-Image

- Open the Terminal
- Make sure you are in the folder **blog-backend**
- Before building the container image run:

  ```
  ./mvnw package
  ```

- Then, build the image with:

  ```
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/blog-backend  .
  ```

# Create Container

    ```
    docker run --name blog-backend -p 8080:8080 --network blog-nw -e KAFKA_BOOTSTRAP_SERVERS=redpanda-2:9092 -e QUARKUS_DATASOURCE_USERNAME=dbuser -e QUARKUS_DATASOURCE_PASSWORD=dbuser -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://blog-mysql:3306/blogdb -d quarkus/blog-backend:latest
    ```

# Testing-Workshop

## BlogServiceTest

- In order to test the Service reliable, I used mockito. Mockito creates dummy version of the repository and makes therefore sure we can test with a isolated repository.
- You can start the tests with:

```
mvn clean test
```

## BlogRessourceTest

- In order to Test the Ressources, its necessarily to start first the project in the dev-mode
- Once started, the tests can me run via the _run button_ in the File or with opening a new terminal and running also:

```
mvn clean test
```

## Test Coverage

- In order to know the test coverage, I made use of JaCoCo. After running the test, JaCoCo creates a report where it displays the coverages.
- The generated Report is located in:

```
target/jacoco-report/index.html
```

## GitHub Actions

- To automate the testing process, I implemented 2 GitHub Actions.

  - **Lint Code Base** -> Check if the Syntax is correct
  - **Quarkus Dev Mode Tests** -> Starts the project in dev mode and runs all tests (BlogRessources & BlogService)

- The GitHub Actions are located here:

```
.github/workflows
```

- Or on GitHub under

```
alainhaldi/blog-validating/Actions
```

## SonarQube

[![Quality Gate Status](http://localhost:9000/api/project_badges/measure?project=ch.hftm%3Areactive-test&metric=alert_status&token=sqb_8554ac4380d71077b5687ce6d2296ad2057fe245)](http://localhost:9000/dashboard?id=ch.hftm%3Areactive-test)
[![Coverage](http://localhost:9000/api/project_badges/measure?project=ch.hftm%3Areactive-test&metric=coverage&token=sqb_8554ac4380d71077b5687ce6d2296ad2057fe245)](http://localhost:9000/dashboard?id=ch.hftm%3Areactive-test)

- Start the Docker-Container **sonarqube**
- Open a Terminal and navigate to

```
cd blog-backend
```

- Start the Quarkus Project in Dev-Mode
- Run the sonarqube analysis with:

```
mvn clean verify sonar:sonar -Dsonar.login=squ_7c9b40f401f7384ba8f7e1ff2a567266b6cc3c45
```

- Login to the sonarqube Webinterface here **http://localhost:9000/projects**
- Credentials: _admin/Testing-Workshop25_

# Goals

- Use a Test DB with Test Data instead of prod DB, but how?
  - Already tried to config a new DB in **src/test/ressources/application.properties** but no success, its keep using the prod-DB instead of Test DB
- Show the Test Coverage for the HTTP Test also in Jacoco
  - Current Problem is that these tests arent mockito test?
