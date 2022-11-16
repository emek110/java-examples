import java.util.Collection;

class Percolation implements PercolationInterface {
    boolean end = false;
    boolean[][][] fabric;
    boolean[][][] test;
    Object[] finals;

    @Override
    public boolean percolation(Collection<Position3D> startPositions, Collection<Position3D> finalPositions, boolean[][][] fabric) {
        this.fabric = fabric;
        finals = finalPositions.toArray();
        test = new boolean[fabric.length][fabric[0].length][fabric[0][0].length];
        Object[] start = startPositions.toArray();
        for (int i = 0; i < start.length; i++) {
            Position3D tmp = (Position3D) start[i];
            moveV2(tmp);
            if(end) {
                return true;
            }
        }
        return false;
    }

    boolean test(Position3D left, Position3D right) {
        if(left.getX() == right.getX() && left.getY() == right.getY() && left.getZ() == right.getZ()) {
            end = true;
            return true;
        }
        return false;
    }

    void testV2(Position3D testing) {

        test[testing.getX()][testing.getY()][testing.getZ()] = true;

        Position3D tmp;
        for (int i = 0; i < finals.length; i++) {
            tmp = (Position3D) finals[i];
            if(test(testing,tmp))return;
        }
    }

    void moveV2(Position3D ending) {
        if(end) return;

        if(ending.getX()-1 >= 0 && fabric[ending.getX()-1][ending.getY()][ending.getZ()] && test[ending.getX()-1][ending.getY()][ending.getZ()] == false) {
            Position3D tmp = new Position3D(ending.getX()-1,ending.getY(),ending.getZ());
            testV2(tmp);
            moveV2(tmp);
        }
        if(ending.getY()-1 >= 0 && fabric[ending.getX()][ending.getY()-1][ending.getZ()] && test[ending.getX()][ending.getY()-1][ending.getZ()] == false) {
            Position3D tmp = new Position3D(ending.getX(),ending.getY()-1,ending.getZ());
            testV2(tmp);
            moveV2(tmp);
        }
        if(ending.getZ()-1 >= 0 && fabric[ending.getX()][ending.getY()][ending.getZ()-1] && test[ending.getX()][ending.getY()][ending.getZ()-1] == false) {
            Position3D tmp = new Position3D(ending.getX(),ending.getY(),ending.getZ()-1);
            testV2(tmp);
            moveV2(tmp);
        }

        if(ending.getX()+1 < fabric.length && fabric[ending.getX()+1][ending.getY()][ending.getZ()] && test[ending.getX()+1][ending.getY()][ending.getZ()] == false) {
            Position3D tmp = new Position3D(ending.getX()+1,ending.getY(),ending.getZ());
            testV2(tmp);
            moveV2(tmp);
        }
        if(ending.getY()+1 < fabric[0].length && fabric[ending.getX()][ending.getY()+1][ending.getZ()] && test[ending.getX()][ending.getY()+1][ending.getZ()] == false) {
            Position3D tmp = new Position3D(ending.getX(),ending.getY()+1,ending.getZ());
            testV2(tmp);
            moveV2(tmp);
        }
        if(ending.getZ()+1 < fabric[0][0].length && fabric[ending.getX()][ending.getY()][ending.getZ()+1] && test[ending.getX()][ending.getY()][ending.getZ()+1] == false){
            Position3D tmp = new Position3D(ending.getX(),ending.getY(),ending.getZ()+1);
            testV2(tmp);
            moveV2(tmp);
        }
    }

}
