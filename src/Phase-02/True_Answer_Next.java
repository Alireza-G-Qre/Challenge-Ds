import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class True_Answer_Next {

    private static Scanner scanner;

    static class Answer_checker {

        static class Pair implements Comparable<Pair> {

            public int number;
            public int frequency;

            public Pair(int number, int frequency) {
                this.number = number;
                this.frequency = frequency;
            }

            @Override
            public int compareTo(Pair o) {
                return Integer.compare(o.frequency, frequency);
            }

            @Override
            public String toString() {
                return "Pair{" +
                        "number=" + number +
                        ", frequency=" + frequency +
                        '}';
            }
        }

        private HashMap<Integer, Pair> map = new HashMap<>();
        private PriorityQueue<Pair> queue = new PriorityQueue<>();

        public Answer_checker update_table(int number_of_data) {

            int number, score;
            for (int i = 0; i < number_of_data; i++) {

                number = scanner.nextInt();
                score = scanner.nextInt();

                if (map.containsKey(number)) {
                    if (map.get(number).frequency < Integer.MAX_VALUE - score) map.get(number).frequency += score;
                    else map.get(number).frequency = Integer.MAX_VALUE;
                }
                else {
                    Pair pair = new Pair(number, score);
                    map.put(number, pair);
                    queue.add(pair);
                }
            }
            return this;
        }

        public void find_result_print(int numbers, PrintWriter writer) {
            int max_number;
            for (int i = 0; i < numbers && map.size() != 0; i++) {
                max_number = Objects.requireNonNull(queue.poll()).number;
                map.remove(max_number);
                writer.write(max_number + "\n");
            }
        }
    }

    public static void generate(File src, File dest) {

        try (FileInputStream fis = new FileInputStream(src)) {
            PrintWriter writer = new PrintWriter(dest);

            scanner = new Scanner(fis);

            Answer_checker answer_checker = new Answer_checker();

            int n = scanner.nextInt();
            int k = scanner.nextInt();

            writer.write("n: " + n + " , k: " + k + "\n");

            LocalTime start = LocalTime.now();

            answer_checker.update_table(n).find_result_print(k, writer);

            LocalTime end = LocalTime.now();
            writer.write("Time: " + Duration.between(start, end).getSeconds() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Hi ...");
        }
    }

    public static void main(String[] args) throws IOException {
        Files.createDirectory(Paths.get("src/resources/Answer"));
        Pattern pattern = Pattern.compile("test-(\\d+)-phase-02.txt");
        Files.walk(Paths.get("src/resources/Test")).forEach(path -> {

            File src = path.toFile();
            Matcher matcher = pattern.matcher(src.getName());

            if (matcher.find()) {
                String temp = String.format("src/resources/Answer/answer-%s.txt", matcher.group(1));
                generate(src, new File(temp));
                System.out.println(temp + " generated.");
            }
        });
    }
}
