package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.util.MyFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;

public class SadrzajRezultataController {

    @FXML
    private Label imeFajla;
    @FXML
    private TextArea fileContent;

    public static MyFile myfile;
    public static void setMyfile(MyFile myfile) {
        SadrzajRezultataController.myfile = myfile;
    }

    @FXML
    public void initialize() {
        imeFajla.setText(myfile.getFileName());
        StringBuilder data = new StringBuilder();
        try {
            File myObj = new File(myfile.getFilePath());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
                data.append("\n");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }

        fileContent.setText(data.toString());

    }

}
