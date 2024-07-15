package examples.server;

import lombok.Getter;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class Daemon_Server {

    public static Firewall firewall;
    public static ICE_Server ice;

    public Daemon_Server() {
        DaemonWindow window = new DaemonWindow();
        initialize();
    }

    public void initialize() {
        firewall = new Firewall();
        ice = new ICE_Server();
    }

    public static void runDaemon(DaemonWindow window) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String ip = window.getCurrentIp();
                if(ice.isBreachFinished(ip)) {
                    timer.cancel();
                    ice.removeBreach(ip);
                    return;
                }
                if(ip.isEmpty()) return;
                firewall.setIpResponse(ip);
                if(firewall.isForwarded(ip)) return;
                if(firewall.isDenied(ip)) {
                    if(firewall.isUnregistered(ip)) {
                        // Enhanced ICE
                        ice.setupICE(ip, false);
                    } else {
                        // Standard ICE
                        ice.setupICE(ip, true);
                    }
                }
            }
        }, 50, 50);
    }

    public static String getCurrentIp() {
        return "222";
    }

    public static Daemon_Server daemon;

    public static void main(String[] args) {
        daemon = new Daemon_Server();
        //daemon.runDaemon();
    }
}
