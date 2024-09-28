package kolya.study.bookservice.entity;

public enum Genre {
    FANTASY("Fantasy"),
    SCIENCE_FICTION("Science Fiction"),
    DYSTOPIAN("Dystopian"),
    ADVENTURE("Adventure"),
    MYSTERY("Mystery"),
    HORROR("Horror"),
    ROMANCE("Romance"),
    CHILDREN("Children");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}