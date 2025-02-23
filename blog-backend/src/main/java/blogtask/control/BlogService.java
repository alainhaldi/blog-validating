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
	BlogRepository blogRepository;

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

}
