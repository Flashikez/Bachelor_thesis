/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chemicals;

import bakalarka.HCgroup.TreeItemViewable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author MarekPC
 */
public class HydroCarbon implements TreeItemViewable {

    private String name;
    private String info;
    private int hNumber;
    private int cNumber;
    private boolean isCyclo;
    private int boundMultiple;
    private CarbonBase[] structure;
    private int multiBoundPosition;
    private coreStructureShape cyclicShape;
    private boolean periodicMultCycloBound;
    private boolean isPlanar;
    private SimpleBooleanProperty multBoundFirstStrategy = new SimpleBooleanProperty() {
        @Override
        public void invalidated() {

            if (!isCyclo) {
                loadStructureLinear();
            } else {
                loadStructureCyclo();
            }

            structure[0].calculateShape();
        }
    };

    private String otherNames;
    private String attributes;

    public SimpleBooleanProperty getMultBoundFirstStrategy() {
        return multBoundFirstStrategy;
    }

    public void bindStrategy(BooleanProperty selectedProperty) {
        multBoundFirstStrategy.bind(selectedProperty);
    }

    private void calculateHnumber() {
        for (CarbonBase carbonB : structure) {
            this.hNumber += carbonB.getOwnHnumber();
        }

    }

    public enum subMoleculeShape {
        LINEAR(180, 2),
        TRIGONAL(120, 3),
        TETRA(109.5, 4);

        private final double angle;
        private final int points;

        private subMoleculeShape(double angle, int points) {
            this.angle = angle;
            this.points = points;
        }

        public double getAngle() {
            return this.angle;
        }

        public int getPoints() {
            return this.points;
        }

    };

    public enum coreStructureShape {
        TRIANGLE(3),
        SQUARE(4),
        PENTAGON(5),
        HEXAGON(6);

        private final int corners;

        private coreStructureShape(int corners) {
            this.corners = corners;
        }

        public int getNumOfCorners() {
            return this.corners;
        }

        public static coreStructureShape getShapeByCorners(int corners) {
            for (coreStructureShape shape : coreStructureShape.values()) {
                if (shape.getNumOfCorners() == corners) {
                    return shape;
                }

            }
            throw new ArithmeticException("This number of cyclic corners is not supported");
        }

    };

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public HydroCarbon(HydroCarbon other) { //copy Constructor
        this.name = other.name;
        this.cNumber = other.cNumber;
        this.isCyclo = other.isCyclo;
        this.boundMultiple = other.boundMultiple;
        this.info = other.info;
        this.attributes = other.attributes;
        this.otherNames = other.otherNames;
        this.periodicMultCycloBound = other.periodicMultCycloBound;
        structure = new CarbonBase[cNumber];
        multBoundFirstStrategy.set(true);

        //printStructure();
        multiBoundPosition = 0;
        calculateHnumber();
        this.cyclicShape = other.cyclicShape;
        this.isPlanar = other.isPlanar;
    }

    public HydroCarbon(String name, int cNumber, int boundMultiple, boolean isCyclo, String pinfo, String pothers, String pAttri, boolean periodicMultCycloBound) {

        this.name = name;
        this.cNumber = cNumber;
        this.isCyclo = isCyclo;
        this.boundMultiple = boundMultiple;
        this.info = pinfo;
        this.attributes = pAttri;
        this.otherNames = pothers;
        this.periodicMultCycloBound = periodicMultCycloBound;
        structure = new CarbonBase[cNumber];

        multBoundFirstStrategy.set(true);
        multiBoundPosition = 0;
        //printStructure();

        calculateHnumber();
        if (this.isCyclo) {
            this.cyclicShape = coreStructureShape.getShapeByCorners(cNumber);
        }

        isPlanar = (this.cyclicShape == coreStructureShape.TRIANGLE || periodicMultCycloBound);

    }

    public boolean isIsPlanar() {
        return isPlanar;
    }

    public coreStructureShape getCyclicShape() {
        return cyclicShape;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public String getAttributes() {
        return attributes;
    }

    public CarbonBase[] getStructureArr() {
        return structure;
    }

    public int gethNumber() {
        return hNumber;
    }

    public int getcNumber() {
        return cNumber;
    }

    public int getBoundMultiple() {
        return boundMultiple;
    }

    public boolean isIsCyclo() {
        return isCyclo;
    }

    @Override
    public String toString() {
        return name;
    }

    private void loadStructureLinear() {

        for (int i = 0; i < this.cNumber; i++) {
            structure[i] = new LinearCarbon();
        }

        if (multBoundFirstStrategy.get() == false) {
            //center the boundMultiple
            multiBoundPosition = Math.floorDiv(this.cNumber, 2) - 1;
            // multiBoundPosition = 1;
        } else {
            multiBoundPosition = 0;
        }

        for (int i = 0; i < this.cNumber; i++) {
            if (multBoundFirstStrategy.get()) {
                structure[i].setCarb((i == 0) ? null : structure[i - 1], (i == this.cNumber - 1) ? null : structure[i + 1], (i == 0 && i != this.cNumber - 1) ? boundMultiple : (i == this.cNumber - 1) ? 0 : 1);
            } else {
                structure[i].setCarb((i == 0) ? null : structure[i - 1], (i == this.cNumber - 1) ? null : structure[i + 1], (i == multiBoundPosition && i != this.cNumber - 1) ? boundMultiple : (i == this.cNumber - 1) ? 0 : 1);
            }
        }

    }

    public int getMultiBoundPosition() {
        return multiBoundPosition;
    }

    private void loadStructureCyclo() {
        for (int i = 0; i < this.cNumber; i++) {
            structure[i] = new CyclicCarbon();
        }

        multiBoundPosition = 0;

        for (int i = 0; i < this.cNumber; i++) {
            if (multBoundFirstStrategy.get()) {
                structure[i].setCarb((i == 0) ? null : structure[i - 1], (i == this.cNumber - 1) ? null : structure[i + 1], (i == 0 && i != this.cNumber - 1) ? boundMultiple : (i == this.cNumber - 1) ? 0 : 1);
            } else {
                structure[i].setCarb((i == 0) ? null : structure[i - 1], (i == this.cNumber - 1) ? null : structure[i + 1], (i == multiBoundPosition && i != this.cNumber - 1) ? boundMultiple : (i == this.cNumber - 1) ? 0 : 1);
            }
        }
        if (periodicMultCycloBound) {
            for (int i = 0; i < this.cNumber; i += 2) {
                if (i == 0) {
                    structure[0].setPrevious(structure[this.cNumber - 1], 1);
                }
                structure[i].setNext(structure[i + 1], boundMultiple);
                structure[i + 1].setPrevious(structure[i], boundMultiple);
            }
            structure[this.cNumber - 1].setNext(structure[0], 1);
            structure[this.cNumber - 1].setIsCycloEndPoint(true);

        } else {

            structure[this.cNumber - 1].setBothNeighbors(structure[this.cNumber - 2], structure[0], 1, 1);
            structure[0].setPrevious(structure[this.cNumber - 1], 1);

            structure[this.cNumber - 1].setIsCycloEndPoint(true);
        }

    }
}
