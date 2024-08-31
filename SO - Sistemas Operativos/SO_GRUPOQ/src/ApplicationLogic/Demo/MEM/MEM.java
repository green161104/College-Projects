package ApplicationLogic.Demo.MEM;
/*
Memory in a satellite system includes both volatile (RAM)
and non-volatile (storage) memory. RAM is used for temporary data storage
and is essential for running applications and processing data.
Non-volatile memory, such as flash memory, stores persistent data
and software programs even when the satellite is powered off.
 */

import ApplicationLogic.Demo.exceptions.*;

public class MEM {


    // This is a class that represents RAM, only the kernel knows of it's existence.

    /* represents the max size of RAM that the satellite has.
     */
    private final int RAM_SPEC = 200;
    /* Tracks the current amount of memory being used at the present time.
     */
    private int usedRAM;
    /* tracks if RAM is full or not.
     */
    private boolean isfull(){
        return usedRAM == RAM_SPEC;
    }

    public MEM(){
        this.usedRAM = 0;
    }

    public int getUsedRAM(){return usedRAM;}

    public synchronized void alocateMemmory(int memmoryToAllocate) {

        if(isfull() || usedRAM + memmoryToAllocate > RAM_SPEC) {
            throw new OutOfRAMException("there isn't enough RAM to perferm the task.");
        }

        this.usedRAM += memmoryToAllocate;

    }

    public synchronized void deallocateMemmory(int memmoryToDeallocate) {

        if (usedRAM - memmoryToDeallocate  < 0  ) {
            throw new RAMnegativeNumbers("there's been an error, RAM can never be lower tan zero.");
        }

        this.usedRAM -= memmoryToDeallocate;
    }
}
