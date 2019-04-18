package elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for {@link BookController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Elastic6Application.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookRepository bookRepository;

    @Before
    public void setUp(){
        bookRepository.deleteAll();
    }

    @org.junit.Test
    public void getBookById() throws Exception {
        final Book book = Book.builder().id("id").price(3000).author("author").build();
        bookRepository.save(book);
        mvc.perform(get("/books/id"))
            .andDo(print())
            .andExpect(status().isOk())
        .andExpect(content().json(new ObjectMapper().writeValueAsString(book)));
    }

    @org.junit.Test
    public void getAllBooks() throws Exception {
    }

    @org.junit.Test
    public void insertBook() {
    }

    @org.junit.Test
    public void updateBookById() {
    }

    @org.junit.Test
    public void deleteBookById() {
    }
}
