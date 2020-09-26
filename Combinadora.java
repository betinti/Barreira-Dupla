package br.pucpr;

import java.io.*;
import java.util.*;

public class Combinadora extends Thread{

    //Todo: Globais
    private final int Id;
    private final int minSleep;
    private final int maxSleep;
    private String alert;
    private int contGlobal;
    private final int[] merging;
    private final int[] merged;
    private final int inf;

    //Todo: Files -> Global
    private String getFileName[];
    private FileParse files[];

    //Todo: Files -> Final
    private BufferedWriter finalFile;
    private String path;

    public Combinadora(int id, int minSleep, int maxSleep) throws IOException {
        this.maxSleep = maxSleep;
        this.minSleep = minSleep;
        this.Id = id;
        this.contGlobal = 0;
        this.getFileName = new String[Main.nThreads];
        this.files = new FileParse[Main.nThreads];
        this.merging = new int[Main.tam*4];
        this.merged = new int[Main.tam*4];
        this.inf = Integer.MAX_VALUE;
    }

    private int getMenor(int[] l){
        int menor = l[0];
        int index = 0;
        for (int i = 0; i < l.length; i++){
            if (menor > l[i]){
                menor = l[i];
                index = i;
            }
        }
        l[index] = inf;
        return menor;
    }

    private void merging(int[] f, int[] b1, int[] b2, int[] b3, int[] b4){
        int index = 0;
        for (int i = 0; i < b1.length; i++){
            int[] mem = {b1[i], b2[i], b3[i], b4[i]};
            for (int j = 0; j < mem.length; j++){
                f[index++] = getMenor(mem);
            }
        }
    }

    private void generateFile(String extetion) throws IOException {
        this.path = "C:\\Users\\User\\OneDrive\\IDE\\InteliJ\\programacaoDistribuidaParalelaEConcorrente\\barreiraDupla\\src\\br\\pucpr\\FinalFiles\\finalFile_" + String.valueOf(contGlobal) + "." + extetion;
        File newFinalFile = new File(path);
        newFinalFile.createNewFile();
        this.finalFile = new BufferedWriter(new FileWriter(newFinalFile, false));
    }

    private void retirarDaFila() throws InterruptedException {
        Main.mutexFiles.acquire();
            for (int i = 0; i < Main.nThreads; i++){
                this.getFileName[i] = Main.arquivosGerados[Main.primeiroFile];
                Main.primeiroFile = Main.primeiroFile +1;
            }
        Main.mutexFiles.release();
    }

    private void readFile() throws IOException {
        finalFile.append("Iteração numero: " + contGlobal);
        finalFile.newLine();    finalFile.newLine();
        finalFile.flush();

        for (int i = 0; i < Main.nThreads; i++)         //  ler os arquivos
            files[i] = new FileParse(getFileName[i]);   //  classe que salve esses arquivos

        merging(merging, files[0].getIntegerArray(), files[1].getIntegerArray(), files[2].getIntegerArray(), files[3].getIntegerArray());

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < merging.length; i++)
            set.add(merging[i]);

        List<Integer> sortedList = new ArrayList<>(set);
        Collections.sort(sortedList);

        for (int i : sortedList){
            finalFile.write(String.valueOf(i));
            finalFile.newLine();
        }

        finalFile.close();
        System.out.println("\033[1;34m" + "Merging: " + merging.length);
        System.out.println("\033[1;34m" + "Merged: " + merged.length);
    }

    private void dormir() throws InterruptedException {
        Thread.sleep(new Random().nextInt(maxSleep -minSleep) +minSleep);
    }

    @Override
    public void run() {
        while (true)
            try {
                this.alert = "\033[0;31m" + "Combinadora (" + Id + ") [" + contGlobal + "] >> ";

                generateFile("csv");    //Cria um novo arquivo de nome único

                //Controlador
                for (int i = 0; i < Main.nThreads; i++)
                    Main.controlador.acquire(); //Controladora -> Espera a sinalização de quatro novos arquivos

                System.out.println(alert+ "começou processo");

                //  ponto critico
                retirarDaFila();    //Remove os quatro arquivos (String) da fila de arquivos gerados
                readFile();         //Adiciona ao novo arquivo de nome único o resultado do merge

                System.out.println(alert+ "terminou processo");

                contGlobal++;
            } catch (Exception e){
                e.printStackTrace();
            }
    }
}
