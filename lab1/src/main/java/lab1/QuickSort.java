package lab1;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class QuickSort {

    private static final Random random = new Random();
    private static final List<String> log = new ArrayList<>();

    public static void quickSort(int[] arr) {
        log.clear();
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            log.add("quickSort(" + low + ", " + high + ")");
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Всегда берем последний элемент как pivot
        log.add("partition(" + low + ", " + high + ") -> pivot: " + pivot + " at index " + high);
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, high);
        return i;
    }


    private static void swap(int[] arr, int i, int j) {
        log.add("swap(" + i + ", " + j + ") -> " + arr[i] + " <-> " + arr[j]);
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static List<String> getLog() {
        return new ArrayList<>(log);
    }
}