import java.util.*;

public class TwoSum {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time; // timestamp

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                System.out.println("Pair Found: (" +
                        map.get(complement).id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with time window (1 hour)
    public void findTwoSumTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {
                    System.out.println("Time Window Pair: (" +
                            other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // K-Sum (simple recursion)
    public void findKSum(int k, int target) {
        List<Integer> result = new ArrayList<>();
        kSumHelper(0, k, target, result);
    }

    private void kSumHelper(int start, int k, int target, List<Integer> result) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum Combination: " + result);
            return;
        }

        if (k == 0 || start >= transactions.size()) {
            return;
        }

        for (int i = start; i < transactions.size(); i++) {

            result.add(transactions.get(i).id);

            kSumHelper(i + 1,
                    k - 1,
                    target - transactions.get(i).amount,
                    result);

            result.remove(result.size() - 1);
        }
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {

            if (entry.getValue().size() > 1) {

                System.out.println("Duplicate Transactions:");

                for (Transaction t : entry.getValue()) {
                    System.out.println("ID: " + t.id + ", Account: " + t.account);
                }
            }
        }
    }

    // Main test
    public static void main(String[] args) {

        TwoSum system = new TwoSum();

        system.addTransaction(new Transaction(1, 500, "Store A", "acc1", 1000));
        system.addTransaction(new Transaction(2, 300, "Store B", "acc2", 1500));
        system.addTransaction(new Transaction(3, 200, "Store C", "acc3", 2000));
        system.addTransaction(new Transaction(4, 500, "Store A", "acc4", 2500));

        System.out.println("Two-Sum Target 500:");
        system.findTwoSum(500);

        System.out.println("\nTwo-Sum Time Window:");
        system.findTwoSumTimeWindow(500, 3600000);

        System.out.println("\nK-Sum (k=3, target=1000):");
        system.findKSum(3, 1000);

        System.out.println("\nDuplicate Detection:");
        system.detectDuplicates();
    }
}