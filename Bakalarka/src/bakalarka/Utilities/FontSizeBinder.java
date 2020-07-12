/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities;

import bakalarka.TreeLink;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author MarekPC
 */
public class FontSizeBinder {

    public static final double INITIAL_FONT_SIZE = 20;
    public static final double FONT_SIZE_INCREMENT = 2;
    public static final String FONT_FAMILY_USED = "Helvetica";
    public static final Font BASE_FONT = new Font(FONT_FAMILY_USED, INITIAL_FONT_SIZE);

    public static final double INITIAL_FONT_SIZE_FORMULAS = 45;
    public static final double FONT_SIZE_INCREMENT_FORMULAS = 4;
    public static final Font FONT_FORMULAS = new Font(FONT_FAMILY_USED, INITIAL_FONT_SIZE_FORMULAS);

    public static void bindFontSizeAll(ObjectProperty<Font> fontProperty, Text... args) {
        for (Text arg : args) {
            arg.fontProperty().bind(fontProperty);

        }

    }

    public static void bindFontSizeAll(ObjectProperty<Font> fontProperty, ObservableList<Text> list) {
        list.forEach((text) -> {
            text.fontProperty().bind(fontProperty);
        });
    }

    public static void bindFontSizeAllLink(ObjectProperty<Font> fontProperty, ObservableList<TreeLink> list) {
        list.forEach((treeLink) -> {
            treeLink.fontProperty().bind(fontProperty);
        });
    }

    public static void bindFontSizeAll(ObjectExpression<Font> fontTracking, Text... args) {
        for (Text arg : args) {

            arg.fontProperty().bind(fontTracking);
        }
    }

    public static void bindFontSizeAll(ObjectProperty<Font> fontProperty, ObservableList<Text>... list) {
        for (ObservableList<Text> observableList : list) {
            observableList.forEach((text) -> {
                text.fontProperty().bind(fontProperty);
            });

        }
    }

}
