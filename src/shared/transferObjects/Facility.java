package shared.transferObjects;

import java.io.Serializable;

public class Facility implements Serializable {
    private String title;
    private String description;

    public Facility(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Facility [title=" + title + ", description=" + description + "]";
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
