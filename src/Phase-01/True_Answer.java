import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;

public class True_Answer {

    private static Scanner scanner;

    static class Answer_checker {

        private HashMap<Integer, Integer> map = new HashMap<>();

        public Answer_checker update_table(int number_of_data) {

            int number;
            for (int i = 0; i < number_of_data; i++) {
                number = scanner.nextInt();

                if (map.containsKey(number)) {
                    map.replace(number, map.get(number) + 1);
                } else map.put(number, 1);
            }

            return this;
        }

        public Answer_checker find_result(int number_of_data, int limit) {

            int number;
            for (int i = 0; i < number_of_data; i++) {
                number = scanner.nextInt();
                if (map.containsKey(number) && map.get(number) <= limit) map.remove(number);
            }

            return this;
        }

        public int print_result(PrintWriter writer) throws IOException {

            for (Integer key : map.keySet()) writer.write(key + "\n");
            return map.size();
        }
    }

//    public static void main(String[] args) throws IOException {
//
//        try (FileInputStream fis = new FileInputStream("src/resources/test-009264-phase-01.txt")) {
//            PrintWriter writer = new PrintWriter("src/resources/answers/answer-009264.txt");
//
//            int customise_number_size = 1;
//            scanner = new Scanner(fis);
//
//            Answer_checker answer_checker = new Answer_checker();
//
//            int n = scanner.nextInt();
//            int k = scanner.nextInt();
//
//            writer.write("n: " + n + " , k: " + k + "\n");
//            writer.write("customised by: " + customise_number_size + "\n");
//
//            LocalTime start = LocalTime.now();
//
//            int result_size = answer_checker.update_table(n)
//                    .find_result(n, k * customise_number_size)
//                    .print_result(writer);
//
//            LocalTime end = LocalTime.now();
//            writer.write("Time: " + Duration.between(start, end).getSeconds() + "\n");
//            writer.write("number of result: " + result_size + "\n");
//            writer.flush();
//        }
//    }
}
