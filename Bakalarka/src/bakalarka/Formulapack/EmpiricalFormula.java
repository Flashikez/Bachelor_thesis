/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import chemicals.HydroCarbon;
import bakalarka.Utilities.SubscriptHelper;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author MarekPC
 */
public class EmpiricalFormula extends Formula {

    private int[] ratio;

    public EmpiricalFormula(HydroCarbon phc, String plabel, String pinfo) {
        super(phc, plabel, pinfo);
        ratio = new int[]{phc.getcNumber(), phc.gethNumber()};
        reduceRatio();
    }

    @Override
    public String toString() {
        return "C" + SubscriptHelper.toSubscript(ratio[0]) + "H" + SubscriptHelper.toSubscript(ratio[1]);
    }

    @Override
    public void drawFormula(GraphicsContext gc, double width, double height) {
        gc.fillText(this.toString(), Math.round(width / 2), Math.round(height / 2));
    }

    private int findGCM(int a, int b) { // najvacsi spolocny delitel
        return b == 0 ? a : findGCM(b, a % b);
    }

    private void reduceRatio() {
        int GCM = findGCM(ratio[0], ratio[1]);
        ratio[0] /= GCM;
        ratio[1] /= GCM;

    }

}
