package ru.koylubaevaem.netology;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);// TODO in ClientLog
//        1.Создание массива продуктов внутри программы (без пользовательского ввода);
        String[] products = {
                "meat, (1kg)",
                "milk, (1l)",
                "loaf of bread",

                "tea, (0.1kg)",
                "sugar, (1kg)",
                "potato, (1kg)",
                "buckwheat, (1kg)"
        };
//        2.Создание массива цен на продукты (без пользовательского ввода);
        int[] prices = {427, 73, 42, 90, 78, 63, 129};

        String pathName = "basket.txt";
        File file = new File(pathName);
        Basket basket;
        if (file.exists()) {
            basket = Basket.loadFromTxtFile(new File(pathName));
            if (basket == null) {
                basket = new Basket(products, prices);
            } else {
                products = basket.getProducts();
                prices = basket.getPrices();
            }
        } else {
            basket = new Basket(products, prices);
        }
        // 3.Вывод списка доступных для покупки продуктов на экран;
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ": " + products[i] + " " + prices[i] + " rub.");
        }

        int itemOfProduct;
        int amountOfProduct;
        while (true) {
//            4.Возможность ввода пользователем одной строкой номера продукта и количества для добавления в корзину;
            System.out.println("Choose the item and amount or 'end' to quit: ");
            String inputStr = scan.nextLine();// TODO in ClientLog
            if (inputStr.equals("end")) {
                break;
            }
            String[] parts = inputStr.split(" "); //1..N 1 10 -> [1, 10]

            if (parts.length != 2) {
                System.out.println("Data were entered incorrectly. Type in an item, then gap, then amount.");
                continue;
            }
            try {
                itemOfProduct = Integer.parseInt(parts[0]) - 1; // 0
                amountOfProduct = Integer.parseInt(parts[1]); // 10
            } catch (NumberFormatException e) {
                System.out.println("Data were entered incorrectly.");
                continue;
            }// TODO in ClientLog

//            5.Пользователь может добавлять несколько раз один и тот же товар в корзину, в этом случае он должен суммировать
            basket.addToCart(itemOfProduct, amountOfProduct);
            basket.saveTxt(file);
        }
//        6.Вывод всех покупок, их общую стоимость и количество, на экран после ввода всех покупок.
        basket.printCart();
    }
}


