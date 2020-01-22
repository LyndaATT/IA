import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Question1 {
    static BufferedImage bi;

    public static int[] occ(double[][] ass, int nbrKlus){
        int[] tabOcc = new int[nbrKlus];
        for (int i = 0; i < ass.length; i++) {
            tabOcc[MixGauss.argmax(ass[i])]+=1;
        }
        return tabOcc;
    }
    /**
     * Procédure qui permet de dessiner et d'enregister nos images
     * @param p        Path/Chemin où dessiner les images
     * @param height   Hauteur de l'image
     * @param width    Largeur de l'image
     * @param ass      Tableau d'assignmenets
     * @param donnees  Nos données
     * @param bui      Notre BufferedImage
     * @throws IOException
     */
    public static void dessiner(String p, int height, int width, double[][] ass, double[][] donnees, BufferedImage bui) throws IOException {
        String path;
        BufferedImage image01 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image02 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image03 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image04 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image05 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image06 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image07 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image08 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image09 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image10 = new BufferedImage(bui.getWidth(), bui.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (MixGauss.argmax(ass[i * width + j]) == 0) {
                    image01.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 1) {
                    image02.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 2) {
                    image03.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 3) {
                    image04.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 4) {
                    image05.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 5) {
                    image06.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 6) {
                    image07.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 7) {
                    image08.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 8) {
                    image09.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }
                if (MixGauss.argmax(ass[i * width + j]) == 9) {
                    image10.setRGB(j, i, new Color((int) (donnees[i * width + j][0] * 255), (int) (donnees[i * width + j][1] * 255), (int) (donnees[i * width + j][2] * 255)).getRGB());
                }

            }
        }

        path = p;
        ImageIO.write(image01, "PNG", new File(path + "image01.png"));
        ImageIO.write(image02, "PNG", new File(path + "image02.png"));
        ImageIO.write(image03, "PNG", new File(path + "image03.png"));
        ImageIO.write(image04, "PNG", new File(path + "image04.png"));
        ImageIO.write(image05, "PNG", new File(path + "image05.png"));
        ImageIO.write(image06, "PNG", new File(path + "image06.png"));
        ImageIO.write(image07, "PNG", new File(path + "image07.png"));
        ImageIO.write(image08, "PNG", new File(path + "image08.png"));
        ImageIO.write(image09, "PNG", new File(path + "image09.png"));
        ImageIO.write(image10, "PNG", new File(path + "image10.png"));
    }


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
        Color[] tabColor= new Color[im_pixels.length]; // COLOR CONTIENT LA VALEUR DE CHAQUE PIXEL SOUS 24 BITS
        for(int i=0 ; i<im_pixels.length ; i++)
            tabColor[i]=new Color(im_pixels[i]);

        /**********************************************/
        /*******************MAIN***********************/
        /**********************************************/

        int K = 10; // Nombre de gaussiennes
        int D =  3; // Dimension de nos données
        int M = height*width; // Nombres de données
        int itteration = 0; int itterationMax = 100;
        double eps = 0.01f;

        double[][] data = new double[M][D];
        double[][] clusters = new double[K][D];
        double[][] clustersClone = new double[K][D];
        double[][] assignements = new double[M][K];
        double[][] variances = new double[K][D];
        double[] densites = new double[K];
        double sigmaVar = 0.05;
        double scoreMax = 0;
        double scoreMin = 1;

        Random rd = new Random();
        // Remplissage de nos tableau data
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                data[i * width + j][0] = (double) (tabColor[i * width + j].getRed()) / 255.;
                data[i * width + j][1] = (double) (tabColor[i * width + j].getGreen()) / 255.;
                data[i * width + j][2] = (double) (tabColor[i * width + j].getBlue()) / 255.;
            }
        }
        for (int cdt = 0; cdt < 10; cdt++) {
            sigmaVar += 0.05;
            // Initialisation de la densité
            for (int k = 0; k < K; k++) {
                densites[k] = 1. / K;
                for (int m = 0; m < D; m++) {
                    variances[k][m] = sigmaVar;
                }
            }

            // Initialisation de nos clusters :
            clusters = MixGauss.ClustersInit(data, rd.nextInt(M), K);

            for (int k = 0; k < K; k++) {
                System.out.println("Cluster " + k + " = " + Arrays.toString(clusters[k]));
            }
            do {
                clustersClone = clusters.clone();
                assignements = MixGauss.assigner(data, clusters, densites, variances);
                densites = MixGauss.UpdateRo(assignements);
                clusters = MixGauss.Updateclusters(assignements, data);
                variances = MixGauss.sigmasUpdate(data,clusters,assignements);
            } while (!MixGauss.sameAs(clusters, clustersClone, eps));

            int[] tab = occ(assignements, K);
            /*for (int i = 0; i < tab.length; i++) {
                System.out.println(tab[i] + " pixels sont assignés au cluster " + i);
            }*/
            double sc = MixGauss.scoreTotal(data, clusters, densites,variances);
            if(cdt == 0){
                scoreMin = sc;
                scoreMax = sc;
                dessiner(path, height, width, assignements, data, bui);
                dessiner(path, height, width, assignements, data, bui);
            } else {
                if(sc > scoreMax){
                    scoreMax = sc;
                    dessiner(path, height, width, assignements, data, bui);
                }
                if(sc < scoreMin){
                    scoreMin = sc;
                    dessiner(path, height, width, assignements, data, bui);
                }

            }
        }
    }
}
