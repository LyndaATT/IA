import java.util.*;
import java.math.*;
public class PerceptronMulti {
	  /**Générateur de nombres aléatoires**/
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);


    /** OneHot :
     * @param etiquette : entier, qui est l'étiquette de notre classe (db.getLabel(i+1) [0, 9]
     * @return vecteur avec la ieme composante correspondate à l'etiquette
     */
    public static int[] OneHot(int etiquette) {
        int[] vec = new int[12];
        for (int i = 0; i < 12; i++) {
            vec[i]=0;
        }
        vec[etiquette]=1;
        return vec;
    }


    /** Produit Scalaire
     * @param donnee :tableau de float contenant les données
     * @param poids : tableau 2d de float contenant les poids du perceptron
     * @param label : entier correspondant à une classe
     * @return la somme du produit donnée*poids
     */
    public static float prodScal(float[] donnee, float[][] poid, int label) {
        float resultat = (float) 0;
        float w0 = poid[label][0];
        for (int j = 1; j < donnee.length; j++) {
            resultat += donnee[j] * poid[label][j];
        }
        resultat += w0;
        return resultat;
    }

    /** InfPerceptron :
     * @param donnee : tableau de float d'une donnée
     * @param poids : tableau 2d de float des poids du perceptron
     * @return le tableau de probabilités d'appartenir à une classe
     *
     */
    public static float[] InfPerception(float[] donnee, float[][] poid){
        float[] probaDeChaqueClasse = new float[poid.length];
        float[] probaAppartenance = new float[poid.length];
        float somme = 0;
        for (int k = 0; k < poid.length; k++) {
            probaDeChaqueClasse[k] = (float)Math.exp((double)prodScal(donnee,poid,k));
            somme += probaDeChaqueClasse[k];
        }
        for (int k = 0; k < poid.length; k++) {
            probaAppartenance[k] = probaDeChaqueClasse[k]/somme;
        }
        return probaAppartenance;
    }

    /**
     *  InitialiseW :
     *  @param D : la dimension
     *  @return le tableau des poids du perceptron initialisé
     *  
     */
    public static float[][] InitialiseW(int D) {
        float[][] poidInitial = new float[12][D];
        for(int j = 0; j<12 ; j++) {
            for (int i = 0; i < D; i++) {
                poidInitial[j][i] =(float) (1. / D) * GenRdm.nextFloat();
            }
        }
        return poidInitial;
    }

    /** 
     * miseAJour :
     * @param donnee : tableau contenat la donnée x
     * @param poids : tableau contenant les poids du perceptron 
     * @param eta : float representant le taux d'apprentissage
     * @param label : entier qui correspond à la classe
     * @void met à jour les poids du perceptron
     *
     */
    public static void miseAJour(float[] donnee, float[][] poid, float eta, int label){
        float[] yl = InfPerception(donnee, poid);
        int[] OH = OneHot(label);
        for (int l = 0; l < poid.length; l++) {
            for (int j = 0; j < poid[l].length; j++) {
                poid[l][j] -= donnee[j]*eta*(yl[l] - OH[l]);
            }
        }
    }

    /**
     * argmax :
     * @param  tab : un tableau
     * @return un entier correspondant à l'indice de la valeur max du tableau
     *
     */
    public static int argmax(float[] tab) {
        float max = tab[0];
        int indice = 0;
        for (int i = 1; i < tab.length; i++) {
            if(tab[i]>max) {max=tab[i]; indice=i;}
        }
        return indice;
    }
    
    /**
     * epoqueApprentissage :
     * @param donnee : tableau contenant la donnee
     * @param poids :  tableau contenant les poids du perceptron
     * @param label :  entier representant la classe
     * @param eta   :   entier representant le taux d'apprentissage
     * @return entier representant le nombre d'erreur sur l'ensemble d'apprentissage
     */
    public static int epoqueApprentissage(float[][] donnee, float[][] poid, int[] label,float eta){
        int nombreErreur = 0;
        for (int m = 0; m < donnee.length; m++) {
            float[] tableauPropab = InfPerception(donnee[m], poid);
            int indiceMax = argmax(tableauPropab);
            miseAJour(donnee[m],poid,eta,label[m]);
            if(indiceMax!=label[m]){
                nombreErreur++; 
            }
        }
        return nombreErreur;
    }
    
    /**
     * epoqueValidation :
     * @param donnee : tableau contenant la donnee
     * @param poids :  tableau contenant les poids du perceptron
     * @param label :  entier representant la classe
     * @param eta   :   entier representant le taux d'apprentissage
     * @return entier representant le nombre d'erreur sur l'ensemble de validation
     */
    public static int epoqueValidation(float[][] donnee, float[][] poid, int[] label,float eta){
        int nombreErreur = 0;
        for (int m = 0; m < donnee.length; m++) {
            float[] tableauPropab = InfPerception(donnee[m], poid);
            int indiceMax = argmax(tableauPropab);
            if(indiceMax!=label[m]){
                nombreErreur++;
            }
        }
        return nombreErreur;
    }
}
