/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import bakalarka.Utilities.FontSizeBinder;
import bakalarka.Utilities.ToolTips;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author MarekPC
 */
public class FormulaInfoController implements Initializable {

    @FXML
    Label labelFormula;
    @FXML
    VBox mainBox;
    @FXML
    TextFlow tfFormulaInfo;

    private Formula formula;
    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.BASE_FONT);

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE;

    private Formula currentFormula;

    public FormulaInfoController(Formula f) {

        currentFormula = f;

    }

    public void changeInfo() {
        tfFormulaInfo.getChildren().clear();
        ObservableList<Text> infoList = ToolTips.constructText(currentFormula.formulaInfo);
        FontSizeBinder.bindFontSizeAll(fontProperty, infoList);
        tfFormulaInfo.getChildren().addAll(infoList);

    }

    public void setFormula(Formula f) {

        currentFormula = f;
        setContent();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContent();
        mainBox.setOnScroll(e -> {
            if (e.isControlDown()) {

                if (e.getDeltaY() > 0) {
                    currentFontSize += FontSizeBinder.FONT_SIZE_INCREMENT;
                } else {
                    currentFontSize -= FontSizeBinder.FONT_SIZE_INCREMENT;
                }
                fontProperty.set(Font.font(FontSizeBinder.FONT_FAMILY_USED, currentFontSize));

            }

        });

    }

    private void setContent() {
        changeInfo();
        labelFormula.textProperty().set(currentFormula.label);

    }

}
