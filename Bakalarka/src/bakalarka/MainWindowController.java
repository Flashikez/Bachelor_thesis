/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import bakalarka.view3d.ModelWindowEntryPoint;
import bakalarka.HCgroup.TreeItemViewable;
import bakalarka.HCgroup.HydroCarbonsBase;
import chemicals.HydroCarbon;
import bakalarka.Formulapack.FormulaWindowEntryPoint;
import bakalarka.HCgroup.HydroCarbonGroup;
import bakalarka.Utilities.FontSizeBinder;
import bakalarka.Utilities.dataPack.DataLoader;
import java.io.IOException;
import java.net.URL;
import javafx.scene.text.Font;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author MarekPC
 */
public class MainWindowController implements Initializable {

    @FXML
    Button btnTest;
    @FXML
    TextFlow tfName;
    @FXML
    ScrollPane scrollPane;
    @FXML
    TreeView<TreeItemViewable> treeMain;
    @FXML
    Button btn3d;
    @FXML
    Button btnFormulas;
    @FXML
    HBox hboxButtons;

    private VBox vboxGroup;
    private VBox vboxHcs;
    private VBox vboxHC;

    private FXMLLoader fxHC = new FXMLLoader(getClass().getResource("HCView.fxml"));
    private FXMLLoader fxHcs = new FXMLLoader(getClass().getResource("HCsView.fxml"));
    private FXMLLoader fxGroup;

    private HCViewController hcController;

    private HCsController hcsController;
    private GroupController groupController;

    private Text nameText = new Text();
    private Font font = new Font(20);
    private SimpleStringProperty nameProperty = new SimpleStringProperty();
//    private SimpleStringProperty aboutProperty = new SimpleStringProperty();
//    private SimpleStringProperty namesProperty = new SimpleStringProperty();
//    private SimpleStringProperty attrProperty = new SimpleStringProperty();

    private TreeItemViewable currentSelectedItem;
    private ObservableList<TreeItem<TreeItemViewable>> groupsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn3d.setOnAction(e -> {
            try {
                view3d();
            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        btnFormulas.setOnAction(e -> {
            try {
                viewFormulas();
            } catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        setupTreeView();
        setupTextField();

    }

    // Vytvorenie navigačného stromu
    private void setupTreeView() {

        HydroCarbonsBase rootNode = new HydroCarbonsBase();
        TreeItem<TreeItemViewable> rootItem = new TreeItem<TreeItemViewable>(rootNode);
        rootItem.setExpanded(true);
        treeMain.rootProperty().set(rootItem);

        treeMain.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            handle(newValue);

        }));

        //Alkany
        ObservableList<TreeItem<TreeItemViewable>> supportedAlkans = FXCollections.observableArrayList();
        HydroCarbonGroup alkans = new HydroCarbonGroup(DataLoader.groupsData.AlkansData.alkansLabel, DataLoader.groupsData.AlkansData.alkansInfo, DataLoader.groupsData.AlkansData.alkansAttri, DataLoader.groupsData.AlkansData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> alkansItem = new TreeItem<TreeItemViewable>(alkans);

        for (HydroCarbon hc : DataLoader.groupsData.AlkansData.alkans) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            alkansItem.getChildren().add(hcitem);
            supportedAlkans.add(hcitem);

        }
        alkansItem.setExpanded(true);
        alkans.setSupportedGroup(supportedAlkans);
        rootItem.getChildren().add(alkansItem);

        // Alkény
        HydroCarbonGroup alkens = new HydroCarbonGroup(DataLoader.groupsData.AlkensData.alkensLabel, DataLoader.groupsData.AlkensData.alkensInfo, DataLoader.groupsData.AlkensData.alkensAttri, DataLoader.groupsData.AlkensData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> alkensItem = new TreeItem<TreeItemViewable>(alkens);
        ObservableList<TreeItem<TreeItemViewable>> supportedAlkens = FXCollections.observableArrayList();

        for (HydroCarbon hc : DataLoader.groupsData.AlkensData.alkens) {

            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            alkensItem.getChildren().add(hcitem);
            supportedAlkens.add(hcitem);

        }
        alkensItem.setExpanded(true);
        alkens.setSupportedGroup(supportedAlkens);
        rootItem.getChildren().add(alkensItem);

        //Alkíny
        HydroCarbonGroup alkins = new HydroCarbonGroup(DataLoader.groupsData.AlkinsData.alkinsLabel, DataLoader.groupsData.AlkinsData.alkinsInfo, DataLoader.groupsData.AlkinsData.alkinsAttri, DataLoader.groupsData.AlkinsData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> alkinsItem = new TreeItem<TreeItemViewable>(alkins);
        ObservableList<TreeItem<TreeItemViewable>> supportedAlkins = FXCollections.observableArrayList();

        for (HydroCarbon hc : DataLoader.groupsData.AlkinsData.alkins) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            alkinsItem.getChildren().add(hcitem);
            supportedAlkins.add(hcitem);
        }

        alkins.setSupportedGroup(supportedAlkins);
        alkinsItem.setExpanded(true);
        rootItem.getChildren().add(alkinsItem);
        groupsList.addAll(alkansItem, alkensItem, alkinsItem);

        ObservableList<TreeItem<TreeItemViewable>> supportedCycloAlkans = FXCollections.observableArrayList();
        HydroCarbonGroup cycloAlkans = new HydroCarbonGroup(DataLoader.groupsData.CycloAlkansData.cycloalkansLabel, DataLoader.groupsData.CycloAlkansData.cycloalkansInfo, DataLoader.groupsData.CycloAlkansData.cycloalkansAttri, DataLoader.groupsData.CycloAlkansData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> cycloAlkansItem = new TreeItem<TreeItemViewable>(cycloAlkans);

        for (HydroCarbon hc : DataLoader.groupsData.CycloAlkansData.cycloalkans) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            cycloAlkansItem.getChildren().add(hcitem);
            supportedCycloAlkans.add(hcitem);

        }
        cycloAlkansItem.setExpanded(true);
        cycloAlkans.setSupportedGroup(supportedCycloAlkans);
        rootItem.getChildren().add(cycloAlkansItem);

        ObservableList<TreeItem<TreeItemViewable>> supportedCycloAlkens = FXCollections.observableArrayList();
        HydroCarbonGroup cycloAlkens = new HydroCarbonGroup(DataLoader.groupsData.CycloAlkensData.cycloalkensLabel, DataLoader.groupsData.CycloAlkensData.cycloalkensInfo, DataLoader.groupsData.CycloAlkensData.cycloalkensAttri, DataLoader.groupsData.CycloAlkensData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> cycloAlkensItem = new TreeItem<TreeItemViewable>(cycloAlkens);

        for (HydroCarbon hc : DataLoader.groupsData.CycloAlkensData.cycloalkens) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            cycloAlkensItem.getChildren().add(hcitem);
            supportedCycloAlkens.add(hcitem);

        }
        cycloAlkensItem.setExpanded(true);
        cycloAlkens.setSupportedGroup(supportedCycloAlkens);
        rootItem.getChildren().add(cycloAlkensItem);

        ObservableList<TreeItem<TreeItemViewable>> supportedCycloAlkins = FXCollections.observableArrayList();
        HydroCarbonGroup cycloAlkins = new HydroCarbonGroup(DataLoader.groupsData.CycloAlkinsData.cycloalkinsLabel, DataLoader.groupsData.CycloAlkinsData.cycloalkinsInfo, DataLoader.groupsData.CycloAlkinsData.cycloalkinsAttri, DataLoader.groupsData.CycloAlkinsData.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> cycloAlkinsItem = new TreeItem<TreeItemViewable>(cycloAlkins);

        for (HydroCarbon hc : DataLoader.groupsData.CycloAlkinsData.cycloalkins) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            cycloAlkinsItem.getChildren().add(hcitem);
            supportedCycloAlkins.add(hcitem);

        }
        cycloAlkinsItem.setExpanded(true);
        cycloAlkins.setSupportedGroup(supportedCycloAlkins);
        rootItem.getChildren().add(cycloAlkinsItem);

        ObservableList<TreeItem<TreeItemViewable>> supportedAromatics = FXCollections.observableArrayList();
        HydroCarbonGroup aromatics = new HydroCarbonGroup(DataLoader.groupsData.Aromatics.aromaticsLabel, DataLoader.groupsData.Aromatics.aromaticsInfo, DataLoader.groupsData.Aromatics.aromaticsAttri, DataLoader.groupsData.Aromatics.notSupported, treeMain.getSelectionModel());
        TreeItem<TreeItemViewable> aromaticsItems = new TreeItem<TreeItemViewable>(aromatics);

        for (HydroCarbon hc : DataLoader.groupsData.Aromatics.aromatics) {
            TreeItem<TreeItemViewable> hcitem = new TreeItem<TreeItemViewable>(hc);
            aromaticsItems.getChildren().add(hcitem);
            supportedAromatics.add(hcitem);

        }
        aromaticsItems.setExpanded(true);
        aromatics.setSupportedGroup(supportedAromatics);
        rootItem.getChildren().add(aromaticsItems);
        groupsList.addAll(cycloAlkansItem, cycloAlkensItem, cycloAlkinsItem, aromaticsItems);
        try {
            setupSubFXMLParts();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        treeMain.getSelectionModel().select(rootItem);

    }

    private void handle(TreeItem<TreeItemViewable> newValue) {

        currentSelectedItem = newValue.getValue();
        nameProperty.set(newValue.valueProperty().get().toString());
        hideContent();
        if (newValue.getValue() instanceof HydroCarbon) {
            handleHydroCarbon((HydroCarbon) newValue.getValue());
        } else {
            handleInfoTAB(newValue.getValue());
        }

    }

    private void handleInfoTAB(TreeItemViewable tr) {

        if (tr instanceof HydroCarbonsBase) {
            scrollPane.setContent(vboxHcs);

        } else {
            groupController.setGroup((HydroCarbonGroup) tr);
            scrollPane.setContent(vboxGroup);

        }

    }

    private void addHCButtons() {

        hboxButtons.setManaged(true);
        hboxButtons.setVisible(true);

    }

    private void hideContent() {
        hboxButtons.setManaged(false);
        hboxButtons.setVisible(false);
        scrollPane.setContent(null);
    }

    private void handleHydroCarbon(HydroCarbon hydroCarbon) {
        addHCButtons();
        hcController.setHC(hydroCarbon);
        scrollPane.setContent(vboxHC);

    }

    private void setupTextField() {
        ObjectExpression<Font> fontTracking = Bindings.createObjectBinding(() -> Font.font(tfName.getWidth() / 15), tfName.widthProperty());
//        nameText.fontProperty().bind(fontTracking);

        FontSizeBinder.bindFontSizeAll(fontTracking, nameText);

        nameText.textProperty().bind(nameProperty);
        tfName.setTextAlignment(TextAlignment.CENTER);
        tfName.getChildren().add(nameText);

    }

    private void setupSubFXMLParts() throws IOException {
        groupController = new GroupController();
        fxGroup = new FXMLLoader(getClass().getResource("GroupView.fxml"));
        fxGroup.setController(groupController);
        vboxGroup = fxGroup.load();

        hcsController = new HCsController(scrollPane.widthProperty(), scrollPane.heightProperty(), groupsList, treeMain.getSelectionModel());
        fxHcs.setController(hcsController);
        vboxHcs = fxHcs.load();

        hcController = new HCViewController();
        fxHC.setController(hcController);
        vboxHC = fxHC.load();

    }

    private void viewFormulas() throws IOException {
        HydroCarbon newHC = new HydroCarbon((HydroCarbon) currentSelectedItem);

        new FormulaWindowEntryPoint().start(newHC);
    }

    private void view3d() throws Exception {
        HydroCarbon newHC = new HydroCarbon((HydroCarbon) currentSelectedItem);
        new ModelWindowEntryPoint(newHC).start();

    }

}
