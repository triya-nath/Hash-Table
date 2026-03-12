import java.util.HashMap;

public class RateLimiter {

    // Token bucket class
    static class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate; // tokens per second
        long lastRefillTime;

        public TokenBucket(int maxTokens, double refillRate) {
            this.tokens = maxTokens;
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on time passed
        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = ((now - lastRefillTime) / 1000.0) * refillRate;

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
                lastRefillTime = now;
            }
        }

        // Check if request allowed
        public synchronized boolean allowRequest() {
            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }

            return false;
        }

        public int getRemainingTokens() {
            refill();
            return tokens;
        }
    }

    // clientId -> token bucket
    private HashMap<String, TokenBucket> clients;

    public RateLimiter() {
        clients = new HashMap<>();
    }

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(1000, 1000.0 / 3600));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry later)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "No usage yet";
        }

        int used = bucket.maxTokens - bucket.getRemainingTokens();

        return "{used: " + used + ", limit: " + bucket.maxTokens + "}";
    }

    // Test program
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(client));
        }

        System.out.println(limiter.getRateLimitStatus(client));
    }
}