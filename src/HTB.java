import java.util.*;

public class HTB {

    private Map<String, Integer> pageViews = new HashMap<>();
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private Map<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void showDashboard() {

        System.out.println("\nTop Pages:");
        pageViews.entrySet().stream()
                .sorted((a,b)->b.getValue()-a.getValue())
                .limit(10)
                .forEach(e -> System.out.println(
                        e.getKey() + " - " + e.getValue() +
                                " views (" + uniqueVisitors.get(e.getKey()).size() + " unique)"
                ));

        System.out.println("\nTraffic Sources:");
        trafficSources.forEach((k,v) -> System.out.println(k + " : " + v));
    }

    public static void main(String[] args) {
        HTB analytics = new HTB();

        analytics.processEvent("/breaking-news","u1","google");
        analytics.processEvent("/breaking-news","u2","facebook");
        analytics.processEvent("/sports","u3","google");

        analytics.showDashboard();
    }
}