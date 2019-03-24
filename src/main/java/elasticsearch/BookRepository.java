package elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

//        List<Book> findByNameAndPrice(String name, Integer price);
//
//        List<Book> findByNameOrPrice(String name, Integer price);
//
//        Page<Book> findByName(String name, Pageable page);
//
//        Page<Book> findByNameNot(String name,Pageable page);
//
//        Page<Book> findByPriceBetween(int price,Pageable page);
//
//        Page<Book> findByNameLike(String name,Pageable page);
//
//        @Query("{\"bool\" : {\"must\" : {\"term\" : {\"message\" : \"?0\"}}}}")
//        Page<Book> findByMessage(String message, Pageable pageable);
    }