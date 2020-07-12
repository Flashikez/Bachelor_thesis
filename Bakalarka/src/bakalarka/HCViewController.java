/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import bakalarka.Utilities.FontSizeBinder;
import bakalarka.Utilities.ToolTips;
import chemicals.HydroCarbon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author MarekPC
 */
public class HCViewController implements Initializable {

    @FXML
    VBox mainBox;
    @FXML
    TextFlow tfBasic;
    @FXML
    TextFlow tfNames;
    @FXML
    TextFlow tfAttri;

    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.BASE_FONT);

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE;
    private HydroCarbon hc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        tfAttri.maxWidthProperty().bind(Bindings.divide(mainBox.widthProperty(), 2));

    }

    public void setHC(HydroCarbon phc) {
        hc = phc;
        setLayout();

    }

    private void setLayout() {
        tfBasic.getChildren().clear();
        tfAttri.getChildren().clear();
        tfNames.getChildren().clear();
        setBasic();
        setNames();
        setAttri();

    }

    private void setBasic() {
        ObservableList<Text> basicList = ToolTips.constructText(hc.getInfo());
        FontSizeBinder.bindFontSizeAll(fontProperty, basicList);
        tfBasic.getChildren().addAll(basicList);
    }

    private void setNames() {

        ObservableList<Text> namesList = ToolTips.constructText(hc.getOtherNames());
        FontSizeBinder.bindFontSizeAll(fontProperty, namesList);
        tfNames.getChildren().addAll(namesList);

    }

    private void setAttri() {
        ObservableList<Text> attriList = ToolTips.constructText(hc.getAttributes());
        FontSizeBinder.bindFontSizeAll(fontProperty, attriList);
        tfAttri.getChildren().addAll(attriList);

    }

}
