package elasticsearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.stereotype.Repository;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Repository
public class ScrollRepository extends SimpleElasticsearchRepository<Book> {

    private final Map<String, Comparator<Book>> comparators;

    @Autowired
    private final ElasticsearchTemplate elasticsearchTemplate;

    public ScrollRepository(
        final ElasticsearchTemplate elasticsearchTemplate
    ) {
        super(elasticsearchTemplate);
        this.comparators = new ConcurrentHashMap<>();
        comparators.put("price", comparing(Book::getPrice));
        comparators.put("title", comparing(Book::getTitle));
        comparators.put("author", comparing(Book::getAuthor));
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Page<Book> getBooks() {
        final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.rangeQuery("price").gte(2000));
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(queryBuilder)
//            .withIndices("bookdata")
//            .withTypes("books")
            .withPageable(PageRequest.of(200, 10, Sort.Direction.ASC, "price"))

            .build();

        if (searchQuery.getPageable().getOffset() + searchQuery.getPageable().getPageSize() > 10000){
            return scrollBooks(searchQuery);
        } else {
            return elasticsearchTemplate.queryForPage(searchQuery, Book.class);
        }
    }

    private Page<Book> scrollBooks(final NativeSearchQuery searchQuery) {
        Page<Book> scroll = elasticsearchTemplate.startScroll(1000, searchQuery, Book.class);

        String scrollId = ((ScrolledPage) scroll).getScrollId();
        List<Book> books = new ArrayList<>(((int) scroll.getTotalElements()));
        while (scroll.hasContent()) {
            final List<Book> content = scroll.getContent();
            books.addAll(content);
            scrollId = ((ScrolledPage) scroll).getScrollId();
            scroll = elasticsearchTemplate.continueScroll(scrollId, 1000, Book.class);
            System.out.println(scroll.getNumber() + " " + books.size() + " " + content
                .stream()
                .map(Book::getTitle)
                .collect(joining(" ")));
        }
        elasticsearchTemplate.clearScroll(scrollId);
        final Sort sort = searchQuery.getSort();
        final Comparator<Book> comparator = comparatorFromSort(sort);
        final Pageable pageable = searchQuery.getPageable();
        final List<Book> page = books
            .stream()
            .sorted(comparator)
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .collect(toList());
        return new AggregatedPageImpl<>(page, pageable, scroll.getTotalElements());
    }

    private Comparator<Book> comparatorFromSort(final Sort sort) {
        Comparator<Book> comparator = (o1, o2) -> 0;
        final Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext()){
            comparator = comparator.thenComparing(comparatorFromOrder(iterator.next()));
        }
        return comparator;
    }

    private Comparator<Book> comparatorFromOrder(final Sort.Order order) {
        return order.isDescending() ? comparators.get(order.getProperty()).reversed()
            : comparators.get(order.getProperty());
    }
}
