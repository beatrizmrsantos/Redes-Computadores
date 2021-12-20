
//responsible for the state of the packet
public class WindowDataState {

    //time of the packet when sent
    private int time;
    //if the packet was received this variable becomes true
    private boolean ackReceived;

    public WindowDataState(int time) {
        this.time = time;
        this.ackReceived = false;
    }

    //when the sender gets the ack of the corresponding packet number the method changes his state to received
    public void received() {
        this.ackReceived = true;
    }

    public int getTime() {
        return time;
    }

    public boolean getState() {
        return ackReceived;
    }

    public void setTime(int time) {
        this.time = time;
    }
}