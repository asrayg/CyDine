package coms309;

public class Movies {
    private String year;

    private String firstName;

    private String lastName;

    private String movie;

    private String rating;

    public Movies(){

    }

    public Movies(String firstName, String lastName, String movie, String rating, String year){
        this.firstName = firstName;
        this.lastName = lastName;
        this.movie = movie;
        this.rating = rating;
        this.year = year;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return this.year;
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
                + year + " "
                + rating;
    }
}
