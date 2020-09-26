package br.pucpr;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Trabalhadora extends Thread{

    //todo: Globais
    private final int Id;
    private int cont = 0;
    private final int minSleep;
    private final int maxSleep;
    private String alert;
    private final Random random = new Random();

    //todo: br.pucpr.Files
    private String nameRandom;
    private String nameSort;
    private BufferedReader newRandomFileR; // = new BufferedReader(new FileReader(new File("newRandomFile.txt")));
    private BufferedWriter newRandomFileW; // = new BufferedWriter(new FileWriter(new File("newRandomFile.txt")));
    private BufferedWriter newSortFile; // = new BufferedWriter(new FileWriter(SortFile));

    private int[] sortedFile = new int[Main.tam];

    public Trabalhadora(int Id, int minSleep, int maxSleep) throws IOException {
        this.Id = Id;
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
    }

    private void dormir() throws InterruptedException {
        Thread.sleep(random.nextInt(maxSleep -minSleep) +minSleep);
    }

    private void generateFile() throws InterruptedException, IOException {
        Main.mutexFiles.acquire();
            int getContGlobal = Main.contadorGlobal++;
        Main.mutexFiles.release();
        this.nameRandom =
                "C:\\Users\\User\\OneDrive\\IDE\\InteliJ\\programacaoDistribuidaParalelaEConcorrente\\barreiraDupla\\src\\br\\pucpr\\Files\\newRandomFile" + "_" + String.valueOf(getContGlobal) + "_" + String.valueOf(this.Id) + ".txt";
        this.nameSort =
                "C:\\Users\\User\\OneDrive\\IDE\\InteliJ\\programacaoDistribuidaParalelaEConcorrente\\barreiraDupla\\src\\br\\pucpr\\Files\\newSortFile" + "_" + String.valueOf(getContGlobal) + "_" + String.valueOf(this.Id) + ".txt";
        File newRandomFile = new File(nameRandom);
        File newSortFile = new File(nameSort);
        newRandomFile.createNewFile();
        newSortFile.createNewFile();
        this.newRandomFileR = new BufferedReader(new FileReader(new File(nameRandom)));
        this.newRandomFileW = new BufferedWriter(new FileWriter(new File(nameRandom)));
        this.newSortFile = new BufferedWriter(new FileWriter(new File(nameSort)));
    }

    private void newRandomFile() throws IOException {
        for (int i = 0; i < Main.tam; i++){
            newRandomFileW.write(String.valueOf(random.nextInt(Main.maxRand)));
            newRandomFileW.newLine();
        }
        newRandomFileW.close();
    }

    private void newSortFile() throws IOException {
        for (int i = 0; i < Main.tam; i++)
            sortedFile[i] = Integer.parseInt(newRandomFileR.readLine());
        newRandomFileR.close();
        Arrays.sort(sortedFile);
        for (int i = 0; i < Main.tam; i++){
            newSortFile.write(String.valueOf(sortedFile[i]));
            newSortFile.newLine();
        }
        newSortFile.close();
    }

    @Override
    public void run() {
        while (true)
        try {
            this.alert = "\033[0;33m" + "Trbalhadora (" + Id + ") [" + cont + "] >>";

            System.out.println(alert+ " começou o processo");

            generateFile();
            newRandomFile();
            newSortFile();

            dormir();

            //  Barreira Dupla #1
            Main.mutexBarreria.acquire();
                Main.contadorBarreira++;
                if (Main.contadorBarreira == Main.nMaxBarreria){
                    Main.barreiraSaida.acquire(); //    fecha
                    Main.barreiraEntrada.release(); //  abre
                }
            System.out.println(alert+ " chegou da primeira barreira");
            Main.mutexBarreria.release();
            Main.barreiraEntrada.acquire();
            Main.barreiraEntrada.release();
            System.out.println(alert+ " passou da primeira barreira");


            //  ponto critico   -> Insere o (nome do) arquivo na fila de arquivos gerados
            Main.mutexFiles.acquire();
                Main.arquivosGerados[Main.ultimoFile] = nameSort; //  Adiciona (String) na fila de File
                Main.ultimoFile = Main.ultimoFile +1;
            Main.mutexFiles.release();

            Main.controlador.release(); //Controlador -> Sinaliza a thread Combinadora que o novo arquivo está disponível.

            System.out.println(alert+ " adicionou na fila de arquivos gerados");


            //  Barreira Dupla #2
            Main.mutexBarreria.release();
                Main.contadorBarreira--;
                if (Main.contadorBarreira == 0){
                    Main.barreiraEntrada.acquire(); //  fecha
                    Main.barreiraSaida.release(); //    abre
                }
            System.out.println(alert+ " chegou da segunda barreira");
            Main.mutexBarreria.release();
            Main.barreiraSaida.acquire();
            Main.barreiraSaida.release();
            System.out.println(alert+ " passou da segunda barreira");

            cont++;
            newRandomFileR.close();
            newRandomFileW.close();
            newSortFile.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
