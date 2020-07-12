/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import chemicals.HydroCarbon;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author MarekPC
 */
public class FormulaWindowEntryPoint {

    public void start(HydroCarbon hc) throws IOException {

        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("FormulaView.fxml"));

        Stage stage2 = new Stage();

        FormulaViewController contr = new FormulaViewController(hc);

        fx.setController(contr);
        Scene scene = new Scene(fx.load(), 1024, 600);
        stage2.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
            if (k.getCode() == KeyCode.SPACE) {
                contr.spacePressed();
                k.consume();
            }
        });
        stage2.titleProperty().set("Formula Viewer " + hc.getName());

        stage2.setScene(scene);
        stage2.show();

    }

}
