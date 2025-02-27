# Start Project

## Docker

- Make sure Docker Dekstop is running

## Containers

### Redpanda (Kafka-Broker)

#### Create (first time only)

```
docker run -d --name=redpanda-2 -p 9092:9092 --network blog-nw docker.redpanda.com/redpandadata/redpanda:v23.3.5 redpanda start --advertise-kafka-addr redpanda-2:9092
```

#### Start

```
docker start redpanda-2
```

### MySQL

#### Create (first time only)

```
docker run --name blog-mysql -p 3306:3306 --network blog-nw -e MYSQL_ROOT_PASSWORD=root -e MYSQL_USER=dbuser -e MYSQL_PASSWORD=dbuser -e MYSQL_DATABASE=blogdb -d mysql:8.0
```

#### Start

```
docker start blog-mysql
```

### Blog-Validierung

#### Create (first time only)

- Check out the readme inside the blog-validierung project

#### Start

```
docker start blog-validierung
```

### Blog-Backend

#### Create (first time only)

- Check out the readme inside the blog-backend project

#### Start

```
docker start blog-backend
```

# Start Project in Dev-Mode without Container

## Quarkus

- Open the project in VS Code
- open 2 terminals
- start both projects:

  ```
    mvn -f blog-backend quarkus:dev
    mvn -f blog-validierung quarkus:dev
  ```

# Interact with Containers

## Add Blogs via httpie

- **Note:** Blogs can't have the same ID twice!
- Open a new Terminal

### Add Blog that passes Validation

```
  http http://localhost:8080/blog/addBlog id=1 title='Mein neuer Blog' content='Content meines neuen Bloges'
```

### Add Blog that dosen't passes Validation

```
  http http://localhost:8080/blog/addBlog id=2 title='Mein neuer Blog' content='hftm sucks'
```

## List all Blogs

- **Note:** Every Blog that passes the validation, will have to boolean value of _true_

```
  http GET http://localhost:8080/blog/listAll
```

## See Topic in GUI (Works only when started in Dev-Mode)

http://localhost:8080/q/dev-ui/io.quarkus.quarkus-kafka-client/topics
