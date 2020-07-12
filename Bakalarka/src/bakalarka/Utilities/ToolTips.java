/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

/**
 *
 * @author MarekPC
 */
public class ToolTips {

    public final static HashMap<String, String> KEYWORDSMAP = new HashMap<>();

    static {
        //Populate HASHMAP

        try {
            InputStream urlTooltips = ToolTips.class.getResourceAsStream("tooltips.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(urlTooltips, "UTF-8"));
            String key = "";
            String value = "";

            while ((key = br.readLine()) != null && (value = br.readLine()) != null) {
                String keys[] = key.split(" ");
                for (String key1 : keys) {

                    KEYWORDSMAP.put(key1, value);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindTooltip(final Node node, final Tooltip tooltip) {
        node.setOnMouseMoved(e -> {
            tooltip.show(node, e.getScreenX(), e.getScreenY() + 15);
        });

        node.setOnMouseExited(e -> {
            tooltip.hide();
        });
    }

    public static ObservableList<Text> constructText(String baseText) {
        ObservableList<Text> returnList = FXCollections.observableArrayList();
        baseText = baseText.replace("NEWLINE", "\n");
        String[] words = baseText.split(" |\\.|,|!|-|:|⚉|-");
        String remeaningPart = baseText;

        for (String word : words) {
//            System.out.println(word);
            if (KEYWORDSMAP.containsKey(word.toLowerCase())) {

                String tooltipDefinition = KEYWORDSMAP.get(word.toLowerCase());

                String previousPart = remeaningPart.substring(0, remeaningPart.indexOf(word));
                Text textNonKeyWord = new Text(previousPart);
                Text keyWord = new Text(word);
                Styler.styleAsTooltip(keyWord);
                Tooltip tt = new Tooltip(tooltipDefinition);
                tt.setWrapText(true);
                tt.setPrefWidth(500);
                ToolTips.bindTooltip(keyWord, tt);
                returnList.addAll(textNonKeyWord, keyWord);
                remeaningPart = remeaningPart.substring(remeaningPart.indexOf(word) + word.length());

            }
        }
        Text textRemaining = new Text(remeaningPart);
        returnList.add(textRemaining);
        return returnList;
    }

}
