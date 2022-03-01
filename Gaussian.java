import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;


public class Gaussian {
    
    static int numberVar = 0; //Number of variables solving for
    //Fill the matrix
    public static void fillMatrix(double[] constant, double [][] matrix, String fileName) throws IOException{
        FileReader file = null;

        // Initiate file reader
        try {
            file = new FileReader(System.getProperty("user.dir").concat("/"+fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found.");
        }

        Scanner readInput = new Scanner(file);
        numberVar = readInput.nextInt();
        for (int row = 0; row < numberVar; row++){
                for (int col = 0; col < numberVar; col++){
                    matrix[row][col] = readInput.nextDouble();
                }
        }
        //Fill Constant vector
        for (int col = 0; col < numberVar; col++){
            constant[col] = readInput.nextDouble();
        }

        readInput.close();
        file.close();
    }

    public static void FwdElimination(double[][] coeff, double[] constant){

        int n = numberVar;
        for(int k = 0; k < n ; k++){

            for (int i = k + 1; i < n; i++){
                double mult = coeff[i][k] / coeff[k][k];

                for (int j = k + 1; j < n ; j++){
                    coeff[i][j] = coeff[i][j] - mult * coeff[k][j];
                }
                
                constant[i] = constant[i] - (mult * constant[k]);
            }
        }
    }

    //Back Substitution

    public static void BackSubst(double[][] coeff, double[] constant, double[] sol){
        int n = numberVar - 1;
        sol[n] = constant[n] / coeff[n][n];
        for(int i = n - 1; i >= 0 ; i--){
            double sum = constant[i];
            for(int j = i + 1; j <= n ;j++){
                sum = sum - coeff[i][j] * sol[j];
            }
            sol[i] = sum/coeff[i][i];
        }
        
    }

    //Naive Gaussian algorithm

    public static void NaiveGaussian(double[][] coeff , double[] consta) throws IOException{
        double[] sol = new double[10];
        FwdElimination(coeff, consta);
        BackSubst(coeff, consta,sol);

        //Write solutions to file 
        try{
            FileWriter writer = new FileWriter(System.getProperty("user.dir").concat("/sys1.sol"), true);
            writer.append("The solutions for Naive Gaussian Elimination (no SPP) are: ");
            for (int i = 0; i < numberVar; i++) {
                writer.append("x"+ (i+1) + ": "+sol[i] + "\t"+ "");
            }
            writer.append("\n");
            writer.close();
        }
        catch(Exception e){
            System.out.println("\nError writing to file.");
        }

        //Output solutions to console.
        System.out.printf("The solutions using Naive Gaussian Elimination (no SPP) are: ");
        for (int i = 0; i < numberVar; i++) {
            System.out.printf("x"+ (i+1) + ": "+ sol[i] + "\t" + "");
        }
        System.out.println("\nThe solutions have been appended to file sys1.sol.");
    }

    // Scaled Partial Pivoting
    public static void SPPFwdElimination (double[][] coeff, double[] constant, int[] ind){
        double[] scaling = new double[6];
        int n = numberVar;

        //Initialize index and scaling vectors
        for (int i = 0; i < n; i++){
            double smax = 0;
            
            for(int j = 0; j < n; j++){
                smax = Math.max(smax, Math.abs(coeff[i][j]));
            }

            scaling[i] = smax;
        }

        for(int k = 0; k < n - 1; k++){
            double rmax = 0;
            int maxInd = k;

            for(int i = k; i < n; i++){
                double r = Math.abs(coeff[ind[i]][k] / scaling[ind[i]]);
                if(r > rmax){
                    rmax = r;
                    maxInd = i;
                }
            }
            
            //swap
            int temp = ind[maxInd];
            ind[maxInd] = ind[k];
            ind[k] = temp;

            for(int i = k + 1; i < n; i++){
                double mult = coeff[ind[i]][k] / coeff[ind[k]][k];

                for(int j = k; j < n; j++){
                    coeff[ind[i]][j] = coeff[ind[i]][j] - mult * coeff[ind[k]][j];
                }

                constant[ind[i]] = constant[ind[i]] - mult * constant[ind[k]];
            }
        }
    }

    // Back Substitution
    public static void SPPBackSubst(double[][] coeff, double[] constant, double[] sol, int[] ind){
        int n = numberVar - 1;
        sol[n] = constant[ind[n]] / coeff[ind[n]][n];
        for(int i = n - 1; i >= 0 ; i--){
            double sum = constant[ind[i]];
            for(int j = i + 1; j <= n; j++){
                sum = sum - coeff[ind[i]][j] * sol[j];
            }
            sol[i] = sum / coeff[ind[i]][i];
        }
    }

    public static void SPPGaussian (double[][] coeff, double[] constant) throws IOException{
        double[] sol = new double[10];
        int [] ind = new int[10];
        int n = numberVar;
        for(int i = 0; i < n; i++){
            ind [i] = i;
        }
        SPPFwdElimination(coeff, constant, ind);
        SPPBackSubst(coeff, constant, sol, ind);

        //Write solutions to file
        try{
        FileWriter writer = new FileWriter(System.getProperty("user.dir").concat("/sys1.sol"), true);
            writer.append("\nThe solutions using Gaussian Elimination with SPP are: ");
            for (int i = 0; i < numberVar; i++) {
                writer.append("x"+ (i+1) + ": "+sol[i] + "\t"+ "");
            }
            writer.append("\n");
            writer.close();
        }
        catch(Exception e){
            System.out.println("\nError trying to write solutions to file.");
        }

        // Output solutions to console
        System.out.printf("The solutions using Gaussian Elimination with SPP are: ");
        for (int i = 0; i < numberVar; i++) {
            System.out.printf("x"+ (i+1) + ": "+ sol[i] + "\t" + "");
        }
        System.out.println("\nThe solutions have been appended to file sys1.sol.");
    }

    public static void main(String[] args) throws IOException{
        double[] constant = new double[10];
        double[][] coeff = new double[10][10];
        if(args.length > 1){
            if(args[1].equals("--spp")){
                fillMatrix(constant,coeff, args[0]);
                SPPGaussian(coeff, constant);
            }
        } else{
            fillMatrix(constant,coeff, args[0]);
            NaiveGaussian(coeff, constant);
        }
    }
}
