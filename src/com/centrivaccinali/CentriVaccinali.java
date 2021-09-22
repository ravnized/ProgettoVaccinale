package com.centrivaccinali;

import com.cittadini.Cittadini;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;


public class CentriVaccinali implements Serializable {

    private static final String CSV_SEPARATOR = ";";
    private static final String filepathCentriVaccinali = "data/CentriVaccinali.dati";
    public String nome = "";
    public String via = "";
    public Tipologia tipologia;
    public String nomeCognome = "";
    public String codiceFiscale = "";
    public String dataSomministrazione = null;
    public TipoVaccino vaccinoSomministrato;
    public int idVaccinazione = 0;

    public CentriVaccinali(String nomeCentro, String viaCentro, Tipologia tipologia) {
        this.nome = nomeCentro;
        this.via = viaCentro;
        this.tipologia = tipologia;
    }

    public CentriVaccinali(String nomeCognome, String codiceFiscale, String dataSomministrazione, TipoVaccino vaccinoSomministrato, int idVaccinazione) {
        this.nomeCognome = nomeCognome;
        this.codiceFiscale = codiceFiscale;
        this.dataSomministrazione = dataSomministrazione;
        this.vaccinoSomministrato = vaccinoSomministrato;
        this.idVaccinazione = idVaccinazione;
    }

    private static void merge(CentriVaccinali[] arr, int l, int m, int r) {

        int n1 = m - l + 1;
        int n2 = r - m;


        CentriVaccinali[] L = new CentriVaccinali[n1];
        CentriVaccinali[] R = new CentriVaccinali[n2];


        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
        int i = 0, j = 0;


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


        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }


        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private static void sort(CentriVaccinali[] arr, int l, int r) {
        if (l < r) {

            int m = l + (r - l) / 2;


            sort(arr, l, m);
            sort(arr, m + 1, r);


            merge(arr, l, m, r);
        }
    }

    private static boolean writeCentriVaccinali(LinkedList<CentriVaccinali> centriVaccinali) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepathCentriVaccinali), StandardCharsets.UTF_8));

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

    public static boolean writeVaccinato(LinkedList<CentriVaccinali> vaccinati, LinkedList<Cittadini> cittadini, String filepath) {
        final int arraySize = vaccinati.size();
        CentriVaccinali[] arrayVaccinati = new CentriVaccinali[arraySize];
        int i = 0;
        for (CentriVaccinali vaccinato : vaccinati) {
            arrayVaccinati[i] = vaccinato;
            i++;
        }


        sort(arrayVaccinati, 0, arraySize - 1);


        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8));

            for (CentriVaccinali vaccinato : arrayVaccinati) {
                String oneLine =
                        "Vaccinato" +
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
            for (Cittadini eventoAvverso : cittadini) {
                String oneLine =
                        eventoAvverso.type +
                                CSV_SEPARATOR +
                                eventoAvverso.evento +
                                CSV_SEPARATOR +
                                eventoAvverso.severita +
                                CSV_SEPARATOR +
                                eventoAvverso.note +
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


    public static LinkedList<CentriVaccinali> readCentroAndVaccinati(String filepath, boolean centroSelect) {
        LinkedList<CentriVaccinali> list = new LinkedList<>();
        File file = new File(filepath);
        if (!file.exists()) return list;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] campiTotali;
            while (true) {
                int i = 0;
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                if (centroSelect) {
                    for (int j = 0; j < campiTotali.length; j += 3) {
                        CentriVaccinali centro = new CentriVaccinali(campiTotali[j], campiTotali[j + 1], Tipologia.valueOf(campiTotali[j + 2]));
                        list.addLast(centro);
                    }
                } else {
                    if (campiTotali[i].equals("Vaccinato")) {
                        CentriVaccinali lastVaccinato = new CentriVaccinali(campiTotali[i + 1], campiTotali[i + 2], campiTotali[i + 3], TipoVaccino.valueOf(campiTotali[i + 4]), Integer.parseInt(campiTotali[i + 5]));
                        list.addLast(lastVaccinato);
                    }
                }


            }
            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static boolean centroVaccinaleFileVisualizer() {
        LinkedList<CentriVaccinali> centroVaccinali = readCentroAndVaccinati(filepathCentriVaccinali, true);
        if (centroVaccinali != null) {
            for (CentriVaccinali centroVac : centroVaccinali) {
                System.out.println("\n");
                System.out.println("Centro nome: " + centroVac.nome);
                System.out.println("Centro via: " + centroVac.via);
                System.out.println("Tipologia: " + centroVac.tipologia);
                System.out.println("\n");
            }
            return true;
        }
        return false;
    }

    private static boolean registraCentroVaccinale(String nomeCentro, String viaCentro, String tipologia) {
        CentriVaccinali centroVac = new CentriVaccinali(nomeCentro, viaCentro, Tipologia.valueOf(tipologia));
        LinkedList<CentriVaccinali> centriPrecedenti = readCentroAndVaccinati(filepathCentriVaccinali, true);
        if (centriPrecedenti != null) {
            centriPrecedenti.addLast(centroVac);
        }
        if (centriPrecedenti != null) {
            writeCentriVaccinali(centriPrecedenti);
            return true;
        }
        return false;

    }

    private static boolean registraVaccinato(String nomeCentro, String nomeCognome, String codiceFiscale, String dataSomministrazione, String vaccinoSomministrato, int id) {
        CentriVaccinali vaccinazione = new CentriVaccinali(nomeCognome, codiceFiscale, dataSomministrazione, TipoVaccino.valueOf(vaccinoSomministrato), id);
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        LinkedList<CentriVaccinali> vaccinazioniList = readCentroAndVaccinati(filepath, false);
        if (vaccinazioniList != null) {
            vaccinazioniList.addLast(vaccinazione);
        }
        LinkedList<Cittadini> cittadinilist = Cittadini.readEventiAndRegistrati(filepath, true);
        if (vaccinazioniList != null) {
            writeVaccinato(vaccinazioniList, cittadinilist, filepath);
            return true;
        }
        return false;
    }
    public static String tipologiaVisualizer(int posizione) {
        return switch (posizione) {
            case 0 -> "OSPEDALIERO";
            case 1 -> "AZIENDALE";
            case 2 -> "HUB";
            default -> "";
        };
    }

    public static void main(String[] args) {
        //registraVaccinato("Pippo", "Gianpaolo Torino", "TRNGPL99C03D912N", "03/03/2021", "JJ", 3);
        //registraCentroVaccinale("Ospedale Giuseppe Casati","Via Luigi Settembrini, 1, 20017 Rho MI","OSPEDALIERO");
        //centroVaccinaleFileVisualizer();
    }

    public enum Tipologia {
        OSPEDALIERO,
        AZIENDALE,
        HUB
    }

    public enum TipoVaccino {
        PFIZER,
        ASTRAZENECA,
        MODERNA,
        JJ
    }
}
