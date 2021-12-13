
public class WindowDataState {

    private int time;
    //private boolean sent;

    public WindowDataState(int time){
        this.time = time;
       // this.sent = true;
    }

   /* public void changeStatus(){
        this.sent = !sent;
    }*/

    public int getTime(){
        return time;
    }

    /*public boolean getState(){
        return sent;
    }*/

    public void setTime(int time){
        this.time= time;
    }
}
