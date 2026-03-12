import java.util.*;

public class ECommerce {


    private HashMap<String, Integer> stock;


    private HashMap<String, LinkedList<Integer>> waitingList;

    public ECommerce() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
    }


    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }


    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }


    public synchronized String purchaseItem(String productId, int userId) {

        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {
            stock.put(productId, available - 1);
            return "Success, " + (available - 1) + " units remaining";
        }
        else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }


    public void showWaitingList(String productId) {
        LinkedList<Integer> queue = waitingList.get(productId);

        if (queue.isEmpty()) {
            System.out.println("Waiting list empty");
        } else {
            System.out.println("Waiting List: " + queue);
        }
    }


    public static void main(String[] args) {

        ECommerce store = new ECommerce();

        store.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + store.checkStock("IPHONE15_256GB") + " units available");

        System.out.println(store.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(store.purchaseItem("IPHONE15_256GB", 67890));


        for (int i = 0; i < 100; i++) {
            store.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(store.purchaseItem("IPHONE15_256GB", 99999));

        store.showWaitingList("IPHONE15_256GB");
    }
}