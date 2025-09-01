import java.util.*;
import java.math.BigInteger;


public class PolynomialSolver {
    public static void main(String[] args) {
        String jsonInput = "{ \"keys\": { \"n\": 10, \"k\": 7 }, \"1\": { \"base\": \"6\", \"value\": \"13444211440455345511\" }, \"2\": { \"base\": \"15\", \"value\": \"aed7015a346d635\" }, \"3\": { \"base\": \"15\", \"value\": \"6aeeb69631c227c\" }, \"4\": { \"base\": \"16\", \"value\": \"e1b5e05623d881f\" }, \"5\": { \"base\": \"8\", \"value\": \"316034514573652620673\" }, \"6\": { \"base\": \"3\", \"value\": \"2122212201122002221120200210011020220200\" }, \"7\": { \"base\": \"3\", \"value\": \"20120221122211000100210021102001201112121\" }, \"8\": { \"base\": \"6\", \"value\": \"20220554335330240002224253\" }, \"9\": { \"base\": \"12\", \"value\": \"45153788322a1255483\" }, \"10\": { \"base\": \"7\", \"value\": \"1101613130313526312514143\" } }";
        
        int n = Integer.parseInt(jsonInput.replaceAll(".*\"n\":\\s*(\\d+).*", "$1"));
        int k = Integer.parseInt(jsonInput.replaceAll(".*\"k\":\\s*(\\d+).*", "$1"));
        List<BigInteger> roots = new ArrayList<>();


        for (int i = 1; i <= k; i++) {
            String entryRegex = "\"" + i + "\":\\s*\\{[^}]*\\}";
            String entry = jsonInput.replaceAll(".*(" + entryRegex + ").*", "$1");
            int base = Integer.parseInt(entry.replaceAll(".*\"base\":\\s*\"(\\d+)\".*", "$1"));
            String value = entry.replaceAll(".*\"value\":\\s*\"([^\"]+)\".*", "$1");
            BigInteger root = new BigInteger(value, base);
            roots.add(root);
        }

        // Use indices as x-values (0, 1, 2, ..., k-1)
        double[] x = new double[k];
        double[] y = new double[k];
        for (int i = 0; i < k; i++) {
            x[i] = i;
            y[i] = roots.get(i).doubleValue();
        }

        double[] coeffs = lagrangeCoefficients(x, y, k);
        System.out.println("Polynomial coefficients:");
        for (int i = 0; i < coeffs.length; i++) {
            System.out.print(coeffs[i] + (i < coeffs.length - 1 ? ", " : "\n"));
        }
    }

    public static double[] lagrangeCoefficients(double[] x, double[] y, int k) {
        double[] coeffs = new double[k];
        Arrays.fill(coeffs, 0);
        for (int i = 0; i < k; i++) {
            double[] basis = new double[]{1.0}; // degree 0
            for (int j = 0; j < k; j++) {
                if (j == i)
                    continue;
                basis = multiplyPoly(basis, new double[]{-x[j] / (x[i] - x[j]), 1.0 / (x[i] - x[j])});
            }
            for (int d = 0; d < basis.length; d++) {
                coeffs[d] += y[i] * basis[d];
            }
        }
        return coeffs;
    }

    public static double[] multiplyPoly(double[] a, double[] b) {
        double[] result = new double[a.length + b.length - 1];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                result[i + j] += a[i] * b[j];
        return result;
    }
}
