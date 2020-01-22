import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class Question4 {

    public static void main(String[] args) throws IOException {
        Random rd = new Random();
        /*********** Traitement du fichier data et extraction des données *************/
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        ArrayList lines = new ArrayList();
        for(String line =br.readLine(); line!=null; line=br .readLine()){
            String[] data = line.split(" ");
            lines.add(data);
        }
        String[][] donnee = (String[][])lines.toArray(new String[lines.size()][]);

        /********************* Mix Gaussian ***********************/
        double eps = 0.1;
        double sig = rd.nextDouble();
        int D = 2;
        int M = donnee.length;

        double[][] data = new double[M][D];
        // affectation tableau data
        for (int d = 0; d <M; d++) {
            for (int i = 0; i < D; i++) {
                data[d][i] = Double.valueOf(donnee[d][i]);
            }
        }
        FileWriter f = new FileWriter("ScoreQuestion4.d");

        for(int K = 2; K<=10; K++){
            double[][] centres = new double[K][D];
            double[][] sigmas = new double[K][D];
            double[] rho = new double[K];
            double score = -Double.MAX_VALUE;
            // initialisation de sigmas et rho
            for (int iter = 0; iter <K ; iter++) {
                for (int j = 0; j <D ; j++) {
                    sigmas[iter][j] = sig;
                }
                rho[iter] = 1./K;
            }
            for (int i = 0; i <10 ; i++) {
                double[][] ass= new double[M][K] ;
                double sc;
                double[][] centres2 = new double[K][D];
                centres = MixGauss.ClustersInit(data,rd.nextInt(M),K);
                do{
                    centres2 = centres.clone();
                    ass= MixGauss.assigner(data,centres,rho,sigmas);
                    rho = MixGauss.UpdateRo(ass);
                    centres = MixGauss.Updateclusters(ass,data);
                    sigmas = MixGauss.sigmasUpdate(data,centres,ass);
                }while(!MixGauss.sameAs(centres,centres2,eps));

                /** calcul du score pour chaque iteration **/
                sc = MixGauss.scoreTotal(data,centres,rho,sigmas);
                /*** On calcule le meilleur score ***/
                if(sc>score){
                    score = sc;
                }

            }
            System.out.println("Le score obtenu pour K = "+K+" le score est "+score);
            f.write("" + K + "   " + score + "\n");
        }
        f.close();
    }
}
