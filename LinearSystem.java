import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;


public class LinearSystem {
    
    //Fill the matrix
    public static void fillMatrix(double n, double[] constant, double [][] matrix) throws IOException{
        FileReader file = null;
        try {
            file = new FileReader(System.getProperty("user.dir").concat("/sys1.lin"));
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found.");
        }

        Scanner readInput = new Scanner(file);
        n = readInput.nextDouble();

        for (int row = 0; row < 4; row++){
                for (int col = 0; col < 4; col++){
                    matrix[row][col] = readInput.nextDouble();
                }
        }
        //Fill Constant vector
        for (int col = 0; col < 4; col++){
            constant[col] = readInput.nextDouble();
        }

        readInput.close();
        file.close();
    }

    public static void FwdElimination(double[][] coeff, double[] constant){

        int n = 4;
        for(int k = 0; k < n ; k++){

            for (int i = k + 1; i < n; i++){
                double mult = coeff[i][k] / coeff[k][k];

                for (int j = k + 1; j < n ; j++){
                    coeff[i][j] = coeff[i][j] - mult * coeff[k][j];
                }
                
                constant[i] = constant[i] - (mult * constant[k]);
            }
        }
        for(int i = 0; i<4 ; i++){
            System.out.println("The constant is"+constant[i] + " at i " + i);
        }
    }

    //Back Substitution

    public static void BackSubst(double[][] coeff, double[] constant, double[] sol){
        int n = 3;
        sol[n] = constant[n] / coeff[n][n];
        for(int i = n - 1; i >= 0 ; i--){
            double sum = constant[i];
            System.out.println("The constant is " + sum);
            for(int j = i + 1; j <= n ;j++){
                sum = sum - coeff[i][j] * sol[j];
            }
            sol[i] = sum/coeff[i][i];
        }
    }

    //Naive Gaussian algorithm

    public static void NaiveGaussian(double[][] coeff , double[] consta) throws IOException{
        double[] sol = new double[4];
        fillMatrix(4,consta,coeff);
        FwdElimination(coeff, consta);
        BackSubst(coeff, consta,sol);
        FileWriter writer = new FileWriter(System.getProperty("user.dir").concat("/sys1.sol"));
        System.out.printf("\n The solutions are: %f - %f - %f - %f", sol[0],sol[1],sol[2],sol[3]);
    }

    public static void main(String[] args) throws IOException{
        double[] constant = new double[4];
        double[][] coeff = new double[4][4];
        double[] sol = new double[4];
        NaiveGaussian(coeff, constant);

        for(int i = 0; i < 4; i++){
            System.out.println("I is" + i);
        }


        /*
        fillMatrix(4, constant, coeff);
        System.out.println("The matrix should be: ");
        System.out.printf("%f %f %f %f \n", coeff[0][0],coeff[0][1] ,coeff[0][2], coeff[0][3]);
        System.out.printf("%f %f %f %f \n", coeff[1][0],coeff[1][1] ,coeff[1][2], coeff[1][3]);
        System.out.printf("%f %f %f %f \n", coeff[2][0],coeff[2][1] ,coeff[2][2], coeff[2][3]);
        System.out.printf("%f %f %f %f \n", coeff[3][0],coeff[3][1] ,coeff[3][2], coeff[3][3]);
        System.out.println("The constants are:");
        System.out.printf("%f %f %f %f\n", constant[0], constant[1], constant[2], constant[3]);
        */

    }
}
