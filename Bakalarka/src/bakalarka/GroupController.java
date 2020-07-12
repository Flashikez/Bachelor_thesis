/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import bakalarka.HCgroup.HydroCarbonGroup;
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
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author MarekPC
 */
public class GroupController implements Initializable {

    @FXML
    VBox mainBox;
    @FXML
    TextFlow tfBasic;
    @FXML
    TextFlow tfAttri;
    @FXML
    HBox hboxCHS;
    @FXML
    VBox vboxNum;
    @FXML
    VBox vboxCHS;
    private HydroCarbonGroup hcGroup;

    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.BASE_FONT);

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE;

    public GroupController() {

    }

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
        hboxCHS.maxWidthProperty().bind(Bindings.divide(mainBox.widthProperty(), 2));
    }

    public void setGroup(HydroCarbonGroup group) {
        hcGroup = group;
        setLayout();

    }

    private void setLayout() {
        tfBasic.getChildren().clear();
        tfAttri.getChildren().clear();
        vboxCHS.getChildren().clear();
        vboxNum.getChildren().clear();

        setBasic();
        setAtri();
        setSupportedList();

    }

    private void setBasic() {
        ObservableList<Text> basicList = ToolTips.constructText(hcGroup.getInfo());
        FontSizeBinder.bindFontSizeAll(fontProperty, basicList);
        tfBasic.getChildren().addAll(basicList);

    }

    private void setAtri() {
        ObservableList<Text> attrList = ToolTips.constructText(hcGroup.getAttri());
        FontSizeBinder.bindFontSizeAll(fontProperty, attrList);
        tfAttri.getChildren().addAll(attrList);
    }

    private void setSupportedList() {

        ObservableList<Text> clist = ToolTips.constructText("Počet atómov uhlíka");
        TextFlow t = new TextFlow();
        t.getChildren().addAll(clist);
        FontSizeBinder.bindFontSizeAll(fontProperty, clist);
        Text zastupca = new Text("Zástupca");
        FontSizeBinder.bindFontSizeAll(fontProperty, zastupca);
        vboxNum.getChildren().add(t);
        vboxCHS.getChildren().add(zastupca);
        Separator sep1 = new Separator(Orientation.HORIZONTAL);
        sep1.getStyleClass().add("niceseparator");
        Separator sep2 = new Separator(Orientation.HORIZONTAL);
        sep2.getStyleClass().add("niceseparator");
        vboxNum.getChildren().add(sep1);
        vboxCHS.getChildren().add(sep2);

        String notSupportedItem = "";
        boolean print = true;
        boolean num1Taken = false;
        for (int i = 1; i <= 20; i++) {
            num1Taken = false;
            print = false;
            Text num = new Text(Integer.toString(i));
            FontSizeBinder.bindFontSizeAll(fontProperty, num);
            for (String string : hcGroup.getNotSupported()) {
                String[] split = string.split(" ");
                if (split[0].matches(Integer.toString(i))) {
                    notSupportedItem = split[1];
                    print = true;
                    break;

                }

            }
            for (TreeLink treeItem : hcGroup.getLinks()) {

                HydroCarbon hc = (HydroCarbon) treeItem.link.getValue();
                if (hc.getcNumber() == i) {
                    FontSizeBinder.bindFontSizeAll(fontProperty, treeItem);
                    vboxNum.getChildren().add(num);
                    num1Taken = true;
                    vboxCHS.getChildren().add(treeItem);
                    Separator sep = new Separator(Orientation.HORIZONTAL);
                    Separator sep3 = new Separator(Orientation.HORIZONTAL);
                    vboxNum.getChildren().add(sep);
                    vboxCHS.getChildren().add(sep3);
                    break;

                }
            }

            if (print) {
               // Text name = new Text(notSupportedItem);
               ObservableList<Text> name = ToolTips.constructText(notSupportedItem);
                if (num1Taken) {
                    Text num2 = new Text(Integer.toString(i));
                    FontSizeBinder.bindFontSizeAll(fontProperty, num2);
                    vboxNum.getChildren().add(num2);
                } else {
                    vboxNum.getChildren().add(num);
                }

                FontSizeBinder.bindFontSizeAll(fontProperty, name);
                Separator sep = new Separator(Orientation.HORIZONTAL);
                Separator sep3 = new Separator(Orientation.HORIZONTAL);
                vboxNum.getChildren().add(sep);

                vboxCHS.getChildren().addAll(name);
                vboxCHS.getChildren().add(sep3);
            }

        }
        Text etc = new Text("...");
        FontSizeBinder.bindFontSizeAll(fontProperty, etc);
        Separator se = new Separator(Orientation.HORIZONTAL);
        Separator se2 = new Separator(Orientation.HORIZONTAL);
        vboxNum.getChildren().addAll(se, etc);
        vboxCHS.getChildren().add(se2);

    }

}
