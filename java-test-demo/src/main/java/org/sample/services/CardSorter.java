package org.sample.services;

import org.springframework.stereotype.Service;

/**
 * This CardSorter is responsible for sorting cards
 *
 * @author Stefan van der Grift
 * @since 20-6-2016
 */
@Service
public class CardSorter {

    /**
     * Sort the provided array of cards
     *
     * @param shuffledCards array with shuffled cards, in a format of "♠A"
     * @return sorted cards
     */
    public String[] sort(String[] shuffledCards) {

        String[] sortedCards = new String[52];

        for (String card : shuffledCards) {
            char suit = card.charAt(0);
            String value = card.substring(1);

            int position = getPosition(suit, value);

            if (position > 52) {
                System.out.println(position + ": " + card);
            } else {
                sortedCards[position - 1] = card;
            }
        }

        return sortedCards;
    }

    private int getPosition(char suit, String value) {
        int position;

//        Determine position of suit
        switch (suit) {
            case '♠':
                position = 0;
                break;

            case '♥':
                position = 13;
                break;

            case '♦':
                position = 26;
                break;

            case '♣':
                position = 39;
                break;

            default:
                throw new IllegalArgumentException("Suit should be ♠, ♥, ♦ or ♣");
        }

//        Determine position of value
        switch (value) {
            case "A":
                position += 1;
                break;
            case "K":
                position += 2;
                break;
            case "Q":
                position += 3;
                break;
            case "J":
                position += 4;
                break;
            default:
                try {

//                position for 10 = 5, 9 = 6, etc.. Therefore, start at 15
                    int number = Integer.parseInt(value);
                    if(number > 10 || number < 2){
                        throw new IllegalArgumentException("Value should be between 2 and 10");
                    }
                    position += (15 - number);
                    break;
                }
                catch(NumberFormatException nfe){
                    throw new IllegalArgumentException("Value should be an integer");
                }
        }

        return position;
    }
}
