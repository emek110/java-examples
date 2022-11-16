/**
 * Created by emek on 05.01.2018.
 */
public class Wrapper implements WrappingInterface {

    int numberOfSlots = 0;
    SourceInterface stream;
    boolean endOfData = false;
    int iterator = 0;

    @Override
    public void setNumberOfSlots(int slots) {
        numberOfSlots = slots;
    }

    @Override
    public void setSource(SourceInterface stream) {
        this.stream = stream;
    }

    @Override
    public Integer[] get() {
        Integer[] tabOut = new Integer[numberOfSlots];
        if(endOfData) return tabOut;

        try{
            for (int i = 0; i < numberOfSlots; i++) {
                tabOut[i] = new Integer(stream.getValue());
            }
            return tabOut;
        } catch (EndOfDataException e) {
            endOfData = true;
            return tabOut;
        } catch (TemporaryNoDataException e) {
            return getContinuum(tabOut);
        } catch (WaitException e) {
            try {
                Thread.sleep(e.doNothingTime);
                getContinuum(tabOut);
            } catch (InterruptedException e1) {}
        } catch (UrgentException e) {
            return tabOut;
        }
        return tabOut;
    }


    public Integer[] getContinuum(Integer[] tabOut) {
        try{
            int i = 0;
            while (tabOut[i]!=null) i++;
            for (; i < numberOfSlots; i++) {
                tabOut[i] = new Integer(stream.getValue());
            }
            return tabOut;
        } catch (EndOfDataException e) {
            endOfData = true;
            return tabOut;
        } catch (TemporaryNoDataException e) {
            return tabOut;
        } catch (WaitException e) {
            try {
                Thread.sleep(e.doNothingTime);
            } catch (InterruptedException e1) {}
        } catch (UrgentException e) {
            return tabOut;
        }
        return tabOut;
    }
}
