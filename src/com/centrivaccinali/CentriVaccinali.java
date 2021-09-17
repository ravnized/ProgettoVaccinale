package com.centrivaccinali;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.LinkedList;


public class CentriVaccinali implements Serializable {

    private static final String CSV_SEPARATOR = ";";
    String nome = "";
    String via = "";
    short tipologia = 0;
    String nomeCognome = "";
    String codiceFiscale = "";
    String dataSomministrazione = null;
    short vaccinoSomministrato = 0;
    int idVaccinazione = 0;

    public CentriVaccinali(String nomeCentro, String viaCentro, short tipologia) {
        this.nome = nomeCentro;
        this.via = viaCentro;
        this.tipologia = tipologia;
    }

    public CentriVaccinali(String nomeCognome, String codiceFiscale, String dataSomministrazione, short vaccinoSomministrato, int idVaccinazione) {

        this.nomeCognome = nomeCognome;
        this.codiceFiscale = codiceFiscale;
        this.dataSomministrazione = dataSomministrazione;
        this.vaccinoSomministrato = vaccinoSomministrato;
        this.idVaccinazione = idVaccinazione;
    }


    static void printArray(CentriVaccinali arr[]) {
        int n = arr.length;
        for (CentriVaccinali centro: arr) System.out.println(centro.idVaccinazione);
    }


    private static void merge(CentriVaccinali[] arr, int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        CentriVaccinali[] L = new CentriVaccinali[n1];
        CentriVaccinali[] R = new CentriVaccinali[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2 && L[i] != null && R[j] != null) {
            if (L[i].idVaccinazione <= R[j].idVaccinazione) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }


    private static void sort(CentriVaccinali[] arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    private static boolean writeCentriVaccinali(LinkedList<CentriVaccinali> centriVaccinali, String filepath) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8));

            for (CentriVaccinali centriVaccinale : centriVaccinali) {
                String oneLine = centriVaccinale.nome +
                        CSV_SEPARATOR +
                        centriVaccinale.via +
                        CSV_SEPARATOR +
                        centriVaccinale.tipologia +
                        CSV_SEPARATOR;
                bw.write(oneLine);
                bw.newLine();

            }
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean writeVaccinato(LinkedList<CentriVaccinali> vaccinati, String filepath) {
        final int arraySize = vaccinati.size();
        CentriVaccinali[] arrayVaccinati = new CentriVaccinali[arraySize];
        int i = 0;
        for (CentriVaccinali vaccinato : vaccinati) {
            arrayVaccinati[i] = vaccinato;
            i++;
        }
        sort(arrayVaccinati, 0, arraySize - 1);
        printArray(arrayVaccinati);


        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8));

            for (CentriVaccinali vaccinato : arrayVaccinati) {
                String oneLine =
                        vaccinato.nomeCognome +
                                CSV_SEPARATOR +
                                vaccinato.codiceFiscale +
                                CSV_SEPARATOR +
                                vaccinato.dataSomministrazione +
                                CSV_SEPARATOR +
                                vaccinato.vaccinoSomministrato +
                                CSV_SEPARATOR +
                                vaccinato.idVaccinazione +
                                CSV_SEPARATOR;
                bw.write(oneLine);
                bw.newLine();

            }
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static LinkedList<CentriVaccinali> leggeVaccinati(String filepath) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MMMMM/yyyy hh:mm");
        LinkedList<CentriVaccinali> vaccinatiList = new LinkedList<>();
        File file = new File(filepath);
        if (!file.exists()) return vaccinatiList;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali = null;
            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length / 5; i += 6) {
                    CentriVaccinali lastVaccinato = new CentriVaccinali( campiTotali[i], campiTotali[i +1], campiTotali[i + 2], Short.parseShort(campiTotali[i + 3]), Integer.parseInt(campiTotali[i + 4]));
                    vaccinatiList.addLast(lastVaccinato);
                }
            }

            return vaccinatiList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LinkedList<CentriVaccinali> leggeCentroVaccinale() {
        String filepath = "data/CentriVaccinali.dati";
        LinkedList<CentriVaccinali> centriVaccinaliList = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali = null;
            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length; i += 3) {
                    CentriVaccinali centro = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], Short.parseShort(campiTotali[i + 2]));
                    centriVaccinaliList.addLast(centro);
                }
            }

            return centriVaccinaliList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void centroVaccinaleFileVisualizer() {
        LinkedList<CentriVaccinali> centroVaccinali = leggeCentroVaccinale();
        for (CentriVaccinali centroVac : centroVaccinali) {
            System.out.println("Centro nome: " + centroVac.nome);
            System.out.println("Centro via: " + centroVac.via);
            switch (centroVac.tipologia) {
                case 0 -> System.out.println("Centro Tipologia: Ospedaliero");
                case 1 -> System.out.println("Centro Tipologia: Aziendale");
                case 2 -> System.out.println("Centro Tipologia: Hub");
            }
            System.out.println("\n");
        }

    }

    private static void registraCentroVaccinale(String nomeCentro, String viaCentro, short tipologia) {
        CentriVaccinali centroVac = new CentriVaccinali(nomeCentro, viaCentro, tipologia);
        String filepath = "data/CentriVaccinali.dati";
        LinkedList<CentriVaccinali> centriPrecedenti = leggeCentroVaccinale();
        if (centriPrecedenti != null) {
            centriPrecedenti.addLast(centroVac);
        }
        writeCentriVaccinali(centriPrecedenti, filepath);
    }

    private static void registraVaccinato(String nomeCentro, String nomeCognome, String codiceFiscale, String dataSomministrazione, short vaccinoSomministrato, int id) {
        CentriVaccinali vaccinazione = new CentriVaccinali(nomeCognome, codiceFiscale, dataSomministrazione, vaccinoSomministrato, id);
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        LinkedList<CentriVaccinali> vaccinazioniList = leggeVaccinati(filepath);
        if (vaccinazioniList != null) {
            vaccinazioniList.addLast(vaccinazione);
        }
        writeVaccinato(vaccinazioniList, filepath);
    }

    public static void main(String[] args) {
        registraVaccinato("Pippo", "Gianpaolo Torino", "TRNGPL99C03D912N", "14/09/2021", (short) 01, 2);
    }


}
