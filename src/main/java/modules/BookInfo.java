package modules;

import java.util.Objects;

public class BookInfo {
    private final String bookName;
    private final String authorFirstName;
    private final String authorLastName;
    private final String bookLink;

    public BookInfo(String bookName, String authorFirstName, String authorLastName, String bookLink) {
        this.bookName = bookName;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
        this.bookLink = bookLink;
    }

    public String getBookName() {
        return this.bookName;
    }

    public String getAuthorName() {
        return this.authorFirstName + this.authorLastName;
    }

    public String getAuthorFirstName() {
        return this.authorFirstName;
    }

    public String getAuthorLastName() {
        return this.authorLastName;
    }

    public String getBookLink() {
        return this.bookLink;
    }

    @Override
    public String toString() {
        return "Book name: " + this.bookName + ", Author name: " + this.getAuthorName();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || this.getClass() != o.getClass()) {
            return false;
        }

        BookInfo book = (BookInfo) o;
        return Objects.equals(this.bookName,book.bookName)
                && Objects.equals(this.authorFirstName,book.authorFirstName)
                && Objects.equals(this.authorLastName,book.authorLastName);
    }
}
