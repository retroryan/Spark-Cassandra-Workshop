package simpleSpark;


import java.io.Serializable;

public class BibleVerse implements Serializable {

    private String book;
    private Integer chapter;
    private Integer verse;
    private String body;

    public BibleVerse(String book, Integer chapter, Integer verse, String body) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.body = body;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getVerse() {
        return verse;
    }

    public void setVerse(Integer verse) {
        this.verse = verse;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "book='" + book + '\'' +
                ", chapter=" + chapter +
                ", verse=" + verse +
                ", body='" + body + '\'' +
                '}';
    }
}
