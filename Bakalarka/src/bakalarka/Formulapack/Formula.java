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
public abstract class Formula {
    protected HydroCarbon hc;
    protected String label;
    protected String formulaInfo;
    public  Formula(HydroCarbon phc,String plabel,String pinfo){
        hc = phc;
        label = plabel;
        formulaInfo = pinfo;
        
    }
        public abstract void drawFormula(GraphicsContext gc , double width, double height);
        public String getLabel(){
            return label;
        }
        public String getInfo(){
            return formulaInfo;
        }
        
        public void contentChanged(){};
              
        public String toString(){
            return "C" + SubscriptHelper.toSubscript(hc.getcNumber()) + "H" + SubscriptHelper.toSubscript(hc.gethNumber());
        }
    
        
    
    
    
}
