/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chemicals;

/**
 *
 * @author MarekPC
 */
public class LinearCarbon extends CarbonBase {

    public LinearCarbon() {
        super();
    }

    @Override
    public int getHnumber() {
        int hNumber = 4 - (neighbors[0] != null ? bounds[0] : 0) - (neighbors[1] != null ? bounds[1] : 0);
        if (neighbors[1] != null) {
            hNumber += neighbors[1].getHnumber();
        }
        return hNumber;
    }

    @Override
    public int getOwnHnumber() {
        return 4 - (neighbors[0] != null ? bounds[0] : 0) - (neighbors[1] != null ? bounds[1] : 0);
    }

    @Override
    public void setCarb(CarbonBase carbonLeft, CarbonBase carbonRight, int rightBounds) {
        if (carbonLeft != null) {
            neighbors[0] = carbonLeft;
            bounds[0] = carbonLeft.getRightBounds();
        }
        neighbors[1] = carbonRight;
        bounds[1] = rightBounds;
    }

    @Override
    public void setBothNeighbors(CarbonBase carbonLeft, CarbonBase carbonRight, int leftBounds, int rightBounds) {
        neighbors[0] = carbonLeft;
        neighbors[1] = carbonRight;
        bounds[0] = leftBounds;
        bounds[1] = rightBounds;
    }

    @Override
    public void calculateShape() {
        int atomGroups = 4 - (bounds[0] + bounds[1]) + (neighbors[0] == null ? 0 : 1) + (neighbors[1] == null ? 0 : 1);
        switch (atomGroups) {
            case 4:
                carbonShape = HydroCarbon.subMoleculeShape.TETRA;
                break;
            case 3:
                carbonShape = HydroCarbon.subMoleculeShape.TRIGONAL;
                break;
            case 2:
                carbonShape = HydroCarbon.subMoleculeShape.LINEAR;
        }

        if (neighbors[1] != null) {
            neighbors[1].calculateShape();

        }

    }

    @Override
    public boolean hasNext() {
        return neighbors[1] == null ? false : true;
    }

    @Override
    public boolean hasPrevious() {
        return neighbors[0] == null ? false : true;
    }

    @Override
    public void setIsCycloEndPoint(boolean b) {
        throw new UnsupportedOperationException("Wrong class for cyclic insertion"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getIsCyclicEndpoint() {
        return false;
    }

}
