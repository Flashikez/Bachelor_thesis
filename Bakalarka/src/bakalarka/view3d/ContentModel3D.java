/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

;

import bakalarka.Utilities.dataPack.DataLoader;
import chemicals.CarbonBase;
import chemicals.HydroCarbon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author MarekPC
 */


public class ContentModel3D {

    private static final double CARBON_KALOT_SIZE = 60;
    private static final double HYDROGEN_KALOT_SIZE = 40;

    private static final double CARBON_BAS_SIZE = 30;
    private static final double HYDROGEN_BAS_SIZE = 25;
    private static final double CYLINDER_BAS_RADIUS = 5;
    private static final double DISTANCE_BETWEEN_BAS_ATOMS = 30;

    private static final double CAMERA_INITIAL_DISTANCE = -1200;
    private static final double CAMERA_INITIAL_X_ANGLE = 60.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 100.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private static final double SCROLL_MODIFIER = 0.8;
    final Xform root = new Xform();

    final Xform axisGroup = new Xform();

    final Xform moleculeGroup = new Xform();

    final Xform world = new Xform();

    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    private HydroCarbon hc;
    private StringProperty title;
    private SubScene subScene;
    private Xform group3d = new Xform();
    private Group axis = new Group();

    private Model3D kalotModel;
    private Model3D ballandstickModel;
    private Model3D stickModel;
    private Model3D[] modelsArray;

    private SimpleObjectProperty<Color> carbonColorProperty = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Color> hydrogenColorProperty = new SimpleObjectProperty<>();
    private AmbientLight ambientLight = new AmbientLight(Color.CORAL);
    private SimpleObjectProperty<Color> boundsColorProperty = new SimpleObjectProperty<>();
    private SimpleBooleanProperty ambientLightProperty = new SimpleBooleanProperty() {
        @Override
        public void invalidated() {
            if (get()) {
                group3d.getChildren().add(ambientLight);
            } else {
                group3d.getChildren().remove(ambientLight);
            }
        }
    };
    private SimpleIntegerProperty selectedModelProperty = new SimpleIntegerProperty() {
        @Override
        public void invalidated() {
            group3d.getChildren().clear();
            group3d.getChildren().addAll(modelsArray[get()]);

        }
    };

    public ContentModel3D(HydroCarbon phc, StringProperty tit) {
        hc = phc;
        title = tit;
        title.set("3D model viewer " + hc.getName());
        kalotModel = new Model3D(DataLoader.ModelsData.kalotInfo, DataLoader.ModelsData.kalotModelLabel);
        ballandstickModel = new Model3D(DataLoader.ModelsData.basInfo, DataLoader.ModelsData.basModelLabel);
        stickModel = new Model3D(DataLoader.ModelsData.stickInfo, DataLoader.ModelsData.stickModelLabel);
        modelsArray = new Model3D[3];
        buildCamera();
        buildAxis();

        buildModels();
        buildScene();
        handleMouse(subScene);
        handleScroll(subScene);

    }

    public HydroCarbon getHc() {
        return hc;
    }

    public SubScene getsubScene() {
        return subScene;
    }

    private void buildScene() {
        root.getChildren().add(world);
        subScene = new SubScene(root, 500, 500, true, javafx.scene.SceneAntialiasing.BALANCED);

        subScene.setCamera(camera);
    }

    private void buildAxis() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        //world.getChildren().add(axisGroup);  Odkomentuj pre zobrazenie x,y,z osí na plátne
        world.getChildren().add(group3d);
    }

    public void bindProperty(ObjectProperty<Color> property, String what) {
        if (what.matches("background")) {
            subScene.fillProperty().bind(property);
        } else if (what.matches("carbon")) {
            carbonColorProperty.bind(property);
        } else if (what.matches("hydrogen")) {
            hydrogenColorProperty.bind(property);
        } else if (what.matches("bounds")) {
            boundsColorProperty.bind(property);

        }
    }

    public SimpleBooleanProperty getAmbientLightProperty() {
        return ambientLightProperty;
    }

    public SimpleIntegerProperty getSelectedModelProperty() {
        return selectedModelProperty;
    }

    public String getModelInfoAtIndex(int modelsIndex) {
        return modelsArray[modelsIndex].getModelInfo();
    }

    public String getModelLabelAtindex(int modelsIndex) {
        return modelsArray[modelsIndex].getModelLabel();
    }

    private void buildModels() {
        // Kalotovy model

        CarbonBase[] arr = hc.getStructureArr();

        PhongMaterial carbonMaterial = new PhongMaterial();
        carbonMaterial.diffuseColorProperty().bind(carbonColorProperty);
        PhongMaterial hydrogenMaterial = new PhongMaterial();
        hydrogenMaterial.diffuseColorProperty().bind(hydrogenColorProperty);
        PhongMaterial multBoundMaterial = new PhongMaterial();
        multBoundMaterial.diffuseColorProperty().bind(boundsColorProperty);

        if (!hc.isIsCyclo()) {
            LinearStructureModeler.modelLinearKalot(arr, carbonMaterial, hydrogenMaterial, CARBON_KALOT_SIZE, HYDROGEN_KALOT_SIZE, kalotModel);
            LinearStructureModeler.modelLinearBallandStick(arr, carbonMaterial, hydrogenMaterial, hydrogenMaterial, CARBON_BAS_SIZE, HYDROGEN_BAS_SIZE, DISTANCE_BETWEEN_BAS_ATOMS, CYLINDER_BAS_RADIUS, ballandstickModel);
            LinearStructureModeler.buildStickModel(arr, carbonMaterial, hydrogenMaterial, DISTANCE_BETWEEN_BAS_ATOMS, CYLINDER_BAS_RADIUS, stickModel);
        } else {

            CyclicStructureModeler.modelCyclicKalot(hc, carbonMaterial, hydrogenMaterial, CARBON_KALOT_SIZE, HYDROGEN_KALOT_SIZE, kalotModel);

            CyclicStructureModeler.modelCyclicBallandStick(hc, carbonMaterial, hydrogenMaterial, CARBON_BAS_SIZE, HYDROGEN_BAS_SIZE, DISTANCE_BETWEEN_BAS_ATOMS, CYLINDER_BAS_RADIUS, ballandstickModel);
            CyclicStructureModeler.modelCyclicStick(hc, carbonMaterial, hydrogenMaterial, DISTANCE_BETWEEN_BAS_ATOMS, CYLINDER_BAS_RADIUS, stickModel);
        }

        modelsArray[0] = kalotModel;
        modelsArray[1] = ballandstickModel;
        modelsArray[2] = stickModel;
    }

    private void handleMouse(SubScene scene) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isPrimaryButtonDown()) {
                    cameraXform.rotationAroundY.setAngle(cameraXform.rotationAroundY.getAngle() - mouseDeltaX * MOUSE_SPEED * modifier * ROTATION_SPEED);
                    cameraXform.rotationAroundX.setAngle(cameraXform.rotationAroundX.getAngle() + mouseDeltaY * MOUSE_SPEED * modifier * ROTATION_SPEED);
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.mainTranslation.setX(cameraXform2.mainTranslation.getX() + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
                    cameraXform2.mainTranslation.setY(cameraXform2.mainTranslation.getY() + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
                }
            }
        });
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.rotationAroundY.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rotationAroundX.setAngle(CAMERA_INITIAL_X_ANGLE);

    }

    private void handleScroll(SubScene subScene) {
        subScene.setOnScroll(e -> {
            if (e.getTouchCount() > 0) { // scroll na touchpade
                cameraXform2.mainTranslation.setX(cameraXform2.mainTranslation.getX() - (0.01 * e.getDeltaX()));
                cameraXform2.mainTranslation.setY(cameraXform2.mainTranslation.getY() + (0.01 * e.getDeltaY()));
            } else {
                double z = camera.getTranslateZ() - (e.getDeltaY() * SCROLL_MODIFIER);

                camera.setTranslateZ(z);
            }

        });

    }

    public void rebuildModels() {
        kalotModel.getChildren().clear();
        ballandstickModel.getChildren().clear();
        stickModel.getChildren().clear();
        buildModels();
    }

}
