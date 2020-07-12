/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import bakalarka.HCgroup.TreeItemViewable;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;

/**
 *
 * @author MarekPC
 */
public class TreeLink extends Text {

    public TreeItem<TreeItemViewable> link;

    public TreeLink(TreeItem<TreeItemViewable> plink, String url) {
        super(url);
        link = plink;
    }
}
