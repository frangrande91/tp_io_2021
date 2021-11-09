package com.utn.tp.io.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BrownTable {

    public HashMap<Double, Double> hash;

    public BrownTable(){
        this.hash = new HashMap<Double, Double>();
        this.hash.put(4.5, -4.5); this.hash.put(4.4, -4.4); this.hash.put(4.3, -4.3);
        this.hash.put(4.2, -4.2); this.hash.put(4.1, -4.1); this.hash.put(3.9, -3.9);
        this.hash.put(3.8, -3.8); this.hash.put(3.7, -3.7); this.hash.put(3.6, -3.6);
        this.hash.put(3.5, -3.5); this.hash.put(3.4, -3.4); this.hash.put(3.3, -3.3);
        this.hash.put(3.2, -3.2); this.hash.put(3.1, -3.1); this.hash.put(3.0, -3.0);
        this.hash.put(2.901, -2.9); this.hash.put(2.801, -2.8); this.hash.put(2.701, -2.7);
        this.hash.put(2.601, -2.6); this.hash.put(2.502, -2.5); this.hash.put(2.403, -2.4);
        this.hash.put(2.304, -2.3); this.hash.put(2.205, -2.2); this.hash.put(2.106, -2.1);
        this.hash.put(2.008, -2.0); this.hash.put(1.911, -1.9); this.hash.put(1.814, -1.8);
        this.hash.put(1.718, -1.7); this.hash.put(1.623, -1.6);this.hash.put(1.529, -1.5);
        this.hash.put(1.437, -1.4); this.hash.put(1.346, -1.3); this.hash.put(1.256, -1.2);
        this.hash.put(1.169, -1.1); this.hash.put(1.083, -1.0); this.hash.put(1.0, -0.9);
        this.hash.put(0.920, -0.8); this.hash.put(0.843, -0.7); this.hash.put(0.769, -0.6);
        this.hash.put(0.698, -0.5); this.hash.put(0.630, -0.4); this.hash.put(0.567, -0.3);
        this.hash.put(0.507, -0.2); this.hash.put(0.451, -0.1); this.hash.put(0.399, 0.0);
        this.hash.put(0.351, 0.1); this.hash.put(0.307, 0.2); this.hash.put(0.267, 0.3);
        this.hash.put(0.230, 0.4); this.hash.put(0.198, 0.5); this.hash.put(0.169, 0.6);
        this.hash.put(0.143, 0.7); this.hash.put(0.120, 0.8); this.hash.put(0.1, 0.9);
        this.hash.put(0.083, 1.0); this.hash.put(0.069, 1.1); this.hash.put(0.056, 1.2);
        this.hash.put(0.046, 1.3); this.hash.put(0.037, 1.4); this.hash.put(0.029, 1.5);
        this.hash.put(0.023, 1.6); this.hash.put(0.018, 1.7); this.hash.put(0.014, 1.8);
        this.hash.put(0.011, 1.9); this.hash.put(0.008, 2.0); this.hash.put(0.006, 2.1);
        this.hash.put(0.005, 2.2); this.hash.put(0.004, 2.3); this.hash.put(0.003, 2.4);
        this.hash.put(0.002, 2.5); this.hash.put(0.00175, 2.6); this.hash.put(0.0015, 2.7);
        this.hash.put(0.00125, 2.8); this.hash.put(0.001, 2.9); this.hash.put(0.0009, 3.0);
        this.hash.put(0.0008, 3.1); this.hash.put(0.0007, 3.2); this.hash.put(0.0006, 3.3);
        this.hash.put(0.0005, 3.4); this.hash.put(0.0004, 3.5); this.hash.put(0.0003, 3.6);
        this.hash.put(0.0002, 3.5); this.hash.put(0.0001, 3.6); this.hash.put(0.00009, 3.7);
        this.hash.put(0.00008, 3.8); this.hash.put(0.00007, 3.9); this.hash.put(0.00006, 4.0);
        this.hash.put(0.00005, 4.1); this.hash.put(0.00004, 4.2); this.hash.put(0.00003, 4.3);
        this.hash.put(0.00002, 4.4); this.hash.put(0.00001, 4.5);
    }

    //Retorna null si el valor de zeta es invalido
    public Double calculateZeta(Double zeta) {
        Double maxKey = null;
        Double minKey = null;

        if(zeta >= 0 && zeta <= 4.5) {
            if (this.hash.containsKey(zeta)) {
                Iterator<Map.Entry<Double, Double>> iterator = hash.entrySet().iterator();
                while (iterator.hasNext() && maxKey == null) {
                    Map.Entry<Double, Double> entry = iterator.next();
                    Double aux = entry.getKey();
                    if (entry.getKey() < zeta) {
                        minKey = aux;   //Guardo el E(z) menor

                    } else
                        maxKey = entry.getKey(); //Guardo el E(z) mayor
                }

                //Comparo en cual de los 2 valores de E(z) hay menor diferencia
                if ((maxKey - zeta) < (zeta - minKey))
                    return hash.get(minKey);
                else
                    return hash.get(maxKey);
            } else
                return hash.get(zeta);
        }
        else
            return null;
    }

}
