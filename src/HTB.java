import java.util.*;

public class HTB {

    String[] lot = new String[10];

    private int hash(String plate){
        return Math.abs(plate.hashCode()) % lot.length;
    }

    public void park(String plate){
        int idx = hash(plate);
        int probes = 0;

        while(lot[idx] != null){
            idx = (idx+1) % lot.length;
            probes++;
        }

        lot[idx] = plate;
        System.out.println(plate+" parked at "+idx+" probes:"+probes);
    }

    public static void main(String[] args){
        HTB p = new HTB();
        p.park("ABC123");
        p.park("ABC124");
    }
}