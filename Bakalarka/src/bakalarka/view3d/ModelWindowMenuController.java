/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import bakalarka.Utilities.FontSizeBinder;
import bakalarka.Utilities.ToolTips;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author MarekPC
 */
public class ModelWindowMenuController implements Initializable {

    @FXML
    ColorPicker cpBackground;
    @FXML
    ColorPicker cpCarbon;
    @FXML
    ColorPicker cpHydrogen;
    @FXML
    CheckBox cbLight;
    @FXML
    TitledPane paneModelInfo;
    @FXML
    TextFlow tfModelInfo;
    @FXML
    Label labelModel;

    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.BASE_FONT);

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE;

    private ContentModel3D contentModel;

    public ModelWindowMenuController(ContentModel3D contentModel) {
        this.contentModel = contentModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contentModel.bindProperty(cpBackground.valueProperty(), "background");
        cpBackground.valueProperty().set(Color.GRAY);
        contentModel.bindProperty(cpCarbon.valueProperty(), "carbon");
        cpCarbon.valueProperty().set(Color.web("rgb(100,100,100)"));
        contentModel.bindProperty(cpHydrogen.valueProperty(), "hydrogen");
        contentModel.getAmbientLightProperty().bind(cbLight.selectedProperty());
        paneModelInfo.setOnScroll(e -> {
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

    public void setModelInfo(String text, String label) {
        tfModelInfo.getChildren().clear();
        ObservableList<Text> infoList = ToolTips.constructText(text);
        FontSizeBinder.bindFontSizeAll(fontProperty, infoList);
        tfModelInfo.getChildren().addAll(infoList);
        labelModel.setText(label);

    }

}
