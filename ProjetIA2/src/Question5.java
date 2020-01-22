import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Question5 {

    /**
     * Fonctions qui permet de créer un tableau de taille 1000X1
     * Dont les 500 premieres valeurs sont des gaussiennes centrées en (-2) et de variance 0.2
     * et dont les 500 valeurs restantes sont centrées en 3 et de variance 1.5
     * @param points : Nombres de points
     * @param dimension : Dimension
     * @return le tableau de gaussiennes
     */
    public static double[][] GenererGauss(int points, int dimension){
        Random rd = new Random();
        int M = points;
        int D = dimension;
        double[][] tableau = new double[M][D];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < D; j++) {
                if(i<500){
                    tableau[i][j] = rd.nextGaussian()*0.2 - 2;
                } else {
                    tableau[i][j] = rd.nextGaussian()*1.5 + 3;
                }
            }
        }
        return tableau;
    }

    /**
     * Fonction mathématique qui représente notre gaussienne
     * @param x une donnée
     * @param m le centre de notre gaussienne
     * @param pi la densité de notre centre (faux PI)
     * @param sigma la variance de notre gaussienne
     * @return f(x)
     */
    public static double f(double x, double m, double pi, double sigma){
        return ((1./(Math.sqrt(2*pi*sigma)))*Math.exp(-((x-m)*(x-m))/(2*sigma)));
    }



    /** Notre main **/
    public static void main(String[] args) throws IOException {
        int m2 = 1000;
        int k2 = 2;
        int d2 = 1;
        double eps = 0.1;
        double[][] echantillon = GenererGauss(m2,d2);
        double[][] assignements = new double[m2][k2];
        double[][] centres = {{-1},{2}};        // Initialisation des positions des clusters
        double[][] variances = {{1},{1}};       // Initialisation des variances de nos deux gaussiennes
        double[] densites = new double[k2];  // Initialisation des densités de nos deux gaussiennes
        densites[0] = 1./k2; densites[1] = 1./k2;
        // Application de l'algorithme 'Mixture de Gaussiennes'
        double[][] centres2 = new double[k2][d2];
        do{
            centres2 = centres.clone();
            // Assignement :
            assignements = MixGauss.assigner(echantillon, centres, densites,variances);
            // Mise-à-jour :
            densites = MixGauss.UpdateRo(assignements);
            centres = MixGauss.Updateclusters(assignements, echantillon);
            variances = MixGauss.sigmasUpdate(echantillon,centres,assignements);
        }while(!(MixGauss.sameAs(centres, centres2, eps)));

        // Affichage des résultats
        for (int k = 0; k < k2; k++) {
            for (int i = 0; i < d2; i++) {
                System.out.println("Pos Cluster["+k+"] = "+ Arrays.toString(centres[k]));
            }
            System.out.println("La densité² du cluster["+k+"] = " + densites[k]);
        }

        // Dessiner mes deux gaussiennes normalisée (air = 1) !
        FileWriter Gaussienne1 = new FileWriter("Question5.d");
        for(int i = 0; i < m2; i++){
            if(i<500) {Gaussienne1.write(echantillon[i][0] + "\t" + f(echantillon[i][0], centres[0][0], densites[0], variances[0][0]) + "\n");}
            else {Gaussienne1.write(echantillon[i][0] + "\t" + f(echantillon[i][0], centres[1][0], densites[1], variances[1][0]) + "\n");}
        }
        Gaussienne1.close();

        // Dessiner l'histogramme et les fonctions des deux Gaussiennes
        double[] ech = new double[m2/2];
        double[] ech2 = new double[m2/2];
        for(int i = 0; i<m2/2; i ++){
            ech[i] = echantillon[i][0];
            ech2[i] = echantillon[i+m2/2][0];
        }
        double[][] HistoEch1 = TasGaussien.histogramme(TasGaussien.minT(ech), TasGaussien.maxT(ech), 50, ech);
        double[][] HistoEch2 = TasGaussien.histogramme(TasGaussien.minT(ech2), TasGaussien.maxT(ech2), 150, ech2);
        FileWriter f = new FileWriter("Question5Histogram.d");
        for (int i = 0; i < 50; i++) {
            f.write(""+ HistoEch1[0][i] + "   " + HistoEch1[1][i]/10 +"\n");
        }
        for (int i = 0; i < 150; i++) {
            f.write(""+ HistoEch2[0][i] + "   " + HistoEch2[1][i]/10 +"\n");
        }

        f.close();

    }
}
