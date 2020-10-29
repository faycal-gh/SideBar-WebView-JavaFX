/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package side.bar;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Example of a sidebar that slides in and out of view
 */
public class SlideOut extends Application {

    private static final String[] Names = {
        "Inside Java \n Episode 6 \n “Project Skara” with \n Joe Darcy \n and Erik Duveblad",
        "Instagram Clone \n UI Application \n using flutter",
        "Uncover Devices \n In Your Home \n WiFi Network!"
    };

    private static final String[] locs = {
//        "https://www.youtube.com/watch?v=_j9fazUaKuc",
//        "https://www.youtube.com/watch?v=UWLPNfcoSjM",
//        "https://www.youtube.com/watch?v=pS2XcqKn7u8"        
    };
    WebView webView;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(final Stage stage) throws Exception {
        stage.setTitle("JavaFXApplication5");
        webView = new WebView();
        webView.setPrefSize(800, 600);
        final Pane videoPane = createSidebarContent();
        SideBar sidebar = new SideBar(250, videoPane);
        VBox.setVgrow(videoPane, Priority.ALWAYS);
        final BorderPane layout = new BorderPane();
        Pane mainPane = VBoxBuilder.create().spacing(10)
                .children(
                        sidebar.getControlButton(),
                        webView
                ).build();
        layout.setLeft(sidebar);
        layout.setCenter(mainPane);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("slideout.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane createSidebarContent() {
        final Text video = new Text();
        video.getStyleClass().add("video-text");
        final Button changeVideo = new Button("Random");
        changeVideo.getStyleClass().add("change-video");
        changeVideo.setMaxWidth(Double.MAX_VALUE);
        changeVideo.setOnAction(new EventHandler<ActionEvent>() {
            int nameIndex = 0;

            @Override
            public void handle(ActionEvent actionEvent) {
                nameIndex++;
                if (nameIndex == Names.length) {
                    nameIndex = 0;
                }
                video.setText(Names[nameIndex]);
                webView.getEngine().load(locs[nameIndex]);
            }
        });
        changeVideo.fire();
        final BorderPane videoPane = new BorderPane();
        videoPane.setCenter(video);
        videoPane.setBottom(changeVideo);
        return videoPane;
    }

    class SideBar extends VBox {

        public Button getControlButton() {
            return controlButton;
        }
        private final Button controlButton;

        SideBar(final double expandedWidth, Node... nodes) {
            getStyleClass().add("sidebar");
            this.setPrefWidth(expandedWidth);
            this.setMinWidth(0);
            setAlignment(Pos.CENTER);
            getChildren().addAll(nodes);
            controlButton = new Button("Close");
            controlButton.getStyleClass().add("hide-left");
            controlButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    final Animation hideSidebar = new Transition() {
                        {
                            setCycleDuration(Duration.millis(250));
                        }
                        protected void interpolate(double frac) {
                            final double curWidth = expandedWidth * (1.0 - frac);
                            setPrefWidth(curWidth);
                            setTranslateX(-expandedWidth + curWidth);
                        }
                    };
                    hideSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            setVisible(false);
                            controlButton.setText("Show");
                            controlButton.getStyleClass().remove("hide-left");
                            controlButton.getStyleClass().add("show-right");
                        }
                    });
                    final Animation showSidebar = new Transition() {
                        {
                            setCycleDuration(Duration.millis(250));
                        }
                        protected void interpolate(double frac) {
                            final double curWidth = expandedWidth * frac;
                            setPrefWidth(curWidth);
                            setTranslateX(-expandedWidth + curWidth);
                        }
                    };
                    showSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            controlButton.setText("Close");
                            controlButton.getStyleClass().add("hide-left");
                            controlButton.getStyleClass().remove("show-right");
                        }
                    });
                    if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
                        if (isVisible()) {
                            hideSidebar.play();
                        } else {
                            setVisible(true);
                            showSidebar.play();
                        }
                    }
                }
            });
        }
    }
}
