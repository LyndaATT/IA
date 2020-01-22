import javax.sound.midi.SysexMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.lang.Math;

public class TasGaussien {
    /*
     * Mes variables Globales :
     */
    public static final int M = 100;
    public static final int D = 1000;

    public static double[][] histogramme(double xmin, double xmax, int NbCases, double[] ech) {
        double[][] Histo = new double[2][NbCases];
        double tailleCase = (xmax - xmin) / NbCases;
        int caseCorrespondante = -1;

        for (int i = 0; i < Histo[1].length; i++) {
            Histo[0][i] = tailleCase * i + xmin;
            Histo[1][i] = 0;
        }
        for (int i = 0; i < ech.length; i++) {
            caseCorrespondante = (int) ((ech[i] - xmin) / tailleCase);
            if (caseCorrespondante >= NbCases) caseCorrespondante = NbCases - 1;
            if (caseCorrespondante < 0) caseCorrespondante = 0;
            Histo[1][caseCorrespondante]++;
        }
        return Histo;
    }

    public static double maxT(double[] tab) {
        double max = tab[0];
        for (int i = 1; i < tab.length; i++) {
            if (tab[i] > max) {
                max = tab[i];
            }
        }
        return max;
    }

    public static double maxT(double[][] tab) {
        double max = tab[0][0];
        for (int j = 0; j < tab.length; j++) {
            for (int i = 1; i < tab[0].length; i++) {
                if (tab[j][i] > max) {
                    max = tab[j][i];
                }
            }
        }
        return max;
    }


    public static double minT(double[] tab) {
        double min = tab[0];
        for (int i = 1; i < tab.length; i++) {
            if (tab[i] < min) {
                min = tab[i];
            }
        }
        return min;
    }

    public static double minT(double[][] tab) {
        double min = tab[0][0];
        for (int j = 0; j < tab.length; j++) {
            for (int i = 1; i < tab[0].length; i++) {
                if (tab[j][i] < min) {
                    min = tab[j][i];
                }
            }
        }
        return min;
    }

    /**
     * Renvoie la moyenne d'un tableau passÃ© en parametre
     *
     * @param tab Tab
     * @return Moy
     */
    public static double moyenne(double[] tab) {
        double somme = 0;
        for (int i = 0; i < tab.length; i++) {
            somme = tab[i];
        }
        return (somme / tab.length);
    }

    /**
     * Nous renvoie l'Ã©cart type d'un tableau de donnÃ©es
     *
     * @param tab notre tableau de donnÃ©es
     * @return l'Ã©cart type de ces donnÃ©es
     */
    public static double ecartType(double[] tab) {
        double moyenne = moyenne(tab);
        double ecartType = 0;
        for (int i = 0; i < M; i++) {
            ecartType += (tab[i] - moyenne) * (tab[i] - moyenne);
        }
        ecartType /= tab.length;
        ecartType = Math.sqrt(ecartType);
        return ecartType;
    }
}