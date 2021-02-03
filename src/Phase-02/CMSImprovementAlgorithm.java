import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class CMSImprovementAlgorithm {

    private static Scanner scanner;

    private static int number_of_hash_function = 4;
    private static final int hashMap_maxi_size = 2500;
    private static int sample_registered = 500;
    private static int number_of_sample_byHash = 2000;

    static class CMSImprovement {

        static class Pair implements Comparable<Pair> {

            public int number;
            public int frequency;

            public Pair(int number, int frequency) {
                this.number = number;
                this.frequency = frequency;
            }

            public int Get_number() {
                return number;
            }

            @Override
            public int compareTo(Pair o) {
                return Integer.compare(frequency, o.frequency);
            }
        }

        private int[][] table = new int[number_of_sample_byHash][number_of_hash_function];
        private int[] hash_mods = new int[]{1987, 1993, 1997, 1999};
        private HashMap<Integer, Pair> map = new HashMap<>();
        private PriorityQueue<Pair> queue = new PriorityQueue<>();
        private int[] registered = new int[sample_registered];

        public CMSImprovement update_table(int number_of_data) {

            int number, minNum, score;
            for (int i = 1; i <= number_of_data; i++) {

                number = scanner.nextInt();
                score = scanner.nextInt();
                registered[i % sample_registered] = number;
                for (int j = 0; j < number_of_hash_function; j++) table[number % hash_mods[j]][j] += score;

                if (map.containsKey(number)) {
                    if (map.get(number).frequency < Integer.MAX_VALUE - score) map.get(number).frequency += score;
                    else map.get(number).frequency = Integer.MAX_VALUE;
                }

                if (i % sample_registered == 0 || i == number_of_data) {

                    if (i == number_of_data) {
                        sample_registered = number_of_data - number_of_data / sample_registered * sample_registered;
                    }

                    for (int j = 0; j < sample_registered; j++) {
                        minNum = Integer.MAX_VALUE;
                        number = registered[j];

                        for (int k = 0; k < number_of_hash_function; k++) {
                            if (table[number % hash_mods[k]][k] < minNum) minNum = table[number % hash_mods[k]][k];
                        }

                        if (map.size() <= hashMap_maxi_size || Objects.requireNonNull(queue.peek()).frequency < minNum) {

                            if (!map.containsKey(number)) {
                                Pair pair = new Pair(number, minNum);
                                map.put(number, pair);
                                queue.add(pair);
                            }
                        }

                        if (map.size() > hashMap_maxi_size) {
                            map.remove(Objects.requireNonNull(queue.poll()).number);
                        }
                    }
                }
            }
            return this;
        }

        public void print_result(int numbers) {
            queue.stream().map(Pair::Get_number).sorted(Comparator.comparing(integer -> -map.get(integer).frequency))
                    .limit(numbers).forEach(System.out::println);
        }
    }

    public static void main(String[] args) throws IOException {

        try (FileInputStream fis = new FileInputStream("src/resources/Test/test-213452-phase-02.txt")) {

            CMSImprovement countMinSketch = new CMSImprovement();
            scanner = new Scanner(fis);

            int n = scanner.nextInt();
            int k = scanner.nextInt();

            System.out.println("n: " + n + " ,k: " + k);

            LocalTime start = LocalTime.now();

            countMinSketch.update_table(n).print_result(k);

            LocalTime end = LocalTime.now();
            System.out.println("Time: " + Duration.between(start, end).getSeconds());
        }
    }
}
