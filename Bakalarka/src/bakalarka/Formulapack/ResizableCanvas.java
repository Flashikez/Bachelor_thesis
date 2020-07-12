/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Formulapack;

import bakalarka.Utilities.FontSizeBinder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author MarekPC
 */
public class ResizableCanvas extends Canvas {

    private boolean hide = true;
    private Formula typeOfFormula;

    private SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<Font>(FontSizeBinder.FONT_FORMULAS);

    private double currentFontSize = FontSizeBinder.INITIAL_FONT_SIZE_FORMULAS;

    public ResizableCanvas(Formula form) {
        typeOfFormula = form;
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());

        onScrollProperty().set(e -> {
            if (e.isControlDown()) {

                if (e.getDeltaY() > 0) {
                    currentFontSize += FontSizeBinder.FONT_SIZE_INCREMENT_FORMULAS;
                } else {
                    currentFontSize -= FontSizeBinder.FONT_SIZE_INCREMENT_FORMULAS;
                }
                fontProperty.set(Font.font(FontSizeBinder.FONT_FAMILY_USED, currentFontSize));
                draw();

            }

        });

        this.setOnMouseClicked(e -> {
            hide = !hide;
            draw();
        });

        hide = false;
        draw();
        hide = true;
        draw();

    }

    public void hideToggle() {
        hide = !hide;
        draw();

    }

    public void draw() {
        double height = heightProperty().get();
        double width = widthProperty().get();
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFont(fontProperty.get());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.clearRect(0, 0, width, height);

        if (hide) {

            gc.fillText(typeOfFormula.getLabel(), Math.round(width / 2), Math.round(height / 2));
        } else {
            typeOfFormula.drawFormula(gc, width, height);
        }

    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

}
