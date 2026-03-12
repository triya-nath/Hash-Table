import java.util.*;

public class MultiLevelCache {

    // Video data class
    static class VideoData {
        String videoId;
        String content;

        VideoData(String id, String content) {
            this.videoId = id;
            this.content = content;
        }
    }

    // L1 cache (LRU)
    private LinkedHashMap<String, VideoData> L1;

    // L2 cache
    private HashMap<String, VideoData> L2;

    // L3 database
    private HashMap<String, VideoData> database;

    private int L1_CAPACITY = 10000;
    private int L2_CAPACITY = 100000;

    private int L1_hits = 0;
    private int L2_hits = 0;
    private int L3_hits = 0;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new HashMap<>();
        database = new HashMap<>();

        // simulate database
        for (int i = 1; i <= 1000; i++) {
            database.put("video_" + i,
                    new VideoData("video_" + i, "Video Content " + i));
        }
    }

    public VideoData getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            L1_hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 check
        if (L2.containsKey(videoId)) {
            L2_hits++;
            System.out.println("L2 Cache HIT → Promoted to L1");

            VideoData video = L2.get(videoId);
            L1.put(videoId, video);
            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if (database.containsKey(videoId)) {

            L3_hits++;
            System.out.println("L3 Database HIT → Added to L2");

            VideoData video = database.get(videoId);

            if (L2.size() >= L2_CAPACITY) {
                Iterator<String> it = L2.keySet().iterator();
                if (it.hasNext()) {
                    L2.remove(it.next());
                }
            }

            L2.put(videoId, video);

            return video;
        }

        System.out.println("Video not found");
        return null;
    }

    public void getStatistics() {

        int total = L1_hits + L2_hits + L3_hits;

        if (total == 0) {
            System.out.println("No requests yet.");
            return;
        }

        double L1_rate = (L1_hits * 100.0) / total;
        double L2_rate = (L2_hits * 100.0) / total;
        double L3_rate = (L3_hits * 100.0) / total;

        System.out.println("Cache Statistics:");
        System.out.printf("L1 Hit Rate: %.2f%%\n", L1_rate);
        System.out.printf("L2 Hit Rate: %.2f%%\n", L2_rate);
        System.out.printf("L3 Hit Rate: %.2f%%\n", L3_rate);
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}