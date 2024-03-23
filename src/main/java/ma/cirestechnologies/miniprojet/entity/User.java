package ma.cirestechnologies.miniprojet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username", "email"})
        }
)
public record User(@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
                   String firstName,
                   String lastName,
                   java.util.Date birthDate,
                   String city,
                   String country,
                   String avatar,
                   String company,
                   String jobPosition,
                   String mobile,
                   String username,
                   String email,
                   String password,
                   String role) {
}