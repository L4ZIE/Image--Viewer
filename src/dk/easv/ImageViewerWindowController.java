package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ImageViewerWindowController
{
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private Timeline timeline;
    private Thread slideshowThread;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;


    private void handleAutoSlideshow(ActionEvent event) {
        currentImageIndex = (currentImageIndex + 1) % images.size();
        displayImage();
    }

    private void initialize() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), this::handleAutoSlideshow));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {

        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
        }
    }
    @FXML
    private void handleBtnStartSlideshowAction() {
        slideshowThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleAutoSlideshow(null);
            }
        });
        slideshowThread.start();
    }

    @FXML
    private void handleBtnStopSlideshowAction() {
        if (slideshowThread != null) {
            slideshowThread.interrupt();
        }
    }


}