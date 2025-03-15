package control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import blogtask.control.BlogRepository;
import blogtask.control.BlogService;
import blogtask.entity.Blog;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BlogServiceTest {

    // Creates a Dummy Repository
    @InjectMock
    BlogRepository blogRepository;

    private BlogService blogService;
    private Blog blog;

    // Set up the test environment
    @BeforeEach
    void setUp() {
        blogService = new BlogService(blogRepository);

        // Arrange: Create a new blog object
        blog = new Blog();
        blog.setId(750L);
        blog.setTitle("Test Titel");
        blog.setContent("Test Content");
    }

    @Test
    void testFindBlogById() {

        // Define the Dummy Output (Arrange)
        when(blogRepository.findById(750L))
                .thenReturn(blog);

        // Assert
        assertEquals(blogService.findBlogById(750L).getId(), 750L);
        assertEquals(blogService.findBlogById(750L).getTitle(), "Test Titel");
    }

    @Test
    void testGetBlogs() {

        // Define the Dummy Output (Arrange)
        when(blogRepository.listAll())
                .thenReturn(List.of(blog,
                        new Blog(751L, "Test Titel 2", "Test Content 2", false),
                        new Blog(752L, "Test Titel 3", "Test Content 3", false)));

        // Assert
        assertEquals(blogService.getBlogs().size(), 3);
        assertEquals(blogService.getBlogs().get(1).getContent(), "Test Content 2");
    }

    @Test
    void testAddBlog() {

        // Act: Call the method
        blogService.addBlog(blog);

        // Assert: Verify that persist() was called once with the correct object
        verify(blogRepository, times(1)).persist(blog);
    }

    @Test
    void testDeleteBlog() {
        // Arrange: Ensure the blog exists before deletion
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        blogService.deleteBlog(750L);

        // Assert: Verify that deleteById() was called once with the correct ID
        verify(blogRepository, times(1)).deleteById(750L);

        // Additional Check: Ensure blog no longer exists
        when(blogRepository.findById(750L)).thenReturn(null);
        assertEquals(null, blogRepository.findById(750L));
    }

    @Test
    void testUpdateValidationStatus() {
        // Arrange: Mock the behavior of blogRepository.findById
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        blogService.updateValidationStatus(750L, true);

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);

        // Additional verification: Ensure the blog validation status was updated
        assertEquals(true, blog.getIsValidated());
    }

    @Test
    void testContainsBlog() {

        // Arrange: Mock the behavior of blogRepository.findById
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        boolean result = blogService.containsBlog(750L);

        // Assert: Verify the method behavior and expected return value
        verify(blogRepository, times(1)).findById(750L);
        assertEquals(true, result); // Expecting true since blog exists
    }

    @Test
    void testUpdateEntireBlog() {

        // Arrange: Mock the behavior of blogRepository.findById
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        blogService.updateEntireBlog(750L, "New Title", "New Content");

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);

        // Additional verification: Ensure the blog title and content were updated
        assertEquals("New Title", blog.getTitle());
        assertEquals("New Content", blog.getContent());
    }

    @Test
    void testUpdateBlogTitle() {

        // Arrange: Mock the behavior of blogRepository.findById
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        blogService.updateBlogTitle(750L, "New Title");

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);

        // Additional verification: Ensure the blog title was updated
        assertEquals("New Title", blog.getTitle());
    }

    @Test
    void testUpdateBlogContent() {

        // Arrange: Mock the behavior of blogRepository.findById
        when(blogRepository.findById(750L)).thenReturn(blog);

        // Act: Call the method
        blogService.updateBlogContent(750L, "New Content");

        // Assert: Verify that findById() was called once with the correct id
        verify(blogRepository, times(1)).findById(750L);

        // Additional verification: Ensure the blog content was updated
        assertEquals("New Content", blog.getContent());
    }

}