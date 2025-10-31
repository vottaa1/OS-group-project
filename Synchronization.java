

public class Synchronization {
	
    //Custom Lock (Mutex) for ensuring that only one thread can execute the critical section 
	//(for example: car's position) at one time 
    public static class CustomLock {
    	
        // Volatile ensures visibility of the lock state across all threads
        private volatile boolean isLocked = false;
        
        // Acquires the lock by spinning until it can set the flag.
        public void lock() {
            // Busy-wait (spinning) loop
            while (true) {
                if (!isLocked) {
                    // Attempt to acquire the lock 
                    isLocked = true;
                    return;
                }
                // Yield the processor to allow other threads to run, reducing contention
                Thread.yield(); 
            }
        }

        // Releases the lock.
        public void unlock() {
            isLocked = false;
        }
    }

    //Winner Flag to ensure only the first car is declared the winner. Then, it terminates other cars
    public static class WinnerFlag {
    	
    	// We use an internal CustomLock to protect the volatile winner reference.
        private volatile car winner = null;
        private final CustomLock lock = new CustomLock(); 
        
        //Attempts to set the winner. Only if the winner has not been set yet.
        //If not, return false.
        public boolean setWinner(car car) {
        	lock.lock(); // Acquire the lock
        	try {
            if (winner == null) {
                winner = car;
                return true;
            }
            return false;
        	} finally {
        		lock.unlock();} //Release the lock     
        }

        public car getWinner() {
            return winner;
        }

        public void reset() {
            winner = null;
        }
    }
}