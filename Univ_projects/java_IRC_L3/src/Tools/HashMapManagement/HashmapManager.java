package Tools.HashMapManagement;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class HashmapManager {
    /**
     * Adds the message to all queues
     * except for the current client represented by his socketIdentity
     * @param hashmapQueues The HashMap of clients
     * @param message The message that will be added
     * @throws InterruptedException Handling exception
     */
    public static void addMessageToQueues(HashMap<String, ArrayBlockingQueue<String>> hashmapQueues,
                                     String socketIdentity, String message) throws InterruptedException {
        ArrayBlockingQueue<String> currentClientQueue = hashmapQueues.get(socketIdentity);
        for( ArrayBlockingQueue<String> q : hashmapQueues.values())
            if( q != currentClientQueue )
                q.put( message );
    }

    /**
     *  Removes a client from this list of ArrayBlockingQueue
     *  based on it's key in the HashMap.
     * @param hashmapQueues The HashMap of clients
     * @param key the key referencing the client to remove
     */
    public static void removeClientQueueFromQueues(HashMap<String, ArrayBlockingQueue<String>> hashmapQueues,
                                                   String key ){
        hashmapQueues.remove( key );
    }
}
/*
////////////// HANDLE THE QUEUES for messages /////////////////////////////////////////
    public static void addMessageToQueues(String message) throws InterruptedException {
        for( ArrayBlockingQueue<String> q : hashmapQueues.values())
            q.put( message );
    }



 */