/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import bakalarka.Utilities.dataPack.DataLoader;
import bakalarka.view3d.ModelWindowMainController;
import chemicals.HydroCarbon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author MarekPC
 */
public class FormulaViewController implements Initializable {

    @FXML
    ToggleButton btnCenter;
    @FXML
    Label labelCH;
    @FXML
    Label labelFormulaName;
    @FXML
    SplitPane splitPane;
    @FXML
    ToggleButton btnInfo;
    @FXML
    VBox formulaBox;
    @FXML
    VBox vboxFormulas;

    private HydroCarbon hc;
    private double lastInfoWidth = -1;
    private VBox infoView;
    private FormulaInfoController infoController;
    private Formula currentFormula;
    private Formula[] formulasArr;
    private ResizableCanvas[] canvases;
    private int currentFormulaIndex = 0;

    private EventHandler<KeyEvent> arrowsSupport = e -> {
        if (e.getCode() == KeyCode.LEFT) {
            previousFormula(null);
        } else if (e.getCode() == KeyCode.RIGHT) {
            nextFormula(null);
        }
        e.consume();

    };

    FormulaViewController(HydroCarbon phc) {
        this.hc = phc;

    }

    public void centerBound(ActionEvent e) {
        formulasArr[2].contentChanged();
        formulasArr[3].contentChanged();
        canvases[currentFormulaIndex].draw();
        labelCH.setText(hc.getMultiBoundPosition() + 1 + " - " + hc.getName());

    }

    public void showInfoPressed(ActionEvent e) {

        SplitPane.Divider divider = splitPane.getDividers().get(0);
        if (btnInfo.isSelected()) {

            if (lastInfoWidth == -1) {
                lastInfoWidth = infoView.prefWidth(-1);

            }
            double dividerPosition = 1 - (lastInfoWidth / splitPane.getWidth());
            new Timeline(
                    new KeyFrame(Duration.seconds(0.3),
                            new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            infoView.setMinWidth(0);
                        }
                    },
                            new KeyValue(divider.positionProperty(), dividerPosition, Interpolator.EASE_BOTH)
                    )
            ).play();

        } else {
            lastInfoWidth = infoView.getWidth();
            infoView.setMinWidth(0);
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(divider.positionProperty(), 1))).play();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadFormulas();
        currentFormula = formulasArr[currentFormulaIndex];
        makeCanvases();
        btnInfo.selectedProperty().set(false);

        FXMLLoader fx = new FXMLLoader(getClass().getResource("FormulaInfoView.fxml"));
        infoController = new FormulaInfoController(currentFormula);
        fx.setController(infoController);
        try {
            infoView = fx.load();
        } catch (IOException ex) {
            Logger.getLogger(ModelWindowMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        formulaBox.getChildren().add(canvases[0]);
//        formulaBox.getChildren().add(canvases[0]);

//        splitPane.getItems().clear();
        labelCH.textProperty().set(hc.getName());
        splitPane.getItems().add(infoView);

        updateCanvas();
        setName();
        splitPane.getDividers().get(0).setPosition(1);

        vboxFormulas.onKeyPressedProperty().set(arrowsSupport);

        btnInfo.onKeyPressedProperty().set(arrowsSupport);

        if (hc.getBoundMultiple() == 1 || hc.getcNumber() < 3 || hc.isIsCyclo()) {
            btnCenter.setManaged(false);
            btnCenter.setVisible(false);
        } else {
            if (hc.getMultiBoundPosition() != 0) {
                btnCenter.selectedProperty().set(false);
            }
            labelCH.setText(hc.getMultiBoundPosition() + 1 + " - " + hc.getName());

        }
        this.hc.bindStrategy(btnCenter.selectedProperty());

    }

    public void previousFormula(ActionEvent e) {

        if (currentFormulaIndex == 0) {
            currentFormulaIndex = 4;
        }
        currentFormula = formulasArr[--currentFormulaIndex];
        updateCanvas();
        infoController.setFormula(currentFormula);
        setName();

    }

    public void spacePressed() {
        canvases[currentFormulaIndex].hideToggle();

    }

    public void nextFormula(ActionEvent e) {
        if (currentFormulaIndex == 3) {
            currentFormulaIndex = -1;
        }

        currentFormula = formulasArr[++currentFormulaIndex];
        updateCanvas();
        infoController.setFormula(currentFormula);
        setName();
    }

    private void makeCanvases() {
        ResizableCanvas summaryCanv = new ResizableCanvas(formulasArr[0]);

        summaryCanv.widthProperty().bind(formulaBox.widthProperty());
        summaryCanv.heightProperty().bind(formulaBox.heightProperty());
        ResizableCanvas empiricCanv = new ResizableCanvas(formulasArr[1]);
        empiricCanv.widthProperty().bind(formulaBox.widthProperty());
        empiricCanv.heightProperty().bind(formulaBox.heightProperty());
        ResizableCanvas racioCanv = new ResizableCanvas(formulasArr[2]);
        racioCanv.widthProperty().bind(formulaBox.widthProperty());
        racioCanv.heightProperty().bind(formulaBox.heightProperty());
        ResizableCanvas structCanv = new ResizableCanvas(formulasArr[3]);
        structCanv.widthProperty().bind(formulaBox.widthProperty());
        structCanv.heightProperty().bind(formulaBox.heightProperty());

        canvases = new ResizableCanvas[]{summaryCanv, empiricCanv, racioCanv, structCanv};

    }

    private void loadFormulas() {

        SummaryFormula summF = new SummaryFormula(hc, DataLoader.FormulasData.summaryLabel, DataLoader.FormulasData.summaryInfo);
        EmpiricalFormula empiricF = new EmpiricalFormula(hc, DataLoader.FormulasData.empiricalabel, DataLoader.FormulasData.empiricalInfo);
        RationalFormula racioF = new RationalFormula(hc, DataLoader.FormulasData.racionalLabel, DataLoader.FormulasData.racionalInfo);

        StructuralFormula structF = new StructuralFormula(hc, DataLoader.FormulasData.structuralLabel, DataLoader.FormulasData.structuralInfo);

        formulasArr = new Formula[]{summF, empiricF, racioF, structF};

    }

    private void updateCanvas() {
        formulaBox.getChildren().clear();
        formulaBox.getChildren().add(canvases[currentFormulaIndex]);

    }

    private void setName() {

        labelFormulaName.textProperty().set(currentFormula.label);

    }

}
