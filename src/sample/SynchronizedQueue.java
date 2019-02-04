package sample;

public class SynchronizedQueue {
    private  int PositionGet;
    private int PositionPut;
    private Object[] AllData;
    private int amountData;


    //Create the data for the queue
    SynchronizedQueue() {
        AllData = new Object[100];
        PositionGet = 0;
        PositionPut = 0;

    }


    //allow put() if space in data
    synchronized boolean put(Object NewData) {
        if (amountData >= 100) {
            return false;
        }

        //ADD DATA
        AllData[PositionPut] = NewData;
        amountData = amountData + 1;

        if (PositionPut < 99) {
            PositionPut = PositionPut + 1;
        } else {
            PositionPut = 0;
        }

        System.out.println("put " + NewData);
        return true;
    }


    //allow get() if no more space for data
    synchronized Object get() {
        if (amountData <= 0) {
            //System.out.println("Error: No more data! Please put().");
            return null;
        }

        //REMOVE DATA
        int originalPositionGet = PositionGet;
        amountData = amountData - 1;

        if (PositionGet < 99) {
            PositionGet = PositionGet + 1;

        } else {
            PositionGet = 0;
        }
        System.out.println("get " + AllData[originalPositionGet]);
        return AllData[originalPositionGet];


    }

}
