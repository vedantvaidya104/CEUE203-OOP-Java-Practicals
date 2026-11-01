import java.util.Random;
import java.util.Scanner;

public class RPSLS {

    static int winner(Move a, Move b) {
        if (a == b) {
            return 0;
        }
        boolean aWins = switch (a) {
            case SCISSORS -> b == Move.PAPER || b == Move.LIZARD;
            case PAPER    -> b == Move.ROCK  || b == Move.SPOCK;
            case ROCK     -> b == Move.LIZARD || b == Move.SCISSORS;
            case LIZARD   -> b == Move.SPOCK || b == Move.PAPER;
            case SPOCK    -> b == Move.SCISSORS || b == Move.ROCK;
        };
        return aWins ? 1 : -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random(7);
        Move[] moves = Move.values();
        int player = 0, computer = 0;

        for (int round = 1; round <= 5; round++) {
            System.out.print("Round " + round
                + " - your move (ROCK/PAPER/SCISSORS/LIZARD/SPOCK): ");
            Move you = Move.valueOf(sc.next().toUpperCase());
            Move cpu = moves[random.nextInt(moves.length)];

            int result = winner(you, cpu);
            String text = switch (result) {
                case 1  -> { player++; yield "You win this round"; }
                case -1 -> { computer++; yield "Computer wins this round"; }
                default -> "Tie";
            };
            System.out.println("  You: " + you + " | Computer: " + cpu + " -> " + text);
        }

        if (player > computer) {
            System.out.println("You win " + player + "-" + computer);
        } else if (computer > player) {
            System.out.println("Computer wins " + computer + "-" + player);
        } else {
            System.out.println("Overall tie " + player + "-" + computer);
        }
        sc.close();
    }
}

enum Move {
    ROCK, PAPER, SCISSORS, LIZARD, SPOCK
}
