import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

import cnss.simulator.Node;
import ft21.FT21AbstractSenderApplication;
import ft21.FT21_AckPacket;
import ft21.FT21_DataPacket;
import ft21.FT21_FinPacket;
import ft21.FT21_UploadPacket;


public class FT21SenderSR extends FT21AbstractSenderApplication {

    private static final int TIMEOUT = 1000;

    static int RECEIVER = 1;

    enum State {
        BEGINNING, UPLOADING, FINISHING, FINISHED
    };

    static int DEFAULT_TIMEOUT = 1000;

    private File file;
    private RandomAccessFile raf;
    private int BlockSize;
    private int nextPacketSeqN, lastPacketSeqN;

    //size of window.
    private int windowsize;

    //last packet the sender sent
    private int lastPacketSent;

    //last ack received.
    private int lastACKReceived;

    //value of the negative ack received.
    private int negativeACK;

    // true if the last package (fin) was sent, false if not.
    private boolean lastSent = false;

    //true if the first package (upload) was sent, false if not.
    private boolean firstSent = false;

    //map of the packages that were sent.
    //The key is the number of the package and the object is his time.
    private SortedMap<Integer, WindowDataState> packets;

    private State state;

    public FT21SenderSR() {
        super(true, "FT21SenderSR");
    }

    public int initialise(int now, int node_id, Node nodeObj, String[] args) {
        super.initialise(now, node_id, nodeObj, args);

        raf = null;
        file = new File(args[0]);
        BlockSize = Integer.parseInt(args[1]);
        windowsize = Integer.parseInt(args[2]);
        packets = new TreeMap<>();
        negativeACK = -1;
        lastACKReceived = -1;
        lastPacketSent=-1;

        state = State.BEGINNING;
        lastPacketSeqN = (int) Math.ceil(file.length() / (double) BlockSize);

        return 1;
    }

    //on each time clock checks if occurred a time out of the first package of the map, checks if it has all the conditions needed to send the next package
    public void on_clock_tick(int now) {

        boolean timeout = timer(now);

        boolean canSend = ((packets.size()<windowsize) && (state != State.FINISHED) && (nextPacketSeqN<=lastPacketSeqN));

        if(packets.get(nextPacketSeqN)!=null && timeout){
            canSend=true;
        }

       if(!timeout){
           receivedNegativeACK();
       }

        sendFirst(now);

        sendLast(now);

        if(canSend && lastACKReceived >=0) {
            changeState();
            sendNextPacket(now);
            if (timeout) {
                nextPacketSeqN = lastPacketSent+1;
            } else {
                nextPacketSeqN++;
            }
        }


    }

    // sends the first package (Upload)
    private void sendFirst(int now){
        if(!firstSent) {
            if (lastACKReceived == -1) {
                sendNextPacket(now);
                nextPacketSeqN++;
                firstSent =true;
            }
        }
    }

    // sends the last package (Fin)
    private void sendLast(int now){
        if(!lastSent) {
            if (lastPacketSeqN == lastACKReceived) {
                changeState();
                sendNextPacket(now);
                lastSent = true;
            }
        }
    }

    // changes the state of the next packaging being sent depending on the number of the package
    private void changeState(){

        if (nextPacketSeqN == 0){
            state = State.BEGINNING;
        } else {
            if (nextPacketSeqN > lastPacketSeqN){
                state = State.FINISHING;
            } else {
                if(nextPacketSeqN>0){
                    state = State.UPLOADING;
                }
            }
        }

    }

    //the package that was received with negative ack is the next to be sent if not occurred a timeout
    private void receivedNegativeACK(){
        if(negativeACK>0){
            nextPacketSeqN = negativeACK;
            negativeACK = -1;
        }
    }

    /*checks if the first package (that was sent and didn't receive yet its ACK) has past the timeout value
        by comparing the time now with the time at it was sent
     */
    private boolean timer(int now){
        boolean hasTimeOut= false;

        if(!packets.isEmpty()) {
            Set<Integer> listKeys = packets.keySet();

            Iterator<Integer> it = listKeys.iterator();

            while (it.hasNext()&& !hasTimeOut) {
                int key = it.next();
                WindowDataState p = packets.get(key);

                if ((now - p.getTime()) > TIMEOUT && !p.getState()) {
                    if (nextPacketSeqN == lastPacketSeqN + 1) {
                        lastSent = false;
                    }
                    if (lastACKReceived == -1) {
                        firstSent = false;
                    }
                    nextPacketSeqN = key;
                    hasTimeOut = true;
                    super.on_timeout(now);
                }
            }
        }
        return hasTimeOut;
    }

    // sends the package and adds the time it was sent to the map of packages that didn't receive their ack
    private void sendNextPacket(int now) {
        switch (state) {
            case BEGINNING:
                super.sendPacket(now, RECEIVER, new FT21_UploadPacket(file.getName(),nextPacketSeqN, now));
                break;
            case UPLOADING:
                super.sendPacket(now, RECEIVER, readDataPacket(file, nextPacketSeqN, now));
                break;
            case FINISHING:
                super.sendPacket(now, RECEIVER, new FT21_FinPacket(nextPacketSeqN, nextPacketSeqN, now));
                break;
            case FINISHED:
        }

        if(packets.get(nextPacketSeqN) == null) {
            if(lastPacketSent < nextPacketSeqN){
                lastPacketSent = nextPacketSeqN;
            }
            packets.put(nextPacketSeqN, new WindowDataState(now));
        }else{
            packets.get(nextPacketSeqN).setTime(now);
        }

    }


    //Receives the ack from the receiver.
    //Updates the last ack received.
    //it can identify if the ack receives was outside the receiver window or if a package was lost.
    @Override
    public void on_receive_ack(int now, int client, FT21_AckPacket ack) {
        deleteAckReceived(ack.cSeqN);
        if(lastACKReceived < ack.cSeqN) lastACKReceived = ack.cSeqN;

        if(ack.optional_dataSEQ > ack.cSeqN) {
            if (ack.outsideWindow) {
                negativeACK = ack.optional_dataSEQ;
            } else {
                packets.get(ack.optional_dataSEQ).received();
            }
        }

        //if the ack received is the fin then state changes to finishing
        if(ack.cSeqN == lastPacketSeqN + 1){
            if(state == State.FINISHING){
                super.log(now, "All Done. Transfer complete...");
                super.printReport(now);
                state = State.FINISHED;
            }
        }

    }


    //deletes from the map the key with the same value as the ACK and all packets before it
    private void deleteAckReceived(int n){
        if(!packets.isEmpty()) {
            for (int i = 0; i <= n; i++) {
                packets.remove(i);
            }
        }

    }

    private FT21_DataPacket readDataPacket(File file, int seqN, int now) {
        try {
            if (raf == null)
                raf = new RandomAccessFile(file, "r");

            raf.seek(BlockSize * (seqN - 1));
            byte[] data = new byte[BlockSize];
            int nbytes = raf.read(data);
            return new FT21_DataPacket(seqN, data, nbytes, seqN, now);
        } catch (Exception x) {
            throw new Error("Fatal Error: " + x.getMessage());
        }
    }
}

