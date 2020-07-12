/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import javafx.scene.SubScene;
import javafx.scene.layout.Pane;

/**
 *
 * @author MarekPC
 */
public class ResizableSubScene extends Pane {

    private SubScene subScene;

    public ResizableSubScene(SubScene psub) {
        subScene = psub;
        setPrefSize(subScene.getWidth(), subScene.getHeight());
        getChildren().add(subScene);
        setMinSize(100, 100);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    }

    @Override
    protected void layoutChildren() {
        if (subScene != null) {
            subScene.setWidth(getWidth());
            subScene.setHeight(getHeight());
        }
    }

}
