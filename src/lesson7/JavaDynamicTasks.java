package lesson7;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaDynamicTasks {

    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */


    public static String longestCommonSubSequence(String first, String second) {
        String result = "";
        int[][] lengths = new int[first.length() + 1][second.length() + 1];

        //Заполняем таблицу
        for (int j = 0; j <= second.length(); j ++) {
            for (int i= 0; i <= first.length(); i ++) {
                if (i == 0 || j == 0) {
                    lengths[i][j] = 0;
                } else if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    lengths[i][j] = lengths[i - 1][j - 1] + 1;
                } else {
                    lengths[i][j] = Math.max(lengths[i - 1][j], lengths[i][j - 1]);
                }
            }
        }

        //Идем с конца, записывая символы, вызвавшие инкремент элемента
        int i = first.length();
        int j = second.length();
        int cursor = lengths[i][j];
        while (cursor != 0) {
            while (cursor == lengths[i - 1][j - 1]) {
                i --;
                j --;
                cursor = lengths[i][j];
            }
            while (cursor == lengths[i][j - 1]) {
                j --;
                cursor = lengths[i][j];
            }
            while (cursor == lengths[i - 1][j]) {
                i --;
                cursor = lengths[i][j];
            }
            result += first.charAt(i - 1);
            i --;
            j --;
            cursor = lengths[i][j];
        }
        return new StringBuffer(result).reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        throw new NotImplementedError();
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) throws IOException {

        List<String> fileContainment = Files.readAllLines(Paths.get(inputName));

        int firstSize = fileContainment.size();
        int secondSize = fileContainment.get(0).split(" ").length;

        int[][] field = new int[firstSize][secondSize];

        int i = 0;
        int j = 0;
        for (String line : fileContainment) {
            for (int cell : Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList())) {
                field[i][j] = cell;
                j ++;
            }
            j = 0;
            i ++;
        }

        for (i = 0; i < firstSize; i ++) {
            for (j = 0; j < secondSize; j ++) {
                if (i == 0) {
                    if (j != 0) {
                        field[i][j] += field[i][j - 1];
                    }
                } else if (j == 0) {
                    field[i][j] += field[i - 1][j];
                } else {
                    field[i][j] += Math.min(Math.min(field[i - 1][j], field[i][j - 1]), field[i - 1][j - 1]);
                }
            }
        }

        return field[firstSize - 1][secondSize - 1];
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
