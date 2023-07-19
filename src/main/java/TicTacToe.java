import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    /**
     * TODO: ЗАДАЧА1: Переписать программу самостоятельно.
     */

    private static final int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '*';
    private static Scanner scanner;
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static final Random random = new Random();

    private static Scanner getScanner() {
        if (scanner == null)
            scanner = new Scanner(System.in);
        return scanner;
    }

    public static void main(String[] args) {
        do {
            initialize();
            while (true) {
                printField();
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!!!"))
                    break;
                aiRandomMove();
                printField();
                if (gameCheck(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз ? (Y - да)");
        } while (getScanner().next().equalsIgnoreCase("Y"));

    }

    /**
     * TODO: Возможно, поправить отрисовку игрового поля
     * Инициализация игрового поля
     */
    private static void initialize() {
        fieldSizeX = 5;
        fieldSizeY = 5;

        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     */
    private static void printField() {

        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print(i % 2 == 0 ? "-" : i / 2 + 1);
        }
        System.out.println();
        for (int x = 0; x < fieldSizeX; x++) {

            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }

            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Метод проверки состояния игры
     *
     * @param c   фишка игрока
     * @param str победный слоган
     * @return состояние игры (true - игра завершена!)
     */
    static boolean gameCheck(char c, String str) {
        if (checkWin(c)) {
            System.out.println(str);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     * Проверка победы
     * TODO: 2 ЗАДАЧА: Переработать метод проверки победы в домашнем задании, необходимо использовать
     *  вспомогательные методы и циклы (например for)
     *
     * @param c фишка игрока
     * @return результат проверки
     */
    static boolean checkWin(char c) {
        int rows = field.length;
        int cols = field[0].length;

        // Проверка горизонталей
        for (char[] chars : field) {
            for (int j = 0; j <= cols - WIN_COUNT; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (chars[j + k] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка вертикалей
        for (int i = 0; i <= rows - WIN_COUNT; i++) {
            for (int j = 0; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (field[i + k][j] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка диагоналей (слева направо)
        for (int i = 0; i <= rows - WIN_COUNT; i++) {
            for (int j = 0; j <= cols - WIN_COUNT; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (field[i + k][j + k] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка диагоналей (справа налево)
        for (int i = 0; i <= rows - WIN_COUNT; i++) {
            for (int j = field.length; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT; k++) {
                    if (field[i + k][j - k] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Проверка на ничью
     *
     * @return результат проверки
     */
    private static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }


    /**
     * Обработка хода игрока
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты хода X и Y (от 1 до "
                    + field.length + ") через пробел >>>");
            x = getScanner().nextInt() - 1;
            y = getScanner().nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * TODO: Задача 3: Компьютер должен помешать игроку победить
     * Ход компьютера
     * <p>
     * TODO:метод работает некорректно, со временем необходимо доработать
     *
     */
    private static void aiTurn(char[][] board, int size) {
        List<List<Integer>> emptyIndices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            emptyIndices.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                if (board[i][j] == DOT_EMPTY) {
                    emptyIndices.get(i).add(j);
                }
            }
        }

        // Проверяем каждую строку на наличие уже поставленных символов
        for (int i = 0; i < size; i++) {
            List<Integer> indices = emptyIndices.get(i);
            for (int columnIndex : indices) {
                if (checkWinForAI()) {
                    board[i][columnIndex] = DOT_AI;
                    return;
                }else {
                    aiRandomMove();
                }
            }
        }

        // Проверяем каждый столбец на наличие уже поставленных символов
        for (int j = 0; j < size; j++) {
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (board[i][j] == DOT_EMPTY) {
                    indices.add(i);
                }
            }
            for (int rowIndex : indices) {
                if (checkWinForAI()) {
                    board[rowIndex][j] = DOT_AI;
                    return;
                }else {
                    aiRandomMove();
                }
            }
        }
    }
    private static void aiRandomMove() {
        // Если нет свободной ячейки, ставим символ компьютера на случайное свободное место
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[x][y] = DOT_AI;

    }

    /**
     * Проверка, является ли ячейка пустой
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива игрового поля)
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    static boolean checkWinForAI() {
        int rows = field.length;
        int cols = field[0].length;

        // Проверка горизонталей
        for (char[] chars : field) {
            for (int j = 0; j <= cols - WIN_COUNT - 1; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT - 1; k++) {
                    if (chars[j + k] != TicTacToe.DOT_HUMAN) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка вертикалей
        for (int i = 0; i <= rows - WIN_COUNT - 1; i++) {
            for (int j = 0; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT - 1; k++) {
                    if (field[i + k][j] != TicTacToe.DOT_HUMAN) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка диагоналей (слева направо)
        for (int i = 0; i <= rows - WIN_COUNT - 1; i++) {
            for (int j = 0; j <= cols - WIN_COUNT - 1; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT - 1; k++) {
                    if (field[i + k][j + k] != TicTacToe.DOT_HUMAN) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        // Проверка диагоналей (справа налево)
        for (int i = 0; i <= rows - WIN_COUNT - 1; i++) {
            for (int j = field.length; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < WIN_COUNT - 1; k++) {
                    if (field[i + k][j - k] != TicTacToe.DOT_HUMAN) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        return false;
    }

}

