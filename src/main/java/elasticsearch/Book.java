package elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.elasticsearch.annotations.Document;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder(toBuilder = true)
@Document(indexName = "bookdata", type = "books", shards = 1, replicas = 0, refreshInterval = "-1")
@Value
@JsonDeserialize(builder = Book.BookBuilder.class)
public class Book {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("price")
    private float price;

    //standard setters and getters

    /**
     * Requires for Jackson builder mapping with lombok.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static final class BookBuilder {
    }
}
