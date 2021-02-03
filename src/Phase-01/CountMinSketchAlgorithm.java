import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CountMinSketchAlgorithm {

    private static Scanner scanner;

    private static int number_of_hash_function = 4;
    private static final int hashMap_maxi_size = 2000;
    private static int sample_registered = 500;
    private static int number_of_sample_byHash = 2000;

    static class CountMinSketch {

        static class Pair implements Comparable<Pair> {

            public int number;
            public int frequency;

            public Pair(int number, int frequency) {
                this.number = number;
                this.frequency = frequency;
            }

            @Override
            public int compareTo(Pair o) {
                return Integer.compare(frequency, o.frequency);
            }
        }

        private int[][] table = new int[number_of_sample_byHash][number_of_hash_function];
        private int[] hash_mods = new int[]{1987 ,1993, 1997, 1999};
        private HashMap<Integer, Pair> map = new HashMap<>();
        private PriorityQueue<Pair> queue = new PriorityQueue<>();
        private int[] registered = new int[sample_registered];

        public CountMinSketch update_table(int number_of_data, int limit) {

            int number, minNum;
            for (int i = 1; i <= number_of_data; i++) {

                number = scanner.nextInt();
                registered[i % sample_registered] = number;
                for (int j = 0; j < number_of_hash_function; j++) table[number % hash_mods[j]][j] += 1;

                if (map.containsKey(number)) map.get(number).frequency++;

                if (i == number_of_data) {
                    sample_registered = number_of_data - number_of_data / sample_registered * sample_registered;
                }

                if (i % sample_registered == 0 || i == number_of_data) {

                    int new_limit = (int) Math.ceil(1.0 * limit * i / number_of_data);

                    for (int j = 0; j < sample_registered; j++) {
                        minNum = Integer.MAX_VALUE;
                        number = registered[j];

                        for (int k = 0; k < number_of_hash_function; k++) {
                            if (table[number % hash_mods[k]][k] < minNum) minNum = table[number % hash_mods[k]][k];
                        }

                        if (minNum > new_limit && !map.containsKey(number)) {
                            Pair pair = new Pair(number, minNum);
                            map.put(number, pair);
                            queue.add(pair);
                        }

                        if (map.size() > hashMap_maxi_size) {
                            map.remove(Objects.requireNonNull(queue.poll()).number);
                        }
                    }

                    for (int j = 0; j < sample_registered; j++) {
                        number = registered[j];
                        for (int k = 0; k < number_of_hash_function; k++) {
                            if (table[number % hash_mods[k]][k] > new_limit)
                                table[number % hash_mods[k]][k] = new_limit;
                        }
                    }
                }
            }
            return this;
        }

        public CountMinSketch find_result(int number_of_data) {

            int number;
            for (int i = 0; i < number_of_data; i++) {
                number = scanner.nextInt();
                if (map.containsKey(number)) map.get(number).frequency++;
            }

            return this;
        }

        public CountMinSketch reset_map() {
            for (Integer key : map.keySet()) map.get(key).frequency = 0;
            return this;
        }

        public int print_result(int limit) {

            int result_size = 0;
            for (Integer key : map.keySet()) {
                if (map.get(key).frequency > limit) System.out.println(++result_size + ":" + key);
            }

            return result_size;
        }
    }

//    public static void main(String[] args) throws IOException {
//
//        try (FileInputStream fis = new FileInputStream("src/resources/test-000216-phase-01.txt")) {
//
//            scanner = new Scanner(fis);
//
//            CMSImprovement countMinSketch = new CMSImprovement();
//
//            int n = scanner.nextInt();
//            int k = scanner.nextInt();
//
//            System.out.println("n: " + n + " ,k: " + k);
//
//            LocalTime start = LocalTime.now();
//
//            int result_size = countMinSketch.update_table(n, k)
//                    .reset_map().find_result(n).print_result(k);
//
//            LocalTime end = LocalTime.now();
//            System.out.println("Time: " + Duration.between(start, end).getSeconds());
//            System.out.println("map size: " + countMinSketch.map.size());
//            System.out.println("number of result: " + result_size);
//        }
//    }
}
