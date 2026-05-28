package ra.demo.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookResporitory bookResporitory;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_returnList() {
        Book book1 = Book.builder().id(1L).title("Book 1").build();
        Book book2 = Book.builder().id(2L).title("Book 2").build();
        when(bookResporitory.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getBooks();

        assertEquals(2, books.size());
        verify(bookResporitory, times(1)).findAll();
    }

    @Test
    void getBookById_found() {
        Book book = Book.builder().id(1L).title("Book 1").build();
        when(bookResporitory.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Book 1", result.getTitle());
        verify(bookResporitory, times(1)).findById(1L);
    }

    @Test
    void getBookById_notFound() {
        when(bookResporitory.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, () -> bookService.getBookById(1L));
        verify(bookResporitory, times(1)).findById(1L);
    }
}