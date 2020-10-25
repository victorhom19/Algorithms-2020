package lesson2;

import kotlin.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaAlgorithms {

    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) throws IOException {
        //Трудоемкость O(N)
        //Ресурсоемкость O(N)

        List<Integer> costDifference = Files.readAllLines(Paths.get(inputName)).stream().map(Integer::parseInt).collect(Collectors.toList());
        for (int i = 0; i < costDifference.size() - 1; i ++) {
            costDifference.set(i, costDifference.get(i + 1) - costDifference.get(i));
        }
        costDifference.remove(costDifference.size() - 1);

        Pair<Integer, Integer> mxSubarrayIndices = findMaxSubarray(costDifference);
        return (new Pair<>(mxSubarrayIndices.getFirst() + 1, mxSubarrayIndices.getSecond() + 2));
    }

    private static Pair findMaxSubarray(List<Integer> array) {
        int mxCurSum = array.get(0);
        int mxSum = array.get(0);
        int stCurIndex = 0;
        int edCurIndex;
        int stGlIndex = 0;
        int edGlIndex = 0;
        for (int i = 1; i < array.size(); i++) {
            if (mxCurSum + array.get(i) >= array.get(i)) {
                mxCurSum += array.get(i);
                edCurIndex = i;
            } else {
                mxCurSum = array.get(i);
                stCurIndex = i;
                edCurIndex = i;
            }
            if (mxCurSum >= mxSum) {
                stGlIndex = stCurIndex;
                edGlIndex = edCurIndex;
            }
            mxSum = Math.max(mxSum, mxCurSum);
        }
        return (new Pair(stGlIndex, edGlIndex));
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        //Трудоемкость O(N)
        //Ресурсоемкость O(1)

        if (choiceInterval == 1) return menNumber;
        if (choiceInterval == 2) {
            if (menNumber == 1) {
                return 1;
            } else if (menNumber % 2 == 0) {
                return 2 * josephTask(menNumber / 2, choiceInterval) - 1;
            } else {
                return 2 * josephTask((menNumber - 1) / 2, choiceInterval) + 1;
            }
        } else {
            if (menNumber == 1) {
                return 1;
            } else {
                return 1 + (josephTask(menNumber - 1, choiceInterval) + choiceInterval - 1) % menNumber;
            }
        }

    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    static public String longestCommonSubstring(String first, String second) {
        //Трудоемкость O(first.length() * second.length())
        //Ресурсоемкость O(first.length() * second.length()

        int[][] searchMatrix = new int[first.length() + 1][second.length() + 1];
        int mxValue = 0;
        int mxIndex = 0;
        for (int i = 1; i < first.length() + 1; i++) {
            for (int j = 1; j < second.length() + 1; j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    searchMatrix[i][j] = searchMatrix[i - 1][j - 1] + 1;
                    if (searchMatrix[i][j] > mxValue) {
                        mxValue = searchMatrix[i][j];
                        mxIndex = i;
                    }
                }
            }
        }

        return first.substring(mxIndex - mxValue, mxIndex);
    }


    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */

    static public int calcPrimesNumber(int limit) {
        //Трудоемкость O(Nlog(logN))
        //Ресурсоемкость O(N)

        int[] numbers = new int[limit + 1];
        int counter = 0;
        for (int i = 2; i <= limit; i ++) {
            numbers[i] = i;
        }

        for (int element : numbers) {
            if (element != 0) {
                counter ++;
                int i = element;
                while (i + element <= limit) {
                    i += element;
                    numbers[i] = 0;
                }
            }
        }
        return counter;
    }

}
