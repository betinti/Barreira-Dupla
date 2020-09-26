package br.pucpr;

import java.io.*;
import java.util.concurrent.Semaphore;

public class Main {

    //TODO: globais
    public static final int tam = 1000000;
//    public static final int tam = 100;
    public static final int maxRand = 10000000;
    public static int contadorGlobal = 0;
    public static final int nThreads = 4;
    public static final int maxSleep = 1000;
    public static final int minSleep = 500;

    //TODO: Controlador
    public static final Semaphore controlador = new Semaphore(0);

    //TODO: barreira
    public static final Semaphore mutexBarreria = new Semaphore(1);
    public static final Semaphore barreiraEntrada = new Semaphore(0);
    public static final Semaphore barreiraSaida = new Semaphore(1);
    public static int contadorBarreira = 0;
    public static final int nMaxBarreria = nThreads;

    //TODO: Fila de Arquivos
    public static Semaphore mutexFiles = new Semaphore(1);
    public static String[] arquivosGerados = new String[tam];
    public static int primeiroFile = 0;
    public static int ultimoFile = 0;

    private static void clearFiles() throws IOException {
        File folderFinal = new File("C:\\Users\\User\\OneDrive\\IDE\\InteliJ\\programacaoDistribuidaParalelaEConcorrente\\barreiraDupla\\src\\br\\pucpr\\FinalFiles");
        File[] listFinalFiles = folderFinal.listFiles();
        for (File f : listFinalFiles)
            if (f.exists()) f.delete();

        File folder = new File("C:\\Users\\User\\OneDrive\\IDE\\InteliJ\\programacaoDistribuidaParalelaEConcorrente\\barreiraDupla\\src\\br\\pucpr\\Files");
        File[] listFiles = folder.listFiles();
        for (File f : listFiles)
            if (f.exists()) f.delete();
    }

    public static void main(String[] args) throws IOException {

        clearFiles();

        //br.pucpr.Combinadora   - start();
        new Combinadora(1, minSleep, maxSleep).start();

        //Trabalhadoras - start();
        for (int i = 0; i < nThreads; i++)
            new Trabalhadora(i+1, minSleep, maxSleep).start();

    }

}
