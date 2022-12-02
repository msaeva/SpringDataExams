package softuni.exam.instagraphlite.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserImportDto {
    @Size(min = 2, max = 18)
    @NotNull
    private String username;

    @Size(min = 4)
    @NotNull
    private String password;

    private String profilePicture;
}
