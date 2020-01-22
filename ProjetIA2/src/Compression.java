import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Compression {

    public static void main(String[] args) throws IOException {
        String path = "./";
        String imageMMS = path + "mms.png";

        // Lecture de l'image ici
        BufferedImage bui = ImageIO.read(new File(imageMMS));

        int width = bui.getWidth();
        int height = bui.getHeight();
        System.out.println("Hauteur=" + width);
        System.out.println("Largeur=" + height);

        //******************************************** obtenir tous les pixel de 0,0 jusqua width,height
        int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);

        /** Creation du tableau **/
        Color[] tabColor = new Color[im_pixels.length]; // COLOR CONTIENT LA VALEUR DE CHAQUE PIXEL SOUS 24 BITS
        for (int i = 0; i < im_pixels.length; i++)
            tabColor[i] = new Color(im_pixels[i]);

        /**********************************************/
        /*******************MAIN***********************/
        /**********************************************/

        int D = 3; // Dimension de nos données
        int M = height * width; // Nombres de données

        double eps = 0.01f;
        for(int K = 5; K <= 20; K+=5){
            double[][] data = new double[M][D];
            double[][] clusters = new double[K][D];
            double[][] clustersClone = new double[K][D];
            double[][] assignements = new double[M][K];
            double[][] variances = new double[K][D];
            double[] densites = new double[K];
            double scoreMax = - Double.MAX_VALUE;
            double scoreMin = Double.MAX_VALUE;

            Random rd = new Random();
            // Remplissage de nos tableau data
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    data[i * width + j][0] = (double) (tabColor[i * width + j].getRed()) / 255.;
                    data[i * width + j][1] = (double) (tabColor[i * width + j].getGreen()) / 255.;
                    data[i * width + j][2] = (double) (tabColor[i * width + j].getBlue()) / 255.;
                }
            }

            // Initialisation de nos clusters :
            clusters = MixGauss.ClustersInit(data, rd.nextInt(M), K);

            for (int k = 0; k < K; k++) {
                densites[k] = 1. / K;
                for (int m = 0; m < D; m++) {
                    variances[k][m] = 0.5;
                }
            }
            do {
                clustersClone = clusters.clone();
                assignements = MixGauss.assigner(data, clusters, densites,variances);
                densites = MixGauss.UpdateRo(assignements);
                clusters = MixGauss.Updateclusters(assignements,data);
                variances = MixGauss.sigmasUpdate(data,clusters,assignements);
            } while (!MixGauss.sameAs(clusters, clustersClone, eps));

            BufferedImage image01 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int var = i * width + j;
                    int numClusAss = MixGauss.argmax(assignements[var]);
                    image01.setRGB(j, i, new Color((int) (clusters[numClusAss][0] * 255), (int) (clusters[numClusAss][1] * 255), (int) (clusters[numClusAss][2] * 255)).getRGB());
                }
            }
            path = "./Compression/";
            ImageIO.write(image01, "PNG", new File(path + Integer.toString(K) + " Clusters.png"));
        }
    }
}
