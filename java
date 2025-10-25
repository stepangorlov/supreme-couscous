import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class EnhancedSortingAlgorithms {
    
    // Структура для хранения результатов сортировки
    static class SortMetrics {
        int operations;
        int comparisons;
        long durationNanos;
        
        SortMetrics(int operations, int comparisons, long durationNanos) {
            this.operations = operations;
            this.comparisons = comparisons;
            this.durationNanos = durationNanos;
        }
        
        double getDurationMs() {
            return durationNanos / 1_000_000.0;
        }
    }
    
    // 1. Улучшенная сортировка выбором с подсчетом операций
    public static SortMetrics selectionSortEnhanced(int[] arr) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                    operations++;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                operations++;
            }
        }
        return new SortMetrics(operations, comparisons, 0);
    }
    
    // 2. Оптимизированная пузырьковая сортировка с флагом
    public static SortMetrics bubbleSortOptimized(int[] arr) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    operations++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return new SortMetrics(operations, comparisons, 0);
    }
    
    // 3. Сортировка вставками с возможностью задания шага
    public static SortMetrics insertionSortWithGap(int[] arr, int gap) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        
        for (int i = gap; i < n; i++) {
            int key = arr[i];
            int j = i - gap;
            
            while (j >= 0) {
                comparisons++;
                if (arr[j] > key) {
                    arr[j + gap] = arr[j];
                    operations++;
                    j -= gap;
                } else {
                    break;
                }
            }
            arr[j + gap] = key;
            operations++;
        }
        return new SortMetrics(operations, comparisons, 0);
    }
    
    // 4. Итеративная версия сортировки слиянием
    public static SortMetrics mergeSortIterative(int[] arr) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        
        if (n <= 1) {
            return new SortMetrics(0, 0, 0);
        }
        
        for (int step = 1; step < n; step *= 2) {
            for (int left = 0; left < n; left += 2 * step) {
                int mid = Math.min(left + step, n);
                int right = Math.min(left + 2 * step, n);
                
                int[] leftArr = Arrays.copyOfRange(arr, left, mid);
                int[] rightArr = Arrays.copyOfRange(arr, mid, right);
                
                int i = 0, j = 0, k = left;
                
                while (i < leftArr.length && j < rightArr.length) {
                    comparisons++;
                    if (leftArr[i] <= rightArr[j]) {
                        arr[k] = leftArr[i];
                        i++;
                    } else {
                        arr[k] = rightArr[j];
                        j++;
                    }
                    k++;
                    operations++;
                }
                
                while (i < leftArr.length) {
                    arr[k] = leftArr[i];
                    i++;
                    k++;
                    operations++;
                }
                
                while (j < rightArr.length) {
                    arr[k] = rightArr[j];
                    j++;
                    k++;
                    operations++;
                }
            }
        }
        return new SortMetrics(operations, comparisons, 0);
    }
    
    // 5. Сортировка Шелла с последовательностью Кнута
    public static SortMetrics shellSortKnuth(int[] arr) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        
        int gap = 1;
        while (gap < n / 3) {
            gap = 3 * gap + 1;
        }
        
        while (gap >= 1) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                
                while (j >= gap) {
                    comparisons++;
                    if (arr[j - gap] > temp) {
                        arr[j] = arr[j - gap];
                        operations++;
                        j -= gap;
                    } else {
                        break;
                    }
                }
                arr[j] = temp;
                operations++;
            }
            gap /= 3;
        }
        return new SortMetrics(operations, comparisons, 0);
    }
    
    // 6. Быстрая сортировка с медианным выбором опорного элемента
    private static int partitionMedian(int[] arr, int low, int high, SortMetrics metrics) {
        int mid = low + (high - low) / 2;
        
        // Медиана трех
        if (arr[low] > arr[mid]) {
            swap(arr, low, mid);
            metrics.operations++;
        }
        if (arr[low] > arr[high]) {
            swap(arr, low, high);
            metrics.operations++;
        }
        if (arr[mid] > arr[high]) {
            swap(arr, mid, high);
            metrics.operations++;
        }
        
        swap(arr, mid, high - 1);
        metrics.operations++;
        int pivot = arr[high - 1];
        
        int i = low;
        for (int j = low; j < high - 1; j++) {
            metrics.comparisons++;
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                metrics.operations++;
                i++;
            }
        }
        swap(arr, i, high - 1);
        metrics.operations++;
        return i;
    }
    
    public static SortMetrics quickSortEnhanced(int[] arr, int low, int high) {
        SortMetrics metrics = new SortMetrics(0, 0, 0);
        
        if (low < high) {
            if (high - low > 10) {
                int pi = partitionMedian(arr, low, high, metrics);
                SortMetrics leftMetrics = quickSortEnhanced(arr, low, pi - 1);
                SortMetrics rightMetrics = quickSortEnhanced(arr, pi + 1, high);
                
                metrics.operations += leftMetrics.operations + rightMetrics.operations;
                metrics.comparisons += leftMetrics.comparisons + rightMetrics.comparisons;
            } else {
                // Для маленьких массивов используем сортировку вставками
                SortMetrics insertionMetrics = insertionSortWithGap(arr, 1);
                metrics.operations += insertionMetrics.operations;
                metrics.comparisons += insertionMetrics.comparisons;
            }
        }
        return metrics;
    }
    
    // 7. Пирамидальная сортировка с bottom-up подходом
    private static void siftDown(int[] arr, int start, int end, SortMetrics metrics) {
        int root = start;
        
        while (2 * root + 1 <= end) {
            int child = 2 * root + 1;
            int swapIdx = root;
            
            metrics.comparisons++;
            if (arr[swapIdx] < arr[child]) {
                swapIdx = child;
            }
            
            metrics.comparisons++;
            if (child + 1 <= end && arr[swapIdx] < arr[child + 1]) {
                swapIdx = child + 1;
            }
            
            if (swapIdx == root) {
                return;
            } else {
                swap(arr, root, swapIdx);
                metrics.operations++;
                root = swapIdx;
            }
        }
    }
    
    public static SortMetrics heapSortBottomUp(int[] arr) {
        int operations = 0;
        int comparisons = 0;
        int n = arr.length;
        SortMetrics metrics = new SortMetrics(operations, comparisons, 0);
        
        // Bottom-up построение кучи
        for (int start = (n - 2) / 2; start >= 0; start--) {
            siftDown(arr, start, n - 1, metrics);
        }
        
        for (int end = n - 1; end > 0; end--) {
            swap(arr, 0, end);
            metrics.operations++;
            siftDown(arr, 0, end - 1, metrics);
        }
        
        return metrics;
    }
    
    // Вспомогательные методы
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private static SortMetrics measureSortPerformance(int[] arr, Consumer<int[]> sortFunction) {
        int[] testArr = arr.clone();
        long startTime = System.nanoTime();
        sortFunction.accept(testArr);
        long endTime = System.nanoTime();
        
        return new SortMetrics(0, 0, endTime - startTime);
    }
    
    // Анализ производительности
    public static void performanceAnalysis() {
        System.out.println("\nАНАЛИЗ ПРОИЗВОДИТЕЛЬНОСТИ АЛГОРИТМОВ СОРТИРОВКИ");
        System.out.println("==========================================================");
        
        int[] testSizes = {10, 50, 100};
        String[] algorithmNames = {
            "Сортировка выбором",
            "Пузырьковая сортировка", 
            "Сортировка вставками",
            "Итеративная сортировка слиянием",
            "Сортировка Шелла",
            "Быстрая сортировка",
            "Пирамидальная сортировка"
        };
        
        Consumer<int[]>[] algorithms = new Consumer[] {
            arr -> selectionSortEnhanced(arr),
            arr -> bubbleSortOptimized(arr),
            arr -> insertionSortWithGap(arr, 1),
            arr -> mergeSortIterative(arr),
            arr -> shellSortKnuth(arr),
            arr -> quickSortEnhanced(arr, 0, arr.length - 1),
            arr -> heapSortBottomUp(arr)
        };
        
        Random random = new Random();
        
        for (int size : testSizes) {
            System.out.println("\nРазмер массива: " + size + " элементов");
            System.out.println("----------------------------------------------------------");
            
            int[] testData = new int[size];
            for (int i = 0; i < size; i++) {
                testData[i] = random.nextInt(1000) + 1;
            }
            
            for (int i = 0; i < algorithms.length; i++) {
                SortMetrics result = measureSortPerformance(testData, algorithms[i]);
                System.out.printf("%-30s | Время: %6.3fms%n", 
                    algorithmNames[i], result.getDurationMs());
            }
        }
    }
    
    // Основной метод тестирования
    public static void main(String[] args) {
        int[] original = {64, 34, 25, 12, 22, 11, 90, 5, 77, 30};
        
        System.out.println("УЛУЧШЕННЫЕ АЛГОРИТМЫ СОРТИРОВКИ НА JAVA");
        System.out.println("Исходный массив: " + Arrays.toString(original));
        System.out.println();
        
        String[] algorithmNames = {
            "Сортировка выбором",
            "Пузырьковая сортировка",
            "Сортировка вставками", 
            "Итеративная сортировка слиянием",
            "Сортировка Шелла",
            "Быстрая сортировка",
            "Пирамидальная сортировка"
        };
        
        Consumer<int[]>[] algorithms = new Consumer[] {
            arr -> selectionSortEnhanced(arr),
            arr -> bubbleSortOptimized(arr),
            arr -> insertionSortWithGap(arr, 1),
            arr -> mergeSortIterative(arr),
            arr -> shellSortKnuth(arr),
            arr -> quickSortEnhanced(arr, 0, arr.length - 1),
            arr -> heapSortBottomUp(arr)
        };
        
        int[][] results = new int[algorithms.length][];
        SortMetrics[] metrics = new SortMetrics[algorithms.length];
        
        for (int i = 0; i < algorithms.length; i++) {
            int[] arr = original.clone();
            long startTime = System.nanoTime();
            metrics[i] = measureSortPerformance(arr, algorithms[i]);
            long endTime = System.nanoTime();
            metrics[i].durationNanos = endTime - startTime;
            results[i] = arr;
            
            System.out.println(algorithmNames[i] + ": " + Arrays.toString(arr));
            System.out.printf("%-30s Операций: %d, Сравнений: %d, Время: %.3fms%n%n", 
                "", metrics[i].operations, metrics[i].comparisons, metrics[i].getDurationMs());
        }
        
        // Проверка корректности
        boolean allCorrect = true;
        int[] sortedOriginal = original.clone();
        Arrays.sort(sortedOriginal);
        
        for (int[] result : results) {
            if (!Arrays.equals(result, sortedOriginal)) {
                allCorrect = false;
                break;
            }
        }
        
        System.out.println("ПРОВЕРКА КОРРЕКТНОСТИ: " + 
            (allCorrect ? "Все алгоритмы работают корректно" : "Обнаружены ошибки"));
        
        // Запуск анализа производительности
        performanceAnalysis();
    }
}
