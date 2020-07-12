/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import chemicals.HydroCarbon;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author MarekPC
 */
public class SummaryFormula extends Formula {

    public SummaryFormula(HydroCarbon phc, String plabel, String pinfo) {
        super(phc, plabel, pinfo);
    }

    @Override
    public void drawFormula(GraphicsContext gc, double width, double height) {

        gc.fillText(super.toString(), Math.round(width / 2), Math.round(height / 2));
    }

}
