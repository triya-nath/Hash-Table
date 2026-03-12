import java.util.*;

public class RealTimeAnalysis {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews;

    // pageUrl -> unique users
    private HashMap<String, Set<String>> uniqueVisitors;

    // traffic source -> count
    private HashMap<String, Integer> trafficSources;

    public RealTimeAnalysis() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }

    // Process page view event
    public void processEvent(String url, String userId, String source) {

        // count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // track traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        // sort pages by visit count
        List<Map.Entry<String, Integer>> pages = new ArrayList<>(pageViews.entrySet());

        pages.sort((a, b) -> b.getValue() - a.getValue());

        int limit = Math.min(10, pages.size());

        for (int i = 0; i < limit; i++) {

            String url = pages.get(i).getKey();
            int views = pages.get(i).getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(
                    (i + 1) + ". " + url + " - " + views + " views (" + unique + " unique)"
            );
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.printf("%s: %.2f%%\n", entry.getKey(), percent);
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        RealTimeAnalysis analytics = new RealTimeAnalysis();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_789", "google");
        analytics.processEvent("/sports/championship", "user_123", "direct");
        analytics.processEvent("/sports/championship", "user_456", "google");

        analytics.getDashboard();
    }
}