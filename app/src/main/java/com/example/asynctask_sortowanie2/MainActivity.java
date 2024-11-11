package com.example.asynctask_sortowanie2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText inputElementCount;
    private Button buttonSortSelection, buttonSortBubble, buttonSortInsertion, buttonSortQuick, buttonSortMerge;
    private ProgressBar progressIndicator;
    private CustomView sortingDisplay;
    private ExecutorService backgroundExecutor;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private boolean isSortingCompletedQuickSort = false;
    private boolean isSortingCompletedMergeSort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputElementCount = findViewById(R.id.editTextNumberOfElements);
        buttonSortSelection = findViewById(R.id.buttonSortSelection);
        buttonSortBubble = findViewById(R.id.buttonSortBubble);
        buttonSortInsertion = findViewById(R.id.buttonSortInsertion);
        buttonSortQuick = findViewById(R.id.buttonSortQuick);
        buttonSortMerge = findViewById(R.id.buttonSortMerge);
        progressIndicator = findViewById(R.id.progressBar);
        sortingDisplay = findViewById(R.id.sortingView);

        backgroundExecutor = Executors.newSingleThreadExecutor();

        buttonSortSelection.setOnClickListener(view -> sortAndDisplay("Selection"));
        buttonSortBubble.setOnClickListener(view -> sortAndDisplay("Bubble"));
        buttonSortInsertion.setOnClickListener(view -> sortAndDisplay("Insertion"));
        buttonSortQuick.setOnClickListener(view -> sortAndDisplay("Quick"));
        buttonSortMerge.setOnClickListener(view -> sortAndDisplay("Merge"));
    }

    private void sortAndDisplay(String algorithm) {
        String elementCountText = inputElementCount.getText().toString();
        if (!elementCountText.isEmpty()) {
            int count = Integer.parseInt(elementCountText);
            int[] elements = generateRandomArray(count);
            progressIndicator.setProgress(0);
            sortingDisplay.setData(elements);

            isSortingCompletedQuickSort = false;
            isSortingCompletedMergeSort = false;

            switch (algorithm) {
                case "Selection":
                    backgroundExecutor.execute(() -> performSelectionSort(elements));
                    break;
                case "Bubble":
                    backgroundExecutor.execute(() -> performBubbleSort(elements));
                    break;
                case "Insertion":
                    backgroundExecutor.execute(() -> performInsertionSort(elements));
                    break;
                case "Quick":
                    backgroundExecutor.execute(() -> performQuickSort(elements, 0, elements.length - 1));
                    break;
                case "Merge":
                    backgroundExecutor.execute(() -> performMergeSort(elements, 0, elements.length - 1));
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Wprowadź liczbę", Toast.LENGTH_SHORT).show();
        }
    }

    private int[] generateRandomArray(int count) {
        int[] elements = new int[count];
        Random rng = new Random();
        for (int i = 0; i < count; i++) {
            elements[i] = rng.nextInt(count);
        }
        return elements;
    }

    // Selection Sort
    private void performSelectionSort(int[] elements) {
        int length = elements.length;
        for (int i = 0; i < length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (elements[j] < elements[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = elements[i];
                elements[i] = elements[minIndex];
                elements[minIndex] = temp;
            }
            updateUI(elements, i, length);
        }
        uiHandler.post(() -> Toast.makeText(MainActivity.this, "Sortowanie zakończone (Selection Sort)", Toast.LENGTH_SHORT).show());
    }

    // Bubble Sort
    private void performBubbleSort(int[] elements) {
        int length = elements.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (elements[j] > elements[j + 1]) {
                    int temp = elements[j];
                    elements[j] = elements[j + 1];
                    elements[j + 1] = temp;
                }
            }
            updateUI(elements, i, length);
        }
        uiHandler.post(() -> Toast.makeText(MainActivity.this, "Sortowanie zakończone (Bubble Sort)", Toast.LENGTH_SHORT).show());
    }

    // Insertion Sort
    private void performInsertionSort(int[] elements) {
        int length = elements.length;
        for (int i = 1; i < length; i++) {
            int key = elements[i];
            int j = i - 1;
            while (j >= 0 && elements[j] > key) {
                elements[j + 1] = elements[j];
                j--;
            }
            elements[j + 1] = key;
            updateUI(elements, i, length);
        }
        uiHandler.post(() -> Toast.makeText(MainActivity.this, "Sortowanie zakończone (Insertion Sort)", Toast.LENGTH_SHORT).show());
    }

    // Quick Sort
    private void performQuickSort(int[] elements, int low, int high) {
        if (low < high) {
            int pi = partition(elements, low, high);
            performQuickSort(elements, low, pi - 1);
            performQuickSort(elements, pi + 1, high);
            updateUI(elements, high, elements.length);
        } else if (low == high && !isSortingCompletedQuickSort) {
            isSortingCompletedQuickSort = true;
            uiHandler.post(() -> Toast.makeText(MainActivity.this, "Sortowanie zakończone (Quick Sort)", Toast.LENGTH_SHORT).show());
        }
    }

    private int partition(int[] elements, int low, int high) {
        int pivot = elements[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (elements[j] < pivot) {
                i++;
                int temp = elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }
        int temp = elements[i + 1];
        elements[i + 1] = elements[high];
        elements[high] = temp;
        return i + 1;
    }

    // Merge Sort
    private void performMergeSort(int[] elements, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            performMergeSort(elements, left, mid);
            performMergeSort(elements, mid + 1, right);
            merge(elements, left, mid, right);
            updateUI(elements, right, elements.length);
        } else if (left == right && !isSortingCompletedMergeSort) {
            isSortingCompletedMergeSort = true;
            uiHandler.post(() -> Toast.makeText(MainActivity.this, "Sortowanie zakończone (Merge Sort)", Toast.LENGTH_SHORT).show());
        }
    }

    private void merge(int[] elements, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(elements, left, leftArr, 0, n1);
        System.arraycopy(elements, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                elements[k] = leftArr[i];
                i++;
            } else {
                elements[k] = rightArr[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            elements[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < n2) {
            elements[k] = rightArr[j];
            j++;
            k++;
        }
    }

    private void updateUI(int[] elements, int currentStep, int totalSteps) {
        int progressStatus = (int) (((float) currentStep / (totalSteps - 1)) * 100);
        uiHandler.post(() -> {
            sortingDisplay.setData(elements);
            progressIndicator.setProgress(progressStatus);
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundExecutor.shutdown();
    }
}
