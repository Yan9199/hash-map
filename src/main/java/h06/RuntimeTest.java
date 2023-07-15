package h06;

import java.util.Calendar;
import java.util.Random;

public class RuntimeTest {
    private final static int TEST_SET_SIZE = 1_000;
    private final static Calendar cal;
    private final static long m;
    static {cal = Calendar.getInstance(); cal.set(2023, Calendar.JANUARY, 1, 0, 0, 0); cal.set(Calendar.MILLISECOND, 0); m = cal.getTimeInMillis();}

    /**
     * Generates two test data sets with 1,000 dates each.
     * The first test data set is in component 0 of the returned array and is initialized with true.
     * The second test data set is in component 1 of the returned array and is initialized with false.
     * The dates are between 1970 and 2022.
     *
     * @return Two test data sets of 1,000 dates each.
     */
    public static MyDate[][] generateTestdata() {
        final MyDate[][] array = new MyDate[2][TEST_SET_SIZE]; final Random r = new Random();
        for (int i = 0; i < TEST_SET_SIZE; i++) {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Math.floorMod(r.nextLong(1, m), m));
            array[0][i] = new MyDate(c, true); array[1][i] = new MyDate(c, false);}
        return array;
    }

    /**
     * Generates a test set.
     *
     * @param i        See exercise sheet.
     * @param j        See exercise sheet.
     * @param k        See exercise sheet.
     * @param l        See exercise sheet.
     * @param testData The testdata used.
     * @return A test set.
     */
    public static TestSet<MyDate> createTestSet(int i, int j, int k, int l, MyDate[][] testData) {
        final MyMap<MyDate, MyDate> map;
        if (j == 1) {
            final BinaryFct2Int<MyDate> b;
            final int init = (int) Math.pow(2, l == 1 ? 12 : 6);
            if (k == 1) b = new LinearProbing<>(new Hash2IndexFct<>(init, 0));
            else b = new DoubleHashing<>(new Hash2IndexFct<>(init, 0), new Hash2IndexFct<>(init, 42));
            map = new MyIndexHoppingHashMap<>(init, 2, 0.75, b);}
        else map = new MyListsHashMap<>(new Hash2IndexFct<>(l == 1 ? 3 * TEST_SET_SIZE : (int) (0.1 * TEST_SET_SIZE), 0));
        return new TestSet<>(map, testData[i - 1]);
    }

    /**
     * Tests the given test set.
     *
     * @param testSet The test set to test.
     */
    public static void test(TestSet<MyDate> testSet) {
        final MyDate[] array = testSet.getTestData(); final MyMap<MyDate, MyDate> map = testSet.getHashTable();
        for (int i = 0; i < 750; i++) {final MyDate m = array[i]; map.put(m, m);}
        for (MyDate m : array) map.containsKey(m);
        for (MyDate m : array) map.getValue(m);
        for (MyDate m : array) map.remove(m);
    }
}
