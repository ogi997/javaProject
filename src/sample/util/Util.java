package sample.util;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    private static final List<Point2D> positions = new ArrayList<>();

    private static final Point2D[] arr = {new Point2D(0,3), new Point2D(1, 4), new Point2D(2, 5), new Point2D(3, 6), new Point2D(4, 7), new Point2D(5, 6), new Point2D(6, 5), new Point2D(7, 4), new Point2D(7, 3)
    , new Point2D(6, 2), new Point2D(5, 1), new Point2D(4, 0), new Point2D(3, 1), new Point2D(2,2), new Point2D(1, 3), new Point2D(2, 4), new Point2D(3, 5), new Point2D(4, 6), new Point2D(5, 5), new Point2D(6, 4)
    , new Point2D(6, 3), new Point2D(5, 2), new Point2D(4, 1), new Point2D(3, 2), new Point2D(2, 3), new Point2D(3, 4), new Point2D(4, 5), new Point2D(5, 4), new Point2D(5, 3), new Point2D(4, 2), new Point2D(3, 3), new Point2D(4, 4), new Point2D(4, 3)};
    private static final List<Point2D> positionsSeven = Arrays.asList(arr);

    private static final Point2D[] arr1 = { new Point2D(0, 4), new Point2D(1, 5), new Point2D(2, 6), new Point2D(3, 7), new Point2D(4, 8), new Point2D(5, 9), new Point2D(6, 8), new Point2D(7, 7), new Point2D(8, 6), new Point2D(9,5), new Point2D(9, 4), new Point2D(8,3)
    ,  new Point2D(7, 2), new Point2D(6, 1), new Point2D(5, 0), new Point2D(4, 1), new Point2D(3, 2), new Point2D(2, 3), new Point2D(1, 4), new Point2D(2, 5), new Point2D(3, 6), new Point2D(4, 7), new Point2D(5, 8), new Point2D(6, 7), new Point2D(7, 6), new Point2D(8, 5), new Point2D(8, 4)
    , new Point2D(7,3), new Point2D(6, 2), new Point2D(5, 1), new Point2D(4, 2), new Point2D(3, 3), new Point2D(2, 4), new Point2D(3, 5), new Point2D(4, 6), new Point2D(5, 7), new Point2D(6, 6), new Point2D(7, 5), new Point2D(7,4), new Point2D(6, 3), new Point2D(5, 2), new Point2D(4 ,3)
    , new Point2D(3, 4), new Point2D(4, 5), new Point2D(5, 6), new Point2D(6, 5), new Point2D(6, 4), new Point2D(5, 3), new Point2D(4, 4), new Point2D(5, 5), new Point2D(5, 4) };
    private static final List<Point2D> positionsTen = Arrays.asList(arr1);

    public static List<Point2D> spiralListTen() {
        return positionsTen;
    }

    public static List<Point2D> spiralListSeven() {
        return positionsSeven;
    }

    public static List<Point2D> spiralDiamondView(Object[][] matrix, int x, int y, int m, int n, int k)
    {
        // Get middle column
        int midCol = y + ((n - y) / 2);
        int midRow = midCol;

        for (int i = midCol, j = 0;
             i < n && k > 0; ++i, k--, j++)
        {
//                positions.add(matrix[x + j][i]);
            positions.add(new Point2D(x + j, i));

        }

        for (int i = n, j = 0;
             i >= midCol && k > 0; --i, k--, j++)
        {
//            positions.add(matrix[midRow + j][i]);
            positions.add(new Point2D(midRow + j, i));
        }

        for (int i = midCol - 1, j = 1;
             i >= y && k > 0; --i, k--, j++)
        {
//            positions.add(matrix[n - j][i]);
            positions.add(new Point2D(n-j, i));
        }

        for (int i = y + 1, j = 1;
             i < midCol && k > 0; ++i, k--, j++)
        {
//            positions.add(matrix[midRow - j][i]);
            positions.add(new Point2D(midRow - j, i));
        }
        if (x + 1 <= m - 1 && k > 0)
        {
            // Recursive call
            spiralDiamondView(matrix, x + 1, y + 1, m - 1, n - 1, k);
        }

        return positions;
    }

//    // Display element
//    public static void show(int[][] arr, int row, int col)
//    {
//        int n = arr.length;
//        for (int i = col; i < (n) - col; i++)
//        {
//            // Display element value
////            System.out.print(" " + arr[row][i]);
//            positions.add(arr[row][i]);
//        }
//    }
//    // Include n space
//    public static void space(int n)
//    {
//        for (int i = 0; i <= n; ++i)
//        {
//            // Display element value
//            System.out.print(" ");
//        }
//    }
//    public static List<Integer> diamondView(int[][] arr, int n)
//    {
//
//        int col = n / 2;
//        int counter = col * 3;
//        int distance = 3;
//        for (int i = 0; i < n; ++i)
//        {
//            // Include space
//            space(counter);
//            // Display element
//            show(arr, i, col);
//            if (i < n / 2)
//            {
//                counter -= distance;
//                col--;
//            }
//            else
//            {
//                col++;
//                counter += distance;
//            }
//            // Include new line
//            System.out.println();
//        }
//        return positions;
//    }



}
