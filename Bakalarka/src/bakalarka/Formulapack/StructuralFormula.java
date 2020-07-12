/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import bakalarka.Utilities.GeometryHelper;
import chemicals.CarbonBase;
import chemicals.HydroCarbon;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

/**
 *
 * @author MarekPC
 */
public class StructuralFormula extends Formula {

    private CarbonBase[] carbonStructure;
    private boolean isTriangleTetrahedral;

    public StructuralFormula(HydroCarbon phc, String plabel, String pinfo) {
        super(phc, plabel, pinfo);
        this.contentChanged();
        isTriangleTetrahedral = (phc.getcNumber() == 3 && phc.getBoundMultiple() == 1 && phc.isIsCyclo());

    }

    @Override
    public void contentChanged() {
        carbonStructure = hc.getStructureArr();

    }

    @Override
    public void drawFormula(GraphicsContext gc, double width, double height) {

        if (hc.isIsCyclo()) {
            drawCyclic(gc, width, height);
            return;
        }

        Text tex = new Text("C");

        // double fontSize = Math.round(width/(2.6*struct.length));
        tex.setFont(Font.font("Helvetica", gc.getFont().getSize()));

        double widthOfC = tex.getBoundsInLocal().getWidth();
        double heightofC = tex.getBoundsInLocal().getHeight();

        double characterOffset = widthOfC * 3;
        double linesOffset = heightofC / 12;
        double lineSize = heightofC / 2;
        double startPositionX = Math.round(width / 2 - carbonStructure.length * characterOffset / 2);
        double startPosotionY = Math.round(height / 2);

        //gc.setFont(Font.font("Helvetica",  fontSize));
        for (CarbonBase currentCarbon : carbonStructure) {

            gc.fillText("C", startPositionX, startPosotionY);

            for (int i = 1; i <= currentCarbon.getOwnHnumber(); i++) {
                gc.setLineWidth(1);
                double HpositionX = 0;
                double HpositionY = 0;
                switch (i) {

                    case 1:
                        if (currentCarbon.getShape() == HydroCarbon.subMoleculeShape.LINEAR) {
                            if (currentCarbon.hasNext() == false) {
                                gc.strokeLine(startPositionX + widthOfC / 4 + 5, startPosotionY, startPositionX + widthOfC / 4 + 5 + lineSize, startPosotionY);
                                HpositionX = startPositionX + widthOfC / 4 + 5 + lineSize + widthOfC / 4 + 10;
                                HpositionY = startPosotionY;
                                break;
                            } else {
                                gc.strokeLine(startPositionX - widthOfC / 4 - 8, startPosotionY, startPositionX - widthOfC / 4 - 8 - lineSize, startPosotionY);
                                HpositionX = startPositionX - widthOfC / 4 - 8 - lineSize - widthOfC / 4 - 10;
                                HpositionY = startPosotionY;
                                break;
                            }
                        }
                        if (currentCarbon.getShape() == HydroCarbon.subMoleculeShape.TRIGONAL) {
                            if (currentCarbon.hasPrevious() == false) {
                                gc.strokeLine(startPositionX - widthOfC / 4 - linesOffset, startPosotionY - widthOfC / 4 - linesOffset, startPositionX - widthOfC / 4 - linesOffset - lineSize, startPosotionY - widthOfC / 4 - linesOffset - lineSize);
                                HpositionX = startPositionX - widthOfC / 4 - linesOffset - lineSize - widthOfC / 4 - linesOffset - linesOffset;
                                HpositionY = startPosotionY - widthOfC / 2 - linesOffset - lineSize;
                                break;
                            } else if (currentCarbon.hasNext() == false) {
                                gc.strokeLine(startPositionX + widthOfC / 4 + linesOffset + linesOffset, startPosotionY - widthOfC / 4 - linesOffset, startPositionX + widthOfC / 4 + linesOffset + lineSize, startPosotionY - widthOfC / 4 - linesOffset - lineSize);
                                HpositionX = startPositionX + widthOfC / 4 + linesOffset + lineSize + widthOfC / 4 + linesOffset + linesOffset;
                                HpositionY = startPosotionY - widthOfC / 2 - linesOffset - lineSize;
                                break;
                            }
                        }
                        gc.strokeLine(startPositionX, startPosotionY - heightofC / 4 - linesOffset, startPositionX, startPosotionY - heightofC / 4 - 5 - lineSize);
                        HpositionX = startPositionX;
                        HpositionY = startPosotionY - heightofC / 4 - 5 - lineSize - heightofC / 4 - 5;
                        break;
                    case 2:
                        if (currentCarbon.getShape() == HydroCarbon.subMoleculeShape.TRIGONAL) {
                            if (currentCarbon.hasPrevious() == false) {
                                gc.strokeLine(startPositionX - widthOfC / 4 - linesOffset, startPosotionY + widthOfC / 4 + linesOffset + linesOffset, startPositionX - widthOfC / 4 - linesOffset - lineSize, startPosotionY + widthOfC / 4 + linesOffset + lineSize);
                                HpositionX = startPositionX - widthOfC / 4 - linesOffset - lineSize - widthOfC / 4 - linesOffset - linesOffset;
                                HpositionY = startPosotionY + widthOfC / 2 + linesOffset + lineSize;
                                break;

                            } else if (currentCarbon.hasNext() == false) {
                                gc.strokeLine(startPositionX + widthOfC / 4 + linesOffset + linesOffset, startPosotionY + widthOfC / 4 + linesOffset + linesOffset, startPositionX + widthOfC / 4 + linesOffset + lineSize + linesOffset, startPosotionY + widthOfC / 4 + linesOffset + lineSize);
                                HpositionX = startPositionX + widthOfC / 4 + linesOffset + lineSize + widthOfC / 4 + linesOffset + linesOffset + linesOffset;
                                HpositionY = startPosotionY + widthOfC / 2 + linesOffset + lineSize;
                                break;
                            }
                        }
                        gc.strokeLine(startPositionX, startPosotionY + heightofC / 4 + 8, startPositionX, startPosotionY + heightofC / 4 + 8 + lineSize);
                        HpositionX = startPositionX;
//                            HpositionY = startPosotionY+2+characterOffset + elementOffset;
                        HpositionY = startPosotionY + heightofC / 4 + 8 + lineSize + heightofC / 4 + 5;
                        break;

                    case 3:
                        if (currentCarbon == carbonStructure[hc.getcNumber() - 1] && hc.getcNumber() > 1) {
                            gc.strokeLine(startPositionX + widthOfC / 4 + 5, startPosotionY, startPositionX + widthOfC / 4 + 5 + lineSize, startPosotionY);
//                            HpositionX = startPositionX + elementOffset + characterOffset + elementOffset;
                            HpositionX = startPositionX + widthOfC / 4 + 5 + lineSize + widthOfC / 4 + 10;
                            HpositionY = startPosotionY;

                        } else {
                            gc.strokeLine(startPositionX - widthOfC / 4 - 8, startPosotionY, startPositionX - widthOfC / 4 - 8 - lineSize, startPosotionY);
//                            HpositionX = startPositionX + elementOffset + characterOffset + elementOffset;
                            HpositionX = startPositionX - widthOfC / 4 - 8 - lineSize - widthOfC / 4 - 10;
                            HpositionY = startPosotionY;
                        }
                        break;
                    case 4:
                        gc.strokeLine(startPositionX + widthOfC / 4 + 5, startPosotionY, startPositionX + widthOfC / 4 + 5 + lineSize, startPosotionY);
                        HpositionX = startPositionX + widthOfC / 4 + 5 + lineSize + widthOfC / 4 + 10;
                        HpositionY = startPosotionY;
                        break;
                }
                gc.fillText("H", HpositionX, HpositionY);

            }
            for (int i = 0; i < currentCarbon.getRightBounds(); i++) {
                gc.setLineWidth(3);
                gc.strokeLine(startPositionX + widthOfC / 4 + 5, startPosotionY + (i * linesOffset), startPositionX + widthOfC / 4 + 5 + characterOffset - characterOffset / 3, startPosotionY + (i * linesOffset));
            }

            startPositionX += characterOffset;

        }

    }

    private void drawCyclic(GraphicsContext gc, double width, double height) {

        Text tex = new Text("C");

        // double fontSize = Math.round(width/(2.6*struct.length));
        tex.setFont(Font.font("Helvetica", gc.getFont().getSize()));

        double widthOfC = tex.getBoundsInLocal().getWidth();

        double characterOffset = widthOfC * 2;

        double startPositionX = Math.round(width / 2);
        double startPosotionY = Math.round(height / 2);

        Point2D[] cornersCarbons = GeometryHelper.calculateCyclicPoints2D(startPositionX, startPosotionY, 0.75 * characterOffset, hc.getCyclicShape());

        for (int i = 0; i < hc.getcNumber(); i++) { //LINES
            gc.setLineWidth(5);
            int lineToPointIndex = ((i + 1) % hc.getcNumber());
            switch (carbonStructure[i].getRightBounds()) {
                case 2:
                    gc.setLineWidth(5);
                    if (i > hc.getcNumber() / 2) // Horná polovica cyklu
                    {
                        gc.strokeLine(cornersCarbons[i].getX() - 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() - 7, cornersCarbons[lineToPointIndex].getY() + 7);
                    } else {
                        gc.strokeLine(cornersCarbons[i].getX() + 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() + 7, cornersCarbons[lineToPointIndex].getY() + 7);
                    }
//                          gc.strokeLine(cornersCarbons[i].getX()+14, cornersCarbons[i].getY()+14, cornersCarbons[lineToPointIndex].getX()+14, cornersCarbons[lineToPointIndex].getY()+14);
                    break;
                case 3:
                    gc.setLineWidth(3);
                    gc.strokeLine(cornersCarbons[i].getX() + 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() + 7, cornersCarbons[lineToPointIndex].getY() + 7);
                    gc.strokeLine(cornersCarbons[i].getX() + 14, cornersCarbons[i].getY() + 14, cornersCarbons[lineToPointIndex].getX() + 14, cornersCarbons[lineToPointIndex].getY() + 14);
                    break;

            }

            gc.strokeLine(cornersCarbons[i].getX(), cornersCarbons[i].getY(), cornersCarbons[lineToPointIndex].getX(), cornersCarbons[lineToPointIndex].getY());

        }

        gc.setLineWidth(2);
        for (int i = 0; i < hc.getcNumber(); i++) {

            int previousCindex = (i == 0) ? hc.getcNumber() - 1 : i - 1;

            Point2D[] Hpositions = GeometryHelper.calculateHPositions(cornersCarbons[previousCindex], cornersCarbons[(i + 1) % hc.getcNumber()], cornersCarbons[i], carbonStructure[i].getShape(), isTriangleTetrahedral);

            for (Point2D Hposition : Hpositions) {
                Text textH = new Text("H");
                textH.setFont(Font.font("Helvetica", gc.getFont().getSize()));
                textH.getTransforms().add(new Translate(Hposition.getX(), Hposition.getY()));

                gc.strokeLine(cornersCarbons[i].getX(), cornersCarbons[i].getY(), Hposition.getX(), Hposition.getY());
                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(textH.getBoundsInParent().getMinX() - textH.getBoundsInParent().getWidth() / 2, textH.getBoundsInParent().getMinY() + textH.getBoundsInParent().getHeight() / 2, textH.getBoundsInParent().getWidth(), textH.getBoundsInParent().getHeight() / 1.5);
                gc.setFill(Color.BLACK);
                gc.fillText("H", Hposition.getX(), Hposition.getY());

            }
            Text text = new Text("C");
            text.setFont(Font.font("Helvetica", gc.getFont().getSize()));
            text.getTransforms().add(new Translate(cornersCarbons[i].getX(), cornersCarbons[i].getY()));
            gc.setFill(Color.LIGHTGRAY);

            gc.fillRect(text.getBoundsInParent().getMinX() - text.getBoundsInParent().getWidth() / 2, text.getBoundsInParent().getMinY() + text.getBoundsInParent().getHeight() / 2, text.getBoundsInParent().getWidth(), text.getBoundsInParent().getHeight() / 1.5);
            gc.setFill(Color.BLACK);
            gc.fillText("C", cornersCarbons[i].getX(), cornersCarbons[i].getY());

        }

    }

}
