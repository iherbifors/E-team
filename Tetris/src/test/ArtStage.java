package test;

public class ArtStage {
    private String theme;
    private String title;
    private String artist;
    private String description;
    private String source;
    private String imagePath;
    private int targetScore;

    public ArtStage(
            String theme,
            String title,
            String artist,
            String description,
            String source,
            String imagePath,
            int targetScore
    ) {
        this.theme = theme;
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.source = source;
        this.imagePath = imagePath;
        this.targetScore = targetScore;
    }

    public String getTheme() {
        return theme;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getTargetScore() {
        return targetScore;
    }

    @Override
    public String toString() {
        return "[" + theme + "] " + title + " - 목표 " + targetScore + "점";
    }
}
