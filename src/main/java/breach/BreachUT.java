package breach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BreachUT {

    private static final List<String> hexCodes = List.of("FF", "55", "1C", "BD", "E9", "7A");
    public static final Random random = new Random();

    public static List<String> getHexCodes() {
        List<String> entries = new ArrayList<>();
        entries.add("01");
        entries.add("02");
        entries.add("03");
        entries.add("04");
        entries.add("05");
        entries.add("06");
        entries.add("07");
        entries.add("08");
        entries.add("09");
        entries.add("1A");
        entries.add("1B");
        entries.add("1C");
        entries.add("1D");
        entries.add("1E");
        entries.add("1F");
        entries.add("2A");
        entries.add("2B");
        entries.add("2C");
        entries.add("2D");
        entries.add("2E");
        entries.add("2F");
        entries.add("3A");
        entries.add("3B");
        entries.add("3C");
        entries.add("3D");
        entries.add("3E");
        entries.add("3F");
        entries.add("4A");
        entries.add("4B");
        entries.add("4C");
        entries.add("4D");
        entries.add("4E");
        entries.add("4F");
        entries.add("5A");
        entries.add("5B");
        entries.add("5C");
        entries.add("5D");
        entries.add("5E");
        entries.add("5F");
        entries.add("6A");
        entries.add("6B");
        entries.add("6C");
        entries.add("6D");
        entries.add("6E");
        entries.add("6F");
        entries.add("7A");
        entries.add("7B");
        entries.add("7C");
        entries.add("7D");
        entries.add("7E");
        entries.add("7F");
        entries.add("8A");
        entries.add("8B");
        entries.add("8C");
        entries.add("8D");
        entries.add("8E");
        entries.add("8F");
        entries.add("9A");
        entries.add("9B");
        entries.add("9C");
        entries.add("9D");
        entries.add("9E");
        entries.add("9F");
        entries.add("A1");
        entries.add("A2");
        entries.add("A3");
        entries.add("A4");
        entries.add("A5");
        entries.add("A6");
        entries.add("A7");
        entries.add("A8");
        entries.add("A9");
        entries.add("B1");
        entries.add("B2");
        entries.add("B3");
        entries.add("B4");
        entries.add("B5");
        entries.add("B6");
        entries.add("B7");
        entries.add("B8");
        entries.add("B9");
        entries.add("C1");
        entries.add("C2");
        entries.add("C3");
        entries.add("C4");
        entries.add("C5");
        entries.add("C6");
        entries.add("C7");
        entries.add("C8");
        entries.add("C9");
        entries.add("D1");
        entries.add("D2");
        entries.add("D3");
        entries.add("D4");
        entries.add("D5");
        entries.add("D6");
        entries.add("D7");
        entries.add("D8");
        entries.add("D9");
        entries.add("E1");
        entries.add("E2");
        entries.add("E3");
        entries.add("E4");
        entries.add("E5");
        entries.add("E6");
        entries.add("E7");
        entries.add("E8");
        entries.add("E9");
        entries.add("F1");
        entries.add("F2");
        entries.add("F3");
        entries.add("F4");
        entries.add("F5");
        entries.add("F6");
        entries.add("F7");
        entries.add("F8");
        entries.add("F9");
        entries.add("AA");
        entries.add("AB");
        entries.add("AC");
        entries.add("AD");
        entries.add("AE");
        entries.add("AF");
        entries.add("BA");
        entries.add("BB");
        entries.add("BC");
        entries.add("BD");
        entries.add("BE");
        entries.add("BF");
        entries.add("CA");
        entries.add("CB");
        entries.add("CC");
        entries.add("CD");
        entries.add("CE");
        entries.add("CF");
        entries.add("DA");
        entries.add("DB");
        entries.add("DC");
        entries.add("DD");
        entries.add("DE");
        entries.add("DF");
        entries.add("EA");
        entries.add("EB");
        entries.add("EC");
        entries.add("ED");
        entries.add("EE");
        entries.add("EF");
        entries.add("FA");
        entries.add("FB");
        entries.add("FC");
        entries.add("FD");
        entries.add("FE");
        entries.add("FF");
        return entries;
    }

    public static BreachEntry[][] getHexMatrix(BreachWindow breach) {
        BreachEntry[][] entries = new BreachEntry[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
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
