package coms309;

public class Movies {
    private String firstName;

    private String lastName;

    private String movie;

    private String rating;

    public Movies(){

    }

    public Movies(String firstName, String lastName, String movie, String rating){
        this.firstName = firstName;
        this.lastName = lastName;
        this.movie = movie;
        this.rating = rating;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMovie() {
        return this.movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return firstName + " "
                + lastName + " "
                + movie + " "
                + rating;
    }
}
