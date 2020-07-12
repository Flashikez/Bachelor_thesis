/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import bakalarka.HCgroup.HydroCarbonsBase;
import bakalarka.HCgroup.TreeItemViewable;
import bakalarka.Utilities.FontSizeBinder;
import bakalarka.Utilities.Styler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import bakalarka.Utilities.ToolTips;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;

/**
 *
 * @author MarekPC
 */
public class HCsController implements Initializable {

    @FXML
    TextFlow tfBasic;
    @FXML
    TextFlow tfGroups;
    @FXML
    TextFlow tfAttri;
    @FXML
    VBox mainBox;

    private String itemDivider = "            -";

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE;

    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.BASE_FONT);

    private SimpleDoubleProperty windowWidth = new SimpleDoubleProperty();
    private SimpleDoubleProperty windowHeigh = new SimpleDoubleProperty();

    private MultipleSelectionModel<TreeItem<TreeItemViewable>> selectionModel;
    ObservableList<TreeItem<TreeItemViewable>> linkList;
    ObservableList<TreeLink> supportedGroups = FXCollections.observableArrayList();

    private Text lol;

    public HCsController(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty, ObservableList<TreeItem<TreeItemViewable>> plinkList, MultipleSelectionModel<TreeItem<TreeItemViewable>> pselectionModel) {
        this.selectionModel = pselectionModel;
        this.linkList = plinkList;

        for (TreeItem<TreeItemViewable> treeItem : linkList) {
            TreeLink link = new TreeLink(treeItem, treeItem.getValue().toString() + "\n");
            Styler.styleAsTreeLink(link);
            link.onMouseClickedProperty().set(e -> {
                handleGroupsClick(link);

            });

            supportedGroups.add(link);

        }
    }

    protected void handleGroupsClick(TreeLink t) {
        selectionModel.select(t.link);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // BASIC INFO
        ObservableList<Text> list = ToolTips.constructText(HydroCarbonsBase.BASE_INFO);
        FontSizeBinder.bindFontSizeAll(fontProperty, list);
        tfBasic.getChildren().addAll(list);
        tfBasic.maxWidthProperty().bind(mainBox.widthProperty());

        // GROUPS
        FontSizeBinder.bindFontSizeAllLink(fontProperty, supportedGroups);
        tfGroups.maxWidthProperty().bind(Bindings.divide(mainBox.widthProperty(), 2));
        ObservableList<Text> alif = ToolTips.constructText("Alifatické:\n");
        ObservableList<Text> nasytene = ToolTips.constructText("   ⚉Nasýtené:\n");
        // add supported group cykloalkans
        ObservableList<Text> nenasyteneD = ToolTips.constructText("   ⚉Nenasýtené (s dvojitými väzbami):\n");
        // add supported group cykloalkens??
        ObservableList<Text> nenasyteneT = ToolTips.constructText("   ⚉Nenasýtené (s trojitými väzbami):\n");
        ObservableList<Text> diens = ToolTips.constructText(itemDivider + "Diény\n");
        ObservableList<Text> polyens = ToolTips.constructText(itemDivider + "Polyény\n");
        ObservableList<Text> cyclic = ToolTips.constructText(" Cyklické:\n");

        ObservableList<Text> dividers = FXCollections.observableArrayList();
        for (int i = 0; i < supportedGroups.size(); i++) {
            Text t = new Text(itemDivider);
            FontSizeBinder.bindFontSizeAll(fontProperty, t);
            dividers.add(t);

        }

        int dividerIndex = 0;
        FontSizeBinder.bindFontSizeAll(fontProperty, alif, nasytene, nenasyteneD, diens, polyens, nenasyteneT, cyclic);
        tfGroups.getChildren().addAll(alif);
        tfGroups.getChildren().addAll(nasytene);
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(0));

        tfGroups.getChildren().addAll(nenasyteneD);
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(1));
        tfGroups.getChildren().addAll(diens);
        tfGroups.getChildren().addAll(polyens);
        tfGroups.getChildren().addAll(nenasyteneT);
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(2));
        tfGroups.getChildren().addAll(cyclic);
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(3));
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(4));
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(5));
        tfGroups.getChildren().add(dividers.get(dividerIndex++));
        tfGroups.getChildren().add(supportedGroups.get(6));

//         tfGroups.getChildren().addAll(supportedGroups);
        // ATTRIBUTES
        ObservableList<Text> listAtr = ToolTips.constructText(HydroCarbonsBase.ATTRI);
        tfAttri.maxWidthProperty().bind(Bindings.divide(mainBox.widthProperty(), 2));
        FontSizeBinder.bindFontSizeAll(fontProperty, listAtr);
        tfAttri.getChildren().addAll(listAtr);

//            tfGroups.getChildren().addAll(groupsTexts);
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

        //Populate Grid groups
//        la.addAll(textFirstHalf,textAtom,space,textCarbon,textA,textHydrogen);
//        tfBasic.getChildren().addAll(la);
    }

}
