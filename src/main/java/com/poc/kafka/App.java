package com.poc.kafka;

public class App {

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("You must inform 'producer' or 'consumer'");

        switch (args[0]) {
            case "producer":
                new Producer().run();
                break;
            case "consumer":
                new Consumer().run();
                break;
            default:
                throw new IllegalArgumentException("Invalid option. You must inform 'producer' or 'consumer'");
        }
    }

}
