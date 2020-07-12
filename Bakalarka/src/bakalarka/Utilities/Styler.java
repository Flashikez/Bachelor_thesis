/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities;

import bakalarka.TreeLink;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author MarekPC
 */
public class Styler {

    public static void styleAsTooltip(Text... texts) {
        for (Text t : texts) {
            t.underlineProperty().set(true);
            t.fillProperty().set(Color.CHOCOLATE);

        }
    }

    public static void styleAsTreeLink(TreeLink... t) {
        for (TreeLink text : t) {
            text.underlineProperty().set(true);
            text.fillProperty().set(Color.BLUE);

        }
    }

}
