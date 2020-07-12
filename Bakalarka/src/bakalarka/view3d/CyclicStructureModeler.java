/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import bakalarka.Utilities.GeometryHelper;
import chemicals.CarbonBase;
import chemicals.HydroCarbon;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 *
 * @author MarekPC
 */
/*
    Trieda modeluje všetky cyklické štruktúry, ktoré nadobúdajú vybrané uhľovodíky
    
    Modelovanie delíme na 3 druhy:
    1. Modelovanie planárnych štruktúr - štruktúry, ktoré sú umiestnené v jednej rovine , napr. benzén, ale v aplikácií modelujeme pomoocu tohto druhu aj propán
    2. Modelovanie štruktúr v rozložení CHAIR - štuktúry, ktoré majú polovicu uhlíkov v jednej rovine a druhú polovicu v druhej, v našej aplikácií len hexán
    3. Modelovanie štruktúr v rozložení HALF-CHAIR - 4 atómy v jednej rovine a 2 v druhej, v našej aplikácií cyklohexén a cyklohexýn


 */
public class CyclicStructureModeler {

    private static final double CUBE_SPACE_DIAGONAL = Math.sqrt(3); // Násobok strany kocky k priestorovej uhlopriečke kocky
    private static final double CUBE_SIDE_DIAGONAL = Math.sqrt(2); // Násobok strany kocky k uhlopriečke strany kocky

    public static void modelCyclicKalot(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double carbonKalotSize, double hydrogenKalotSize, Model3D kalotModel) {
        if (hc.isIsPlanar()) {
            modelPlanarKalot(hc, carbonMaterial, hydrogenMaterial, carbonKalotSize, hydrogenKalotSize, kalotModel);
            return;
        }
        boolean hasPlanarComponent = (hc.getBoundMultiple() == 3 || hc.getBoundMultiple() == 2);
        CarbonBase[] arr = hc.getStructureArr();
        int cyclePeriod = hc.getCyclicShape().getNumOfCorners() / 2;
        boolean flip = false;
        Point3D carbonOrigin = new Point3D(0, 0, 0);
        Point3D carbonOriginCubeSigns = new Point3D(-1, -1, -1);
        boolean hasNext;
        Point3D[] cubeCorners;
        double cubeSideLength = carbonKalotSize;
        int startIndex;
        int index = 0;
        for (CarbonBase carbon : arr) {

            Sphere carbonSphere = new Sphere(carbonKalotSize);
            carbonSphere.setMaterial(carbonMaterial);
            kalotModel.getChildren().add(carbonSphere);
            hasNext = carbon.hasNext();
            carbonSphere.getTransforms().add(new Translate(carbonOrigin.getX(), carbonOrigin.getY(), carbonOrigin.getZ()));

            if (hasPlanarComponent) {
                cubeCorners = calculateCornersCyclicWithLinearComponent(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            } else {
                cubeCorners = calculateCornersCyclic(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            }
            startIndex = 0;
            if (hasNext) {
                startIndex = 1;
            }

            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {
                Sphere hydrogenSphere = new Sphere(hydrogenKalotSize);

                cubeCorners[i] = calculateActualCorners(cubeCorners[i], carbonOrigin, cubeSideLength / 2, flip);

                hydrogenSphere.setMaterial(hydrogenMaterial);
                hydrogenSphere.getTransforms().add(new Translate(cubeCorners[i].getX(), cubeCorners[i].getY(), cubeCorners[i].getZ()));
                kalotModel.getChildren().add(hydrogenSphere);

            }
            carbonOriginCubeSigns = cubeCorners[0];
            // Pri cyklických štruktúrach v modelovaní kalótového modelu nezachytávame násobnosť väzieb, pretože by model vyzeral zle
            carbonOrigin = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
            flip = !flip;

            index++;

        }

    }

    public static void modelCyclicStick(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D stickModel) {
        if (hc.isIsPlanar()) {
            modelPlanarStick(hc, carbonMaterial, hydrogenMaterial, distanceBetweenBaSpoints, cylinderBaSradius, stickModel);
            return;
        }
        PhongMaterial doubleBoundM = new PhongMaterial(LinearStructureModeler.StickModelcolor2Bound);
        PhongMaterial tripleBoundM = new PhongMaterial(LinearStructureModeler.StickModelcolor3Bound);
        boolean hasPlanarComponent = (hc.getBoundMultiple() == 3 || hc.getBoundMultiple() == 2);
        CarbonBase[] arr = hc.getStructureArr();
        int cyclePeriod = hc.getCyclicShape().getNumOfCorners() / 2;
        boolean flip = false;
        Point3D carbonOrigin = new Point3D(0, 0, 0);
        Point3D carbonOriginCubeSigns = new Point3D(-1, -1, -1);
        boolean hasNext;
        Point3D[] cubeCorners;
        double cubeSideLength = distanceBetweenBaSpoints * 3;
        int startIndex;
        int index = 0;
        for (CarbonBase carbon : arr) {

            hasNext = carbon.hasNext();

            if (hasPlanarComponent) {
                cubeCorners = calculateCornersCyclicWithLinearComponent(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            } else {
                cubeCorners = calculateCornersCyclic(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            }

            if (hasNext) {
                startIndex = 1;
            } else {
                startIndex = 0;
            }
            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {

                cubeCorners[i] = calculateActualCorners(cubeCorners[i], carbonOrigin, cubeSideLength, flip);

                //--------------------------------------------------------------
                Point3D midPoint = cubeCorners[i].midpoint(carbonOrigin); //
                Node FirstboundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, midPoint, cylinderBaSradius);//
                ((Cylinder) FirstboundCylinder).setMaterial(carbonMaterial);//
                stickModel.getChildren().add(FirstboundCylinder);//
                Node secondBoundCylinder = getCylinderBetweenTwoPoints(midPoint, cubeCorners[i], cylinderBaSradius);//
                ((Cylinder) secondBoundCylinder).setMaterial(hydrogenMaterial);//
                stickModel.getChildren().add(secondBoundCylinder);//
                //--------------------------------------------------------------

            }
            carbonOriginCubeSigns = cubeCorners[0];

            Point3D newPointB = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
            Node boundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, newPointB, cylinderBaSradius);

            switch (carbon.getRightBounds()) {
                case 1:
                    ((Cylinder) boundCylinder).setMaterial(carbonMaterial);
                    break;
                case 2:
                    ((Cylinder) boundCylinder).setMaterial(doubleBoundM);
                    break;
                case 3:
                    ((Cylinder) boundCylinder).setMaterial(tripleBoundM);
                    break;

            }

            stickModel.getChildren().add(boundCylinder);

            if (hasPlanarComponent && carbon.getIsCyclicEndpoint()) { // Posledný atóm uhlíku spájame s prvým pri konformácií HALF-CHAIR
                stickModel.getChildren().remove(boundCylinder);
                Cylinder cylinder = (Cylinder) getCylinderBetweenTwoPoints(carbonOrigin, new Point3D(0, 0, 0), cylinderBaSradius);
                cylinder.setMaterial(carbonMaterial);
                stickModel.getChildren().add(cylinder);

            }

            carbonOrigin = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);

            flip = !flip;

            index++;

        }

    }
    // Metóda počíta znamienka rohov kocky pri konformácií CHAIR ,  v tejto konformácií môžeme využiť symetriu index%period, kde perióda je polovica konformácie 

    private static Point3D[] calculateCornersCyclic(double cubeSideLength, Point3D centerPoint, Point3D centerSigns, HydroCarbon.subMoleculeShape shape, boolean flip, int index, int period) {
        Point3D[] retarr = new Point3D[4];
        double halfSide = cubeSideLength;
        double cX = centerPoint.getX();
        double cY = centerPoint.getY();
        double cZ = centerPoint.getZ();

        double pX = centerSigns.getX();
        double pY = centerSigns.getY();
        double pZ = centerSigns.getZ();

        switch (shape) {
            case LINEAR:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(pX, pY, pZ);
                break;
            case TRIGONAL:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(cX, cY, cZ < 0 ? cZ - ((halfSide / Math.sqrt(3)) / 2) - halfSide / 2 : cZ + ((halfSide / Math.sqrt(3)) / 2) + halfSide / 2);
                retarr[2] = new Point3D(pX, pY, pZ);

                break;
            case TETRA:
                switch (index % period) {
                    case 0:
                        retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                        retarr[1] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;
                    case 1:
                        retarr[0] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[1] = new Point3D(pX * (-1), pY * (-1), pZ);

                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                    case 2:
                        retarr[0] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[1] = new Point3D(pX * (-1), pY * (-1), pZ);
                        retarr[2] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;
                    case 3:
                        retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                        retarr[1] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                    case 4:
                        retarr[0] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[1] = new Point3D(pX * (-1), pY * (-1), pZ);

                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);

                        break;
                    case 5:
                        retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                        retarr[1] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                }

            default:
                break;
        }
        return retarr;
    }

    public static void modelCyclicBallandStick(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double carbonBaSsize, double hydrogenBaSsize, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D basModel) {
        if (hc.isIsPlanar()) {
            modelPlanarBaS(hc, carbonMaterial, hydrogenMaterial, carbonBaSsize, hydrogenBaSsize, distanceBetweenBaSpoints, cylinderBaSradius, basModel);
            return;
        }

        boolean hasPlanarComponent = (hc.getBoundMultiple() == 3 || hc.getBoundMultiple() == 2); // HALF-CHAIR CONFORMATION pre cyklohexene a cyklohexyne
        CarbonBase[] arr = hc.getStructureArr();
        int cyclePeriod = hc.getCyclicShape().getNumOfCorners() / 2;
        boolean flip = false;
        Point3D carbonOrigin = new Point3D(0, 0, 0);
        Point3D carbonOriginCubeSigns = new Point3D(-1, -1, -1);
        boolean hasNext;
        Point3D[] cubeCorners;
        double cubeSideLength = distanceBetweenBaSpoints * 3;

        int startIndex;
        int index = 0;
        for (CarbonBase carbon : arr) {

            Sphere carbonSphere = new Sphere(carbonBaSsize);
            carbonSphere.setMaterial(carbonMaterial);

            basModel.getChildren().add(carbonSphere);

            hasNext = carbon.hasNext();
            carbonSphere.getTransforms().add(new Translate(carbonOrigin.getX(), carbonOrigin.getY(), carbonOrigin.getZ()));

            // System.out.println("\nPLACING CARBON AT: " + "X = " + carbonOrigin.getX() + "Y = " + carbonOrigin.getY() + "Z = " + carbonOrigin.getZ() + "\n");
            if (hasPlanarComponent) {
                cubeCorners = calculateCornersCyclicWithLinearComponent(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            } else {
                cubeCorners = calculateCornersCyclic(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape(), flip, index, cyclePeriod);
            }
            startIndex = 0;
            if (hasNext) {
                startIndex = 1;
            }

            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {
                Sphere hydrogenSphere = new Sphere(hydrogenBaSsize);
                hydrogenSphere.setMaterial(hydrogenMaterial);

                cubeCorners[i] = calculateActualCorners(cubeCorners[i], carbonOrigin, cubeSideLength, flip);

                //---------------------------- Väzba uhlík -  vodík ----------------------------------
                Point3D midPoint = cubeCorners[i].midpoint(carbonOrigin); //
                Node FirstboundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, midPoint, cylinderBaSradius);//
                ((Cylinder) FirstboundCylinder).setMaterial(carbonMaterial);//

                basModel.getChildren().add(FirstboundCylinder);//

                Node secondBoundCylinder = getCylinderBetweenTwoPoints(midPoint, cubeCorners[i], cylinderBaSradius);//
                ((Cylinder) secondBoundCylinder).setMaterial(hydrogenMaterial);//

                basModel.getChildren().add(secondBoundCylinder);//
                //--------------------------------------------------------------

                hydrogenSphere.getTransforms().add(new Translate(cubeCorners[i].getX(), cubeCorners[i].getY(), cubeCorners[i].getZ()));

                basModel.getChildren().add(hydrogenSphere);

            }
            carbonOriginCubeSigns = cubeCorners[0];

            // Pridanie väzby/väzieb medzi atómy uhlíku
            double offsetZ = carbon.getRightBounds() == 2 ? -15 : 0;
            for (int i = 0; i < carbon.getRightBounds(); i++) {
                switch (i) {
                    case 1:
                        offsetZ = 15;
                        break;
                    case 2:
                        offsetZ = -15;
                        break;

                }

                Point3D newOrigin = new Point3D(carbonOrigin.getX(), carbonOrigin.getY(), carbonOrigin.getZ() + offsetZ);
                Point3D newPointB = calculateActualCorners(carbonOriginCubeSigns, newOrigin, cubeSideLength, flip);

                Node boundCylinder = getCylinderBetweenTwoPoints(newOrigin, newPointB, cylinderBaSradius);
                ((Cylinder) boundCylinder).setMaterial(carbonMaterial);

                basModel.getChildren().add(boundCylinder);

                if (hasPlanarComponent && carbon.getIsCyclicEndpoint()) { // Posledný atóm uhlíku spájame s prvým pri konformácií HALF-CHAIR
                    basModel.getChildren().remove(boundCylinder);
                    Cylinder cylinder = (Cylinder) getCylinderBetweenTwoPoints(carbonOrigin, new Point3D(0, 0, 0), cylinderBaSradius);
                    cylinder.setMaterial(carbonMaterial);
                    basModel.getChildren().add(cylinder);

                }

            }

            carbonOrigin = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
            flip = !flip;

            index++;

        }

    }

    private static Point3D[] calculateCornersCyclicWithLinearComponent(double cubeSideLength, Point3D centerPoint, Point3D centerSigns, HydroCarbon.subMoleculeShape shape, boolean flip, int index, int period) {
        Point3D[] retarr = new Point3D[4];

        double pX = (centerSigns.getX() < 0) ? -1 : 1;
        double pY = (centerSigns.getY() < 0) ? -1 : 1;
        double pZ = (centerSigns.getZ() < 0) ? -1 : 1;

        switch (shape) {
            case LINEAR:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(pX, pY, pZ);
                break;

            case TRIGONAL:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(-1, -1, -1);

                if (flip) {
                    retarr[1] = new Point3D(pX * 0, pY * 0, pZ * -CUBE_SPACE_DIAGONAL);
                }

                retarr[2] = new Point3D(pX, pY, pZ);
                break;
            case TETRA:
                switch (index) {

                    case 1:
                        retarr[0] = new Point3D(pX, pY * (-1), pZ * (-1));
                        retarr[1] = new Point3D(pX * (-1), pY * (-1), pZ);

                        retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                    case 2:
                        retarr[0] = new Point3D(pX * 0, pY * 1, pZ * (-CUBE_SIDE_DIAGONAL));
                        retarr[1] = new Point3D(pX * 0, pY * -CUBE_SPACE_DIAGONAL, pZ * 0);
                        retarr[2] = new Point3D(pX * -CUBE_SIDE_DIAGONAL, pY * 0, pZ * 1);

                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;
                    case 3:

                        retarr[0] = new Point3D(pX * CUBE_SIDE_DIAGONAL, pY * 0, pZ * -1);

                        retarr[1] = new Point3D(pX * (-1), pY * 1, pZ * -1);
                        retarr[2] = new Point3D(pX * (-1), pY * (-CUBE_SIDE_DIAGONAL), pZ * (0));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                    case 4:
                        retarr[0] = new Point3D(pX * -0.8, pY * -1, pZ * 0.8);
                        retarr[1] = new Point3D(pX * 0, pY * (-0.8), pZ * -CUBE_SIDE_DIAGONAL);
                        retarr[2] = new Point3D(pX * (-0.8), pY * CUBE_SIDE_DIAGONAL, pZ * 0);
                        retarr[3] = new Point3D(pX, pY, pZ);

                        break;
                    case 5:
                        retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                        retarr[1] = new Point3D(pX * 0, pY * -CUBE_SIDE_DIAGONAL, pZ * 1);
                        retarr[2] = new Point3D(pX * -CUBE_SIDE_DIAGONAL, pY, pZ * (0));
                        retarr[3] = new Point3D(pX, pY, pZ);
                        break;

                }
        }

        return retarr;
    }

    // metóda na základe boolean flip vráti vypočítané súradnice konkrétneho rohu kocky na základe znamienok tohto rohu (signes) a na základe stredu kocky(originPoint), kde vzdialenosť rohu od stredu kocky je scaleValue
    private static Point3D calculateActualCorners(Point3D signes, Point3D originPoint, double scaleValue, boolean flip) {
        Point3D returnPoint;
        double signX = signes.getX();
        double signY = signes.getY();
        double signZ = signes.getZ();

        double originX = originPoint.getX();
        double originY = originPoint.getY();
        double originZ = originPoint.getZ();

        if (flip) {
            returnPoint = new Point3D(originX - (signX * scaleValue), originY - (signY * scaleValue), originZ - (signZ * scaleValue));
        } else {

            returnPoint = new Point3D(originX + (signX * scaleValue), originY + (signY * scaleValue), originZ + (signZ * scaleValue));
        }
        return returnPoint;

    }

    // metóda vracia Cylinder ( valec ), ktorým modelujeme väzby v ball and stick modely, valec je umiestení medzi dvoma bodmi Point3D pointA, Point3D pointB a má polomer cylinderRadius
    private static Node getCylinderBetweenTwoPoints(Point3D pointA, Point3D pointB, double cylinderRadius) { // move cylinder to midPoint , then rotate
        Point3D differencePoint = pointB.subtract(pointA);
        Point3D yAxis = new Point3D(0, 1, 0);
        double cylinderLength = differencePoint.magnitude();

        Point3D midPoint = pointB.midpoint(pointA);
        Point3D rotationAxis = differencePoint.crossProduct(yAxis);

        double rotationAngle = Math.acos(differencePoint.normalize().dotProduct(yAxis));
        Cylinder cylinder = new Cylinder(cylinderRadius, cylinderLength);
        cylinder.getTransforms().addAll(new Translate(midPoint.getX(), midPoint.getY(), midPoint.getZ()), new Rotate(-Math.toDegrees(rotationAngle), rotationAxis));
        return cylinder;

    }

    private static void modelPlanarKalot(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double carbonKalotSize, double hydrogenKalotSize, Model3D kalotModel) {
        Point3D[] xyOfCoreCarbons = GeometryHelper.calculateCyclicPoints3D(0, 0, carbonKalotSize / 2.5, hc.getCyclicShape());

        CarbonBase[] cArr = hc.getStructureArr();

        for (int i = 0; i < hc.getcNumber(); i++) {
            Sphere carbonSpeher = new Sphere(carbonKalotSize);
            carbonSpeher.setMaterial(carbonMaterial);
            carbonSpeher.getTransforms().add(new Translate(xyOfCoreCarbons[i].getX(), xyOfCoreCarbons[i].getY(), xyOfCoreCarbons[i].getZ()));
            kalotModel.getChildren().add(carbonSpeher);
            int previousNeighborIndex = (i == 0) ? hc.getcNumber() - 1 : i - 1;

            modelChildrenKalot(cArr[i].getOwnHnumber(), xyOfCoreCarbons[i], xyOfCoreCarbons[previousNeighborIndex], xyOfCoreCarbons[(i + 1) % hc.getcNumber()], hydrogenKalotSize, hydrogenMaterial, kalotModel);

        }

    }

    // metóda modeluje v kalótovom modely susediace vodíky pre jeden atóm uhlíku v planárnej cyklickej štruktúre
    private static void modelChildrenKalot(int numOfChildren, Point3D centerPoint, Point3D firstNeighborPoint, Point3D secondNeighborPoint, double hydrogenKalotSize, PhongMaterial hydrogenMaterial, Model3D kalotModel) {
        switch (numOfChildren) {
            // jeden susediaci vodík znamená, že sa musí nachádzať v jednej rovine s ostatnými atómami, v našej aplikácií len benzén, stačí rotácia o 120 stupnňov , ak cheme dosiahnuť rovnostranný trojuhplník
            case 1:
                Sphere hydrogenSphere = new Sphere(hydrogenKalotSize);
                hydrogenSphere.setMaterial(hydrogenMaterial);

                kalotModel.getChildren().add(hydrogenSphere);

                hydrogenSphere.getTransforms().add(new Translate(secondNeighborPoint.getX(), secondNeighborPoint.getY(), secondNeighborPoint.getZ()));
                hydrogenSphere.getTransforms().add(new Rotate(-120, firstNeighborPoint.getX(), firstNeighborPoint.getY(), firstNeighborPoint.getZ(), Rotate.Z_AXIS));

                break;
            // dva susediace vodíky, v našej aplikácií propán
            case 2:
                for (int j = 0; j < 2; j++) {
                    hydrogenSphere = new Sphere(hydrogenKalotSize);

                    hydrogenSphere.setMaterial(hydrogenMaterial);
                    Point3D pointBh = new Point3D(centerPoint.getX() * 1.5, centerPoint.getY() * 1.5, (j == 0) ? -hydrogenKalotSize : hydrogenKalotSize);

                    hydrogenSphere.getTransforms().add(new Translate(pointBh.getX(), pointBh.getY(), pointBh.getZ()));

                    kalotModel.getChildren().add(hydrogenSphere);
                }

                break;

            default:
                break;
        }

    }

    private static void modelPlanarBaS(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double carbonBaSsize, double hydrogenBaSsize, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D basModel) {
        Point3D[] xyOfCoreCarbons = GeometryHelper.calculateCyclicPoints3D(0, 0, distanceBetweenBaSpoints, hc.getCyclicShape());

        CarbonBase[] cArr = hc.getStructureArr();

        for (int i = 0; i < hc.getcNumber(); i++) {
            CarbonBase current = cArr[i];
            Sphere carbonSpeher = new Sphere(carbonBaSsize);
            Tooltip.install(carbonSpeher, new Tooltip("" + i));
            carbonSpeher.setMaterial(carbonMaterial);
            carbonSpeher.getTransforms().add(new Translate(xyOfCoreCarbons[i].getX(), xyOfCoreCarbons[i].getY(), xyOfCoreCarbons[i].getZ()));
            basModel.getChildren().add(carbonSpeher);
            int previousNeighborIndex = (i == 0) ? hc.getcNumber() - 1 : i - 1;

            modelChildrenBaS(cArr[i].getOwnHnumber(), xyOfCoreCarbons[i], xyOfCoreCarbons[previousNeighborIndex], xyOfCoreCarbons[(i + 1) % hc.getcNumber()], hydrogenBaSsize, carbonMaterial, hydrogenMaterial, distanceBetweenBaSpoints, cylinderBaSradius, basModel);
            double offset = 10;
            for (int j = 0; j < current.getRightBounds(); j++) {
                Point3D newOrigin;
                Point3D newB;

                if (i > hc.getcNumber() / 2) {
                    newOrigin = new Point3D(xyOfCoreCarbons[i].getX() - j * offset, xyOfCoreCarbons[i].getY() + j * offset, xyOfCoreCarbons[i].getZ());
                    newB = new Point3D(xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getX() - j * offset, xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getY() + j * offset, xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getZ());
                } else {
                    newOrigin = new Point3D(xyOfCoreCarbons[i].getX() + j * offset, xyOfCoreCarbons[i].getY() + j * offset, xyOfCoreCarbons[i].getZ());
                    newB = new Point3D(xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getX() + j * offset, xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getY() + j * offset, xyOfCoreCarbons[(i + 1) % hc.getcNumber()].getZ());
                }

                Node boundCylinder = getCylinderBetweenTwoPoints(newOrigin, newB, cylinderBaSradius);
                ((Cylinder) boundCylinder).setMaterial(carbonMaterial);
                basModel.getChildren().add(boundCylinder);
            }

        }

    }

    private static void modelChildrenBaS(int numOfChildren, Point3D xyOfCoreCarbon, Point3D previous, Point3D next, double hydrogenSize, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D basModel) {
        switch (numOfChildren) {
            // BENZENE
            case 1:
                Sphere hydrogenSphere = new Sphere(hydrogenSize);
                hydrogenSphere.setMaterial(hydrogenMaterial);

                hydrogenSphere.getTransforms().add(new Translate(next.getX(), next.getY(), next.getZ()));
                hydrogenSphere.getTransforms().add(new Rotate(-120, previous.getX(), previous.getY(), previous.getZ(), Rotate.Z_AXIS));
                basModel.getChildren().add(hydrogenSphere);
                Point3D p = GeometryHelper.getCenterOfBounds(hydrogenSphere.getBoundsInParent());
                Point3D midPoint = p.midpoint(xyOfCoreCarbon);
                Cylinder c = (Cylinder) getCylinderBetweenTwoPoints(xyOfCoreCarbon, midPoint, cylinderBaSradius);
                c.setMaterial(carbonMaterial);
                basModel.getChildren().add(c);
                Cylinder c2 = (Cylinder) getCylinderBetweenTwoPoints(midPoint, p, cylinderBaSradius);
                c2.setMaterial(hydrogenMaterial);
                basModel.getChildren().add(c2);

                break;

            case 2:
                for (int j = 0; j < 2; j++) {
                    hydrogenSphere = new Sphere(hydrogenSize);

                    hydrogenSphere.setMaterial(hydrogenMaterial);
                    Point3D pointBh = new Point3D(xyOfCoreCarbon.getX() * 1.7, xyOfCoreCarbon.getY() * 1.7, (j == 0) ? -2 * distanceBetweenBaSpoints : 2 * distanceBetweenBaSpoints);
                    Point3D midPoint2 = xyOfCoreCarbon.midpoint(pointBh);
                    Cylinder c1 = (Cylinder) getCylinderBetweenTwoPoints(xyOfCoreCarbon, midPoint2, cylinderBaSradius);
                    c1.setMaterial(carbonMaterial);
                    Cylinder c2h = (Cylinder) getCylinderBetweenTwoPoints(midPoint2, pointBh, cylinderBaSradius);
                    c2h.setMaterial(hydrogenMaterial);
                    basModel.getChildren().addAll(c1, c2h);

                    hydrogenSphere.getTransforms().add(new Translate(pointBh.getX(), pointBh.getY(), pointBh.getZ()));

                    basModel.getChildren().add(hydrogenSphere);
                }
//                

                break;

            default:
                break;
        }

    }

    private static void modelPlanarStick(HydroCarbon hc, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D stickModel) {
        Point3D[] xyOfCoreCarbons = GeometryHelper.calculateCyclicPoints3D(0, 0, distanceBetweenBaSpoints, hc.getCyclicShape());
        CarbonBase[] cArr = hc.getStructureArr();
        PhongMaterial doubleBoundM = new PhongMaterial(LinearStructureModeler.StickModelcolor2Bound);
        PhongMaterial tripleBoundM = new PhongMaterial(LinearStructureModeler.StickModelcolor3Bound);
        for (int i = 0; i < hc.getcNumber(); i++) {
            CarbonBase current = cArr[i];

            int previousNeighborIndex = (i == 0) ? hc.getcNumber() - 1 : i - 1;

            modelChildrenStick(cArr[i].getOwnHnumber(), xyOfCoreCarbons[i], xyOfCoreCarbons[previousNeighborIndex], xyOfCoreCarbons[(i + 1) % hc.getcNumber()], carbonMaterial, hydrogenMaterial, distanceBetweenBaSpoints, cylinderBaSradius, stickModel);
            Node boundCylinder = getCylinderBetweenTwoPoints(xyOfCoreCarbons[i], xyOfCoreCarbons[(i + 1) % hc.getcNumber()], cylinderBaSradius);
            switch (current.getRightBounds()) {
                case 1:
                    ((Cylinder) boundCylinder).setMaterial(carbonMaterial);
                    break;
                case 2:
                    ((Cylinder) boundCylinder).setMaterial(doubleBoundM);
                    break;
                case 3:
                    ((Cylinder) boundCylinder).setMaterial(tripleBoundM);
                    break;

            }

            stickModel.getChildren().add(boundCylinder);
        }
    }

    private static void modelChildrenStick(int numOfChildren, Point3D xyOfCoreCarbon, Point3D previous, Point3D next, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double distanceBetweenBaSpoints, double cylinderBaSradius, Model3D stickModel) {
        switch (numOfChildren) {
            // 
            case 1:
                Sphere helpSphere = new Sphere(0);
                helpSphere.getTransforms().add(new Translate(next.getX(), next.getY(), next.getZ()));
                helpSphere.getTransforms().add(new Rotate(-120, previous.getX(), previous.getY(), previous.getZ(), Rotate.Z_AXIS));
                stickModel.getChildren().add(helpSphere);
                Point3D p = GeometryHelper.getCenterOfBounds(helpSphere.getBoundsInParent());
                Point3D midPoint = p.midpoint(xyOfCoreCarbon);
                Cylinder c = (Cylinder) getCylinderBetweenTwoPoints(xyOfCoreCarbon, midPoint, cylinderBaSradius);
                c.setMaterial(carbonMaterial);
                stickModel.getChildren().add(c);
                Cylinder c2 = (Cylinder) getCylinderBetweenTwoPoints(midPoint, p, cylinderBaSradius);
                c2.setMaterial(hydrogenMaterial);
                stickModel.getChildren().add(c2);

                break;

            case 2:
                for (int j = 0; j < 2; j++) {

                    Point3D pointBh = new Point3D(xyOfCoreCarbon.getX() * 1.7, xyOfCoreCarbon.getY() * 1.7, (j == 0) ? -2 * distanceBetweenBaSpoints : 2 * distanceBetweenBaSpoints);
                    Point3D midPoint2 = xyOfCoreCarbon.midpoint(pointBh);
                    Cylinder c1 = (Cylinder) getCylinderBetweenTwoPoints(xyOfCoreCarbon, midPoint2, cylinderBaSradius);
                    c1.setMaterial(carbonMaterial);
                    Cylinder c2h = (Cylinder) getCylinderBetweenTwoPoints(midPoint2, pointBh, cylinderBaSradius);
                    c2h.setMaterial(hydrogenMaterial);
                    stickModel.getChildren().addAll(c1, c2h);

                }

                break;

            default:
                break;
        }

    }

}
