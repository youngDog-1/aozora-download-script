package modules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class BookData {
    private Map<String, List<BookInfo>> authors;
    private Map<String, List<BookInfo>> books;
    private static final String DATA_CSV_FILE = "src/main/resources/list_person_all_extended_utf8.csv";

    public BookData() {
        this.authors = new HashMap<String, List<BookInfo>>();
        this.books = new HashMap<String, List<BookInfo>>();
        this.init();
    }

    public void init() {
        try (Stream<String> lines = Files.lines(Paths.get(DATA_CSV_FILE))) {
//          col[1]=作品名, col[15]=姓, col[16]=名, col[length-5]=XHTML/HTMLファイルURL
            lines.map(line -> line.replace("\"", "").split(","))
                    .forEach(fields -> {
                BookInfo bookInfo = new BookInfo(fields[1], fields[16], fields[15], fields[fields.length - 5]);
                this.addMap(this.authors, fields[15].concat(fields[16]), bookInfo);
                this.addMap(this.books, fields[1], bookInfo);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read csv data file");
            System.exit(0);
        }
    }

    private void addMap(Map<String, List<BookInfo>> map, String name, BookInfo bookInfo) {
        if (!map.containsKey(name)) {
            map.put(name, new ArrayList<>());
        }
        map.get(name).add(bookInfo);
    }

    public Map<String, List<BookInfo>> getAuthors() {
        return this.authors;
    }

    public Map<String, List<BookInfo>> getBooks() {
        return this.books;
    }

    public List<BookInfo> getBooksByAuthor(String author) {
        return this.authors.get(author);
    }

    public List<BookInfo> getBookByTitle(String title) {
        return this.books.get(title);
    }


}
