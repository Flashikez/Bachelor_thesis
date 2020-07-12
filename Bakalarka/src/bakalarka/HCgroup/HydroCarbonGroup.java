/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.HCgroup;

import bakalarka.TreeLink;
import bakalarka.Utilities.Styler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;

/**
 *
 * @author MarekPC
 */
public class HydroCarbonGroup implements TreeItemViewable {

    private String nameOfGroup;
    private String attri;
    private String[] notSupported;
    private String info;
    private ObservableList<TreeItem<TreeItemViewable>> supportedGroup;
    private ObservableList<TreeLink> links = FXCollections.observableArrayList();
    private MultipleSelectionModel<TreeItem<TreeItemViewable>> selectionModel;

    public HydroCarbonGroup(String pnameOfGroup, String pinfo, String pattri, String[] notSupportedCHS, MultipleSelectionModel<TreeItem<TreeItemViewable>> pselectionModel) {

        attri = pattri;
        notSupported = notSupportedCHS;
        nameOfGroup = pnameOfGroup;
        info = pinfo;
        selectionModel = pselectionModel;

    }

    public String getAttri() {
        return attri;
    }

    public String[] getNotSupported() {
        return notSupported;
    }

    public void setSupportedGroup(ObservableList<TreeItem<TreeItemViewable>> supported) {
        supportedGroup = supported;
        makeLinks();

    }

    // public abstract void loadGroup(String fileName); 
    public ObservableList<TreeItem<TreeItemViewable>> getSupported() {
        return supportedGroup;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return nameOfGroup;
    }

    public ObservableList<TreeLink> getLinks() {
        return links;
    }

    private void makeLinks() {
        for (TreeItem<TreeItemViewable> treeItem : supportedGroup) {
            TreeLink link = new TreeLink(treeItem, treeItem.getValue().toString());
            Styler.styleAsTreeLink(link);
            link.onMouseClickedProperty().set(e -> {
                handleHClink(link);
            });
            links.add(link);
        }
    }

    private void handleHClink(TreeLink link) {
        selectionModel.select(link.link);

    }

}
