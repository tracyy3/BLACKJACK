import java.util.Scanner;

public class Blackjack {

    public static void main(String[] args) {
        System.out.println("---------Welcome to ACE's BLACKJACK!--------");
        Scanner scanner = new Scanner(System.in);

        Deck playingDeckDeck = new Deck();
        playingDeckDeck.createFullDeck();
        playingDeckDeck.shuffleDeck();

        Deck playersHand = new Deck();
        Deck dealersHand = new Deck();

        int playersAmount = 100;

        while (playersAmount > 0) {
            System.out.print("You have $" + playersAmount + ", how much do you want to bet? ");
            int playersBet = scanner.nextInt();
            if (playersBet % 5 != 0) { // OPTIONAL 2 --- player must bet in increments of $5 ---
                System.out.println("Sorry - you are only allowed to bet in $5 increments.");
                continue;
            }

            if (playersBet > playersAmount) {
                System.out.println("Let's get real, you cannot bet more than what you have.");
                break;
            }

            boolean endRound = false;
            playersHand.draw(playingDeckDeck);
            playersHand.draw(playingDeckDeck);
            dealersHand.draw(playingDeckDeck);
            dealersHand.draw(playingDeckDeck);

            // Optional 3 --- Player allowed to split their cardS ---
            if (playersHand.getCard(0).getValue() == playersHand.getCard(1).getValue()) {
                Deck split = new Deck();
                split.addCard(new Card());
                split.addCard(new Card());
            }

            while (true) {
                System.out.println("Your hand is: ");
                System.out.println(playersHand.toString());
                System.out.println("Your hand is valued @: " + playersHand.cardsValue());

                System.out.println("Dealer's Hand: " + dealersHand.getCard(0).toString() + " and [Hidden]");
                System.out.println("Do you want to Double Down?"); // OPTION 2 -- Allow players to double down start--

                String answers = scanner.next();
                boolean doubleDown = false;
                String hitORstand = "";

                do { // IF player doesn't want to double down, they can hit or stand:
                    if (!answers.equals("yes")) {
                        System.out.print("Do you want to hit or stand? ");
                        hitORstand = scanner.next();
                        if (hitORstand.charAt(0) == 'h' || hitORstand.charAt(0) == 'H') { // IF player HITS
                            playersHand.draw(playingDeckDeck);
                            System.out.println("You draw a: "
                                    + playersHand.getCard(playersHand.deckSize()
                                            - 1).toString());
                            System.out.println("Your hand is now valued @: "
                                    + playersHand.cardsValue());

                            if (playersHand.cardsValue() > 21) {
                                System.out.println("Bust. You've BUSTED! Current valued @ : "
                                        + playersHand.cardsValue());
                                playersAmount -= playersBet;
                                endRound = true;
                                break;
                            }
                        }

                        if (hitORstand.charAt(0) == 's' || hitORstand.charAt(0) == 'S') { // IF Player STAND
                            break;
                        }
                    }

                    if (answers.equals("yes")) { // Player wants to double down
                        if (playersBet * 2 > playersAmount) {
                            System.out.println(
                                    "Sorry, you don't have enough money to double down. But, you can continue to play your hand.");

                            System.out.print("Do you want to hit or stand? ");
                            hitORstand = scanner.next();

                            if (hitORstand.charAt(0) == 'h' || hitORstand.charAt(0) == 'H') {
                                playersHand.draw(playingDeckDeck);
                                System.out.println(
                                        "You draw a: " + playersHand.getCard(playersHand.deckSize() - 1).toString());
                                System.out.println("Your hand is now valued at: " + playersHand.cardsValue());

                                if (playersHand.cardsValue() > 21) { // Player BUST if > 21
                                    System.out.println(
                                            "Bust. You've BUSTED! Currently valued at: " + playersHand.cardsValue());
                                    playersAmount -= playersBet;
                                    endRound = true;
                                    break;
                                }
                            }

                            if (hitORstand.charAt(0) == 's' || hitORstand.charAt(0) == 'S') { // Player stands
                                break;
                            }
                        }

                        else { // Player can double down if they have enough $$$
                            playersBet = playersBet * 2;
                            doubleDown = true;
                            System.out.println("Your bet is now $" + playersBet);

                            playersHand.draw(playingDeckDeck);
                            System.out.println(
                                    "You draw a: " + playersHand.getCard(playersHand.deckSize() - 1).toString());
                            System.out.println("Your hand is now valued at: " + playersHand.cardsValue());

                            if (playersHand.cardsValue() > 21) { // Player BUST if > 21
                                System.out.println(
                                        "Bust. You've BUSTED! Currently valued at: "
                                                + playersHand.cardsValue());
                                playersAmount -= playersBet;
                                endRound = true;
                                break;
                            }
                        }
                    }
                }

                while (!doubleDown);
                break;
            }

            System.out.println("Dealer's Cards: " + dealersHand.toString());
            if ((dealersHand.cardsValue() >= 17) && (dealersHand.cardsValue() > playersHand.cardsValue())
                    && !endRound) {
                System.out.println("Dealer beats you!");
                playersAmount -= playersBet;
                endRound = true;
            }

            while ((dealersHand.cardsValue() < 17) && !endRound) {
                dealersHand.draw(playingDeckDeck);
                System.out.println("Dealer Draws: "
                        + dealersHand.getCard(dealersHand.deckSize()
                                - 1).toString());
            }

            System.out.println("Dealer's Hand value: "
                    + dealersHand.cardsValue()
                    + "\nPlayer's Hand value: "
                    + playersHand.cardsValue());

            if ((dealersHand.cardsValue() > 21) && !endRound) {
                System.out.println("Dealer busts! You're the WINNER!");
                playersAmount += playersBet;
                endRound = true;
            }

            if ((playersHand.cardsValue() == dealersHand.cardsValue()) && !endRound) {
                System.out.println("-----PUSH!-----");
                endRound = true;
            }

            if ((playersHand.cardsValue() > dealersHand.cardsValue()) && !endRound) {
                System.out.println("WINNER! - You win the hand!");
                playersAmount += playersBet;
            }

            else if (!endRound) {
                System.out.println("LOSER! - You lose the hand!");
                playersAmount -= playersBet;
            }

            playersHand.moveAllToDeck(playingDeckDeck);
            dealersHand.moveAllToDeck(playingDeckDeck);
            System.out.println("-----End of Hand-----");
        }

        System.out.println("-----Game Over. You're out of money. Goodbye!-----");
    }
}
