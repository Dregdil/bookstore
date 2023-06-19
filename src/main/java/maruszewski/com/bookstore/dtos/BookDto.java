package maruszewski.com.bookstore.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookDto {
    private String title;
    private String author;
    private Double price;
}
