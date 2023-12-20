package breach.logic;

public class BreachDecryptor {

    public static void decrypt(BreachTask breach) {
        try {
            Thread.sleep(1000);
            while (breach.solveAmount() < breach.level() && breach.state() == BreachTask.BreachState.RUNNING) {
                for (Integer[] entry : breach.window().master()) {
                    breach.updatedBreachPosition(entry[0], entry[1]);
                    Thread.sleep(25);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
