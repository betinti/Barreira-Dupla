package br.pucpr;

import java.io.*;

public class FileParse {

    private final String name;
    private final BufferedReader readFile;
    private final int size;
    private int[] integerArray = new int[Main.tam];

    public FileParse(String name) throws IOException {
        this.name = name;
        this.size = Main.tam;
        this.readFile = new BufferedReader(new FileReader(new File(name)));

        for (int i = 0; i < size; i++){
            integerArray[i] = Integer.parseInt(readFile.readLine());
        }
    }

    public int[] getIntegerArray() {
        return integerArray;
    }

}
