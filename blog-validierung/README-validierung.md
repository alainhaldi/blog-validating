# Start Project in Dev-Mode

- Open the Terminal
- Make sure you are in the folder **blog-validierung** and run:

  ```
  mvn quarkus:dev
  ```

# Create Container-Image

- Open the Terminal
- Make sure you are in the folder **blog-validierung**
- Before building the container image run:

  ```
  ./mvnw package
  ```

- Then, build the image with:

  ```
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/blog-validierung  .
  ```

# Create Container

    ```
    docker run --name blog-validierung --network blog-nw -e KAFKA_BOOTSTRAP_SERVERS=redpanda-2:9092 -d quarkus/blog-validierung:latest
    ```
