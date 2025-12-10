import java.util.ArrayList;

public class StockPriceAnalyzer {
    
    // Method 1: Calculate average stock price from array
    public static double calculateAveragePrice(int[] stockPrices) {
        double sum = 0;
        for (int i = 0; i < stockPrices.length; i++) {
            sum += stockPrices[i];
        }
        return sum / stockPrices.length;
    }
    
    // Method 1 (ArrayList version): Calculate average stock price from ArrayList
    public static double calculateAveragePrice(ArrayList<Integer> stockPrices) {
        double sum = 0;
        for (int i = 0; i < stockPrices.size(); i++) {
            sum += stockPrices.get(i);
        }
        return sum / stockPrices.size();
    }
    
    // Method 2: Find maximum stock price from array
    public static int findMaximumPrice(int[] stockPrices) {
        int maxPrice = stockPrices[0];
        for (int i = 1; i < stockPrices.length; i++) {
            if (stockPrices[i] > maxPrice) {
                maxPrice = stockPrices[i];
            }
        }
        return maxPrice;
    }
    
    // Method 2 (ArrayList version): Find maximum stock price from ArrayList
    public static int findMaximumPrice(ArrayList<Integer> stockPrices) {
        int maxPrice = stockPrices.get(0);
        for (int i = 1; i < stockPrices.size(); i++) {
            if (stockPrices.get(i) > maxPrice) {
                maxPrice = stockPrices.get(i);
            }
        }
        return maxPrice;
    }
    
    // Method 3: Count occurrences of a specific price in array
    public static int countOccurrences(int[] stockPrices, int targetPrice) {
        int count = 0;
        for (int i = 0; i < stockPrices.length; i++) {
            if (stockPrices[i] == targetPrice) {
                count++;
            }
        }
        return count;
    }
    
    // Method 3 (ArrayList version): Count occurrences of a specific price in ArrayList
    public static int countOccurrences(ArrayList<Integer> stockPrices, int targetPrice) {
        int count = 0;
        for (int i = 0; i < stockPrices.size(); i++) {
            if (stockPrices.get(i) == targetPrice) {
                count++;
            }
        }
        return count;
    }
    
    // Method 4: Compute cumulative sum from ArrayList
    public static ArrayList<Integer> computeCumulativeSum(ArrayList<Integer> stockPrices) {
        ArrayList<Integer> cumulativeSum = new ArrayList<>();
        int runningSum = 0;
        
        for (int i = 0; i < stockPrices.size(); i++) {
            runningSum += stockPrices.get(i);
            cumulativeSum.add(runningSum);
        }
        
        return cumulativeSum;
    }
    
    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        // Sample data - array of stock prices
        int[] stockPricesArray = {100, 105, 102, 108, 105, 110, 105, 112};
        
        // Sample data - ArrayList of stock prices
        ArrayList<Integer> stockPricesArrayList = new ArrayList<>();
        stockPricesArrayList.add(100);
        stockPricesArrayList.add(105);
        stockPricesArrayList.add(102);
        stockPricesArrayList.add(108);
        stockPricesArrayList.add(105);
        stockPricesArrayList.add(110);
        stockPricesArrayList.add(105);
        stockPricesArrayList.add(112);
        
        // Display original data
        System.out.println("=== Stock Price Analysis ===\n");
        System.out.print("Array Stock Prices: ");
        for (int price : stockPricesArray) {
            System.out.print(price + " ");
        }
        System.out.println("\n");
        
        // Test Method 1: Calculate Average Price
        System.out.println("--- Average Stock Price ---");
        double avgPriceArray = calculateAveragePrice(stockPricesArray);
        System.out.printf("Average Price (Array): $%.2f\n", avgPriceArray);
        
        double avgPriceArrayList = calculateAveragePrice(stockPricesArrayList);
        System.out.printf("Average Price (ArrayList): $%.2f\n\n", avgPriceArrayList);
        
        // Test Method 2: Find Maximum Price
        System.out.println("--- Maximum Stock Price ---");
        int maxPriceArray = findMaximumPrice(stockPricesArray);
        System.out.println("Maximum Price (Array): $" + maxPriceArray);
        
        int maxPriceArrayList = findMaximumPrice(stockPricesArrayList);
        System.out.println("Maximum Price (ArrayList): $" + maxPriceArrayList + "\n");
        
        // Test Method 3: Count Occurrences
        int targetPrice = 105;
        System.out.println("--- Occurrence Count ---");
        int occurrencesArray = countOccurrences(stockPricesArray, targetPrice);
        System.out.println("Occurrences of $" + targetPrice + " (Array): " + occurrencesArray);
        
        int occurrencesArrayList = countOccurrences(stockPricesArrayList, targetPrice);
        System.out.println("Occurrences of $" + targetPrice + " (ArrayList): " + occurrencesArrayList + "\n");
        
        // Test Method 4: Compute Cumulative Sum
        System.out.println("--- Cumulative Sum ---");
        ArrayList<Integer> cumulativeSum = computeCumulativeSum(stockPricesArrayList);
        System.out.print("Cumulative Sum: ");
        for (int sum : cumulativeSum) {
            System.out.print(sum + " ");
        }
        System.out.println();
    }
}