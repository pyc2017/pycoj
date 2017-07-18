import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Main1 {
    public static void main(String[] args){
        ThreadMXBean BEAN= ManagementFactory.getThreadMXBean();
        Thread sub=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.main(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sub.start();
        long cost=0L, temp=0L;
        do {
            try {
                Thread.sleep(100);
                temp=BEAN.getThreadUserTime(sub.getId());
                if (temp!=-1){
                    cost=temp;
                }else{
                    break;
                }
            } catch (InterruptedException e) {
            }
        }while(sub.isAlive() && cost<=2000000000 );
        if (sub.isAlive()){
            System.exit(-1);
        }else{
            System.exit(  (int)
                    (
                        cost/1000000
                    )
            );
        }
    }
}