import java.util.Arrays;
import java.util.Random;
public class MixGauss {

    /**
     * Teste si les contenus de deux tableau son identique à un epsilon près
     * @param a un des tableaux à comparer
     * @param b l'autre tableau à comparer
     * @param e epsilon de comparaison
     * @return
     */
    public static boolean sameAs(double[][] a, double[][] b, double e){
        if(a.length != b.length) return false;
        if(a[0].length != b[0].length) return false;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if ((a[i][j] > b[i][j] + e) || a[i][j] < b[i][j] - e) return false;
            }
        }
        return true;
    }
    /**
     * renvoie l'indice de l'element max d'un tableau
     * @param tab
     * @return
     */
    public static int argmax(double[] tab){
        int indiceMax = 0;
        double max = tab[0];
        for (int i = 1; i < tab.length ; i++) {
            if(tab[i]>max){
                max = tab[i];
                indiceMax = i;
            }
        }
        return indiceMax;
    }


    /**
     * renvoie le Rk
     * @param rk
     * @return le tableau de Rk,somme des rk
     */
    public static double[] sommeRk(double[][]rk) {
        int M = rk.length; //nbr data
        int K = rk[0].length; //nbr clusters
        double[]Rk = new double[K];
        for (int k = 0; k < K; k++) {
            Rk[k] = 0;
            for (int d = 0; d < M; d++) {
                Rk[k]+= rk[d][k];
            }
        }
        return Rk;
    }

    /**
     * màj ro
     * @param rk
     * @return le ro mis à jour
     */
    public static double[] UpdateRo(double[][] rk) {
        int M = rk.length;
        int K = rk[0].length;
        double[] Rk = sommeRk(rk);
        double[] ro = new double[K];
        for (int k= 0; k < K; k++) {
            ro[k] = Rk[k]/M;  // somme de Rk par la taille de M
        }
        return ro;
    }

    /**
     *
     * @param rk
     * @param data
     * @return tableau de centres
     */
    public static double[][] Updateclusters(double[][] rk, double[][] data) {
        int M = data.length;
        int K = rk[0].length;
        int D = data[0].length;
        double[] Rk = sommeRk(rk);
        double[][] newClusters = new double[K][D];
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < D ; i++) {
                double sum = 0;
                for(int d=0; d<M; d++){
                    sum += rk[d][k]*data[d][i];
                }
                newClusters[k][i] = sum/Rk[k];
            }
        }
        return newClusters;

    }


    /**
     * mise à jour du tableau de sigma
     * @param data
     * @param centres
     * @param rk
     * @return
     */
    public static double[][] sigmasUpdate(double[][] data, double[][] centres, double[][] rk) {
        int D = data[0].length;
        int M = data.length;
        int K = centres.length;
        double[][] sigmaUpdated = new double[K][D];
        double[] Rk = sommeRk(rk);
        for (int k = 0; k < K ; k++) {
            for (int i = 0; i < D; i++) {
                double tot = 0f;
                for (int d = 0; d < M; d++) {
                    tot+= rk[d][k]*(data[d][i]-centres[k][i])*(data[d][i]-centres[k][i]);
                }
                sigmaUpdated[k][i] = tot/Rk[k];
            }
        }
        return sigmaUpdated;
    }

    /**
     * calcule de l'enumerateur
     * @param data
     * @param centres
     * @param donnee
     * @param ro
     * @param k
     * @param sigmas
     * @return calcul enumerateur
     */
    public static double enumerateur(double[][] data, double[][] centres, int donnee, double[] ro,int k, double[][]sigmas) {
        double pdt = 1f;
        int M = data.length;
        int D = data[0].length;
        int K = centres.length;
        for (int j = 0; j < D; j++) {
            double xc = (data[donnee][j]-centres[k][j])*(data[donnee][j]-centres[k][j]);
            pdt *=Math.exp( (-1)*xc/(2*sigmas[k][j]))/ Math.sqrt(2*Math.PI*(sigmas[k][j]));
        }
        pdt*=ro[k];
        return pdt;
    }



    /**
     *
     * @param data
     * @param centres
     * @param ro
     * @param sigma
     * @param donnee
     * @return
     */
    public static double denumerateur(double[][] data, double[][] centres,double[]ro, double[][] sigma, int donnee) {
        double sum = 0f;
        int K = centres.length;
        for (int l = 0; l < K ; l++) {
            sum+= enumerateur(data, centres,donnee, ro, l, sigma);
        }
        return sum;
    }



    /**
     * assigne chaque donnée à une gaussienne
     * @param data
     * @param centres
     * @param ro
     * @param sigma
     * @return les assignements
     */
    public static double[][] assigner(double[][] data,double[][] centres, double[]ro, double[][] sigma){
        int M = data.length;
        int K = centres.length;
        double[][] tabAssignement = new double[M][K];
        for (int d = 0; d < M; d++) {
            double denum = denumerateur(data,centres,ro,sigma,d);
            for (int k = 0; k < K; k++) {
                double enumerateur = enumerateur(data,centres,d,ro,k,sigma);
                tabAssignement[d][k] = enumerateur/denum;
            }
        }
        return tabAssignement;
    }

    /**
     * Calcul le score pour chaque donnée
     * @param data
     * @param centres
     * @param ro
     * @param sigmas
     * @param donnee
     * @return le score
     */
    public static double score(double[][] data, double[][]centres,double[] ro, double[][] sigmas, int donnee){
        return(Math.log(denumerateur(data,centres,ro,sigmas,donnee)));
    }

    /**
     * Calcul le score total de la gaussienne
     * @param data
     * @param centres
     * @param ro
     * @param sigmas
     * @return le score total
     */
    public static double scoreTotal(double[][] data, double[][]centres, double[]ro, double[][]sigmas){
        int M = data.length;
        double scoreTotal = 0f;
        for (int d = 0; d <M ; d++) {
            scoreTotal+=score(data,centres,ro,sigmas,d);
        }
        return scoreTotal/M;
    }
    /**
     * Fonction qui calcul une distance entre un point et un cluster
     * @param X         Notre donnnée
     * @param centre    Notre cluster
     * @return La distance (double)
     */
    public static double distance(double[] X, double[] centre){
        double dist = 0.;
        for (int i = 0; i < X.length ; i++) {
            dist += (centre[i]-X[i])*(centre[i]-X[i]);
        }
        return Math.sqrt(dist);
    }

    /**
     * Fonction qui calcul la distance entre une donnée et des clusters
     * @param clusters  Nos clusters
     * @param data      Notre donnée
     * @return    La distance minimale
     */
    public static double minDist(double[][] clusters, double[] data){
        double distMin = distance(clusters[0], data);;
        for (int l = 1; l < clusters.length; l++) {
            double d = distance(clusters[l], data);
            if(d<distMin){
                distMin = d;
            }
        }
        return distMin;
    }

    /**
     * Fonction qui initialise nos clusters de façon aléatoire
     * @param X Nos données
     * @param i Notre indice_aléatoire_unifome
     * @param K Le nombre de clusters dont on a besoin
     * @return  Nos clusters initialisés
     */
    public static double[][] ClustersInit(double[][] X, int i, int K){
        int M = X.length;       // Nombres de données
        int D = X[0].length;    // Dimension de nos données

        double[][] clusters = new double[K][D];
        clusters[0] = X[i].clone();
        for (int k = 1; k < K; k++) {
            double[] dist_min = new double[M];
            for (int m = 0; m < M; m++) {
                dist_min[m] = minDist(clusters, X[m]);
            }
            clusters[k] = X[MixGauss.argmax(dist_min)].clone();

        }
        return clusters;
    }
}
