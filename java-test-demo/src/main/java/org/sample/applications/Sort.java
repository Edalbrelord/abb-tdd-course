package org.sample.applications;

import org.sample.services.CardSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Application to test the CardSorter
 *
 * @author Stefan van der Grift
 * @since 20-6-2016
 */
@Component
public class Sort {
    private static final String[] shuffledCards =  {"♥4", "♠A", "♣6", "♥J", "♦6", "♦4", "♠K", "♣J", "♣2", "♣4", "♦3", "♥6", "♣9", "♦9", "♠8", "♥K", "♦8", "♦K", "♥5", "♠10", "♥7", "♠3", "♠2", "♣A", "♥3", "♥A", "♣8", "♦10", "♠7", "♦J", "♠9", "♣5", "♥8", "♣K", "♣Q", "♦5", "♥2", "♠6", "♦2", "♥10", "♣3", "♠5", "♥9", "♥Q", "♠Q", "♠J", "♦Q", "♦A", "♣7", "♦7", "♣10", "♠4"};
    private static final String requiredDeck = "♠A;♠K;♠Q;♠J;♠10;♠9;♠8;♠7;♠6;♠5;♠4;♠3;♠2;♥A;♥K;♥Q;♥J;♥10;♥9;♥8;♥7;♥6;♥5;♥4;♥3;♥2;♦A;♦K;♦Q;♦J;♦10;♦9;♦8;♦7;♦6;♦5;♦4;♦3;♦2;♣A;♣K;♣Q;♣J;♣10;♣9;♣8;♣7;♣6;♣5;♣4;♣3;♣2";

    private CardSorter cardSorter;

    @Autowired
    public Sort(CardSorter cardSorter){
        this.cardSorter = cardSorter;
    }

    public void run(){
        String shuffledDeck = "";

//        Setup shuffled deck
        for(String card : shuffledCards){
            shuffledDeck = shuffledDeck + card + ";";
        }

        System.out.println("Shuffled:");
        System.out.println(shuffledDeck);

//        Sort
        String[] sortedCards = cardSorter.sort(shuffledCards);

//        Setup sorted deck
        String sortedDeck = "";
        for(String card : sortedCards){
            sortedDeck = sortedDeck + card + ";";
        }

        System.out.println("Sorted:");
        System.out.println(sortedDeck);

        System.out.println("Required:");
        System.out.println(requiredDeck);
    }
}
