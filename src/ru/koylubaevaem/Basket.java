package ru.koylubaevaem;

import java.io.*;
import java.util.*;

public class Basket {

    public static final String ARRAY_ELEMENTS_SEPARATOR = ";";
    private String[] products;
    private int[] prices;
    private int[] quantity;

    public Basket(String[] products, int[] prices) {
        if (products.length != prices.length) {
            throw new RuntimeException("Data were entered incorrectly. Amount of products " +
                    "must be equal the amount of prices.");
        }
        this.products = products;
        this.prices = prices;
        quantity = new int[prices.length];
    }

    public void addToCart(int productNum, int amount) {
        if (productNum < 0 || amount < 0) {
            System.out.println("Data were entered incorrectly. The position of product or its amount " +
                    "can't be negative");
            return;
        }

        if (productNum > products.length) {
            System.out.println("There is no item with this number.");
            return;
        }
        quantity[productNum] += amount;
    }

    public void printCart() {
        int totalSum = 0;
        for (int i = 0; i < quantity.length; i++) {
            if (quantity[i] != 0) {
                int sum = quantity[i] * prices[i];
                System.out.println(products[i] + " " + quantity[i] + " items " + prices[i] + " rub/item, " + sum + " rub " + " totally");
                totalSum += sum;
            }
        }
        System.out.println("Total: " + totalSum + " rub");
    }

    public void saveTxt(File textFile) {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (int e : quantity) {
                out.print(e + ARRAY_ELEMENTS_SEPARATOR);
            }
            out.println();
            for (String e : products) {
                out.print(e + ARRAY_ELEMENTS_SEPARATOR);
            }
            out.println();
            for (int e : prices) {
                out.print(e + ARRAY_ELEMENTS_SEPARATOR);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        try (BufferedReader buf = new BufferedReader(new FileReader(textFile))) {
            if (buf.ready()) {
                int[] quantity = Arrays.stream(buf.readLine().split(ARRAY_ELEMENTS_SEPARATOR))
                        .mapToInt(Integer::valueOf)
                        .toArray();
                String[] products = buf.readLine().split(ARRAY_ELEMENTS_SEPARATOR);
                int[] prices = Arrays.stream(buf.readLine().split(ARRAY_ELEMENTS_SEPARATOR))
                        .mapToInt(Integer::valueOf)
                        .toArray();
                Basket basket = new Basket(products, prices);
                basket.quantity = quantity;
                return basket;
            }
        } catch (Exception ignore) {}
        return null;
    }
}
