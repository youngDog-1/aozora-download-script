package controller;

import modules.BookData;
import modules.BookInfo;
import modules.Epub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {
    private Path saveDir;
    private final BookData data;
    private Set<BookInfo> books;

    public Controller() {
        this.data = new BookData();
        this.books = new HashSet<>();
    }

    private Map<String, List<BookInfo>> findKeys(Map<String, List<BookInfo>> map, String input) {
        return map.entrySet()
                .stream()
                .filter(e -> e.getKey().contains(input))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    public Map<String, List<BookInfo>> findAuthors(String input) {
        return findKeys(this.data.getAuthors(), input);
    }

    public Map<String, List<BookInfo>> findBooks(String input) {
        return findKeys(this.data.getBooks(), input);
    }

    public void addBooks(List<BookInfo> books) {
        this.books.addAll(books);
    }

    public int getNumberOfBooks() {
        return this.books.size();
    }

    public boolean setSaveDir(String dir) {
        Path saveDir = Paths.get(dir);
        try {
            if (!Files.exists(saveDir)) return false;
        } catch(InvalidPathException e) {
            return false;
        }
            this.saveDir = saveDir;
        return true;
    }

    public int writeEpubs() {
        int i = 0;
        for(BookInfo bookInfo : this.books) {
            Path dir = this.saveDir.resolve("AuzoraEpubs").resolve(bookInfo.getAuthorName());
            try {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    System.out.println("Failed to create directory " + dir);
                    throw e;
                }
                Path file = dir.resolve(bookInfo.getBookName() + ".epub");
                if(!Files.exists(file)) {
                    Epub epub = new Epub(bookInfo);
                    epub.createEpub(file);
                    i++;
                }

            } catch (IOException ignored) {
            }
        }
        return i;
    }

}
