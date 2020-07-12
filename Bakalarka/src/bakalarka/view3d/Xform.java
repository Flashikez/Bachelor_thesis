

package bakalarka.view3d;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;




public class Xform extends Group {



    public Translate mainTranslation  = new Translate(); 
    public Translate pivot  = new Translate(); 
    public Translate inversePivot = new Translate(); 
    public Rotate rotationAroundX = new Rotate();
    { rotationAroundX.setAxis(Rotate.X_AXIS); }
    public Rotate rotationAroundY = new Rotate();
    { rotationAroundY.setAxis(Rotate.Y_AXIS); }
    public Rotate rotationAroundZ = new Rotate();
    { rotationAroundZ.setAxis(Rotate.Z_AXIS); }
    public Scale scale = new Scale();

    public Xform() { 
        super(); 
        getTransforms().addAll(mainTranslation, rotationAroundZ, rotationAroundY, rotationAroundX, scale); 
    }


    public void setTranslate(double x, double y, double z) {
        mainTranslation.setX(x);
        mainTranslation.setY(y);
        mainTranslation.setZ(z);
    }

    public void setTranslate(double x, double y) {
        mainTranslation.setX(x);
        mainTranslation.setY(y);
    }

    public void setTranslationX(double x) { mainTranslation.setX(x); }
    public void setTranslationY(double y) { mainTranslation.setY(y); }
    public void setTranslationZ(double z) { mainTranslation.setZ(z); }

    public void setRotate(double x, double y, double z) {
        rotationAroundX.setAngle(x);
        rotationAroundY.setAngle(y);
        rotationAroundZ.setAngle(z);
    }

    public void setRotateX(double x) { rotationAroundX.setAngle(x); }
    public void setRotateY(double y) { rotationAroundY.setAngle(y); }
    public void setRotateZ(double z) { rotationAroundZ.setAngle(z); }








}
