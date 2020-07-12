/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.view3d;

import chemicals.CarbonBase;
import chemicals.HydroCarbon;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 *
 * @author MarekPC
 */
public class LinearStructureModeler {

    public final static Color StickModelcolor2Bound = Color.BLUE;
    public final static Color StickModelcolor3Bound = Color.YELLOW;

    public static void modelLinearKalot(CarbonBase[] carbonArray, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double carbonSize, double hydrogenSize, Model3D kalotModel) {
        double halfCarbon = carbonSize / 2;
        double cubeSideLength = carbonSize;
        Point3D carbonOrigin = new Point3D(0, 0, 0);

        Point3D[] corners = new Point3D[4];
        boolean hasNext = true;
        boolean flip = false;
        int startIndex = 0;
        Point3D carbonOriginSings = new Point3D(-1, -1, -1);

        int carNum = 0;
        for (CarbonBase carbon : carbonArray) {
            Sphere carbonSphere = new Sphere(carbonSize);
            carbonSphere.setMaterial(carbonMaterial);
            kalotModel.getChildren().add(carbonSphere);
            hasNext = carbon.hasNext();

            carbonSphere.getTransforms().add(new Translate(carbonOrigin.getX(), carbonOrigin.getY(), carbonOrigin.getZ()));
            Tooltip.install(carbonSphere, new Tooltip("X = " + carbonOrigin.getX() + "\nY = " + carbonOrigin.getY() + "\nZ = " + carbonOrigin.getZ() + "\n\n SIGNES: \nX " + carbonOriginSings.getX() + "\nY " + carbonOriginSings.getY() + "\nZ " + carbonOriginSings.getZ()));
//            System.out.println("\nPLACING CARBON AT: " + "X = " + carbonOrigin.getX() + "Y = " + carbonOrigin.getY() + "Z = " + carbonOrigin.getZ() + "\n");

            corners = calculateCornersLinear(cubeSideLength, carbonOrigin, carbonOriginSings, carbon.getShape());

            startIndex = 0;

            if (hasNext) {
                startIndex = 1;

            }
//     addBallAndStickComponent(carbon,carbonOrigin,corners,hydrogenMaterial,carbonMaterial,startIndex);
            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {

                Sphere hydrogenSphere = new Sphere(hydrogenSize);
                hydrogenSphere.setMaterial(hydrogenMaterial);

//                if(startIndex == 1)
                if (!(carbon.getShape() == HydroCarbon.subMoleculeShape.TRIGONAL && i == 1)) {
                    corners[i] = calculateActualCorners(corners[i], carbonOrigin, halfCarbon, flip);

                } else {
                    // System.out.println("KALOTMODEL TRIGONAL I == 1 BEFORE CALCULATE" + corners[i].toString());
                    double Zc;
                    if (flip) {
                        Zc = (carbonOrigin.getZ() - halfCarbon / carbon.getLeftBounds() < 0 ? carbonOrigin.getZ() - ((cubeSideLength / Math.sqrt(3)) / 2) - cubeSideLength / 2 : carbonOrigin.getZ() + ((cubeSideLength / Math.sqrt(3)) / 2) + cubeSideLength / 2);
                    } else {
                        Zc = (carbonOrigin.getZ() - halfCarbon / carbon.getLeftBounds() < 0 ? carbonOrigin.getZ() + ((cubeSideLength / Math.sqrt(3)) / 2) + cubeSideLength / 2 : carbonOrigin.getZ() - ((cubeSideLength / Math.sqrt(3)) / 2) - cubeSideLength / 2);
                    }
                    corners[i] = new Point3D(corners[i].getX(), corners[i].getY(), Zc);

                }
                hydrogenSphere.getTransforms().add(new Translate(corners[i].getX(), corners[i].getY(), corners[i].getZ()));
                Tooltip.install(hydrogenSphere, new Tooltip("X = " + corners[i].getX() + "\nY = " + corners[i].getY() + "\nZ = " + corners[i].getZ()));
//                System.out.println("PLACING HYDROGEN AT: " + "X = " + corners[i].getX() + "Y = " + corners[i].getY() + "Z = " + corners[i].getZ());
                kalotModel.getChildren().add(hydrogenSphere);
            }
            carbonOriginSings = corners[0];
            carbonOrigin = calculateActualCorners(carbonOriginSings, carbonOrigin, halfCarbon / carbon.getRightBounds(), flip);
            flip = !flip;

            carNum++;

        }

    }

    public static void modelLinearBallandStick(CarbonBase[] carbonArr, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, PhongMaterial cylinderMaterial, double carbonSize, double hydrogenSize, double basicDistance, double cylinderRadius, Model3D basModel) {

        boolean flip = false;
        Point3D carbonOrigin = new Point3D(0, 0, 0);
        Point3D carbonOriginCubeSigns = new Point3D(-1, -1, -1);
        boolean hasNext = true;
        Point3D[] cubeCorners;
        double cubeSideLength = basicDistance * 1.5;
        int startIndex;

        for (CarbonBase carbon : carbonArr) {
            Sphere carbonSphere = new Sphere(carbonSize);
            carbonSphere.setMaterial(carbonMaterial);
            basModel.getChildren().add(carbonSphere);
            hasNext = carbon.hasNext();
            carbonSphere.getTransforms().add(new Translate(carbonOrigin.getX(), carbonOrigin.getY(), carbonOrigin.getZ()));
            Tooltip.install(carbonSphere, new Tooltip("X = " + carbonOrigin.getX() + "\nY = " + carbonOrigin.getY() + "\nZ = " + carbonOrigin.getZ() + "\n\n SIGNES: \nX " + carbonOriginCubeSigns.getX() + "\nY " + carbonOriginCubeSigns.getY() + "\nZ " + carbonOriginCubeSigns.getZ()));
            // System.out.println("\nPLACING CARBON AT: " + "X = " + carbonOrigin.getX() + "Y = " + carbonOrigin.getY() + "Z = " + carbonOrigin.getZ() + "\n");
            cubeCorners = calculateCornersLinear(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape());
            startIndex = 0;
            if (hasNext) {
                startIndex = 1;
            }

            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {
                String tooltipText = "";
                Sphere hydrogenSphere = new Sphere(hydrogenSize);
                hydrogenSphere.setMaterial(hydrogenMaterial);
                tooltipText += "SIGNS\nX: " + cubeCorners[i].getX() + "\nY = " + cubeCorners[i].getY() + "\nZ = " + cubeCorners[i].getZ();
//                if(startIndex == 1)
                if (!(carbon.getShape() == HydroCarbon.subMoleculeShape.TRIGONAL && i == 1)) {
                    cubeCorners[i] = calculateActualCorners(cubeCorners[i], carbonOrigin, cubeSideLength, flip);
                } else {

                    cubeCorners[i] = new Point3D(cubeCorners[i].getX(), cubeCorners[i].getY(), cubeCorners[i].getZ() < 0 ? cubeCorners[i].getZ() - basicDistance : cubeCorners[i].getZ() + basicDistance);

                }
                Point3D midPoint = cubeCorners[i].midpoint(carbonOrigin);

                Node FirstboundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, midPoint, cylinderRadius);

                ((Cylinder) FirstboundCylinder).setMaterial(carbonMaterial);
                basModel.getChildren().add(FirstboundCylinder);

                Node secondBoundCylinder = getCylinderBetweenTwoPoints(midPoint, cubeCorners[i], cylinderRadius);

                ((Cylinder) secondBoundCylinder).setMaterial(hydrogenMaterial);
                basModel.getChildren().add(secondBoundCylinder);
                tooltipText += "\nPOSITION:\n" + "X = " + cubeCorners[i].getX() + "\nY = " + cubeCorners[i].getY() + "\nZ = " + cubeCorners[i].getZ();
                hydrogenSphere.getTransforms().add(new Translate(cubeCorners[i].getX(), cubeCorners[i].getY(), cubeCorners[i].getZ()));
                Tooltip.install(hydrogenSphere, new Tooltip(tooltipText));
                basModel.getChildren().add(hydrogenSphere);

            }
            carbonOriginCubeSigns = cubeCorners[0];
            if (hasNext) {
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
                    Node boundCylinder = getCylinderBetweenTwoPoints(newOrigin, newPointB, cylinderRadius);
                    ((Cylinder) boundCylinder).setMaterial(carbonMaterial);
                    basModel.getChildren().add(boundCylinder);

                }

            }
            carbonOrigin = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
            flip = !flip;

        }

    }

    public static void buildStickModel(CarbonBase[] arr, PhongMaterial carbonMaterial, PhongMaterial hydrogenMaterial, double basicDistance, double cylinderRadius, Model3D stickModel) {

        boolean flip = false;
        Point3D carbonOrigin = new Point3D(0, 0, 0);
        Point3D carbonOriginCubeSigns = new Point3D(-1, -1, -1);
        boolean hasNext = true;
        Point3D[] cubeCorners;
        double cubeSideLength = basicDistance;
        int startIndex;

        for (CarbonBase carbon : arr) {

            hasNext = carbon.hasNext();

            cubeCorners = calculateCornersLinear(cubeSideLength, carbonOrigin, carbonOriginCubeSigns, carbon.getShape());
            startIndex = 0;
            if (hasNext) {
                startIndex = 1;
            }

            for (int i = startIndex; i < carbon.getOwnHnumber() + startIndex; i++) {

//                if(startIndex == 1)
                if (!(carbon.getShape() == HydroCarbon.subMoleculeShape.TRIGONAL && i == 1)) {
                    cubeCorners[i] = calculateActualCorners(cubeCorners[i], carbonOrigin, cubeSideLength, flip);
                } else {

                    cubeCorners[i] = new Point3D(cubeCorners[i].getX(), cubeCorners[i].getY(), cubeCorners[i].getZ() < 0 ? cubeCorners[i].getZ() - basicDistance : cubeCorners[i].getZ() + basicDistance);

                }
                Point3D midPoint = cubeCorners[i].midpoint(carbonOrigin);

                Node FirstboundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, midPoint, cylinderRadius);

                ((Cylinder) FirstboundCylinder).setMaterial(carbonMaterial);
                stickModel.getChildren().add(FirstboundCylinder);

                Node secondBoundCylinder = getCylinderBetweenTwoPoints(midPoint, cubeCorners[i], cylinderRadius);

                ((Cylinder) secondBoundCylinder).setMaterial(hydrogenMaterial);
                stickModel.getChildren().add(secondBoundCylinder);

            }
            carbonOriginCubeSigns = cubeCorners[0];
            if (hasNext) {

                Point3D newPointB = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
                // System.out.println("PLACING BOUNDS CYLINDER BETWEEN POINTS : " + newOrigin.toString() + " \n " + newPointB.toString());
                Node boundCylinder = getCylinderBetweenTwoPoints(carbonOrigin, newPointB, cylinderRadius);
                switch (carbon.getRightBounds()) {

                    case 2:
                        ((Cylinder) boundCylinder).setMaterial(new PhongMaterial(StickModelcolor2Bound));
                        break;
                    case 3:
                        ((Cylinder) boundCylinder).setMaterial(new PhongMaterial(StickModelcolor3Bound));
                        break;
                    default:
                        ((Cylinder) boundCylinder).setMaterial(carbonMaterial);
                        break;
                }

                stickModel.getChildren().add(boundCylinder);

            }

            carbonOrigin = calculateActualCorners(carbonOriginCubeSigns, carbonOrigin, cubeSideLength, flip);
            flip = !flip;
        }

    }

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

    private static Point3D[] calculateCornersLinear(double cubeSideLength, Point3D centerPoint, Point3D centerSigns, HydroCarbon.subMoleculeShape shape) {
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
                // flip
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ * (-1));  // next place
                retarr[1] = new Point3D(pX, pY, pZ);
                break;
            case TRIGONAL:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(cX, cY, cZ < 0 ? cZ - ((halfSide / Math.sqrt(3)) / 2) - halfSide / 2 : cZ + ((halfSide / Math.sqrt(3)) / 2) + halfSide / 2);
                retarr[2] = new Point3D(pX, pY, pZ);

                break;
            case TETRA:
                retarr[0] = new Point3D(pX * (-1), pY * (-1), pZ);
                retarr[1] = new Point3D(pX, pY * (-1), pZ * (-1));
                retarr[2] = new Point3D(pX * (-1), pY, pZ * (-1));
                retarr[3] = new Point3D(pX, pY, pZ);
                break;
            default:
                break;
        }
        return retarr;
    }

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
//        returnPoint = new Point3D((signX <0 ? (originX - scaleValue) : (originX + scaleValue)) ,(signY <0 ? (originY - scaleValue) : (originY + scaleValue)),(signZ <0 ? (originZ - scaleValue) : (originZ + scaleValue))  );
            returnPoint = new Point3D(originX + (signX * scaleValue), originY + (signY * scaleValue), originZ + (signZ * scaleValue));
        }
        return returnPoint;

    }

}
