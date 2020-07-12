/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.HCgroup;

import bakalarka.Utilities.dataPack.DataLoader;

/**
 *
 * @author MarekPC
 */
public class HydroCarbonsBase implements TreeItemViewable {

    public static final String BASE_INFO = DataLoader.HydroCarbonsInfo.hcInfo;
    public static final String ATTRI = DataLoader.HydroCarbonsInfo.hcAttri;

    private static String atri;

    @Override
    public String toString() {
        return "Uhlovodíky";
    }

}
