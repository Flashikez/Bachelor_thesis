/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import bakalarka.Utilities.GeometryHelper;
import bakalarka.Utilities.SubscriptHelper;
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
public class RationalFormula extends Formula {

    private CarbonBase[] carbonStructure;
    private String linearPrint = "";
    private String[] cyclicPrint;

    public RationalFormula(HydroCarbon phc, String plabel, String pinfo) {
        super(phc, plabel, pinfo);
        cyclicPrint = new String[hc.getcNumber()];
        this.contentChanged();

    }

    @Override
    public void contentChanged() {
        linearPrint = "";

        carbonStructure = hc.getStructureArr();

        int hNumber = 0;
        int index = 0;
        for (CarbonBase carbonB : carbonStructure) {
            cyclicPrint[index] = "";
            linearPrint += "C";
            cyclicPrint[index] += "C";
            hNumber = carbonB.getOwnHnumber();
            if (hNumber > 0) {
                linearPrint += "H" + SubscriptHelper.toSubscript(hNumber);
                cyclicPrint[index] += "H" + SubscriptHelper.toSubscript(hNumber);
            }
            if (carbonB.hasNext()) {
                switch (carbonB.getRightBounds()) {
                    case 1:
                        linearPrint += "-";
                        break;
                    case 2:
                        linearPrint += "=";
                        break;
                    case 3:
                        linearPrint += "≡";
                        break;
                    default:
                        break;
                }

            }
            index++;
        }

    }

    @Override
    public void drawFormula(GraphicsContext gc, double width, double height) {

        if (hc.isIsCyclo()) {

            Text tex = new Text("C");

            // double fontSize = Math.round(width/(2.6*struct.length));
            tex.setFont(Font.font("Helvetica", gc.getFont().getSize()));

            double widthOfC = tex.getBoundsInLocal().getWidth();

            double characterOffset = widthOfC * 2;

            double startPositionX = Math.round(width / 2);
            double startPosotionY = Math.round(height / 2);

            // Point2D[] cornersLines = GeometryHelper.calculateCyclicPoints2D(startPositionX, startPosotionY, characterOffset, hc.getCyclicShape());
            Point2D[] cornersCarbons = GeometryHelper.calculateCyclicPoints2D(startPositionX, startPosotionY, 1.35 * characterOffset, hc.getCyclicShape());

            for (int i = 0; i < hc.getcNumber(); i++) { //LINES
                gc.setLineWidth(5);
                int lineToPointIndex = ((i + 1) % hc.getcNumber());
                switch (carbonStructure[i].getRightBounds()) {
                    case 2:
                        gc.setLineWidth(3);

                        if (i > hc.getcNumber() / 2) // Horná polovica cyklu
                        {
                            gc.strokeLine(cornersCarbons[i].getX() - 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() - 7, cornersCarbons[lineToPointIndex].getY() + 7);
                        } else {
                            gc.strokeLine(cornersCarbons[i].getX() + 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() + 7, cornersCarbons[lineToPointIndex].getY() + 7);
                        }
                        break;
                    case 3:
                        gc.setLineWidth(2);
                        gc.strokeLine(cornersCarbons[i].getX() + 7, cornersCarbons[i].getY() + 7, cornersCarbons[lineToPointIndex].getX() + 7, cornersCarbons[lineToPointIndex].getY() + 7);
                        gc.strokeLine(cornersCarbons[i].getX() + 14, cornersCarbons[i].getY() + 14, cornersCarbons[lineToPointIndex].getX() + 14, cornersCarbons[lineToPointIndex].getY() + 14);
                        break;

                }

                gc.strokeLine(cornersCarbons[i].getX(), cornersCarbons[i].getY(), cornersCarbons[lineToPointIndex].getX(), cornersCarbons[lineToPointIndex].getY());

            }
            for (int i = 0; i < hc.getcNumber(); i++) {
                Text text = new Text(cyclicPrint[i]);
                text.setFont(Font.font("Helvetica", gc.getFont().getSize()));
                text.getTransforms().add(new Translate(cornersCarbons[i].getX(), cornersCarbons[i].getY()));
                gc.setFill(Color.LIGHTGRAY);

                gc.fillRect(text.getBoundsInParent().getMinX() - text.getBoundsInParent().getWidth() / 2, text.getBoundsInParent().getMinY() + text.getBoundsInParent().getHeight() / 2, text.getBoundsInParent().getWidth(), text.getBoundsInParent().getHeight());

                gc.setFill(Color.BLACK);

                gc.fillText(cyclicPrint[i], cornersCarbons[i].getX(), cornersCarbons[i].getY());

            }

        } else {
            gc.fillText(linearPrint, Math.round(width / 2), Math.round(height / 2));
        }

    }

}
