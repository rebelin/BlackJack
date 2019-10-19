/**
* This program simulates a game of Blackjack either with random inputs or with a user and a dealer.
* By using two global arrays to caluclate the odds of being safe when hitting with the current hand,
* this program can decide to hit or stay based on those odds.
* @author Rebecca Lin
* @version 1.0
*/

import java.util.Scanner;
public class RebeccaLinBlackjack
{
  // Global arrays
  static int [] seen = new int [21];
  static int [] safe = new int [21];

  // Note: comments with no space between the // and words were used as tests
  public static void main(String [] args)
  {
    Scanner kboard = new Scanner(System.in);
    /* Tests:

    int [] deck = buildDeck();
    printArray(deck);

    // Shuffle deck
    shuffle(deck);
    printArray(deck);
    System.out.println(getCard(deck));

    System.out.print("How many hands do you want to play? ");
    String input = kboard.nextLine();
    int n = Integer.parseInt(input);
    simulate(deck, n);
    */

    // local to main
    double[] odds;
    int[] deck = buildDeck();
    shuffle(deck);
    //printArray(deck);
    simulate(deck, 1000);

    odds = calculateOdds();
    //printOdds(odds);
    System.out.println("If hand is 14, hit based on odds: " + shouldIHit(14, odds));

    // User vs dealer
    System.out.println("VS Dealer Simulation:");

    // Starting set-up
    int user = getHand(deck);
    System.out.println("Your hand: " + user);
    int dealerUp = getCard(deck);
    System.out.println("Dealer's face-up card: " + dealerUp);
    int dealerDown = getCard(deck);
    //System.out.println("Dealer's face-down card: " + dealerDown);

    // Ask for user input
    System.out.println("Do you want to hit or stay? ");
    String userInput = kboard.nextLine();

    // Continue to loop if the user wants to hit and if the user's hand is less than 22
    while (userInput.equals("hit") && user < 21)
    {
      user += getCard(deck);
      System.out.println("Your new hand: " + user);

      if (user < 21)
      {
        System.out.println("Do you want to hit or stay? ");
        userInput = kboard.nextLine();
      }
    }

    int dealer = dealerHand(dealerUp, dealerDown, deck);

    // If user and dealer don't bust
    if (user < 22 && dealer < 22)
    {
      System.out.println("Your hand: " + user);
      System.out.println("Dealer's hand: " + dealer);
      if (user == 21)
      {
        System.out.print("Blackjack! ");
      }
      if (user == dealer)
      {
        System.out.println("It's a tie/push. ");
      }
      else if (user > dealer)
      {
        System.out.println("You win! ");
      }
      else
      {
        System.out.println("You lose.");
      }
    }
    // If user busts
    else if (user > 21)
    {
      System.out.println("You busted. You lose. ");
    }
    // If dealer busts
    else if (dealer > 21)
    {
      System.out.println("The dealer busted; you win by default. ");
    }
  }

  /** Returns whether or not you should hit, based on your current odds.
    * @param currVal Sum of current hand
    * @param odds The odds hitting is safe
    * @return The boolean
    */
    public static boolean shouldIHit(int currVal, double[] odds)
    {
      // A number between 0 and 1
      double n = Math.random();
      //System.out.print(n);

      // Return true if the random number is less than or equal to the current odds
      if (n <= odds[currVal])
      {
        return true;
      }
      else
      {
        return false;
      }
    }

  /** Calculates the odds of you being safe if you hit at values below 21
    * @return A double array
    */
    public static double[] calculateOdds()
    {
      // The odds for 0 and 1 are not possible totals for a hand
      int count = 2;

      // Creates a double array to store the odds
      double [] odds = new double [21];

      while (count < 21)
      {
        //double odd = (double) safe[count] / (double) seen[count];
        //System.out.print(odd);
        // Calculate the odds and set it to the corresponding index in the odds array
        odds[count] = (double) safe[count] / (double) seen[count];
        count ++;
      }
      //System.out.print("Odds: ");
      //printOdds(odds);
      return odds;
    }

  /** Takes in a deck and uses it to simulate numTimes different hands by calling playHand
    * @param deck A deck as an integer array
    * @param numTimes Different hands
    */
    public static void simulate(int[] deck, int numTimes)
    {
      for (int i = 0; i < numTimes; i ++)
      {
        playHand(deck);
        //System.out.print("Seen: ");
        //printArray(seen);
        //System.out.print("Safe: ");
        //printArray(safe);
      }
    }

  /** Creates the initial playing hand (2 cards)
    * @param deck The deck as an integer array
    * @return The sum of the initial hand
    */
    public static int getHand(int[] deck)
    {
      int hand = 0;

      for (int i = 0; i < 2; i ++)
      {
        hand += getCard(deck);
      }
      //System.out.println("Hand " + hand);
      return hand;
    }
  /** Given a deck, will play one hand of BlackJack
    * and record the number of times you get the value
    * and the number of times you are safe when you hit at that value
    * @param deck The deck as an integer array
    */
    public static void playHand(int[] deck)
    {
      int hand;
      int index;

      // Create the initial playing hand (2 cards)
      hand = getHand(deck);
      //System.out.println("firstHand: " + hand);

      // Increment the index of the initial hand to the seen array
      seen[hand] ++;
      //System.out.print("Seen: ");
      //printArray(seen);

      // Get the top card of the deck
      int newCard = getCard(deck);
      //System.out.println("newCard: " + newCard);

      // Keep hitting (taking the top card) until you either get blackjack or bust
      while (hand + newCard < 21)
      {
        safe[hand] ++;
        //System.out.print("Safe: ");
        //printArray(safe);

        hand += newCard;
        //System.out.println("hand: " + hand);

        seen[hand] ++;
        //System.out.print("Seen: ");
        //printArray(seen);

        // If the hand is 21, the player has blackjack; it is safe, so increment the index to
        if (hand == 21)
        {
          safe[hand] ++;
          //System.out.print("Safe (blackjack): ");
          //printArray(safe);
        }
        else
        {
          // Pick the next top card
          newCard = getCard(deck);
          //System.out.println("newCard: " + newCard);
        }
      }
      //System.out.println("newCard: " + newCard);
      //int sum = hand + newCard;
      //System.out.println("sum: " + sum);
      //System.out.print("Safe (busted): ");
      //printArray(safe);
    }

  /** Returns a new deck, in order. The suit does not matter, so the
    * array will contain 1 (A), 2, 3, 4, ..., 10, 10 (J), 10 (Q), 10 (K).
    * The array should have 53 spots, so the last spot will hold the index of the top card (which should be zero to start).
    * @return The deck as an integer array
    */
  public static int[] buildDeck()
  {
    int [] deck = new int [53];

    // Place value in the array
    int i = 0;

    // Make a stack of cards, in order, for each suit (4)
    for (int suit = 0; suit < 4; suit++)
    {
      // This counter is also the number on the card
      int num = 1;
      while (num < 11)
      {
        if (num < 10)
        {
          deck[i] = num;
          num ++;
          i ++;
        }
        // Add 4 tens to the deck to represent the 10, J, Q, and K cards
        else
        {
          int tens = 0;
          while (tens < 4)
          {
            deck[i] += num;
            tens ++;
            i ++;
          }
          num ++;
        }
      }
    }

    // The last slot in the array has the index of the top card
    deck[52] = 0;

    return deck;
  }

  /** Randomize the array by going through it and swapping every
    * item with another value from a random index from 0 to 51.
    * @param deck The deck as an integer array
    */
  public static void shuffle(int[] deck)
  {
    for (int i = 0; i < deck.length - 1; i++)
    {
      // Random index from 0 to 51
      int index = (int) (Math.random() * 52);

      // Set original values as integers (immutable), in order to swap the values
      int oi = deck[i];
      int oindex = deck[index];
      deck[i] = oindex;
      deck[index] = oi;

      // The last slot in the array has the index of the top card
      deck[52] = 0;
    }
  }

  /** Return the top card and increment the top card variable,
    * unless you have hit the end of the array. In that case,
    * shuffle the deck and set the top card variable to 0.
    * @param deck The deck as an integer array
    * @return The value of the top card
    */
  public static int getCard(int[] deck)
  {
    // When the index of the top card is 52, shuffle and restart the deck
    if (deck[52] == 52)
    {
      shuffle(deck);
    }

    // Reminder: deck[52] is the index of the top card
    int top = deck[deck[52]];
    //System.out.println("Top " + top);

    // Increment the index of the top card
    deck[52] ++;
    //System.out.println("Index of top " + deck[52]);
    return top;
  }

  /**
    * Simulates the blackjack dealer according to standard casino rules
    * @param up The dealer's face-up card
    * @param down The dealer's face-down card
    * @param deck The deck as an integer array
    * @return The dealer's hand
    */
  public static int dealerHand(int up, int down, int[] deck)
  {
    int hand = up + down;

    // The dealer has to hit if his hand is under 17
    while (hand < 17)
    {
      hand += getCard(deck);
    }
    return hand;
  }

  /**
    * This method takes in an integer array and prints its content
    * (Mostly used for testing purposes)
    * @param arr An integer array
    */
  public static void printArray(int[] arr)
  {
    System.out.print("{ ");
    for (int i = 0; i < arr.length; i++)
    {
      if (i != arr.length - 1)
      {
        System.out.print(arr[i] + " , ");
      }
      else
      {
        System.out.print(arr[i]);
      }
    }
    System.out.println(" }");
  }

  /**
    * This method takes in an double array and prints its content
    * (Mostly used for testing purposes)
    * @param arr A double array
    */
  public static void printOdds(double[] arr)
  {
    System.out.print("{ ");
    for (int i = 0; i < arr.length; i++)
    {
      if (i != arr.length - 1)
      {
        System.out.print(arr[i] + " , ");
      }
      else
      {
        System.out.print(arr[i]);
      }
    }
    System.out.println(" }");
  }
}
