package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;

public class Main {
    private static Board board;
    private static final int BOARD_LIMIT = 9;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int option;
        while (true) {
            System.out.println("\n=== Sudoku CLI ===");
            System.out.println("1 - Iniciar jogo");
            System.out.println("2 - Inserir número");
            System.out.println("3 - Remover número");
            System.out.println("4 - Visualizar tabuleiro");
            System.out.println("5 - Status do jogo");
            System.out.println("6 - Limpar tabuleiro");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");
            System.out.print("Escolha uma opção: ");
            option = Integer.parseInt(reader.readLine());

            switch (option) {
                case 1 -> startGame(args);
                case 2 -> inputNumber(reader);
                case 3 -> removeNumber(reader);
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame(reader);
                case 7 -> finishGame();
                case 8 -> {
                    System.out.println("Encerrando...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void startGame(String[] args) { // Explique melhor essa funcao, o que ela faz e como funciona? e as tecnicas de HPC usadas
        board = new Board(IntStream.range(0, BOARD_LIMIT).parallel()
            .mapToObj(i -> IntStream.range(0, BOARD_LIMIT).parallel()
                .mapToObj(j -> {
                    String[] config = args[i * BOARD_LIMIT + j].split(";")[1].split(",");
                    int expected = Integer.parseInt(config[0]);
                    boolean fixed = Boolean.parseBoolean(config[1]);
                    return new Space(expected, fixed);
                }).toArray(Space[]::new))
            .toArray(Space[][]::new));
        System.out.println("Jogo iniciado com sucesso.");
    }

    private static void inputNumber(BufferedReader reader) throws Exception { // Explique melhor essa funcao, o que ela faz e como funciona? e as tecnicas de HPC usadas
        if (board == null) return;
        System.out.println("Insira número:");
        int col = getInput(reader, 0, 8, "Coluna (0-8): ");
        int row = getInput(reader, 0, 8, "Linha (0-8): ");
        int value = getInput(reader, 1, 9, "Valor (1-9): ");
        boolean success = board.changeValue(col, row, value);
        System.out.println(success ? "Número inserido com sucesso." : "Espaço fixo! Não é possível alterar.");
    }

    private static void removeNumber(BufferedReader reader) throws Exception {
        if (board == null) return;
        System.out.println("Remover número:");
        int col = getInput(reader, 0, 8, "Coluna (0-8): ");
        int row = getInput(reader, 0, 8, "Linha (0-8): ");
        boolean success = board.clearValue(col, row);
        System.out.println(success ? "Número removido com sucesso." : "Espaço fixo! Não pode ser removido.");
    }

    private static void showCurrentGame() {
        if (board == null) {
            System.out.println("Jogo não iniciado.");
            return;
        }

        Object[] args = new Object[81];
        IntStream.range(0, BOARD_LIMIT).parallel().forEach(i -> // Expica melhor essa arrow function e como o processador paraleliza e renderiza e os ganhos disso.
            IntStream.range(0, BOARD_LIMIT).forEach(j -> // E isso? qye merda quer dizer issso? 
                args[i * BOARD_LIMIT + j] = board.getSpaces()[j][i].getActual() == null ? " " : board.getSpaces()[j][i].getActual() // Pode explicar a logica dessa condicional?
            )
        );
        System.out.printf(BOARD_TEMPLATE, args);
    }

    private static void showGameStatus() {
        if (board == null) {
            System.out.println("Jogo não iniciado.");
            return;
        }

        System.out.println("Status do jogo: " + board.getStatus().getLabel());
        System.out.println("Há erros no tabuleiro? " + (board.hasErrors() ? "Sim" : "Não"));
    }

    private static void clearGame(BufferedReader reader) throws Exception {
        if (board == null) return;
        System.out.print("Tem certeza que deseja limpar o jogo? (s/n): ");
        String confirm = reader.readLine();
        if ("s".equalsIgnoreCase(confirm)) {
            board.reset();
            System.out.println("Tabuleiro resetado.");
        } else {
            System.out.println("Limpeza cancelada.");
        }
    }

    private static void finishGame() {
        if (board == null) {
            System.out.println("Jogo não iniciado.");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns! Você completou o Sudoku com sucesso!");
        } else {
            System.out.println("O jogo ainda não está concluído corretamente.");
        }
    }

    private static int getInput(BufferedReader reader, int min, int max, String prompt) throws Exception { // Isso e um regex feito no facao?
        int input;
        do {
            System.out.print(prompt);
            input = Integer.parseInt(reader.readLine());
        } while (input < min || input > max);
        return input;
    }
}
