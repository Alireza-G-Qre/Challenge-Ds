import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

//only sample code to start ---> to be improved


public class GoodCMS {

    static class MyPair implements Comparable<MyPair> {

        public int number;
        public int freq;

        public MyPair(int number, int freq) {
            this.number = number;
            this.freq = freq;
        }

        @Override
        public int compareTo(MyPair o) {
            return Integer.compare(freq, o.freq);
        }
    }

    static int[] arrModeTo_1499 = new int[1500];
    static int[] arrModeTo_1493 = new int[1500];
    static int[] arrModeTo_1489 = new int[1500];
    static int[] arrModeTo_1487 = new int[1500];
    static int[] arrSample = new int[1000];
    static HashMap<Integer, MyPair> hashMap = new HashMap<>();
    static PriorityQueue<MyPair> pQueue = new PriorityQueue<>();

    public static void main(String[] args) throws IOException {

        //reading from file
        try (FileInputStream fis = new FileInputStream("src/test-000216-phase-01.txt")) {

            Scanner scanner = new Scanner(fis);

            int n = Integer.parseInt(scanner.next());
            int k = Integer.parseInt(scanner.next());
            int counter = 0;

            System.out.println("n= " + n + " and k= " + k);
            LocalTime start = LocalTime.now();

            //main iteration
            for (int j = 1; j <= n; j++) {

                int a = Integer.parseInt(scanner.next());
                hash(a, 1499, arrModeTo_1499);
                hash(a, 1493, arrModeTo_1493);
                hash(a, 1489, arrModeTo_1489);
                hash(a, 1487, arrModeTo_1487);
                arrSample[j % 1000] = a;

                if (hashMap.containsKey(a)) {
                    hashMap.get(a).freq++;
                }

                if (j % 1000 == 0 || j == n) {

                    int maxCounter = 1000;
                    if (j == n) maxCounter = n - n / 1000 * 1000;

                    for (int i = 0; i < maxCounter; i++) {
                        int b = arrSample[i];
                        int min = calculateMin(b);
                        if (min > k * ((j * 1.0) / n) && !hashMap.containsKey(b)) {

                            MyPair pair = new MyPair(b, min);
                            hashMap.put(b, pair);
                            pQueue.add(pair);

                            if (hashMap.size() > 2000) {
                                min = Objects.requireNonNull(pQueue.poll()).number;
                                hashMap.remove(min);
                            }
                        }
                    }

                    int limit = (int) (k * ((j * 1.0) / n));
                    for (int i = 0; i < 1000; i++) {
                        a = arrSample[i];
                        update(a, 1499, arrModeTo_1499, limit);
                        update(a, 1493, arrModeTo_1493, limit);
                        update(a, 1489, arrModeTo_1489, limit);
                        update(a, 1487, arrModeTo_1487, limit);
                    }
                }
            }


            //second iteration
            for (int key : hashMap.keySet()) {
                hashMap.get(key).freq = 0;
            }

            for (int i = 0; i < n; i++) {
                int a = Integer.parseInt(scanner.next());
                if (hashMap.containsKey(a)) {
                    hashMap.get(a).freq++;
                }
            }
            //final resault
            for (int key : hashMap.keySet()) {
                if (hashMap.get(key).freq > k) {
                    counter++;
                    System.out.println(key);
                }
            }
            //sout
            LocalTime end = LocalTime.now();
            System.out.println("time to calculate: " + Duration.between(start, end).getSeconds());
            System.out.println("size of the hashMap: " + hashMap.size());
            System.out.println("number of result >= k : " + counter);
        }
    }

    private static int calculateMin(int a) {
        int[] arrModes = {
                arrModeTo_1499[a % 1499],
                arrModeTo_1493[a % 1493],
                arrModeTo_1489[a % 1489],
                arrModeTo_1487[a % 1487],
        };
        int min = Integer.MAX_VALUE;
        for (int l = 0; l < 4; l++) {
            if (arrModes[l] < min) {
                min = arrModes[l];
            }
        }
        return min;
    }

    public static void hash(int number, int mode, int[] arr) {
        arr[number % mode]++;
    }

    public static void update(int number, int mode, int[] arr, int limitPrime) {
        if (arr[number % mode] > limitPrime) {
            arr[number % mode] = limitPrime;
        }
    }
}



