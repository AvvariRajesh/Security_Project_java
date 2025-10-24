package com.udasecurity.gui;

import com.udasecurity.security.SecurityService;
import com.udasecurity.security.ArmingStatus;
import com.udasecurity.imageservice.ImageService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecurityGUI extends Application {

    private SecurityService securityService;
    private Label systemStatusLabel;
    private ImageView cameraView;

    // images (resource paths inside target/classes/images/)
    private final String[] cameraImages = {
            "/images/no_cat_placeholder.png",
            "/images/cat_placeholder.png"
    };
    private int currentImageIndex = 0;
    private final Random random = new Random();

    // Sensor UI state (keeps sensors for display)
    private final List<SensorRow> sensorRows = new ArrayList<>();
    private VBox sensorsListBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // initialize service (you already have SecurityService(ImageService) constructor)
        securityService = new SecurityService(new ImageService());

        // Root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // TOP: Project name on top-right
        Label projectName = new Label("MY HOME SECURITY");
        projectName.setFont(Font.font("Arial", 20));
        projectName.setStyle("-fx-font-weight: bold;");
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.TOP_CENTER);
        topBox.getChildren().add(projectName);
        root.setTop(topBox);

        // CENTER: main content: right-aligned status above image, image, camera buttons
        VBox centerVBox = new VBox(10);
        centerVBox.setAlignment(Pos.TOP_CENTER);

        // System Status (top-right above the image)
        systemStatusLabel = new Label("System Status: Cool and Good");
        systemStatusLabel.setFont(Font.font(14));
        setSystemStatus(false); // initial: no cat, green

        // Put the status in an HBox aligned top-center
        HBox statusBox = new HBox();
        statusBox.setAlignment(Pos.TOP_CENTER);
        statusBox.getChildren().add(systemStatusLabel);

        // Camera image view (single image shown at a time)
        cameraView = new ImageView();
        cameraView.setFitWidth(360);
        cameraView.setFitHeight(240);
        cameraView.setPreserveRatio(true);
        loadCameraImage(currentImageIndex);

        // Camera buttons: Refresh Camera and Scan Picture on same line
        Button refreshBtn = new Button("Refresh Camera");
        Button scanBtn = new Button("Scan Picture");
        HBox cameraButtons = new HBox(10, refreshBtn, scanBtn);
        cameraButtons.setAlignment(Pos.CENTER);

        // Method to sync system status with current image
        refreshBtn.setOnAction(e -> {
            currentImageIndex = (currentImageIndex + 1) % cameraImages.length;
            loadCameraImage(currentImageIndex);
            updateSystemStatusBasedOnImage();
        });

        scanBtn.setOnAction(e -> {
            boolean cat = updateSystemStatusBasedOnImage();
            String msg = cat ? "Cat detected!" : "Everything is fine!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setTitle("Scan Result");
            alert.setHeaderText(null);
            alert.showAndWait();
        });

        centerVBox.getChildren().addAll(statusBox, cameraView, cameraButtons);

        // SYSTEM CONTROL block
        Label systemControlLabel = new Label("System Control:");
        systemControlLabel.setStyle("-fx-font-weight: bold;");
        Button disarmBtn = new Button("Disarmed");
        Button armedHomeBtn = new Button("Armed - At Home");
        Button armedAwayBtn = new Button("Armed - Away");

        disarmBtn.setOnAction(e -> securityService.setArmingStatus(ArmingStatus.DISARMED));
        armedHomeBtn.setOnAction(e -> securityService.setArmingStatus(ArmingStatus.ARMED_HOME));
        armedAwayBtn.setOnAction(e -> securityService.setArmingStatus(ArmingStatus.ARMED_AWAY));

        HBox systemControlBox = new HBox(10, disarmBtn, armedHomeBtn, armedAwayBtn);
        systemControlBox.setAlignment(Pos.CENTER);

        VBox systemControlLayout = new VBox(6, systemControlLabel, systemControlBox);
        systemControlLayout.setAlignment(Pos.CENTER);
        systemControlLayout.setPadding(new Insets(10, 0, 0, 0));

        centerVBox.getChildren().add(systemControlLayout);

        root.setCenter(centerVBox);

        // RIGHT: Sensor Management
        VBox rightVBox = new VBox(10);
        rightVBox.setPadding(new Insets(0, 0, 0, 12));
        rightVBox.setPrefWidth(320);
        rightVBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");

        Label sensorTitle = new Label("Sensor Management");
        sensorTitle.setStyle("-fx-font-weight: bold;");
        Label nameLabel = new Label("Name:");
        TextField sensorNameField = new TextField();
        sensorNameField.setPromptText("Sensor Name");

        Label typeLabel = new Label("Sensor Type:");
        ComboBox<String> sensorTypeBox = new ComboBox<>();
        sensorTypeBox.getItems().addAll("DOOR", "WINDOW", "MOTION");
        sensorTypeBox.setValue("DOOR");

        Button addSensorBtn = new Button("Add New Sensor");
        HBox addControls = new HBox(8, sensorNameField, sensorTypeBox, addSensorBtn);
        addControls.setAlignment(Pos.CENTER_LEFT);

        sensorsListBox = new VBox(6);
        sensorsListBox.setPadding(new Insets(6));
        addSensorToUI("Dog", "DOOR", false);
        addSensorToUI("Fish", "WINDOW", false);

        addSensorBtn.setOnAction(e -> {
            String name = sensorNameField.getText().trim();
            String type = sensorTypeBox.getValue();
            if (name.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Please enter a sensor name.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            addSensorToUI(name, type, false);
            sensorNameField.clear();
        });

        rightVBox.getChildren().addAll(sensorTitle, nameLabel, addControls, new Separator(), sensorsListBox);
        root.setRight(rightVBox);

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(8, 0, 0, 0));
        root.setBottom(footer);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Home Security System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCameraImage(int index) {
        String path = cameraImages[index];
        try {
            if (getClass().getResource(path) != null) {
                cameraView.setImage(new Image(getClass().getResource(path).toExternalForm()));
            } else {
                cameraView.setImage(new Image("file:SecurityService/src/main/resources/images/" + path.substring(path.lastIndexOf('/') + 1)));
            }
        } catch (Exception ex) {
            cameraView.setImage(null);
            System.err.println("Failed to load image " + path + ": " + ex.getMessage());
        }
    }

    private boolean isCurrentImageCat() {
        String path = cameraImages[currentImageIndex];
        return path.toLowerCase().contains("cat");
    }

    // This ensures system status always matches the current image
    private boolean updateSystemStatusBasedOnImage() {
        boolean catDetected = isCurrentImageCat();
        setSystemStatus(catDetected);
        return catDetected;
    }

    private void setSystemStatus(boolean catDetected) {
        if (catDetected) {
            systemStatusLabel.setText("System Status: DANGER - CAT DETECTED");
            systemStatusLabel.setTextFill(Color.WHITE);
            systemStatusLabel.setStyle("-fx-background-color: #C62828; -fx-padding: 6px; -fx-font-weight: bold;");
        } else {
            systemStatusLabel.setText("System Status: Cool and Good");
            systemStatusLabel.setTextFill(Color.WHITE);
            systemStatusLabel.setStyle("-fx-background-color: #2E7D32; -fx-padding: 6px; -fx-font-weight: bold;");
        }
    }

    private void addSensorToUI(String name, String type, boolean active) {
        SensorRow row = new SensorRow(name, type, active);
        sensorRows.add(row);
        sensorsListBox.getChildren().add(row.getHBox());
    }

    private static class SensorRow {
        private final String name;
        private final String type;
        private boolean active;
        private final Label statusLabel;
        private final HBox hbox;

        SensorRow(String name, String type, boolean active) {
            this.name = name;
            this.type = type;
            this.active = active;

            Label label = new Label(name + " (" + type + "): ");
            statusLabel = new Label(active ? "Active" : "Inactive");
            Button activateBtn = new Button(active ? "Deactivate" : "Activate");
            Button removeBtn = new Button("Remove Sensor");

            HBox rowBox = new HBox(8, label, statusLabel, activateBtn, removeBtn);
            rowBox.setAlignment(Pos.CENTER_LEFT);
            this.hbox = rowBox;

            activateBtn.setOnAction(e -> {
                this.active = !this.active;
                statusLabel.setText(this.active ? "Active" : "Inactive");
                activateBtn.setText(this.active ? "Deactivate" : "Activate");
            });

            removeBtn.setOnAction(e -> {
                VBox parent = (VBox) rowBox.getParent();
                if (parent != null) {
                    parent.getChildren().remove(rowBox);
                }
            });
        }

        HBox getHBox() {
            return hbox;
        }
    }
}
