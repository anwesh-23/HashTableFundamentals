import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HTB {

    // productId -> stock count
    private ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    // waiting list (FIFO)
    private LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    public HTB() {
        inventory.put("IPHONE15_256GB", new AtomicInteger(100));
    }

    // check stock in O(1)
    public int checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        return stock != null ? stock.get() : 0;
    }

    // process purchase request safely
    public synchronized String purchaseItem(String productId, int userId) {

        AtomicInteger stock = inventory.get(productId);

        if (stock == null) {
            return "Product not found";
        }

        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        } else {
            waitingList.put(userId, productId);
            return "Added to waiting list, position #" + waitingList.size();
        }
    }

    // display waiting list
    public void showWaitingList() {
        System.out.println("\nWaiting List:");
        waitingList.forEach((user, product) ->
                System.out.println("User " + user + " waiting for " + product));
    }

    public static void main(String[] args) {

        HTB manager = new HTB();

        System.out.println("checkStock(\"IPHONE15_256GB\") -> "
                + manager.checkStock("IPHONE15_256GB") + " units available");

        // simulate multiple users purchasing
        for (int i = 1; i <= 102; i++) {
            System.out.println("User " + i + " -> "
                    + manager.purchaseItem("IPHONE15_256GB", i));
        }

        manager.showWaitingList();
    }
}