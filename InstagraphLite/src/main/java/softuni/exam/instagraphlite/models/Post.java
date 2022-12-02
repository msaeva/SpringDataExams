package softuni.exam.instagraphlite.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Size(min = 21)
    @Column(nullable = false)
    private String caption;

    @ManyToOne(optional = false)
    private User user;


    @ManyToOne(optional = false)
    private Picture picture;

    @Override
    public String toString() {
        return "==Post Details:" + System.lineSeparator() +
                "----Caption: " + this.getCaption() + System.lineSeparator() +
                "----Picture Size: " + this.getPicture().getSize() + System.lineSeparator();
    }
}
