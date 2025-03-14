package blogtask.control;

import java.util.List;

import blogtask.entity.Blog;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
public class BlogService {

	@Inject
	public BlogRepository blogRepository;

	// Needs Constructor in order to Mock the Repository
	public BlogService(BlogRepository blogRepository) {
		this.blogRepository = blogRepository;
	}

	public List<Blog> getBlogs() {
		var blogs = blogRepository.listAll();
		Log.info("Returning " + blogs.size() + " blogs");
		return blogs;
	}

	@Transactional
	public void addBlog(Blog blog) {
		Log.info("Adding blog " + blog.getTitle());
		blogRepository.persist(blog);
	}

	@Transactional
	public void deleteBlog(Long id) {
		Log.info("Deleting blog with id " + id);
		blogRepository.deleteById(id);
	}

	public Blog findBlogById(Long id) {
		return blogRepository.findById(id);
	}

	@Transactional
	public void updateValidationStatus(Long id, Boolean valid) {

		Blog blog = findBlogById(id);

		if (blog != null) {
			blog.setIsValidated(valid);
		}
	}

	public boolean containsBlog(Long id) {
		Blog blog = findBlogById(id);
		return blog != null;
	}

	@Transactional
	public void updateEntireBlog(Long id, String newTitle, String newContent) {
		Blog blog = findBlogById(id);
		if (blog != null) {
			blog.setTitle(newTitle);
			blog.setContent(newContent);
		} else {
			Log.error("Blog with id " + id + " not found");
		}
	}

	@Transactional
	public void updateBlogTitle(Long id, String newTitle) {
		Blog blog = findBlogById(id);
		if (blog != null) {
			blog.setTitle(newTitle);
			Log.info("> Title von Blog " + id + " wurde mit ->" + newTitle + "<- aktualisiert.");
		} else {
			Log.error("Blog with id " + id + " not found");
		}
	}

	@Transactional
	public void updateBlogContent(Long id, String newContent) {
		Blog blog = blogRepository.findById(id);
		if (blog != null) {
			blog.setContent(newContent);
			Log.info("> Content von Blog " + id + " wurde mit ->" + newContent + "<- aktualisiert.");
		} else {
			Log.error("Blog with id " + id + " not found");
		}
	}

}
