package ru.koylubaevaem.netology;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import ru.koylubaevaem.netology.config.Config;
import ru.koylubaevaem.netology.config.Prop;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // чтение XML файла
        String fileName = "shop.xml";
        Config config = readXmlFile(fileName);

        Scanner scan = new Scanner(System.in);
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

        Basket basket = null;
        if (config != null && config.getLoad().isEnabled()) {
            Prop configLoad = config.getLoad();
            // загружаем корзину из файла
            String format = configLoad.getFormat();
            if (format == null || format.isBlank()) {
                format = "json";
            }
            String logFileName = configLoad.getFileName();
            if (logFileName == null || logFileName.isBlank()) {
                logFileName = "basket." + format;
            }
            File file = new File(logFileName);
            if ("json".equals(format)) {
                basket = Basket.loadFromJsonFile(file);
            } else {
                basket = Basket.loadFromTxtFile(file);
            }
        }
        if (basket == null){
            basket = new Basket(products, prices);
        }
        // 3.Вывод списка доступных для покупки продуктов на экран;
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ": " + products[i] + " " + prices[i] + " rub.");
        }

        int productNum;
        int amountOfProduct;
        ClientLog clientLog = new ClientLog();
        while (true) {
//            4.Возможность ввода пользователем одной строкой номера продукта и количества для добавления в корзину;
            System.out.println("Choose the item and amount or 'end' to quit: ");
            String inputStr = scan.nextLine();
            if (inputStr.equals("end")) {
                break;
            }
            String[] parts = inputStr.split(" "); //1..N 1 10 -> [1, 10]

            if (parts.length != 2) {
                System.out.println("Data were entered incorrectly. Type in an item, then gap, then amount.");
                continue;
            }
            try {
                productNum = Integer.parseInt(parts[0]) - 1; // 0
                amountOfProduct = Integer.parseInt(parts[1]); // 10
            } catch (NumberFormatException e) {
                System.out.println("Data were entered incorrectly.");
                continue;
            }

            clientLog.log(productNum, amountOfProduct);

//            5.Пользователь может добавлять несколько раз один и тот же товар в корзину, в этом случае он должен суммировать
            basket.addToCart(productNum, amountOfProduct);

            if (config != null && config.getSave().isEnabled()) {
                Prop configSave = config.getSave();
                String format = configSave.getFormat();
                if (format == null || format.isBlank()) {
                    format = "json";
                }
                String saveFileName = configSave.getFileName();
                if (saveFileName == null || saveFileName.isBlank()) {
                    saveFileName = "basket." + format;
                }
                File saveFile = new File(saveFileName);
                if ("json".equals(format)) {
                    basket.saveJson(saveFile);
                } else {
                    basket.saveTxt(saveFile);
                }
            }
        }
//        6.Вывод всех покупок, их общую стоимость и количество, на экран после ввода всех покупок.
        basket.printCart();

        if (config != null && config.getLog().isEnabled()) {
            String logFileName = config.getLog().getFileName();
            if (logFileName == null || logFileName.isBlank()) {
                logFileName = "log.csv";
            }
            File csvFile = new File(logFileName);
            clientLog.exportAsCSV(csvFile);
        }
    }

    private static Config readXmlFile(String xmlFileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFileName);

            // Fill Load Settings
            Prop load = fillSettings("load", doc);

            // Fill Save Settings
            Prop save = fillSettings("save", doc);

            // Fill Log Settings
            Prop log = fillSettings("log", doc);

            Config config = new Config();
            config.setLoad(load);
            config.setSave(save);
            config.setLog(log);
            return config;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Prop fillSettings(String tagName, Document doc) {
        Prop prop = new Prop();
        NodeList nodeList = doc.getElementsByTagName(tagName);
        Element saveElement = (Element) nodeList.item(0);
        var enabled = getBooleanFromElement("enabled", saveElement);
        var fileName = getTextFromElement("fileName", saveElement);
        var format = getTextFromElement("format", saveElement);
        prop.setFileName(fileName);
        prop.setEnabled(enabled);
        prop.setFormat(format);
        return prop;
    }

    public static String getTextFromElement(String tagName, Element element) {
        var nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            var item = (Element) nodeList.item(0);
            var text = (Text) item.getFirstChild();
            return text.getWholeText();
        } else {
            return null;
        }
    }

    public static Boolean getBooleanFromElement(String tagName, Element element) {
        String text = getTextFromElement(tagName, element);
        return Boolean.parseBoolean(text);
    }
}


