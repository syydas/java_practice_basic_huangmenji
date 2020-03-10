package com.thoughtworks;

import java.util.Arrays;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("请点餐（菜品Id x 数量，用逗号隔开）：");
        String selectedItems = scan.nextLine();
        String summary = bestCharge(selectedItems);
        System.out.println(summary);
    }

    public static String bestCharge(String selectedItems) {
        String[] itemIds = getItemIds();
        String[] itemNames = getItemNames();
        double[] itemPrices = getItemPrices();
        String[] halfPriceIds = getHalfPriceIds();
        int[] itemsIndex = getSelectedItemsIndex(selectedItems, itemIds);
        int[] itemsCount = getSelectedItemsCount(selectedItems);
        selectedItems = printItemsInfo(itemsIndex, itemsCount, itemNames, itemPrices);
        double originalPrice = getOriginalPrices(itemsIndex, itemsCount, itemPrices);
        double discountOne = getDiscountOnePrices(originalPrice);
        double discountTwo = getDiscountTwoPrices(itemsIndex, itemsCount, itemIds, itemPrices, halfPriceIds);
        String halfItemsName = getHalfItemsName(itemsIndex, itemIds, itemNames, halfPriceIds);
        selectedItems += printDiscountInfo(originalPrice, discountOne, discountTwo, halfItemsName);
        selectedItems += printTotalInfo(discountOne, discountTwo);
        return selectedItems;
    }

    public static int[] getSelectedItemsIndex(String selectedItems, String[] itemIds) {
        String[] selectedItem = selectedItems.split(",");
        int[] itemIndex = new int[selectedItem.length];
        int index = 0;
        for (int i = 0; i < itemIds.length; i++) {
            if (selectedItems.contains(itemIds[i])) {
                itemIndex[index++] = i;
            }
        }
        return itemIndex;
    }

    public static int[] getSelectedItemsCount(String selectedItems) {
        String[] selectedItem = selectedItems.split(",");
        int[] itemCount = new int[selectedItem.length];
        for (int i = 0; i < selectedItem.length; i++) {
            itemCount[i] = Integer.parseInt(selectedItem[i].split(" x ")[1]);
        }
        return itemCount;
    }

    private static String printItemsInfo(int[] itemsIndex, int[] itemsCount, String[] itemNames, double[] itemPrices) {
        String itemsInfo = "============= 订餐明细 =============\n";
        for (int i = 0; i < itemsIndex.length; i++) {
            int totalPrices = 0;
            int index = itemsIndex[i];
            totalPrices += itemPrices[index] * itemsCount[i];
            itemsInfo += (itemNames[index] + " x " + itemsCount[i] + " = " + totalPrices + "元\n");
        }
        return itemsInfo;
    }

    private static double getOriginalPrices(int[] itemsIndex, int[] itemsCount, double[] itemPrices) {
        int originalPrice = 0;
        for (int i = 0; i < itemsIndex.length; i++) {
            originalPrice += itemsCount[i] * itemPrices[itemsIndex[i]];
        }
        return originalPrice;
    }

    private static double getDiscountOnePrices(double price) {
        if (price >= 30) {
            price -= 6;
        }
        return price;
    }

    private static double getDiscountTwoPrices(int[] itemsIndex, int[] itemsCount, String[] itemIds, double[] itemPrices, String[] halfPriceIds) {
        int discountPrice = 0;
        for (int i = 0; i < itemsIndex.length; i++) {
            if (Arrays.asList(halfPriceIds).contains(itemIds[itemsIndex[i]])) {
                discountPrice += itemsCount[i] * itemPrices[itemsIndex[i]] / 2;
            } else {
                discountPrice += itemsCount[i] * itemPrices[itemsIndex[i]];
            }
        }
        return discountPrice;
    }

    private static String getHalfItemsName(int[] itemsIndex, String[] itemIds, String[] itemNames, String[] halfPriceIds) {
        String halfItemsName = "";
        for (int i = 0; i < itemsIndex.length; i++) {
            if (Arrays.asList(halfPriceIds).contains(itemIds[itemsIndex[i]])) {
                int indexOfItem = Arrays.binarySearch(itemIds, itemIds[itemsIndex[i]]);
                halfItemsName += itemNames[indexOfItem] + "，";
            }
        }
        return halfItemsName;
    }

    private static String printDiscountInfo(double originalPrice, double discountOne, double discountTwo, String halfItemsName) {
        String discountInfo = "";
        if (discountOne <= discountTwo) {
            if (originalPrice == discountOne) {
                discountInfo = "";
            } else {
                discountInfo = "-----------------------------------\n"
                        + "使用优惠:\n"
                        + "满30减6元，省6元\n";
            }
        } else {
            discountInfo = "-----------------------------------\n"
                    + "使用优惠:\n"
                    + "指定菜品半价(" + halfItemsName + ")，省" + (int) (originalPrice - discountTwo) + "元\n";
        }
        return discountInfo;
    }

    private static String printTotalInfo(double discountOne, double discountTwo) {
        String totalInfo = "-----------------------------------\n"
                + "总计：" + (int) (Math.min(discountOne, discountTwo)) + "元\n"
                + "===================================";
        return totalInfo;
    }

    public static String[] getItemIds() {
        return new String[]{"ITEM0001", "ITEM0013", "ITEM0022", "ITEM0030"};
    }

    public static String[] getItemNames() {
        return new String[]{"黄焖鸡", "肉夹馍", "凉皮", "冰粉"};
    }

    public static double[] getItemPrices() {
        return new double[]{18.00, 6.00, 8.00, 2.00};
    }

    public static String[] getHalfPriceIds() {
        return new String[]{"ITEM0001", "ITEM0022"};
    }
}
