package ru.koylubaevaem.netology;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Basket {

    public static final String ARRAY_ELEMENTS_SEPARATOR = ";";
    public static final String QUANTITY_KEY = "quantity";
    public static final String PRODUCTS_KEY = "products";
    public static final String PRICES_KEY = "prices";
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

    public void saveJson(File textFile) {
        JSONObject jsonObject = new JSONObject(); // {}
        JSONArray jsonArray = new JSONArray();

        for (int q : quantity) {
            jsonArray.add(q);
        }
        jsonObject.put(QUANTITY_KEY, jsonArray);

        jsonArray = new JSONArray();
        jsonArray.addAll(Arrays.asList(products));
        jsonObject.put(PRODUCTS_KEY, jsonArray);

        jsonArray = new JSONArray();
        for (int price : prices) {
            jsonArray.add(price);
        }
        jsonObject.put(PRICES_KEY, jsonArray);

        try (PrintWriter out = new PrintWriter(textFile)) {
            out.print(jsonObject);
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
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(textFile));
            JSONArray quantityJSONArray = (JSONArray) jsonObject.get(QUANTITY_KEY);
            JSONArray productsJSONArray = (JSONArray) jsonObject.get(PRODUCTS_KEY);
            JSONArray pricesJSONArray = (JSONArray) jsonObject.get(PRICES_KEY);

            if (quantityJSONArray != null && productsJSONArray != null && pricesJSONArray != null) {
                int[] quantity = new int[quantityJSONArray.size()];
                for (int i = 0; i < quantityJSONArray.size(); i++) {
                    quantity[i] = ((Long) quantityJSONArray.get(i)).intValue();
                }

                String[] products = new String[productsJSONArray.size()];
                for (int i = 0; i < productsJSONArray.size(); i++) {
                    products[i] = (String) productsJSONArray.get(i);
                }

                int[] prices = new int[pricesJSONArray.size()];
                for (int i = 0; i < pricesJSONArray.size(); i++) {
                    prices[i] = ((Long) pricesJSONArray.get(i)).intValue();
                }

                Basket basket = new Basket(products, prices);
                basket.quantity = quantity;
                return basket;
            }
        } catch (IOException | ParseException e) {
        }

        return null;
    }
}
