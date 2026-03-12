import java.util.*;

class DNS {

    // Entry class for DNS records
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttl) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int MAX_CACHE_SIZE = 5;

    // LRU cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNS() {

        cache = new LinkedHashMap<String, DNSEntry>(MAX_CACHE_SIZE, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    // Simulated upstream DNS lookup
    private String queryUpstream(String domain) {

        // simulate different IPs
        int random = new Random().nextInt(255);
        return "172.217.14." + random;
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            }
            else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED → Querying upstream");
            }
        }

        // Cache miss
        misses++;

        String ip = queryUpstream(domain);
        DNSEntry newEntry = new DNSEntry(domain, ip, 5); // TTL 5 sec

        cache.put(domain, newEntry);

        return "Cache MISS → Upstream IP: " + ip;
    }

    // Remove expired entries
    public void cleanupExpired() {

        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    // Main method
    public static void main(String[] args) throws InterruptedException {

        DNS dns = new DNS();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000); // wait for TTL expiry

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}
