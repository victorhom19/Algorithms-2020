package lesson1;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaTasks {

    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     * <p>
     * Пример:
     * <p>
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) throws IOException {
        //Трудоемкость O(NlogN)
        //Ресурсоемкость O(N)
        List<String> times = Files.readAllLines(Paths.get(inputName));
        times.sort(Comparator.comparing(JavaTasks::getTimeComparableValue));
        Files.write(Paths.get(outputName), times);
    }

    static private Integer getTimeComparableValue(String givenTime) throws IllegalArgumentException {
        try {
            String[] split = givenTime.substring(0, givenTime.length() - 3).split(":");
            int addition = 0;
            int hours = Integer.parseInt(split[0]) % 12;
            if (givenTime.charAt(givenTime.length() - 2) == 'P') {
                addition = 12;
            }
            int minutes = Integer.parseInt(split[1]);
            int seconds = Integer.parseInt(split[2]);
            return (((hours + addition) * 60) + minutes) * 60 + seconds;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format");
        }
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        //Трудоемкость O(NlogN)
        //Ресурсоемкость O(N)

        Comparator<String> byAddress = (o1, o2) -> {
            String street1 = o1.split(" ")[0];
            int building1 = Integer.parseInt(o1.split(" ")[1]);
            String street2 = o2.split(" ")[0];
            int building2 = Integer.parseInt(o2.split(" ")[1]);
            for (int i = 0; i < Math.min(street1.length(), street2.length()); i++) {
                char ch1 = street1.charAt(i);
                char ch2 = street2.charAt(i);
                if (ch1 != ch2) {
                    return ch1 - ch2;
                }
            }
            if (street1.length() == street2.length()) {
                return building1 - building2;
            }
            else {
                return street1.length() - street2.length();
            }
        };

        try {
            Map<String, TreeSet<String>> addresses = new TreeMap<>(byAddress);
            for (String line : Files.readAllLines(Paths.get(inputName))) {
                String key = line.split(" - ")[1];
                String value = line.split(" - ")[0];
                if (addresses.containsKey(key)) {
                    addresses.get(key).add(value);
                } else {
                    TreeSet<String> ts = new TreeSet<>();
                    ts.add(value);
                    addresses.put(key, ts);
                }
            }

            BufferedWriter wr = new BufferedWriter(new FileWriter(outputName));
            for (String key : addresses.keySet()) {
                wr.write(key + " - " + String.join(", ", addresses.get(key)));
                wr.newLine();
            }
            wr.flush();
            wr.close();

        } catch (Exception e) {
            throw new IllegalArgumentException("Street name has wrong format");
        }
    }


    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    static public void sortTemperatures(String inputName, String outputName) {
        //Трудоемкость O(N)
        //Ресурсоемкость O(1)

        try {
            int temperaturesRange = (273 + 500) * 10 + 1;
            int[] temperatures = new int[temperaturesRange];
            for (int i = 0; i < temperaturesRange; i ++) {
                temperatures[i] = 0;
            }
            BufferedReader rd = new BufferedReader(new FileReader(inputName));
            String line;
            while ((line = rd.readLine()) != null) {
                temperatures[(int) (Double.parseDouble(line) * 10) + 2730] += 1;
            }
            BufferedWriter wr = new BufferedWriter(new FileWriter(outputName));
            for (int i = 0; i < temperaturesRange; i ++) {
                if (temperatures[i] > 0) {
                    for (int j = 0; j < temperatures[i]; j ++) {
                        wr.write(((double) (i - 2730) / 10) + System.lineSeparator());
                    }
                }
            }
            wr.flush();
            wr.close();
        } catch (Exception e) {
            throw (new IllegalArgumentException("Temperature value is out of bounds or has invalid format"));
        }

    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */

    static public void sortSequence(String inputName, String outputName) throws IOException {
        //Трудоемкость O(NlogN)
        //Ресурсоемкость O(N)

        List<Integer> numSequence = new ArrayList<>();
        Map<Integer, Integer> countMap = new TreeMap<>();
        BufferedReader rd = new BufferedReader(new FileReader(inputName));
        String line;
        while ((line = rd.readLine()) != null) {
            int key = Integer.parseInt(line);
            numSequence.add(key);
            if (countMap.containsKey(key)) {
                int count = countMap.get(key) + 1;
                countMap.put(key, count);
            } else {
                countMap.put(key, 1);
            }
        }
        int mxCount = 0;
        int mxKey = 0;
        for (Integer key : countMap.keySet()) {
            if (countMap.get(key) > mxCount) {
                mxCount = countMap.get(key);
                mxKey = key;
            }
        }

        final int finalMxKey = mxKey;
        numSequence = numSequence.stream().filter(p -> !p.equals(finalMxKey)).collect(Collectors.toList());
        numSequence.addAll(Collections.nCopies(mxCount, mxKey));
        Files.write(Paths.get(outputName), numSequence.stream().map(String::valueOf).collect(Collectors.toList()));
        rd.close();
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */

    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        //Трудоемкость O(N)
        //Ресурсоемкость O(1)

        int i = 0;
        int index = 0;
        int j = first.length;
        while (i != first.length) {
            if (j == second.length || first[i].compareTo(second[j]) < 0) {
                second[index] = first[i];
                i++;
            } else {
                second[index] = second[j];
                j++;
            }
            index++;
        }
    }
}
