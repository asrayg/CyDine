package CyDine.MealPlanChat;

import java.util.Date;
import java.util.List;

import CyDine.MealPlans.MealPlans;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "MPmessages")
@Data
public class MealPlanMessage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer userId;

    @Column
    private Integer mealplanId;

    @Lob
    private String content;

    private boolean reported;

    @ManyToOne
    private MealPlans mealPlans;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();
	
	
	public MealPlanMessage() {
        reported = false;
    };
	
	public MealPlanMessage(Integer userId, String content, Integer mealplanId) {
		this.userId = userId;
		this.content = content;
        this.mealplanId = mealplanId;
        reported = false;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserName(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String toJson(){
        String json = "{" +
                                    "\"userId\": \"" + userId + "\"," +
                                    " \"message\": " + content + "," +
                                    " \"mealplanId\": " + mealplanId + ","  +
                                    " \"id\": " + id +
                                    "}";
        return json;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMealplanId() {
        return mealplanId;
    }

    public void setMealplanId(Integer mealplanId) {
        this.mealplanId = mealplanId;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }


    public MealPlans getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(MealPlans mealPlans) {
        this.mealPlans = mealPlans;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported() {
        reported = !reported;
    }
}



