import java.util.*;

public class HTB {

    // Maximum cache size (for LRU eviction)
    private static final int MAX_CACHE_SIZE = 5;

    // Cache: domain -> entry
    private LinkedHashMap<String, DNSEntry> cache;

    // statistics
    private int hits = 0;
    private int misses = 0;

    public HTB() {

        // accessOrder=true enables LRU eviction
        cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    // DNS Entry class
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, long ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // resolve domain
    public String resolve(String domain) {

        long start = System.nanoTime();

        // check cache
        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                long time = System.nanoTime() - start;
                return "Cache HIT → " + entry.ipAddress +
                        " (" + time / 1_000_000.0 + " ms)";
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED for " + domain);
            }
        }

        // cache miss
        misses++;
        String ip = queryUpstreamDNS(domain);

        // store in cache with TTL 5 seconds (demo)
        cache.put(domain, new DNSEntry(domain, ip, 5));

        return "Cache MISS → Query upstream → " + ip;
    }

    // simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        try { Thread.sleep(100); } catch (Exception ignored) {}
        return "172.217." + (int)(Math.random()*100) + "." + (int)(Math.random()*255);
    }

    // cleanup expired entries
    public void cleanup() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    // statistics
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("\nCache Stats:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        HTB dnsCache = new HTB();

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));

        // wait to expire
        Thread.sleep(6000);

        System.out.println(dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}