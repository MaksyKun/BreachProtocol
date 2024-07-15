package examples.server;

import java.util.*;

public class Firewall {
    private List<String> ipList = new ArrayList<>();
    private final Map<String, FirewallResponse> responses = new TreeMap<>();

    public void updateIpList(List<String> whitelist, List<String> blacklist) {
        this.ipList.addAll(whitelist);
        List<String> allIps = new ArrayList<>(whitelist);
        allIps.addAll(blacklist);
        allIps.forEach(this::setIpResponse);
    }

    public boolean setIpResponse(String ip) {
        if (ipList.contains(ip)) responses.put(ip, FirewallResponse.Forwarded);
        else responses.put(ip, FirewallResponse.Denied);
        return ipList.contains(ip);
    }

    public boolean setIpResponse(String ip, FirewallResponse response) {
        if(response == null) return false;
        responses.put(ip, response);
        return true;
    }

    public boolean isForwarded(String ip) {
        if(!responses.containsKey(ip)) {
            responses.put(ip, FirewallResponse.Unregistered);
            return false;
        }
        return responses.get(ip) == FirewallResponse.Forwarded;
    }

    public boolean isDenied(String ip) {
        if(!responses.containsKey(ip)) {
            responses.put(ip, FirewallResponse.Unregistered);
            return false;
        }
        return responses.get(ip) == FirewallResponse.Denied;
    }

    public boolean isUnregistered(String ip) {
        return !responses.containsKey(ip) || responses.get(ip) == FirewallResponse.Unregistered;
    }

    public enum FirewallResponse {
        Forwarded,
        Denied,
        Unregistered;
    }
}
