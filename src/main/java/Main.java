import modules.BookData;
import ui.UI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Step 1: Read csv file to string, convert to 2 maps. Key: author, Value: array of works and Key: work, Value: link
//        If user input match 100% retrieve link, otherwise find all
//        Step 2: Send http request fetch xhtml file from link
//        Step 3: Convert to epub
//        Step 4: Save files
        UI ui = new UI();
        ui.start();
    }
}