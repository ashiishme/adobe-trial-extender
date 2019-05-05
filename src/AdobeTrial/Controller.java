package AdobeTrial;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Controller {

    @FXML
    private BorderPane trailExt;

    @FXML
    private ComboBox<String> productList;

    @FXML
    private TextField pathField;

    @FXML
    private TextFlow consoleOutput;

    @FXML
    private ScrollPane scrollpane;

    private static Path path;

    private static String sPath;

    private static String selectedValue;

    private static final String COMMON_PATH = "/AMT/application.xml";

    private static final String PHOTOSHOP_2018 = "Adobe Photoshop CC 2018";

    private static final String IN_DESIGN_2018 = "Adobe InDesign CC 2018";

    private static final String LIGHTROOM_2018 = "Adobe Lightroom CC";

    private static final String PREMIERE_PRO_2018 = "Adobe Premiere Pro CC 2018";

    private static final String AFTER_EFFECTS_2018 = "Adobe After Effects 2018";

    private static final String DREAMWEAVER_2018 = "Adobe Dreamweaver CC 2018";

    private static final String ILLUSTRATOR_2018 = "Adobe Illustrator CC 2018";


    @FXML
    public void selectFile() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDir = directoryChooser.showDialog(trailExt.getScene().getWindow());

        if(selectedDir != null) {

            path = Paths.get(selectedDir.getAbsolutePath());

            pathField.setText(String.valueOf(path));


        } else {

            Text dirError =
                    new Text(" No directory selected. \n Please select Adobe product directory. \n");

            dirError.setStyle("-fx-fill: #cc0000;");

            consoleOutput.getChildren().add(dirError);

        }

    }

    @FXML
    public void patchAdobe() {

        scrollpane.vvalueProperty().bind(consoleOutput.heightProperty());

        if(path != null && selectedValue != null) {

            switch (selectedValue) {

                case PHOTOSHOP_2018:
                    sPath = path + COMMON_PATH;
                    break;

                case IN_DESIGN_2018:
                    sPath = path + COMMON_PATH;
                    break;

                case LIGHTROOM_2018:
                    sPath = path + COMMON_PATH;
                    break;

                case PREMIERE_PRO_2018:
                    sPath = path + COMMON_PATH;
                    break;

                case AFTER_EFFECTS_2018:
                    sPath = path + "/Support Files/AMT/application.xml";
                    break;

                case DREAMWEAVER_2018:
                    sPath = path + "/amt/application.xml";
                    break;

                case ILLUSTRATOR_2018:
                    sPath = path + "/Support Files/Contents/Windows/AMT/application.xml";
                    break;

                    default:
                        sPath = "/error";
                        break;

            }

            if(Files.exists(Paths.get(sPath))) {

                Text pathFound = new Text(" Folder found! \n");

                pathFound.setStyle("-fx-fill: #79b47c;");

                consoleOutput.getChildren().add(pathFound);

                File file = new File(sPath);

                xmlDocParser(file);

            } else {

                Text invalidPath =
                        new Text(" Invalid path. \n Please select Adobe product path. \n ---- \n Patch Failed!! \n");

                invalidPath.setStyle("-fx-fill: #cc0000;");

                consoleOutput.getChildren().add(invalidPath);
            }

        } else {

            Text selectedError = new Text(" Path or product must be selected. \n  ---- \n Patch Failed!! \n" );

            selectedError.setStyle("-fx-fill: #cc0000;");

            consoleOutput.getChildren().add(selectedError);

        }


    }

    @FXML
    private void xmlDocParser(File file) {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);

            NodeList nodeList = document.getElementsByTagName("Data");

            if(nodeList != null && nodeList.getLength() > 0) {

                for(int i = 0; i < nodeList.getLength(); i++) {

                    Element trail = (Element) nodeList.item(i);

                    if(trail.hasAttribute("key") && trail.getAttribute("key").equals("TrialSerialNumber")) {

                        String getKey = trail.getTextContent();

                        trail.setTextContent(randomKeyGenerator(getKey));

                        Text processing = new Text(" Processing.. \n");

                        processing.setStyle("-fx-fill: #79b47c;");

                        consoleOutput.getChildren().add(processing);

                        updateAdobeFile(file, document);

                    }

                }

            }


        } catch (ParserConfigurationException | SAXException | IOException e) {

            e.printStackTrace();

        }

    }


    private String randomKeyGenerator(String str) {

        Random randomDigit = new Random();

        StringBuilder generatedDigits = new StringBuilder();

        String randomStr = str.substring(0,21);

        for(int i = 0; i < 3; i++) {

            generatedDigits.append(randomDigit.nextInt(10));

        }

        return randomStr + generatedDigits;

    }

    @FXML
    private void updateAdobeFile(File file, Document document) {

        File output = new File(String.valueOf(file));

        Source source = new DOMSource(document);

        Result result = new StreamResult(output);

        try {

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.transform(source, result);

            Text success = new Text();

            success.setStyle("-fx-fill: #79b47c;");

            success.setText(" Making some changes.. \n Task completed successfully!! \n ---- \n " +
                    " Trial Period Has Been Renewed!! \n");

            Thread t = new Thread(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> consoleOutput.getChildren().add(success));
            });

            t.setDaemon(true);
            t.start();



        } catch (TransformerException e) {

            e.printStackTrace();

        }

    }

    @FXML
    public void selectedProduct() {

        selectedValue = productList.getValue();

    }

    @FXML
    public void openSite() {
        try {
            Desktop.getDesktop().browse(new URL("http://www.ashiish.me/projects/adobe-trial-extender").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeAdobeTrial(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }


}
