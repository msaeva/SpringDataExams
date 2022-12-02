package softuni.exam.instagraphlite.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String path;

    @Column(nullable = false)
    @Min(500)
    @Max(60000)
    private Double size;

    @OneToMany(targetEntity = User.class, mappedBy = "profilePicture")
    private Set<User> users;
}
