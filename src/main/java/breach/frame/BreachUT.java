package breach.frame;

import settings.SecurityProperties;

import java.util.List;
import java.util.Random;

public class BreachUT {

    private static final List<String> hexCodes = List.of("FF", "55", "1C", "BD", "E9", "7A");
    public static final Random random = new Random();

    public static BreachEntry[][] getHexMatrix(BreachWindow breach) {
        BreachEntry[][] entries = new BreachEntry[SecurityProperties.breachSize][SecurityProperties.breachSize];
        for (int i = 0; i < SecurityProperties.breachSize; i++) {
            for (int j = 0; j < SecurityProperties.breachSize; j++) {
                entries[i][j] = new BreachEntry(breach, hexCodes.get(random.nextInt(0, hexCodes.size() - 1)), i, j);
            }
        }
        return entries;
    }

    public static String getRandomHexOrder(int amount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(hexCodes.get(random.nextInt(0, hexCodes.size() - 1)));
            if (i < amount - 1)
                builder.append("-");
        }
        return builder.toString();
    }
}
