/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import bakalarka.Formulapack.SummaryFormula;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author MarekPC
 */
public class ModelWindowMainController implements Initializable {

    @FXML
    ToggleButton btnMoznosti;
    @FXML
    SplitPane splitPane;
    @FXML
    ChoiceBox choiceBox;
    @FXML
    Label labelCH;
    @FXML
    ToggleButton btnCenter;
    private double lastSettingsWidth = -1;
    private Accordion menu;
    private ContentModel3D contentModel;
    private ModelWindowMenuController menuControl;

    public ModelWindowMainController(ContentModel3D cm) {
        contentModel = cm;
    }

    public void showMenuPressed(ActionEvent e) {

        SplitPane.Divider divider = splitPane.getDividers().get(0);
        if (btnMoznosti.isSelected()) {

            if (lastSettingsWidth == -1) {
                lastSettingsWidth = menu.prefWidth(-1);

            }
            double dividerPosition = 1 - (lastSettingsWidth / splitPane.getWidth());
            new Timeline(
                    new KeyFrame(Duration.seconds(0.3),
                            new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            menu.setMinWidth(0);
                        }
                    },
                            new KeyValue(divider.positionProperty(), dividerPosition, Interpolator.EASE_BOTH)
                    )
            ).play();

        } else {
            lastSettingsWidth = menu.getWidth();
            menu.setMinWidth(0);
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(divider.positionProperty(), 1))).play();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnMoznosti.selectedProperty().set(true);

        FXMLLoader fx = new FXMLLoader(getClass().getResource("menu.fxml"));
        menuControl = new ModelWindowMenuController(contentModel);
        fx.setController(menuControl);
        try {
            menu = fx.load();
        } catch (IOException ex) {
            Logger.getLogger(ModelWindowMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pane pane2 = new Pane();
        TitledPane titledPane1 = new TitledPane("Panel 1", pane2);
        splitPane.getItems().addAll(new ResizableSubScene(contentModel.getsubScene()), menu);

        populateChoiceBox();
        String textLabel = contentModel.getHc().getName() + "  " + new SummaryFormula(contentModel.getHc(), "", "").toString();
        labelCH.setText(textLabel);
        splitPane.getDividers().get(0).setPosition(1);
        if (contentModel.getHc().getBoundMultiple() == 1 || contentModel.getHc().getcNumber() < 3 || contentModel.getHc().isIsCyclo()) {
            btnCenter.setManaged(false);
            btnCenter.setVisible(false);
        } else {
            updateLabel();

            if (contentModel.getHc().getMultiBoundPosition() != 0) {
                btnCenter.setSelected(false);
            } else {
                btnCenter.setSelected(true);
            }
            btnCenter.onActionProperty().set(e -> {
                contentModel.getHc().getMultBoundFirstStrategy().set(btnCenter.selectedProperty().get());
                contentModel.rebuildModels();
                updateLabel();
            });

        }
    }

    private void updateLabel() {
        labelCH.setText(contentModel.getHc().getMultiBoundPosition() + 1 + " - " + contentModel.getHc().getName() + "  " + new SummaryFormula(contentModel.getHc(), "", "").toString());

    }

    private void populateChoiceBox() {
        choiceBox.setItems(FXCollections.observableArrayList("Kalótový model", "Guličkový model", "Trubičkový model"));
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            menuControl.setModelInfo(contentModel.getModelInfoAtIndex(newValue.intValue()), contentModel.getModelLabelAtindex(newValue.intValue()));

        });

        choiceBox.getSelectionModel().selectFirst();
        contentModel.getSelectedModelProperty().bind(choiceBox.getSelectionModel().selectedIndexProperty());

    }

}
