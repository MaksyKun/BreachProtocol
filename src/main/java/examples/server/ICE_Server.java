package examples.server;

import breach.logic.BreachEvent;
import breach.logic.BreachTask;

import java.util.Map;
import java.util.TreeMap;

public class ICE_Server {

    private static final int SecurityGrade = 3;
    private static final int EnhancedDuration = 3000;
    private final Map<String, BreachTask> breaches = new TreeMap<>();

    public void setupICE(String ip, boolean registered) {
        if(breaches.containsKey(ip)) return;
        System.out.println("Ip is not forwarded by firewall");
        if(!registered)
            System.out.println("Ip is not registered on firewall. Setup of Enhanced-ICE...");
        else
            System.out.println("Setup of Standard-ICE...");

        BreachTask task = new BreachTask(SecurityGrade);
        setEvents(task, ip, registered);
        breaches.put(ip, task);
        breaches.get(ip).run();
    }

    public void setEvents(BreachTask task, String ip, boolean registered) {
        task.createEvent(new BreachEvent() {
            @Override
            public void onBreachPostRunning(BreachTask breach) {
                if (registered) return;
               Thread thread = new Thread(() -> {
                   try {
                       Thread.sleep(EnhancedDuration);
                       runEnhancedBreach(ip);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
               thread.start();
            }

            @Override
            public void onBreachSuccess(BreachTask breach) {
                Daemon_Server.firewall.setIpResponse(ip, Firewall.FirewallResponse.Forwarded);
            }

            @Override
            public void onBreachFailed(BreachTask breach) {
                if (Daemon_Server.firewall.isUnregistered(ip)) {
                    Daemon_Server.firewall.setIpResponse(ip, Firewall.FirewallResponse.Denied);
                }
            }
        });
    }

    public void runEnhancedBreach(String ip) {
        BreachTask task = new BreachTask(SecurityGrade);
        breaches.get(ip).setState(BreachTask.BreachState.FAILED);
        setEvents(task, ip, false);
        breaches.put(ip, task);
        breaches.get(ip).run();
    }

    public boolean isBreachFinished(String ip) {
        return breaches.containsKey(ip) && breaches.get(ip).state() != BreachTask.BreachState.RUNNING;
    }

    public void removeBreach(String ip) {
        breaches.remove(ip);
    }
}
