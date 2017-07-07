package css.csslibrary.model;

import java.util.Date;

/**
 * Created by yoohan95 on 2017-07-02.
 */

public class Book {
    private int id;
    private String type;
    private String title;
    private String author;
    private int amount;

    public Book(int id, String type, String title, String author, int amount) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.amount = amount;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

