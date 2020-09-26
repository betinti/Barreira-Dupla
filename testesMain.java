package br.pucpr;

import java.io.*;
import java.util.*;

public class testesMain {

    private static Random random = new Random();

    // br.pucpr.Trabalhadora
    private static int[] sortedFile = new int[Main.tam];


    private static void newRandomFile(BufferedWriter newRandomFileW) throws IOException {
        for (int i = 0; i < Main.tam; i++){
            newRandomFileW.write(String.valueOf(random.nextInt(Main.maxRand)));
            newRandomFileW.newLine();
        }
        newRandomFileW.close();
    }

    private static void newSortFile(BufferedReader newRandomFileR, BufferedWriter newSortFileW) throws IOException {
        for (int i = 0; i < Main.tam; i++)
            sortedFile[i] = Integer.parseInt(newRandomFileR.readLine());
        newRandomFileR.close();
        Arrays.sort(sortedFile);
        for (int i = 0; i < Main.tam; i++){
            newSortFileW.write(String.valueOf(sortedFile[i]));
            newSortFileW.newLine();
        }
        newSortFileW.close();
    }

    private static void readFile(BufferedReader newSortFile) throws InterruptedException, IOException {
        int inf = Integer.MAX_VALUE;
        BufferedWriter finalFile = new BufferedWriter(new FileWriter("finalFile.txt", true));
        int[] sort = new int[Main.tam*4];
        int cont = 0;
        for (int i = 0; i < Main.tam-10; i++)
            sort[cont++] = Integer.parseInt(newSortFile.readLine());
        Arrays.sort(sort);
        System.out.println(Arrays.toString(sort));
        for(int i : sort){
            int eguals = 0;
            for (int j = 0; j < sort.length; j++){
                if (i == sort[j]){
                    eguals++;
                    if (eguals > 1)
                        sort[j] = inf;
                }
            }
        }
        System.out.println(Arrays.toString(sort));
        for (int i : sort){
            if (i != inf){
                System.out.println(i);
                finalFile.append(String.valueOf(i));
                finalFile.newLine();
                finalFile.flush();
            }
        }
    }

    private static int getMenor(int[] l){
        int inf = Integer.MAX_VALUE;
        int menor = l[0];
        int index = 0;
        for (int i = 0; i < l.length; i++)
            if (menor > l[i]){
                menor = l[i];
                index = i;
            }
        l[index] = inf;
        return menor;
    }

    private static void merging(int[] f, int[] b1, int[] b2, int[] b3, int[] b4){
        int index = 0;
        for (int i = 0; i < b1.length; i++){
            int[] mem = {b1[i], b2[i], b3[i], b4[i]};
            for (int j = 0; j < mem.length; j++)
                f[index++] = getMenor(mem);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        /*
        //br.pucpr.Trabalhadora
        final BufferedReader newRandomFileR = new BufferedReader(new FileReader(new File("newRandomFile.txt")));
        final BufferedWriter newRandomFileW = new BufferedWriter(new FileWriter(new File("newRandomFile.txt")));
        final BufferedWriter newSortFileW = new BufferedWriter(new FileWriter(new File("newSortFile.txt")));
        final BufferedReader newSortFileR = new BufferedReader(new FileReader(new File("newSortFile.txt")));

        newRandomFile(newRandomFileW);
        newSortFile(newRandomFileR, newSortFileW);

        readFile(newSortFileR);
         */

        float time01 = System.currentTimeMillis();

        Random r = new Random();

        int size = 1000000;

        int[] f = new int[size*4];

        int[] b1 = new int[size];
        int[] b2 = new int[size];
        int[] b3 = new int[size];
        int[] b4 = new int[size];

        for (int i = 0; i < size; i++)
            b1[i] = r.nextInt(100);

        for (int i = 0; i < size; i++)
            b2[i] = r.nextInt(100);

        for (int i = 0; i < size; i++)
            b3[i] = r.nextInt(100);

        for (int i = 0; i < size; i++)
            b4[i] = r.nextInt(100);

        merging(f, b1, b2, b3, b4);

        System.out.println(Arrays.toString(f));

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < f.length; i++)
            set.add(f[i]);

        System.out.println();
        System.out.println(set);

        float time02 = System.currentTimeMillis();

        System.out.println();
        System.out.println(time02 - time01);

/*
        int[] listaGrande = new int[10000];
        for (int i = 0; i < 10000; i++)
            listaGrande[i] = new Random().nextInt(50);

        Arrays.sort(listaGrande);

        System.out.println(Arrays.toString(listaGrande));

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < listaGrande.length; i++){
            set.add(listaGrande[i]);
        }

        System.out.println(set);

 */

    }

}
