package elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
    public Iterable<Book> getAllBooks(){
        final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.rangeQuery("price").gte(2000));
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(queryBuilder)
//            .withIndices("bookdata")
//            .withTypes("books")
            .withPageable(PageRequest.of(1200, 10, Sort.Direction.DESC, "price"))

            .build();
        return bookDao.scroll(searchQuery);
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
