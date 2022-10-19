package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.util.FindFiles;
import sample.util.MyFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

public class PrikaziRezultatController {

    @FXML
    private TableView<MyFile> tabela;
    @FXML
    private TableColumn<String, String> imeFajla;

    private final String folderName = "rezultati";

    @FXML
    public void initialize() {

        String pattern = "*.txt";
        String path = System.getProperty("user.dir") + File.separator + folderName;

        File f = new File(path);
        if (!f.exists()) {
            return;
        }

        Path startingDir = Paths.get(path);
        FindFiles findFiles = new FindFiles(pattern);
        try {
            Files.walkFileTree(startingDir, findFiles);
        } catch (IOException e) {
            Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }

        imeFajla.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        List<MyFile> listaFajlova = findFiles.getListaFajlova();

        final ObservableList<MyFile> filesModels = FXCollections.observableList(listaFajlova);

        tabela.setItems(filesModels);

        tabela.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (tabela.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                String fileName = tabela.getSelectionModel().getSelectedItem().getFileName();
                String filePath = tabela.getSelectionModel().getSelectedItem().getFilePath();
                MyFile myFile = new MyFile(fileName, filePath);

                Stage newStage = new Stage();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sadrzajRezultata.fxml"));
                    SadrzajRezultataController.setMyfile(myFile);
                    Parent root = loader.load();
                    newStage.setTitle("Rezultati");
                    newStage.setScene(new Scene(root));
                    newStage.setResizable(false);
                    newStage.show();
                } catch (Exception e) {
                    Simulacija.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
                }
            }
        });

    }
}
