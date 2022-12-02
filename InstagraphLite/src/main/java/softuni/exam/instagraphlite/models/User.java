package softuni.exam.instagraphlite.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Size(min = 2, max = 18)
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 4)
    @Column(nullable = false)
    private String password;

    @ManyToOne(optional = false)
    private Picture profilePicture;

    @OneToMany(targetEntity = Post.class, mappedBy = "user")
    private Set<Post> posts;

    @Override
    public String toString() {
        return "User: " + this.getUsername() + System.lineSeparator() +
                "Post count: " + this.getPosts().size() + System.lineSeparator();
    }
}
