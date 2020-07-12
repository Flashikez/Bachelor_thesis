/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities;

import chemicals.HydroCarbon;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 *
 * @author MarekPC
 */
public class GeometryHelper {

    public static Point2D rotate2DpointAroundPivot(Point2D pivot, Point2D rotatePoint, double angle) {
        double sin = Math.sin(Math.toRadians(angle));
        double cos = Math.cos(Math.toRadians(angle));
        double rotateX = rotatePoint.getX();
        double rotateY = rotatePoint.getY();

        double pxmox = rotateX - pivot.getX();
        double pymoy = rotateY - pivot.getY();

        double x = cos * pxmox - sin * pymoy + pivot.getX();
        double y = sin * pxmox + cos * pymoy + pivot.getY();

        return new Point2D(x, y);

    }

    public static Point2D[] calculateCyclicPoints2D(double xCenter, double yCenter, double offset, HydroCarbon.coreStructureShape cyclicShape) {
        Point2D[] returnArray = new Point2D[cyclicShape.getNumOfCorners()];
        switch (cyclicShape) {
            case TRIANGLE: {
                double firstAngle = Math.toRadians(90);
                double secondAngle = Math.toRadians(120);
                double thirdAngle = Math.toRadians(120);
                returnArray[0] = new Point2D(xCenter - 2 * offset * Math.cos(firstAngle), yCenter - 2 * offset * Math.sin(firstAngle));
                returnArray[1] = new Point2D(xCenter - 2 * offset * Math.cos(firstAngle + secondAngle), yCenter - 2 * offset * Math.sin(firstAngle + secondAngle));
                returnArray[2] = new Point2D(xCenter - 2 * offset * Math.cos(firstAngle + secondAngle + thirdAngle), yCenter - 2 * offset * Math.sin(firstAngle + secondAngle + thirdAngle));

                break;
            }
            case HEXAGON: {
                double angleRadians;
                for (int i = 0; i < 6; i++) {
                    angleRadians = Math.toRadians(60 * i + 30);
                    returnArray[i] = new Point2D(xCenter + 2.5 * offset * Math.cos(angleRadians), yCenter + 2.5 * offset * Math.sin(angleRadians));
                }

                break;
            }

        }

        return returnArray;
    }

    public static Point3D[] calculateCyclicPoints3D(double xCenter, double yCenter, double offset, HydroCarbon.coreStructureShape cyclicShape) {
        Point3D[] returnArray = new Point3D[cyclicShape.getNumOfCorners()];
        switch (cyclicShape) {
            case TRIANGLE: {
                double firstAngle = Math.toRadians(90);
                double secondAngle = Math.toRadians(120);
                double thirdAngle = Math.toRadians(120);
                returnArray[0] = new Point3D(xCenter - 2 * offset * Math.cos(firstAngle), yCenter - 2 * offset * Math.sin(firstAngle), 0);
                returnArray[1] = new Point3D(xCenter - 2 * offset * Math.cos(firstAngle + secondAngle), yCenter - 2 * offset * Math.sin(firstAngle + secondAngle), 0);
                returnArray[2] = new Point3D(xCenter - 2 * offset * Math.cos(firstAngle + secondAngle + thirdAngle), yCenter - 2 * offset * Math.sin(firstAngle + secondAngle + thirdAngle), 0);

                break;
            }
            case HEXAGON: {
                double angleRadians;
                for (int i = 0; i < 6; i++) {
                    angleRadians = Math.toRadians(60 * i + 30);
                    returnArray[i] = new Point3D(xCenter + 2.5 * offset * Math.cos(angleRadians), yCenter + 2.5 * offset * Math.sin(angleRadians), 0);
                }

                break;
            }

        }

        return returnArray;
    }

    public static Point2D[] calculateHPositions(Point2D firstNeighbor, Point2D secondNeighbor, Point2D centerPosition, HydroCarbon.subMoleculeShape shape, boolean justExtendBothNrighbors) {
        Point2D[] returnArray = new Point2D[shape.getPoints() - 2];
        switch (shape) {

            case TETRA:

                returnArray[0] = rotate2DpointAroundPivot(centerPosition, firstNeighbor, 90);

                returnArray[1] = rotate2DpointAroundPivot(centerPosition, secondNeighbor, -90);

                break;

            case TRIGONAL:

                returnArray[0] = rotate2DpointAroundPivot(centerPosition, firstNeighbor, shape.getAngle());

                break;

            default:
                break;
        }
        if (justExtendBothNrighbors) {

            returnArray[0] = rotate2DpointAroundPivot(centerPosition, firstNeighbor, 180);

            returnArray[1] = rotate2DpointAroundPivot(centerPosition, secondNeighbor, 180);

        }

        return returnArray;
    }

    public static Point3D getCenterOfBounds(Bounds b) {

        double dx = (b.getMinX() + b.getMaxX()) / 2;
        double dy = (b.getMinY() + b.getMaxY()) / 2;
        double dz = (b.getMinZ() + b.getMaxZ()) / 2;

        return new Point3D(dx, dy, dz);
    }
}
