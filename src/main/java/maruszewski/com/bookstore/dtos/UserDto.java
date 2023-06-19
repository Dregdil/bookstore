package maruszewski.com.bookstore.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.InputType;


@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private String email;
    private String login;
    @InputType("password")
    private String password;
}
