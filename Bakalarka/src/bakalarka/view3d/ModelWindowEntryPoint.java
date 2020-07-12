/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import chemicals.HydroCarbon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author MarekPC
 */
public class ModelWindowEntryPoint {

    private HydroCarbon hc;

    public ModelWindowEntryPoint(HydroCarbon phc) throws Exception {
        hc = phc;

    }

    public void start() throws Exception {
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("window3d.fxml"));

        Stage stage2 = new Stage();
        ContentModel3D cm = new ContentModel3D(hc, stage2.titleProperty());
        ModelWindowMainController contr = new ModelWindowMainController(cm);

        fx.setController(contr);
        Scene scene = new Scene(fx.load(), 1480, 800);

        stage2.setScene(scene);
        stage2.show();

    }

}
