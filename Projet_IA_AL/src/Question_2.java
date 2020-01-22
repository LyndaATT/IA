import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import mnisttools.MnistReader;

public class Question_2 {
	public static String path="/home/lynda/Documents/ml/";
    public static String labelDB=path+"emnist-byclass-train-labels-idx1-ubyte";
    public static String imageDB=path+"emnist-byclass-train-images-idx3-ubyte";
    public static String imageTestDB=path+"emnist-byclass-test-images-idx3-ubyte";
    public static String labelTestDB=path+"emnist-byclass-test-labels-idx1-ubyte";
    
    /** Parametres **/
    /* Parametres */
    // Na exemples pour l'ensemble d'apprentissage
    public static final int Na = 5000;
    // Nv exemples pour l'ensemble d'évaluation
    public static final int Nv =1000;
    // Nv exemples pour l'ensemble de test
    public static final int Nt =1000;
    // MnistReader db = new MnistReader(labelDB, imageDB) :
    public static MnistReader db = new MnistReader(labelDB, imageDB);
    public static int longueur = db.getImage(1)[1].length;
    public static int largeur = db.getImage(1).length;
    public static MnistReader dbTest = new MnistReader(labelTestDB, imageTestDB);
    /*BinariserImage :
     *      image: une image int à deux dimensions (extraite de MNIST)
     *      seuil: parametre pour la binarisation
     *  on binarise l'image à l'aide du seuil indiqué
     */
    public static int[][] BinariserImage(int[][] image, int seuil) {
        int[][] imageBinarise = new int[image.length][image[0].length];
        for(int j = 0; j < image.length; j++) {
            for(int i = 0; i < image[j].length; i++) {
                if (image[j][i] >= seuil) {
                    imageBinarise[j][i] = 1;
                } else {
                    imageBinarise[j][i] = 0;
                }
            }
        }
        return imageBinarise;
    }
    
    /*
     *  ConvertImage :
     *      image: une image int binarisée à deux dimensions
     *
     *  1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de tail dx.dy
     *  2. on rajoute un élément en première position du tableau qui sera à 1
     *  La taille finale renvoyée sera dx.dy + 1
     *
     */
    public static float[] ConvertImage(int[][] image) {
        float[] imageConvertie = new float[image.length*image[0].length+1]; imageConvertie[0] = 1;
        int z=1;
        for (int k = 0; k < image.length; k++) {
            for (int l = 0; l <image[0].length; l++) {
                imageConvertie[z] = image[k][l];
                z++;
            }
        }
        return imageConvertie;
    }
	
	public static void main(String[] args) throws IOException {
		
            int itlabel =0;
            int ilabel = 0;
            int i = 0;
            int it = 1;
            float[][] trainData = new float[Na][largeur*longueur+1];
            float[][] validData = new float[Nv][largeur*longueur+1];
            float[][] testData = new float[Nt][largeur*longueur+1];

            int [] label = new int[Na];
            int [] validLabel = new int [Nv];
            int[] sonIndiceDansDB = new int[Nv];
            int [] testLabel = new int[Nt];
            /** Ensemble de Test **/
            while (it   < Nt + 1) {
                i++;
                itlabel = dbTest.getLabel(i);
                if (itlabel >= 10 && itlabel <= 21) {
                	testLabel[it - 1] = itlabel - 10;
                    testData[it - 1] = ConvertImage(BinariserImage(dbTest.getImage(i), 180));
                    it++;
                }
            }
            

            /**Ensemble d'Apprentissage**/
            it=1;
            i=0;
            while (it   < Na + 1) {
                i++;
                ilabel = db.getLabel(i);
                if (ilabel >= 10 && ilabel <= 21) {
                    label[it - 1] = ilabel - 10;
                    trainData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
                    it++;
                }
            }
            
            

            /**Ensemble de validation**/
            it=1;
            while (it+i < Nv + 1+i) {
                i++;
                ilabel = db.getLabel(i);
                if (ilabel >= 10 && ilabel <= 21) {
                    validLabel[it - 1] = ilabel - 10;
                    validData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
                    it++;
                }
            }
        
		
		FileWriter fw = new FileWriter("etaAndNv.d");
	
		for(float eta = 0.08f; eta>=0.0f;eta-=0.001) {
			/** On initialise les poids**/
			float[][] poids = PerceptronMulti.InitialiseW(longueur * largeur + 1);
			/** On se fixe une itterationMAx**/
			int itterationMax = 0;
            for (int itteration = 0; itteration < itterationMax; itteration++) {
                int nbErreur = PerceptronMulti.epoqueApprentissage(trainData, poids, label, eta);
            }
            int nbErreurValidation = PerceptronMulti.epoqueValidation(validData, poids,validLabel, eta);
            fw.write("" + eta + "    " + nbErreurValidation + "\n");
            /** Score avec le meilleur eta **/
            System.out.println("Le score de l'ensemble de validation"+(float)PerceptronMulti.epoqueValidation(validData, poids,validLabel,(float)0.01)/100+"%");
            System.out.println("Le score de l'ensemble de Test : "+(float)PerceptronMulti.epoqueValidation(testData, poids,testLabel,(float)0.01)/100+"%");

		}
		fw.close();
		
           
	}
}
