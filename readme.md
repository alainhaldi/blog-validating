## Start Project

- open 2 terminals
- start both projects:

  ```
    mvn -f blog-backend quarkus:dev
    mvn -f blog-validierung quarkus:dev
  ```

## Add Blogs via httpie

- Open a new Terminal
- **The Blogs cant have the same ID**

### Blog that passes Validation

```
  http http://localhost:8080/blog/addBlog id=1 title='Mein neuer Blog' content='Content meines neuen Bloges'
```

### Blog that dosen't passes Validation

```
  http http://localhost:8080/blog/addBlog id=2 title='Mein neuer Blog' content='hftm sucks'
```

## List all Blogs

- Every Blog that passes the validation, will have to boolean value of _true_

```
  http GET http://localhost:8080/blog/listAll
```

## See Topic in GUI

http://localhost:8080/q/dev-ui/io.quarkus.quarkus-kafka-client/topics
