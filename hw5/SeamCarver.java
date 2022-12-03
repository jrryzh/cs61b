import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class SeamCarver {
    private Picture picture;
    private int width, height;
    private boolean transposed;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
        this.transposed = false;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        Color topColor = fetchTop(x, y);
        Color bottomColor = fetchBotom(x, y);
        Color leftColor = fetchLeft(x, y);
        Color rightColor = fetchRight(x, y);

        return calcDeltaSquare(topColor, bottomColor) + calcDeltaSquare(leftColor, rightColor);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
        int[] res = findVerticalSeam();
        detransposePicture();
        return res;
    }

    private void detransposePicture() {
        Picture transPic = new Picture(height, width);
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                transPic.set(height - 1 - j, i, picture.get(i, j));
            }
        }

        picture = transPic;
        width = picture.width();
        height = picture.height();
        transposed = false;
    }

    private void transposePicture() {
        Picture transPic = new Picture(height, width);
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                transPic.set(j, width - 1 - i, picture.get(i, j));
            }
        }

        picture = transPic;
        width = picture.width();
        height = picture.height();
        transposed = true;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] res = new int[height];
        int[][] resList = new int[height][width];
        // 初始化firstRow
        double[] prevRowEnergy = new double[width];
        // firstRow赋值
        for (int x = 0; x < width; x++) {
            prevRowEnergy[x] = energy(x, 0);
            resList[0][x] = x; // 先row再col
        }



        // 对接下来的每一个row都循环
        // resList的每个位置都存上一行的正确数字
        double[] curRowEnergy = prevRowEnergy;
        for (int y = 1; y < height; y++) {
            curRowEnergy = new double[width];
            for (int x = 0; x < width; x++) {
                double[] temp = findVerticalSeamHelper(x, y - 1, prevRowEnergy);
                curRowEnergy[x] = temp[1] + energy(x, y);
                resList[y][x] = (int) temp[0];
            }
            prevRowEnergy = curRowEnergy;
        }
        // 整理结果
        int curIndex, prevIndex;
        curIndex = argMinOfArr(curRowEnergy);
        res[height - 1] = curIndex;
        prevIndex = curIndex;
        for (int y = height - 1; y > 0; y --) {
            curIndex = resList[y][prevIndex];
            res[y - 1] = curIndex;
            prevIndex = curIndex;
        }

        if (transposed) {
            return reverseArr(res);
        } else {
            return res;
        }
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }


    private Color fetchLeft(int x, int y) {
        // 超出左边
        if (x - 1 < 0) {
            return this.picture.get(width - 1, y);
        } else {
            return this.picture.get(x - 1, y);
        }
    }

    private Color fetchRight(int x, int y) {
        // 超出右边
        if (x + 1 > width - 1) {
            return this.picture.get(0, y);
        } else {
            return this.picture.get(x + 1, y);
        }

    }

    private Color fetchTop(int x, int y) {
        // 超出上边
        if (y - 1 < 0) {
            return this.picture.get(x, height - 1);
        } else {
            return this.picture.get(x, y - 1);
        }
    }

    private Color fetchBotom(int x, int y) {
        // 超出下边
        if (y + 1 > height - 1) {
            return this.picture.get(x, 0);
        } else {
            return this.picture.get(x, y + 1);
        }
    }

    private double calcDeltaSquare(Color color1, Color color2) {
        return Math.pow(color1.getRed() - color2.getRed(), 2) + Math.pow(color1.getGreen() - color2.getGreen(), 2) + Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    // 原本：找到下面一行三个的最小值
    // 同样适用于 找到上面一行三个的最小值
    private double[] findVerticalSeamHelper(int prevX, int y, double[] prevRowEnergy) {
        double[] currentThreeEnergy = new double[3];

        if (prevX - 1 < 0) {
            currentThreeEnergy[0] = Integer.MAX_VALUE;
        } else {
            currentThreeEnergy[0] = prevRowEnergy[prevX - 1];
        }
        if (prevX + 1 >= width) {
            currentThreeEnergy[2] = Integer.MAX_VALUE;
        } else {
            currentThreeEnergy[2] = prevRowEnergy[prevX + 1];
        }
        currentThreeEnergy[1] = prevRowEnergy[prevX];

        int res = 0;
        double currentEnergy = currentThreeEnergy[0];
        for (int i = 1; i <= 2; i++) {
            if (currentThreeEnergy[i] < currentEnergy) {
                res = i;
                currentEnergy = currentThreeEnergy[i];
            }
        }

        return new double[]{prevX + res - 1, currentEnergy};
    }

    private double sumOfArr(double[] arr) {
        double sum = 0;
        for (double i: arr) {
            sum += i;
        }
        return sum;
    }

    private int argMinOfArr(double[] arr) {
        int res = -1;
        double currMin = Integer.MAX_VALUE;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < currMin) {
                currMin = arr[i];
                res = i;
            }
        }

        return res;
    }

    private int[] reverseArr(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[arr.length - i - 1];
        }
        return res;
    }
}
