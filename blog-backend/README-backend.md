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

# Testing

## BlogServiceTest

- In order to test the Service reliable, I used mockito. Mockito creates dummy version of the repository and makes therefore sure we can test with a isolated repository.
- You can start the tests with

```
mvn clean test
```

## BlogRessourceTest

## Test Coverage

- In order to know the test coverage, I made use of JaCoCo. After running the test, JaCoCo creates a report where it displays the coverages.
- The generated Report is located in:

```
target/jacoco-report/index.html
```
