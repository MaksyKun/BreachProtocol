import breach.BreachTask;
import settings.SecurityProperties;

public class BreachApi {

    public SecurityProperties properties = new SecurityProperties();
    public BreachApi instance;

    public BreachApi() {
        instance = this;
    }

    public BreachApi(SecurityProperties properties) {
        instance = this;
        this.properties = properties;
    }

    public BreachTask createBreach(int level) {
        return new BreachTask(level);
    }
}
