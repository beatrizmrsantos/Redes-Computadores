public class WindowDataState {

    private int time;
    private boolean sent;

    public WindowDataState(int time) {
        this.time = time;
        this.sent = false;
    }

    public void received() {
        this.sent = true;
    }

    public int getTime() {
        return time;
    }

    public boolean getState() {
        return sent;
    }

    public void setTime(int time) {
        this.time = time;
    }
}