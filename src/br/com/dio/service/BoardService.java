package br.com.dio.service;

import br.com.dio.model.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; 
import java.util.stream.IntStream;

public class BoardService {
    private final static int BOARD_LIMIT = 9; 
    private final Board board;

    public BoardService(Map<String, String> gameConfig) {
        this.board = new Board(Board.initBoard(gameConfig)); 
    }

    public Space[][] getSpaces() {
        return board.getSpaces();
    }

    public void reset() {
        board.reset();
    }

    public boolean hasErrors() {
        return board.hasErrors();
    }

    public boolean gameIsFinished() {
        return board.getStatus() == GameStatusEnum.COMPLETE;
    } 

    private Space [][] initBoard(Map<String, String> gameConfig) {
        return IntStream.range(0, BOARD_LIMIT).parallel()
            .mapToObj(i -> IntStream.range(0, BOARD_LIMIT).parallel()
                .mapToObj(j -> {
                    String[] config = gameConfig.get(i + "," + j).split(",");
                    int expected = Integer.parseInt(config[0]);
                    boolean fixed = Boolean.parseBoolean(config[1]);
                    return new Space(expected, fixed); 
                    //Para cada coluna j, pega a configuração da célula (gameConfig.get(i + "," + j)), separa em dois valores (split(",")), converte para inteiro (expected) e booleano (fixed), e cria um novo objeto Space.
                }).toArray(Space[]::new)) // Para cada linha i, cria um array de Space (cada linha do tabuleiro).
            .toArray(Space[][]::new);
    }
}