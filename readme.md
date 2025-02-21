## Start Project

- open 2 terminals
- start both projects:

  ```
    mvn -f blog-backend quarkus:dev
    mvn -f blog-validierung quarkus:dev
  ```

## Add Blogs via httpie

- Open a new Terminal

### Blog that passes Validation

```
http http://localhost:8080/blog/addBlog id=1 title='Mein neuer Blog' content='Content meines neuen Bloges'
```

### Blog that dosen't passes Validation

```
http http://localhost:8080/blog/addBlog id=1 title='Mein neuer Blog' content='hftm sucks'
```
