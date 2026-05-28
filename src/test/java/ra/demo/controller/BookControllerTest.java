package ra.demo.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void getBooks_ReturnsList() throws Exception {
        Book book1 = Book.builder().id(1L).title("Book 1").author("Author 1").build();
        Book book2 = Book.builder().id(2L).title("Book 2").author("Author 2").build();

        when(bookService.getBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Book 1"))
                .andExpect(jsonPath("$.data[1].title").value("Book 2"));
    }

    @Test
    void getBookById_found() throws Exception {
        Book book = Book.builder().id(1L).title("Book 1").author("Author 1").build();

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Book 1"));
    }

    @Test
    void getBookById_notFound() throws Exception {
        when(bookService.getBookById(1L)).thenThrow(new BookNotFound("Không tồn tại sách có mã 1"));

        mockMvc.perform(get("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}