package elasticsearch;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link BookRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Elastic6Application.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = Elastic6Application.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void setUp(){
        bookRepository.deleteAll();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        bookRepository.save(Book.builder().id("id").author("author").build());

        assertThat(bookRepository.count(), equalTo(1L));



//        final EmbeddedElastic embeddedElastic = EmbeddedElastic.builder()
//            .withElasticVersion("6.6.0")
//            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9350)
//            .withSetting(PopularProperties.CLUSTER_NAME, "my_cluster")
//            .withIndex("books")
////            .withIndex("books", IndexSettings.builder()
////                .withType("paper_book", getSystemResourceAsStream("paper-book-mapping.json"))
////                .withType("audio_book", getSystemResourceAsStream("audio-book-mapping.json"))
////                .withSettings(getSystemResourceAsStream("elastic-settings.json"))
////                .build())
//            .build()
//            .start();
//        embeddedElastic.

    }

}
