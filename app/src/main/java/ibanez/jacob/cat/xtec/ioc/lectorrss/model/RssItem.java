package ibanez.jacob.cat.xtec.ioc.lectorrss.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * This class represents a single entry from an RSS xml
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class RssItem implements Serializable {

    private static final long serialVersionUID = 5416260651526471310L;

    private String title;
    private String link;
    private String author;
    private String description;
    private Date pubDate;
    private String categories;
    private String thumbnail;

    public RssItem(String title, String link, String author, String description,
                   Date pubDate, String category, String thumbnail) {
        this.title = title;
        this.link = link;
        this.author = author;
        this.description = description;
        this.pubDate = pubDate;
        this.categories = category;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RssItem rssItem = (RssItem) o;
        return Objects.equals(title, rssItem.title) &&
                Objects.equals(link, rssItem.link) &&
                Objects.equals(author, rssItem.author) &&
                Objects.equals(description, rssItem.description) &&
                Objects.equals(pubDate, rssItem.pubDate) &&
                Objects.equals(categories, rssItem.categories) &&
                Objects.equals(thumbnail, rssItem.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, author, description, pubDate, categories, thumbnail);
    }

    @Override
    public String toString() {
        return "RssItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                ", categories='" + categories + '\'' +
                ", thumbnail=" + thumbnail +
                '}';
    }
}
