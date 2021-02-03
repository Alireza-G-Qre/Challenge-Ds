import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class TestGen {
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        Files.createDirectory(Paths.get("src/resources/Test"));
        for (int kalam = 0; kalam < 10; kalam++) {
            int k = 2001 + random.nextInt(400) - 200;
            int m = random.nextInt((int) Math.pow(10, 7)) + 4 * (int) Math.pow(10, 7);

            int element;
            int[] arr = new int[m];
            int[] result = new int[m];
            for (int i = 0; i < m; i++) {
                result[i] = 0;
            }
            int state;
            int numberOfFrequented = random.nextInt((k / 5) + k / 4);
            int[] frequencies = new int[numberOfFrequented];
            for (int i = 0; i < numberOfFrequented; i++) {
                frequencies[i] = random.nextInt(m - 77);
            }
            int numberOfFakeFrequented = random.nextInt(k / 5) + k / 4;
            int[] fakeFreq = new int[numberOfFakeFrequented];
            for (int i = 0; i < numberOfFakeFrequented; i++) {
                while (true) {
                    fakeFreq[i] = random.nextInt(m - 77);
                    boolean breakCon = true;
                    for (int q = 0; q < numberOfFrequented; q++) {
                        if (fakeFreq[i] == frequencies[q]) {
                            breakCon = false;
                            break;
                        }
                    }
                    for (int q = 0; q < i; q++) {
                        if (fakeFreq[i] == fakeFreq[q]) {
                            breakCon = false;
                            break;
                        }
                    }
                    if (breakCon)
                        break;
                }
            }

            int index;
            for (int i = 0; i < m; i++) {
                state = random.nextInt(m);
                if (state < numberOfFrequented * (1.2 * m / k + 100)) {
                    index = (int) Math.floor(state / (1.2 * m / k + 100));
                    element = frequencies[index];
                } else {
                    double v = numberOfFrequented * (1.1 * m / k + 100);
                    if (state < v + numberOfFakeFrequented * (0.8 * m / k + 100)) {
                        index = (int) Math.floor((state - v) / (0.8 * m / k + 100));
                        element = fakeFreq[index];
                    } else {
                        while (true) {
                            element = random.nextInt(m - 77);
                            boolean breakCon = true;
                            for (int q = 0; q < numberOfFrequented; q++) {
                                if (element == frequencies[q]) {
                                    breakCon = false;
                                    break;
                                }
                            }
                            for (int q = 0; q < numberOfFakeFrequented; q++) {
                                if (element == fakeFreq[q]) {
                                    breakCon = false;
                                    break;
                                }
                            }
                            if (breakCon)
                                break;
                        }
                    }
                }
                arr[i] = element;
                result[element] += 1;
            }

            StringBuilder arrayBuilder = new StringBuilder();
            for (int i = 0; i < m; i++) {
                arrayBuilder.append(arr[i]).append(" ").append(random.nextInt(81) - 40).append('\n');
            }

            String array = arrayBuilder.toString();
            String header = String.valueOf(m) + ' ' + k + '\n';
            try (PrintWriter out = new PrintWriter(String.format("src/resources/Test/test-%06d-phase-02.txt", random.nextInt(1000000)))) {
                out.print(header);
                out.print(array);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}