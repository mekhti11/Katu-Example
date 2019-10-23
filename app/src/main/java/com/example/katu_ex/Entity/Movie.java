package com.example.katu_ex.Entity;


public class Movie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Actors;
    private String Plot;
    private String imdbRating;
    private String Rated;

    public Movie() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "Title='" + Title + '\'' +
                ", Year='" + Year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", Type='" + Type + '\'' +
                ", Poster='" + Poster + '\'' +
                ", Runtime='" + Runtime + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Director='" + Director + '\'' +
                ", Actors='" + Actors + '\'' +
                ", Plot='" + Plot + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", Rated='" + Rated + '\'' +
                '}';
    }
}
