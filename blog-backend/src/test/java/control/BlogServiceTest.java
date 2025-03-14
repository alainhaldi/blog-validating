package control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import blogtask.control.BlogRepository;
import blogtask.control.BlogService;
import blogtask.entity.Blog;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class BlogServiceTest {

    // Creates a Dummy Repository
    @InjectMock
    BlogRepository blogRepository;

    private BlogService blogService;

    @BeforeEach
    void setUp() {
        blogService = new BlogService(blogRepository);
    }

    @Test
    void testFindBlogById() {

        // Define the Dummy Output (Arrange)
        when(blogRepository.findById(750L))
                .thenReturn(new Blog(750L, "Test Titel", "Test Content", false));

        // Assert
        assertEquals(blogService.findBlogById(750L).getId(), 750L);
        assertEquals(blogService.findBlogById(750L).getTitle(), "Test Titel");
    }

    @Test
    void testGetBlogs() {

        // Define the Dummy Output (Arrange)
        when(blogRepository.listAll())
                .thenReturn(List.of(new Blog(750L, "Test Titel", "Test Content", false),
                        new Blog(751L, "Test Titel 2", "Test Content 2", false),
                        new Blog(752L, "Test Titel 3", "Test Content 3", false)));

        // Assert
        assertEquals(blogService.getBlogs().size(), 3);
        assertEquals(blogService.getBlogs().get(1).getContent(), "Test Content 2");
    }

    @Test
    void testAddBlog() {

        // Arrange: Create a new blog object
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("This is a test blog");

        // Act: Call the method
        blogService.addBlog(blog);

        // Assert: Verify that persist() was called once with the correct object
        verify(blogRepository, times(1)).persist(blog);
    }

    @Test
    void testDeleteBlog() {

        // Arrange: Create a new blog object
        Blog blog = new Blog();
        blog.setId(750L);
        blog.setTitle("Test Blog");
        blog.setContent("This is a test blog");

        // Act: Call the method
        blogService.deleteBlog(750L);

        // Assert: Verify that deleteById() was called once with the correct id
        verify(blogRepository, times(1)).deleteById(750L);
    }

    @Test
    void testUpdateValidationStatus() {

        // Arrange: Create a new blog object
        Blog blog = new Blog();
        blog.setId(750L);
        blog.setTitle("Test Blog");
        blog.setContent("This is a test blog");

        // Act: Call the method
        blogService.updateValidationStatus(750L, true);

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);
    }

    @Test
    void testContainsBlog() {

        // Arrange: Create a new blog object
        Blog blog = new Blog();
        blog.setId(750L);
        blog.setTitle("Test Blog");
        blog.setContent("This is a test blog");

        // Act: Call the method
        blogService.containsBlog(750L);

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);
    }

    // @Test
    // void containsBlogTest() {
    // // Arrange
    // Blog blog = new Blog(904, "Testing Blog", "This is my testing blog", false);
    // boolean containsBlog;

    // // Act
    // blogService.addBlog(blog);
    // containsBlog = blogService.containsBlog(blog.getId());

    // // Assert
    // assertEquals(true, containsBlog);
    // }

    // @Test
    // void updateEntireBlogTest() {
    // // Arrange
    // Blog blog = new Blog(905, "Testing Blog", "This is my testing blog", false);
    // String newTitle = "New Title";
    // String newContent = "New Content";

    // // Act
    // blogService.addBlog(blog);
    // blogService.updateEntireBlog(blog.getId(), newTitle, newContent);
    // Blog updatedBlog = blogService.findBlogById(blog.getId());

    // // Assert
    // assertEquals(newTitle, updatedBlog.getTitle());
    // assertEquals(newContent, updatedBlog.getContent());
    // }

    // @Test
    // void updateBlogTitleTest() {
    // // Arrange
    // Blog blog = new Blog(906, "Testing Blog", "This is my testing blog", false);
    // String newTitle = "New Title";

    // // Act
    // blogService.addBlog(blog);
    // blogService.updateBlogTitle(blog.getId(), newTitle);
    // Blog updatedBlog = blogService.findBlogById(blog.getId());

    // // Assert
    // assertEquals(newTitle, updatedBlog.getTitle());
    // }

    // @Test
    // void updateBlogContentTest() {
    // // Arrange
    // Blog blog = new Blog(907, "Testing Blog", "This is my testing blog", false);
    // String newContent = "New Content";

    // // Act
    // blogService.addBlog(blog);
    // blogService.updateBlogContent(blog.getId(), newContent);
    // Blog updatedBlog = blogService.findBlogById(blog.getId());

    // // Assert
    // assertEquals(newContent, updatedBlog.getContent());
    // }
}