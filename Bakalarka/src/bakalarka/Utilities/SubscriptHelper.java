/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities;

/**
 *
 * @author MarekPC
 */
public class SubscriptHelper {

    public static String toSubscript(int num) {
        char[] orig = Integer.toString(num).toCharArray();
        if (num == 1) {
            orig = "".toCharArray();
        } else {
            for (int i = 0; i < orig.length; i++) {
                orig[i] = (char) (Integer.parseInt(Character.toString(orig[i])) + 8320);
            }
        }

        return String.valueOf(orig);

    }

}
