package ru.koylubaevaem.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;

public class Basket {

    public static final String ARRAY_ELEMENTS_SEPARATOR = ";";

    private final static Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        GSON = builder.create();
    }

    private String[] products;

    private Basket obj;

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

    public void saveJson(File textFile) {
        String json = GSON.toJson(this);
        try (PrintWriter out = new PrintWriter(textFile)) {
            out.print(json);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
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
        } catch (Exception ignore) {
        }
        return null;
    }


    public static Basket loadFromJsonFile(File textFile) {
        try (Reader reader = new FileReader(textFile)){
            // convert json string to object
            Basket basket = GSON.fromJson(reader, Basket.class);
            return basket;
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return null;
    }
}
