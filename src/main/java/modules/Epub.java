package modules;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

public class Epub {
    private BookInfo bookInfo;
    private Book book;

    public Epub(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.book = new Book();
    }

    public InputStream getBookHtmlFromUrl() throws IOException{
        try {
            return URI.create(this.bookInfo.getBookLink()).toURL().openStream();
        } catch(IOException e) {
            System.out.println("Failed to fetch HTML file of book: " + this.bookInfo);
        }
        return null;
    }

    public void createEpub(Path file) throws IOException{
        try {
            Metadata metadata = this.book.getMetadata();
            metadata.addTitle(this.bookInfo.getBookName());
            metadata.addAuthor(new Author(this.bookInfo.getAuthorFirstName(),this.bookInfo.getAuthorLastName()));

            this.book.addSection("Content",
                    new Resource(getBookHtmlFromUrl(), "content.html"));
            EpubWriter epubWriter = new EpubWriter();
            epubWriter.write(this.book, new FileOutputStream(file.toString()));
        } catch(IOException e) {
            System.out.println("Failed to write epub of book: " + this.bookInfo);
        }
    }
}
