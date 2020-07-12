/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka.Utilities.dataPack;

import chemicals.HydroCarbon;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author MarekPC
 */
public class DataLoader {

    public static class HydroCarbonsInfo {

        public static String hcInfo;
        public static String hcAttri;

        static {
            InputStream urlInfo = DataLoader.class.getResourceAsStream("groups/hydrocarbons/hydrocarbonsInfo.txt");
            InputStream urlAttri = DataLoader.class.getResourceAsStream("groups/hydrocarbons/hydrocarbonsAttri.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlInfo, "UTF-8"));
                hcInfo = br.readLine();
                br = new BufferedReader(new InputStreamReader(urlAttri, "UTF-8"));
                hcAttri = groupsData.loadAttributes(br);
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public static class ModelsData {

        public static String kalotModelLabel;
        public static String kalotInfo;
        public static String basModelLabel;
        public static String basInfo;
        public static String stickModelLabel;
        public static String stickInfo;

        static {
            InputStream urlKalotModel = DataLoader.class.getResourceAsStream("models/kalotovyModelInfo.txt");
            InputStream urlBaSmodel = DataLoader.class.getResourceAsStream("models/gulickovyModelInfo.txt");
            InputStream urlStickModel = DataLoader.class.getResourceAsStream("models/trubickovyModelInfo.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlKalotModel, "UTF-8"));

                kalotModelLabel = br.readLine();
                kalotInfo = br.readLine();

                br = new BufferedReader(new InputStreamReader(urlBaSmodel, "UTF-8"));
                basModelLabel = br.readLine();
                basInfo = br.readLine();

                br = new BufferedReader(new InputStreamReader(urlStickModel, "UTF-8"));
                stickModelLabel = br.readLine();
                stickInfo = br.readLine();
                br.close();
            } catch (Exception ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class FormulasData {

        public static String summaryInfo;
        public static String summaryLabel;

        public static String racionalInfo;
        public static String racionalLabel;

        public static String empiricalInfo;
        public static String empiricalabel;

        public static String structuralInfo;
        public static String structuralLabel;

        public static final String formulasFolder = "formulas/";

        static {
            InputStream structStream = DataLoader.class.getResourceAsStream(formulasFolder + "structural.txt");
            InputStream summaryStream = DataLoader.class.getResourceAsStream(formulasFolder + "summary.txt");
            InputStream racionalStream = DataLoader.class.getResourceAsStream(formulasFolder + "racional.txt");
            InputStream empiricalStream = DataLoader.class.getResourceAsStream(formulasFolder + "empirical.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(structStream, "UTF-8"));

                structuralLabel = br.readLine();
                structuralInfo = br.readLine();

                br = new BufferedReader(new InputStreamReader(summaryStream, "UTF-8"));
                summaryLabel = br.readLine();
                summaryInfo = br.readLine();

                br = new BufferedReader(new InputStreamReader(racionalStream, "UTF-8"));
                racionalLabel = br.readLine();
                racionalInfo = br.readLine();

                br = new BufferedReader(new InputStreamReader(empiricalStream, "UTF-8"));
                empiricalabel = br.readLine();
                empiricalInfo = br.readLine();

                br.close();
            } catch (Exception ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static class groupsData {

        public static final String groupsBasePath = "groups/";
        public static final String groupsInfoSuffix = "Info.txt";
        public static final String groupsAttriSuffix = "Attri.txt";

        public static class AlkansData {

            public static final String alkansFolder = "alkans/";
            public static final String alkansBase = "alkans";
            public static String alkansLabel;
            public static String alkansInfo;
            public static String alkansAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> alkans;
            public static final boolean isCyclo = false;
            public static final int boundMultiple = 1;

            static {
                // Info
                String path = groupsBasePath + alkansFolder + alkansBase + groupsInfoSuffix;
                InputStream alkansInfoStream = DataLoader.class.getResourceAsStream(path);
                InputStream alkansAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkansFolder + alkansBase + groupsAttriSuffix);
                InputStream alkansStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkansFolder + alkansBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkansInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    alkansLabel = loadedInfo[0];
                    alkansInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkansAttriStream, "UTF-8"));

                    alkansAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkansStream, "UTF-8"));
                    alkans = loadHydroCarbons(br, isCyclo, boundMultiple, false);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class AlkensData {

            public static final String alkensFolder = "alkens/";
            public static final String alkensBase = "alkens";
            public static String alkensLabel;
            public static String alkensInfo;
            public static String alkensAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> alkens;

            public static final boolean isCyclo = false;
            public static final int boundMultiple = 2;

            static {
                // Info
                InputStream alkensInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkensFolder + alkensBase + groupsInfoSuffix);
                InputStream alkensAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkensFolder + alkensBase + groupsAttriSuffix);
                InputStream alkensStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkensFolder + alkensBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkensInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    alkensLabel = loadedInfo[0];
                    alkensInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkensAttriStream, "UTF-8"));

                    alkensAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkensStream, "UTF-8"));
                    alkens = loadHydroCarbons(br, isCyclo, boundMultiple, false);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class AlkinsData {

            public static final String alkinsFolder = "alkins/";
            public static final String alkinsBase = "alkins";
            public static String alkinsLabel;
            public static String alkinsInfo;
            public static String alkinsAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> alkins;

            public static final boolean isCyclo = false;
            public static final int boundMultiple = 3;

            static {
                // Info
                InputStream alkensInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkinsFolder + alkinsBase + groupsInfoSuffix);
                InputStream alkensAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkinsFolder + alkinsBase + groupsAttriSuffix);
                InputStream alkensStream = DataLoader.class.getResourceAsStream(groupsBasePath + alkinsFolder + alkinsBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkensInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    alkinsLabel = loadedInfo[0];
                    alkinsInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkensAttriStream, "UTF-8"));

                    alkinsAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkensStream, "UTF-8"));
                    alkins = loadHydroCarbons(br, isCyclo, boundMultiple, false);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class CycloAlkansData {

            public static final String cycloalkansFolder = "cycloAlkans/";
            public static final String cycloalkansBase = "cycloAlkans";
            public static String cycloalkansLabel;
            public static String cycloalkansInfo;
            public static String cycloalkansAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> cycloalkans;

            public static final boolean isCyclo = true;
            public static final int boundMultiple = 1;
            public static final boolean periodicMultBound = false;

            static {
                // Info
                InputStream alkensInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkansFolder + cycloalkansBase + groupsInfoSuffix);
                InputStream alkensAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkansFolder + cycloalkansBase + groupsAttriSuffix);
                InputStream alkensStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkansFolder + cycloalkansBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkensInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    cycloalkansLabel = loadedInfo[0];
                    cycloalkansInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkensAttriStream, "UTF-8"));

                    cycloalkansAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkensStream, "UTF-8"));
                    cycloalkans = loadHydroCarbons(br, isCyclo, boundMultiple, periodicMultBound);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class CycloAlkensData {

            public static final String cycloalkensFolder = "cycloAlkens/";
            public static final String cycloalkensBase = "cycloAlkens";
            public static String cycloalkensLabel;
            public static String cycloalkensInfo;
            public static String cycloalkensAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> cycloalkens;

            public static final boolean isCyclo = true;
            public static final int boundMultiple = 2;
            public static final boolean periodicMultBound = false;

            static {
                // Info
                InputStream alkensInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkensFolder + cycloalkensBase + groupsInfoSuffix);
                InputStream alkensAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkensFolder + cycloalkensBase + groupsAttriSuffix);
                InputStream alkensStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkensFolder + cycloalkensBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkensInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    cycloalkensLabel = loadedInfo[0];
                    cycloalkensInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkensAttriStream, "UTF-8"));

                    cycloalkensAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkensStream, "UTF-8"));
                    cycloalkens = loadHydroCarbons(br, isCyclo, boundMultiple, periodicMultBound);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class CycloAlkinsData {

            public static final String cycloalkinsFolder = "cycloAlkins/";
            public static final String cycloalkinsBase = "cycloAlkins";
            public static String cycloalkinsLabel;
            public static String cycloalkinsInfo;
            public static String cycloalkinsAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> cycloalkins;

            public static final boolean isCyclo = true;
            public static final int boundMultiple = 3;
            public static final boolean periodicMultBound = false;

            static {
                // Info
                InputStream alkensInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkinsFolder + cycloalkinsBase + groupsInfoSuffix);
                InputStream alkensAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkinsFolder + cycloalkinsBase + groupsAttriSuffix);
                InputStream alkensStream = DataLoader.class.getResourceAsStream(groupsBasePath + cycloalkinsFolder + cycloalkinsBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(alkensInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    cycloalkinsLabel = loadedInfo[0];
                    cycloalkinsInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(alkensAttriStream, "UTF-8"));

                    cycloalkinsAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(alkensStream, "UTF-8"));
                    cycloalkins = loadHydroCarbons(br, isCyclo, boundMultiple, periodicMultBound);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static class Aromatics {

            public static final String aromaticsFolder = "aromatics/";
            public static final String aromaticsBase = "aromatics";
            public static String aromaticsLabel;
            public static String aromaticsInfo;
            public static String aromaticsAttri;
            public static String[] notSupported;
            public static Collection<HydroCarbon> aromatics;

            public static final boolean isCyclo = true;
            public static final int boundMultiple = 2;
            public static final boolean periodicMultBound = true;

            static {
                // Info
                InputStream aromaticsInfoStream = DataLoader.class.getResourceAsStream(groupsBasePath + aromaticsFolder + aromaticsBase + groupsInfoSuffix);
                InputStream aromaticsAttriStream = DataLoader.class.getResourceAsStream(groupsBasePath + aromaticsFolder + aromaticsBase + groupsAttriSuffix);
                InputStream aromaticsStream = DataLoader.class.getResourceAsStream(groupsBasePath + aromaticsFolder + aromaticsBase + ".txt");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(aromaticsInfoStream, "UTF-8"));
                    String[] loadedInfo = loadInfo(br);
                    aromaticsLabel = loadedInfo[0];
                    aromaticsInfo = loadedInfo[1];
                    notSupported = loadedInfo[2].split(",");
                    br = new BufferedReader(new InputStreamReader(aromaticsAttriStream, "UTF-8"));

                    aromaticsAttri = loadAttributes(br);
                    br = new BufferedReader(new InputStreamReader(aromaticsStream, "UTF-8"));
                    aromatics = loadHydroCarbons(br, isCyclo, boundMultiple, periodicMultBound);
                    br.close();
                } catch (Exception ex) {
                    Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        public static String[] loadInfo(BufferedReader br) throws Exception {
            String[] returnArr = new String[3];
            for (int i = 0; i < 3; i++) {
                returnArr[i] = br.readLine();
            }
            return returnArr;
        }

        public static String loadAttributes(BufferedReader br) throws Exception {
            String returnString = "";
            String loadedLine = "";
            String prefix = "⚉  ";

            while ((loadedLine = br.readLine()) != null) {
                returnString += prefix + loadedLine + "\n";
            }
            //returnString = returnString.replace("NEWLINE","\n");
            return returnString;
        }

        public static ObservableList<HydroCarbon> loadHydroCarbons(BufferedReader br, boolean isCyclo, int multiBound, boolean periodicMultBound) throws Exception {
            ObservableList<HydroCarbon> returnList = FXCollections.observableArrayList();
            String line1 = "";
            String line2 = "";
            String line3 = "";
            String line4 = "";
            while ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null && (line3 = br.readLine()) != null && (line4 = br.readLine()) != null) {
                String[] splitted = line1.split(" ");
                String[] splitAttr = line4.split("\\.");
                String attributes = "";
                for (String string : splitAttr) {
                    attributes = attributes + "  ⚉" + string + "\n\n";

                }

                HydroCarbon hc = new HydroCarbon(splitted[0], Integer.parseInt(splitted[1]), multiBound, isCyclo, line2, "  ⚉  " + line3.replace(",", "\n  ⚉  "), attributes, periodicMultBound);
                returnList.add(hc);
            }

            return returnList;
        }

    }
}
