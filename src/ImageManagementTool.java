
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.imageio.ImageIO;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageManagementTool extends Application {

    /*
        * imgViewPic is for showing selected image in gui
        * selected image is reference of selected image
        * originalFile is path of original selected file
        * tmpFile is for creating temporary file for applying filters
        * format is for storing outputed format
     */
    private static ImageView imgViewPic;
    private static Image selectedImage;
    private static File originalFile;
    private static File tmpFile;
    private static String format = "jpg";

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //lblTitle is for showing title on gui
        Label lblTitle = new Label("Image Management");
        lblTitle.setStyle("-fx-text-fill: Red; -fx-font-size:25");
        //lblLocation is for showing location of selected image
        Label lblLocation = new Label("");
        lblLocation.setStyle("-fx-text-fill: blue; -fx-font-size:15");
        //initialize and set properties of imageview
        imgViewPic = new ImageView();
        imgViewPic.setFitHeight(400);
        imgViewPic.setFitWidth(400);
        //file choose filter for selecting only images files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        //file chooser for selecting file
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(imageFilter);
        //btnFileChoose for opening file chooser window
        Button btnFileChoose = new Button("Select Image");
        //add filters for applying on image
        VBox vbOriginal = filterButton("original");
        VBox vbBlack = filterButton("black&white");
        VBox vbBlue = filterButton("blue");
        VBox vbRed = filterButton("red");
        VBox vbGreen = filterButton("green");
        //label represent the filter
        Label lblFilter = new Label("Filter: ");
        lblFilter.setStyle("-fx-text-fill: Red; -fx-font-size:20");
        //hbox for adding label and filters and set properties
        HBox hbFilter = new HBox();
        hbFilter.setAlignment(Pos.CENTER);
        hbFilter.setSpacing(20);
        hbFilter.getChildren().addAll(lblFilter, vbOriginal, vbBlack, vbBlue, vbRed, vbGreen);
        hbFilter.setVisible(false);
        //label represent resize
        Label lblResize = new Label("Resize Image: ");
        lblResize.setStyle("-fx-text-fill: Red; -fx-font-size:20");
        //labels and textfields for setting the height and width
        Label lblWidth = new Label("Width: ");
        Label lblHeight = new Label("Height: ");
        TextField tfWidth = new TextField();
        tfWidth.setPrefSize(70, 20);
        TextField tfHeight = new TextField();
        tfHeight.setPrefSize(70, 20);
        //hbox for adding labels and textfields for resizing
        HBox hbResize = new HBox();
        hbResize.setAlignment(Pos.CENTER);
        hbResize.setSpacing(20);
        hbResize.getChildren().addAll(lblResize, lblWidth, tfWidth, lblHeight, tfHeight);
        //radio buttons for selecting the format
        RadioButton rbJPG = new RadioButton("jpg");
        RadioButton rbJPEG = new RadioButton("jpeg");
        RadioButton rbPNG = new RadioButton("png");
        //toggle group for adding radiobuttons in it
        ToggleGroup rbGroup = new ToggleGroup();
        rbJPG.setToggleGroup(rbGroup);
        rbJPEG.setToggleGroup(rbGroup);
        rbPNG.setToggleGroup(rbGroup);
        rbJPG.setSelected(true);
        //label represent format
        Label lblFormat = new Label("Format: ");
        lblFormat.setStyle("-fx-text-fill: Red; -fx-font-size:20");
        //hbox for adding label and radio buttons
        HBox hbFormat = new HBox();
        hbFormat.setAlignment(Pos.CENTER);
        hbFormat.setSpacing(20);
        hbFormat.getChildren().addAll(lblFormat, rbJPG, rbJPEG, rbPNG);
        hbFormat.setVisible(false);
        hbResize.setVisible(false);
        //button for saving the file
        Button btnSave = new Button("Save Image");
        btnSave.setVisible(false);
        //method for adding only numbers in textfield width
        tfWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfWidth.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        //method for adding only numbers in textfield height
        tfHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfHeight.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        //action on file choose button
        btnFileChoose.setOnAction((ActionEvent event) -> {
            //select file
            originalFile = fc.showOpenDialog(primaryStage);
            if (originalFile != null) {
                try {
                    //show image in image view
                    selectedImage = new Image(new FileInputStream(originalFile));
                    imgViewPic.setImage(selectedImage);
                    //show location of selected file
                    lblLocation.setText("Location: "+originalFile);
                    //show all components e.g filter, format, resize
                    hbFilter.setVisible(true);
                    hbFormat.setVisible(true);
                    hbResize.setVisible(true);
                    btnSave.setVisible(true);
                    tmpFile = originalFile;
                    //set height and width of image to textfields
                    tfWidth.setText(String.valueOf(selectedImage.getWidth()));
                    tfHeight.setText(String.valueOf(selectedImage.getHeight()));

                } catch (FileNotFoundException ex) {

                }

            }
        });

        //action on save button click
        btnSave.setOnAction((event) -> {

            try {
                //initialize format from radio buttons
                if (rbJPG.isSelected()) {
                    format = "jpg";
                } else if (rbJPEG.isSelected()) {
                    format = "jpeg";
                } else {
                    format = "png";
                }
                //get width and height from text fields
                int width = Integer.parseInt(tfWidth.getText());
                int height = Integer.parseInt(tfHeight.getText());
                //select path for saving imae
                FileChooser file = new FileChooser();
                file.setTitle("Save Image");
                File file1 = file.showSaveDialog(primaryStage);
                String path = file1.getAbsolutePath() + "." + format;
                File output = new File(path);
                //create new file if not exist
                if (!output.exists()) {
                    output.createNewFile();
                }
                //created and save updated image
                BufferedImage biOriginal = ImageIO.read(tmpFile);
                BufferedImage resized = new BufferedImage(width, height, biOriginal.getType());
                Graphics2D g2 = resized.createGraphics();
                g2.drawImage(biOriginal, 0, 0, width, width, null);
                g2.dispose();
                ImageIO.write(resized, format, output);
                //show message
                showMessage("Save Image","Succuessfully Saved!");
                //delete temporary file
                tmpFile.delete();
                //reset form
                format = "jpg";
                rbJPG.setSelected(true);
                hbFilter.setVisible(false);
                hbResize.setVisible(false);
                hbFormat.setVisible(false);
                lblLocation.setText("");
                originalFile = null;
                tmpFile = null;
                imgViewPic.setImage(null);
                selectedImage = null;
                btnSave.setVisible(false);

            } catch (Exception e) {

            }
        });
        //vbox for adding all components and set vbox properties
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(lblTitle,lblLocation, imgViewPic, hbFilter, hbResize, hbFormat, btnFileChoose, btnSave);
        //scene for showing gui
        Scene scene = new Scene(root, 700, 830);

        primaryStage.setTitle("Image Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //method for creating filters with their actions
    public static VBox filterButton(String name) throws FileNotFoundException {
        //add label for filter name
        Label lbl = new Label(name);
        //add image and imageview for setting filter icons
        Image img = new Image(new FileInputStream("icons/" + name + ".jpg"));
        ImageView imgView = new ImageView();
        imgView.setFitHeight(50);
        imgView.setFitWidth(50);
        imgView.setImage(img);
        //add button for showing filter
        Button btn = new Button();
        btn.setPrefSize(50, 50);
        btn.setGraphic(imgView);
        //set action on click on filter button
        btn.setOnAction((event) -> {
            try {
                //if click on black&white filter
                if (name.equals("black&white")) {
                    //read image and get width and height
                    BufferedImage biFilter = ImageIO.read(originalFile);
                    int width = biFilter.getWidth();
                    int height = biFilter.getHeight();
                    //nested loop over width and height
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            //get rgb of that pixel
                            int p = biFilter.getRGB(x, y);
                            int a = (p >> 24) & 0xff;
                            int r = (p >> 16) & 0xff;
                            int g = (p >> 8) & 0xff;
                            int b = p & 0xff;

                            //calculate average
                            int avg = (r + g + b) / 3;

                            //replace RGB value with avg
                            p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                            biFilter.setRGB(x, y, p);
                        }
                    }
                    //create new temp file and store, show the temp file in imageview in gui
                    tmpFile = new File("tmp.jpg");
                    ImageIO.write(biFilter, "jpg", tmpFile);
                    selectedImage = new Image(new FileInputStream(tmpFile));
                    imgViewPic.setImage(selectedImage);

                } //if click on red filter
                else if (name.equals("red")) {
                    //read image and get width and height
                    BufferedImage biFilter = ImageIO.read(originalFile);
                    int width = biFilter.getWidth();
                    int height = biFilter.getHeight();
                    //nested loop over width and height
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            //get rgb of that pixel
                            int p = biFilter.getRGB(x, y);

                            int a = (p >> 24) & 0xff;
                            int r = (p >> 16) & 0xff;

                            //set new RGB
                            p = (a << 24) | (r << 16) | (0 << 8) | 0;
                            biFilter.setRGB(x, y, p);
                        }
                    }
                    //create new temp file and store, show the temp file in imageview in gui
                    tmpFile = new File("tmp.jpg");
                    ImageIO.write(biFilter, "jpg", tmpFile);
                    selectedImage = new Image(new FileInputStream(tmpFile));
                    imgViewPic.setImage(selectedImage);

                } //if click on green filter
                else if (name.equals("green")) {
                    //read image and get width and height
                    BufferedImage biFilter = ImageIO.read(originalFile);
                    int width = biFilter.getWidth();
                    int height = biFilter.getHeight();
                    //nested loop over width and height
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            //get rgb of that pixel
                            int p = biFilter.getRGB(x, y);
                            int a = (p >> 24) & 0xff;
                            int g = (p >> 8) & 0xff;

                            //set new RGB
                            p = (a << 24) | (0 << 16) | (g << 8) | 0;
                            biFilter.setRGB(x, y, p);
                        }
                    }
                    //create new temp file and store, show the temp file in imageview in gui
                    tmpFile = new File("tmp.jpg");
                    ImageIO.write(biFilter, "jpg", tmpFile);
                    selectedImage = new Image(new FileInputStream(tmpFile));
                    imgViewPic.setImage(selectedImage);

                } //if click on blue filter
                else if (name.equals("blue")) {
                    //read image and get width and height
                    BufferedImage biFilter = ImageIO.read(originalFile);
                    int width = biFilter.getWidth();
                    int height = biFilter.getHeight();
                    //nested loop over width and height
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            //get rgb of that pixel

                            int p = biFilter.getRGB(x, y);

                            int a = (p >> 24) & 0xff;
                            int b = p & 0xff;

                            //set new RGB
                            p = (a << 24) | (0 << 16) | (0 << 8) | b;
                            biFilter.setRGB(x, y, p);
                        }
                    }
                    //create new temp file and store, show the temp file in imageview in gui
                    tmpFile = new File("tmp.jpg");
                    ImageIO.write(biFilter, "jpg", tmpFile);
                    selectedImage = new Image(new FileInputStream(tmpFile));
                    imgViewPic.setImage(selectedImage);

                }//if click on original filter 
                else if (name.equals("original")) {
                    //set temp file to original file and show in gui
                    tmpFile = originalFile;
                    selectedImage = new Image(new FileInputStream(tmpFile));
                    imgViewPic.setImage(selectedImage);
                }
            } catch (Exception e) {

            }
        });
        //add components to vbox and return
        VBox vb = new VBox();
        vb.getChildren().addAll(lbl, btn);
        return vb;
    }
    public static void showMessage(String title, String header){
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(header);
        a.showAndWait();
    }

}
