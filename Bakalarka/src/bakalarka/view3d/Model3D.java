/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

/**
 *
 * @author MarekPC
 */
public class Model3D extends Xform {

    private String modelInfo;
    private String modelLabel;

    public Model3D(String pInfo, String plabel) {
        super();
        modelInfo = pInfo;
        modelLabel = plabel;
    }

    public String getModelInfo() {
        return modelInfo;
    }

    public String getModelLabel() {
        return modelLabel;
    }

}
