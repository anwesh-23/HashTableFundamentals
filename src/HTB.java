import java.util.*;

public class HTB {

    private Map<String,Integer> freq = new HashMap<>();

    public void addQuery(String q){
        freq.put(q, freq.getOrDefault(q,0)+1);
    }

    public List<String> search(String prefix){
        return freq.keySet().stream()
                .filter(q -> q.startsWith(prefix))
                .sorted((a,b)->freq.get(b)-freq.get(a))
                .limit(5)
                .toList();
    }

    public static void main(String[] args){
        HTB ac = new HTB();
        ac.addQuery("java tutorial");
        ac.addQuery("javascript");
        ac.addQuery("java download");

        System.out.println(ac.search("jav"));
    }
}