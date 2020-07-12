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
public abstract class CarbonBase {

    protected CarbonBase[] neighbors;
    protected int[] bounds;
    protected HydroCarbon.subMoleculeShape carbonShape;
    public CarbonBase(){
        neighbors= new CarbonBase[2];
        bounds = new int[2];
    }
    
    public abstract void setCarb(CarbonBase carbonLeft, CarbonBase carbonRight, int rightBounds);

    public abstract void setBothNeighbors(CarbonBase carbonLeft, CarbonBase carbonRight, int leftBounds, int rightBounds);

    public abstract int getHnumber();

    public abstract int getOwnHnumber();

    public abstract void calculateShape();
    
    public abstract boolean hasNext();

    public abstract boolean hasPrevious();

    public abstract void setIsCycloEndPoint(boolean b);
    
    public void setPrevious(CarbonBase previous , int leftBounds){
        neighbors[0] = previous;
        bounds[0] = leftBounds;
    }
    public void setNext(CarbonBase next,int rightBounds){
        neighbors[1] = next;
        bounds[1] = rightBounds;
    }
    
    public  String toString(){
        return "LEFT BOUNDS " + bounds[0] + " RIGHT BOUNDS " + bounds[1];
        
    }
    public HydroCarbon.subMoleculeShape getShape() {
        return carbonShape;
    }

    public int getRightBounds() {
        return bounds[1];
    }

    public int getLeftBounds() {
        return bounds[0];
    }
    public abstract boolean getIsCyclicEndpoint();
    


}
