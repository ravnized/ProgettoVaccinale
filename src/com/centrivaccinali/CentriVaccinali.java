package com.centrivaccinali;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public CentriVaccinali(String nomeCentro, String nomeCognome, String codiceFiscale, String dataSomministrazione, short vaccinoSomministrato, int idVaccinazione) {
        this.nome = nomeCentro;
        this.nomeCognome = nomeCognome;
        this.codiceFiscale = codiceFiscale;
        this.dataSomministrazione = dataSomministrazione;
        this.vaccinoSomministrato = vaccinoSomministrato;
        this.idVaccinazione = idVaccinazione;
    }


    static void printArray(CentriVaccinali[] arr)
    {
        int n = arr.length;
        for (CentriVaccinali centriVaccinali : arr) System.out.print(centriVaccinali + " ");
        System.out.println();
    }

    private static void merge(LinkedList<CentriVaccinali> arrayVaccinazioni, int min, int med, int max) {
        LinkedList<CentriVaccinali> a = new LinkedList<CentriVaccinali>();
        int i1 = min;
        int i2 = med + 1;
        int i3 = 1;
        for (; (i1 <= med) && (i2 < max); i3++) {
            if (arrayVaccinazioni.get(i2).idVaccinazione > arrayVaccinazioni.get(i1).idVaccinazione) {
                a.add(i3,arrayVaccinazioni.get(i1));
                i1++;
            } else {
                a.add(i3,arrayVaccinazioni.get(i2));
                i2++;
            }
        }
        for (; i1 <= med; i1++, i3++) a.add(i3,arrayVaccinazioni.get(i1));
        for (; i2 < max; i2++, i3++) a.add(i3,arrayVaccinazioni.get(i1));
        for (i3 = 1, i1 = min; i1 < max; i1++, i3++) arrayVaccinazioni.add(i1,a.get(i3));
    }


    private static void mergeSort(LinkedList<CentriVaccinali> array, int min, int max) {
        if (max <= min) return;
        int medio = (min + max) / 2;   // Trovo l' indice per suddividere il vettore
            mergeSort(array, min, medio); // Primo sotto-vettore
            mergeSort(array, medio + 1, max);
        // Secondo sotto-vettore
        merge(array, min, medio, max);
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
            if (vaccinati.size() >= 3){
                mergeSort(vaccinati, 0, vaccinati.size());
            }


        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8));

            for (CentriVaccinali vaccinato : vaccinati) {
                String oneLine = vaccinato.nome +
                        CSV_SEPARATOR +
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
        if(!file.exists()) return vaccinatiList;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali = null;
            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length / 6; i += 6) {
                    CentriVaccinali lastVaccinato = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], campiTotali[i + 2], campiTotali[i + 3], Short.parseShort(campiTotali[i + 4]), Integer.parseInt(campiTotali[i + 5]));
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
        CentriVaccinali vaccinazione = new CentriVaccinali(nomeCentro, nomeCognome, codiceFiscale, dataSomministrazione, vaccinoSomministrato, id);
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        LinkedList<CentriVaccinali> vaccinazioniList = leggeVaccinati(filepath);
        if (vaccinazioniList != null) {
            vaccinazioniList.addLast(vaccinazione);
        }
        writeVaccinato(vaccinazioniList, filepath);
    }

    public static void main(String[] args) {
        registraVaccinato("Pippo","Gianpaolo Torino","TRNGPL99C03D912N","14/09/2021",(short) 01, 3);
    }


}
