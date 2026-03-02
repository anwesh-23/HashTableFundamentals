import java.util.*;

public class HTB {

    class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefill;

        TokenBucket(int max) {
            maxTokens = max;
            tokens = max;
            lastRefill = System.currentTimeMillis();
        }
    }

    private Map<String, TokenBucket> clients = new HashMap<>();
    private static final int LIMIT = 5; // demo limit

    public boolean allowRequest(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT));
        TokenBucket bucket = clients.get(clientId);

        long now = System.currentTimeMillis();

        if (now - bucket.lastRefill > 60000) { // refill every minute
            bucket.tokens = bucket.maxTokens;
            bucket.lastRefill = now;
        }

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        HTB limiter = new HTB();

        for(int i=1;i<=7;i++)
            System.out.println("Request "+i+" → "+limiter.allowRequest("abc"));
    }
}