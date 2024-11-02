package CyDine.DiningHall;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "messages")

@Data
public class DiningHallFoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    public DiningHallFoodItem() {}

    public DiningHallFoodItem(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public boolean canDelete(String username) {
        return this.userName.equals(username);
    }
}
