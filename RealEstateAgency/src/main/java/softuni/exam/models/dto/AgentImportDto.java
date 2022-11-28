package softuni.exam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class AgentImportDto {
    @Size(min = 2)
    private String firstName;
    @Size(min = 2)
    private String lastName;
    private String town;
    private String email;
}
