package elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private ScrollRepository bookDao;

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id){
        return bookDao.findById(id).orElse(null);
    }

    @GetMapping
    public Page<Book> getAllBooks(){

        return bookDao.getBooks();
    }

    @PostMapping
    public Book insertBook(@RequestBody Book book) throws Exception {
        return bookDao.save(book);
    }

    @PutMapping("/{id}")
    public Book updateBookById(@RequestBody Book book, @PathVariable String id) {
        return bookDao.save(book.toBuilder().id(id).build());
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable String id) {
        bookDao.deleteById(id);
    }

}
