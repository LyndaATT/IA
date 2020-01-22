import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.util.*;
import java.io.*;
import mnisttools.MnistReader;
import javax.imageio.ImageIO;

public class Question_3 {
	public static String path="/home/lynda/Documents/ml/";
    public static String labelDB=path+"emnist-byclass-train-labels-idx1-ubyte";
    public static String imageDB=path+"emnist-byclass-train-images-idx3-ubyte";
    public static String imageTestDB=path+"emnist-byclass-test-images-idx3-ubyte";
    public static String labelTestDB=path+"emnist-byclass-test-labels-idx3-ubyte";
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
    
    /* MATRICE DE CONFUSION
     * @param : Donnees, labels, poids
     * @return : "Affiche et retourne la matrice de confusion
     */
    public static int[][] confusionMatrix(float[][] datas, int[] labels, float[][] w){
        int[][] confMat = new int[12][12];
        for (int Ligne = 0; Ligne < 12; Ligne++) {
            for (int Colonne = 0; Colonne < 12; Colonne++) {
                confMat[Ligne][Colonne] = 0;
            }
        }
        for (int m = 0; m < Nv; m++) {
            int labelPerceptron = PerceptronMulti.argmax(PerceptronMulti.InfPerception(datas[m], w));
            int labelReel = labels[m];
            confMat[labelReel][labelPerceptron] ++;
        }
        for (int j = 0; j < 12; j++) {
            System.out.println(Arrays.toString(confMat[j]));
        }
        return confMat;
    }
    
    public static void main(String[] args) {
    	int ilabel = 0;
        int i = 0;
        int i2 = 0;
        int it = 1;
        float[][] trainData = new float[Na][largeur*longueur+1];
        float[][] validData = new float[Nv][largeur*longueur+1];
        float[][] testData = new float[Nt][largeur*longueur+1];

        int [] label = new int[Na];
        int [] validLabel = new int [Nv];
        int[] sonIndiceDansDB = new int[Nv];
        int [] testLabel = new int[Nt];
    //  Apprentissage
	    while (it + i < Na + 1 + i) {
	        i++;
	        i2++;
	        ilabel = db.getLabel(i);
	        if (ilabel >= 10 && ilabel <= 21) {
	            label[it - 1] = ilabel - 10;
	            trainData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
	            it++;
	        }
	    }
	    it = 1;
     // Ainsi si on veut aceder a l'image m de validData il suffira de faire db.getImage(i2+m+1)
        while (it + i< Nv + 1 + i) {
            i++;
            ilabel = db.getLabel(i);
            if (ilabel >= 10 && ilabel <= 21) {
                validLabel[it - 1] = ilabel - 10;
                validData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
                sonIndiceDansDB[it-1]= i;
                it++;
            }
        }
        	// On se fixe un etat
        float eta = (float)0.01;
	    	// On initialise un poid au hasard
	    float[][] poid = PerceptronMulti.InitialiseW(longueur * largeur + 1);
	    	// ne se fixe une itterationMAx
        int itterationMax = 200;
        	// Phase d'apprentissage
        for (int itteration = 0; itteration < itterationMax; itteration++) {
            int nbErreur = PerceptronMulti.epoqueApprentissage(trainData, poid, label, eta);
        }
        int nbErreurValidation = PerceptronMulti.epoqueValidation(validData, poid, validLabel, eta);
        System.out.println("Nombre d'erreur dans la phase de validation  : " + nbErreurValidation);
        
        
        	// ON PASSE A LA MATRICE DE CONFUSION
        System.out.println("La matrice de confusion pour l'ensemble d'apprentissage : ");
        int[][] confMatTrain = confusionMatrix(trainData,label,poid); // Affiche et renvoie la matrice de confusion
        System.out.println("La matrice de confusion pour l'ensemble de validation : ");
        int[][] confMatValid = confusionMatrix(validData,validLabel,poid);        
	}
}
