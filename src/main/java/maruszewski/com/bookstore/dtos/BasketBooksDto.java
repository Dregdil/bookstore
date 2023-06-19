package maruszewski.com.bookstore.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BasketBooksDto {
    private Long basketId;
    private Long bookId;
}
