import java.util.Scanner;

interface TicTacToeBoard {
  public static final int ROWS = 3;
  public static final int COLUMNS = 3;

  public boolean isEmpty(int row, int column);
  public boolean isFull();
  public boolean checkWin();
  public void clearBoard();
  public String toString();
  public void makeMove(int row, int column);
}

class TicTacToeBoardImpl implements TicTacToeBoard {
  private static final char EMPTY_CELL = ' ';
  private static final char[] PLAYERS = {'X', 'O'};

  private char[][] board;
  private int currentPlayer;

  public TicTacToeBoardImpl() {
    board = new char[ROWS][COLUMNS];
    clearBoard();
  }

  @Override
  public boolean isEmpty(int row, int column) {
    return board[row][column] == EMPTY_CELL;
  }

  @Override
  public boolean isFull() {
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        if (isEmpty(row, col)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean checkWin() {
    // Check rows
    for (int row = 0; row < ROWS; row++) {
      if (board[row][0] != EMPTY_CELL && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
        return true;
      }
    }

    // Check columns
    for (int col = 0; col < COLUMNS; col++) {
      if (board[0][col] != EMPTY_CELL && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
        return true;
      }
    }

    // Check diagonals
    if (board[0][0] != EMPTY_CELL && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
      return true;
    }
    if (board[0][2] != EMPTY_CELL && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
      return true;
    }

    return false;
  }

  @Override
  public void clearBoard() {
    currentPlayer = 0;
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        board[row][col] = EMPTY_CELL;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        sb.append(board[row][col]);
        if (col < COLUMNS - 1) {
          sb.append("|");
        }
      }
      sb.append("\n");
      if (row < ROWS - 1) {
        sb.append("-----\n");
      }
    }
    return sb.toString();
  }

  public void makeMove(int row, int column) throws IllegalArgumentException {
    if (!isEmpty(row, column)) {
      throw new IllegalArgumentException("The cell at row " + row + " and column " + column + " is already occupied");
    }
    board[row][column] = PLAYERS[currentPlayer];
    currentPlayer = (currentPlayer + 1) % 2;
  }

}

class TicTacToeGame implements Runnable {
  private static final Scanner INPUT_SCANNER = new Scanner(System.in);

  private TicTacToeBoard board;
  private int currentPlayer;

  public TicTacToeGame(TicTacToeBoard board) {
    this.board = board;
  }

  @Override
  public void run() {
    while (!board.isFull() && !board.checkWin()) {
      System.out.println("Player " + currentPlayer + ", enter your move (row column): ");
      int row = INPUT_SCANNER.nextInt();
      int column = INPUT_SCANNER.nextInt();
      try {
        board.makeMove(row, column);
        System.out.println(board);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid move. Try again.");
      }
      currentPlayer = (currentPlayer + 1) % 2;
    }

    if (board.checkWin()) {
      System.out.println("Player " + currentPlayer + " wins!");
    } else {
      System.out.println("It's a draw.");
    }
  }
}


public class TicTacToe {
  public static void main(String[] args) {
    TicTacToeBoard board = new TicTacToeBoardImpl();
    TicTacToeGame game = new TicTacToeGame(board);
    Thread gameThread = new Thread(game);
    gameThread.start();
  }
}
