package lab1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.List;


@RunWith(Parameterized.class)
public class QuickSortTest {

    private final int[] input;
    private final int[] expected;

    public QuickSortTest(int[] input, int[] expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters(name = "sort({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{}, new int[]{}},
                {new int[]{5}, new int[]{5}},
                {new int[]{1, 2, 3, 4, 5}, new int[]{1, 2, 3, 4, 5}},
                {new int[]{5, 4, 3, 2, 1}, new int[]{1, 2, 3, 4, 5}},
                {new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5}, new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}},
                {new int[]{7, 2, 1, 8, 6, 3, 5, 4}, new int[]{1, 2, 3, 4, 5, 6, 7, 8}},
                // Generated test cases
                {generateRandomArray(10), null},  // Expected will be calculated in the test
                {generateRandomArray(100), null},
                {generateRandomArray(1000), null},
                {generateRandomArray(5000),null}
        });
    }

    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 2); // Generate numbers in a range
        }
        return arr;
    }


    @Test
    public void testQuickSort() {
        int[] arrCopy = Arrays.copyOf(input, input.length); // Create a copy to modify
        QuickSort.quickSort(arrCopy);
        int[] expectedResult = (expected == null) ? Arrays.stream(input).sorted().toArray() : expected; // Sort if expected is null
        assertArrayEquals(expectedResult, arrCopy);
    }

    @Test
        public void testLogSequence() {
            int[] arr = {3, 2, 1};
            QuickSort.quickSort(arr);
            List<String> log = QuickSort.getLog();

            List<String> expectedLog = Arrays.asList(
                "quickSort(0, 2)",
                "partition(0, 2) -> pivot: 1 at index 2",
                "swap(0, 2) -> 3 <-> 1",
                "quickSort(1, 2)",
                "partition(1, 2) -> pivot: 3 at index 2",
                "swap(1, 1) -> 2 <-> 2",
                "swap(2, 2) -> 3 <-> 3"
            );
        assertEquals(expectedLog, log);
    }
}