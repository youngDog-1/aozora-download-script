package modules;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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

    public boolean createEpub(Path file) throws IOException{
        try {
            Metadata metadata = this.book.getMetadata();
            metadata.addTitle(this.bookInfo.getBookName());
            metadata.addAuthor(new Author(this.bookInfo.getAuthorFirstName(),this.bookInfo.getAuthorLastName()));

//            Convert Source HTML encoding from SHIFT-JIS to UTF-8
            InputStream stream = getBookHtmlFromUrl();
            String str = new String(stream.readAllBytes(), "SHIFT-JIS");
            String html = new String(str.getBytes(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html,"");
            doc.select("meta[content*=\"text/html\"]").attr("content", "text/html;charset=UTF-8");

//            Replace css file of html
            String cssPath = "src/main/resources/stylesheet.css";
            File css = new File(cssPath);
            doc.select("link[type=\"text/css\"]").attr("href", "stylesheet.css");

            InputStream bookHtml = new ByteArrayInputStream(doc.outerHtml().getBytes());
            this.book.addSection("Content",
                    new Resource(bookHtml, "content.html"));
            this.book.getResources().add(new Resource(new FileInputStream(css),"stylesheet.css"));
            EpubWriter epubWriter = new EpubWriter();
            epubWriter.write(this.book, new FileOutputStream(file.toString()));
            return true;
        } catch(IOException e) {
            System.out.println("Failed to write epub of book: " + this.bookInfo);
            e.printStackTrace();
            return false;
        }
    }
}
