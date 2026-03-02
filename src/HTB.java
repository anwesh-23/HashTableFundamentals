import java.util.*;

public class HTB {

    private LinkedHashMap<String,String> L1 =
            new LinkedHashMap<>(5,0.75f,true){
                protected boolean removeEldestEntry(Map.Entry e){
                    return size()>3;
                }
            };

    private Map<String,String> L2 = new HashMap<>();

    public HTB(){
        L2.put("video1","SSD");
        L2.put("video2","SSD");
    }

    public void getVideo(String id){

        if(L1.containsKey(id)){
            System.out.println("L1 HIT");
            return;
        }

        if(L2.containsKey(id)){
            System.out.println("L2 HIT → promoted to L1");
            L1.put(id,"MEM");
            return;
        }

        System.out.println("DB HIT → added to L2");
        L2.put(id,"SSD");
    }

    public static void main(String[] args){
        HTB cache = new HTB();
        cache.getVideo("video1");
        cache.getVideo("video1");
        cache.getVideo("video3");
    }
}