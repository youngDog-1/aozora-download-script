package ui;

import controller.Controller;
import modules.BookInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UI {
    private Scanner sc;
    private Controller controller;

    public UI() {
        this.sc = new Scanner(System.in);
        this.controller = new Controller();
    }

    public void start() {
        this.promptDir();
        this.promptSearchInput();
        this.printWriteResult();
    }

    private void promptDir() {
        controller.setSaveDir("C:\\Users\\Admin\\Downloads\\Manga");
//        do {
//            System.out.println("Enter directory for saving epubs: ");
//        } while (!controller.setSaveDir(sc.nextLine()));
    }

    private void promptSearchInput() {
        while(true) {
            System.out.println("Choose download options: ");
            System.out.println("1. Download books by author name");
            System.out.println("2. Download book by book name");
            System.out.println("x. Terminate program and download chosen books");

            String option = sc.nextLine();
            Map<String, List<BookInfo>> map = new HashMap<>();

            if(option.equals("x")) break;

            switch(option) {
                case "1":
                    System.out.println("Enter author name: ");
                    map = controller.findAuthors(sc.nextLine());
                    break;
                case "2":
                    System.out.println("Enter book name: ");
                    map = controller.findBooks(sc.nextLine());
                    break;
                default:
                    continue;
            }

            System.out.println("Found " + map.size() + " results: ");

            AtomicInteger index = new AtomicInteger(0);
            List<List<BookInfo>> booksWithIndex = new ArrayList<>(map.size());
            map.forEach((key, books) -> {
                System.out.println(index.get() + 1 + ". Name: " + key + ". Number of books: " + books.size());
                booksWithIndex.add(books);
                index.getAndIncrement();
            });

            if(map.size() == 0) continue;

            if(map.size() == 1) {
                controller.addBooks(booksWithIndex.getFirst());
            }
            int entry=1;
            if (map.size() > 1) {
                while (true) {
                    System.out.println("Choose applicable entry: ");
                    if (sc.hasNextInt()) {
                        entry = sc.nextInt();
                        if (entry > 0 && entry <= map.size()) {
                            controller.addBooks(booksWithIndex.get(entry-1));
                            sc.nextLine();
                            break;
                        }
                    }
                }
            }
            System.out.println("Added " + booksWithIndex.get(entry - 1).size() + " books to download list");
            System.out.println("Current number of books to download: " + controller.getNumberOfBooks());
        }
    }

    private void printWriteResult() {
        System.out.println("Creating epub from " + controller.getNumberOfBooks() + " books...");
        System.out.println("Successfully wrote " + controller.writeEpubs() + " files to directory");
    }

}
