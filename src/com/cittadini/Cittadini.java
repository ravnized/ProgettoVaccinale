package com.cittadini;

import com.centrivaccinali.CentriVaccinali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Cittadini {
    public int evento = 0;
    public int severita = 0;
    public String note = "";
    public String type = "";

    public Cittadini(String type, int evento, int severita, String note) {
        this.evento = evento;
        this.severita = severita;
        this.note = note;
        this.type = type;

    }

    public static LinkedList<Cittadini> leggiEventi(String filepath) {
        LinkedList<Cittadini> eventiAvversi = new LinkedList<>();
        File file = new File(filepath);
        if (!file.exists()) return eventiAvversi;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali = null;

            while (true) {
                int i = 0;
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                if (campiTotali[i].equals("Evento")) {
                    Cittadini eventoAvverso = new Cittadini(campiTotali[i], Integer.parseInt(campiTotali[i + 1]), Integer.parseInt(campiTotali[i + 2]), campiTotali[i + 3]);
                    eventiAvversi.addLast(eventoAvverso);
                }
            }


            return eventiAvversi;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CentriVaccinali cercaCentroVaccinaleFromNome(String nomeCentro) {
        String filepath = "data/CentriVaccinali.dati";
        LinkedList<CentriVaccinali> centriVaccinaliList = new LinkedList<>();
        CentriVaccinali centro = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali;

            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length; i += 3) {
                    if (campiTotali[i].equals(nomeCentro)) {
                        centro = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], Short.parseShort(campiTotali[i + 2]));
                        centriVaccinaliList.addLast(centro);
                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return centro;

    }

    private static boolean cercaCentroVaccinaleFromViaTipologia(String via, Short tipologia) {
        String filepath = "data/CentriVaccinali.dati";
        LinkedList<CentriVaccinali> centriVaccinaliList = new LinkedList<>();
        CentriVaccinali centro = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali;

            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length; i += 3) {
                    if (campiTotali[i + 1].equals(via) && Short.parseShort(campiTotali[i + 2]) == tipologia) {
                        centro = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], Short.parseShort(campiTotali[i + 2]));
                        centriVaccinaliList.addLast(centro);
                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return centro != null;

    }

    private static boolean inserisciEventiAvversi(String nomeCentro, int evento, int severita, String note) {
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        Cittadini eventoAvverso = new Cittadini("Evento", evento, severita - 1, note);
        LinkedList<CentriVaccinali> vaccinazioniList = CentriVaccinali.leggeVaccinati(filepath);
        LinkedList<Cittadini> cittadinilist = Cittadini.leggiEventi(filepath);
        if (cittadinilist != null) {
            cittadinilist.addLast(eventoAvverso);
        }
        if (vaccinazioniList != null) {
            CentriVaccinali.writeVaccinato(vaccinazioniList, cittadinilist, filepath);
        }

        return true;

    }

    private static boolean visualizzaInfoCentroVaccinale(String nomeCentro) {
        CentriVaccinali centro = cercaCentroVaccinaleFromNome(nomeCentro);
        System.out.println("Centro Nome: " + centro.nome);
        System.out.println("Centro Via: " + centro.via);
        switch (centro.tipologia) {
            case 0 -> System.out.println("Centro Tipologia: Ospedaliero");
            case 1 -> System.out.println("Centro Tipologia: Aziendale");
            case 2 -> System.out.println("Centro Tipologia: Hub");
        }
        return true;
    }

    public static void riassuntoEventiAvversi(String nomeCentro) {
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        LinkedList<Cittadini> eventiList = leggiEventi(filepath);
        int[] mediaPonderataEvento = new int[6];
        int[] mediaPonderataSeverita = new int[5];
        int mediaEvento = 0, mediaSeverita = 0;
        for (Cittadini evento : eventiList) {
            mediaPonderataEvento[evento.evento] += 1;
            mediaPonderataSeverita[evento.severita] += 1;
        }

        for (int i = 0; i < mediaPonderataEvento.length; i++) {
            if (mediaEvento < mediaPonderataEvento[i]) {
                mediaEvento = i;
            }
        }
        for (int i = 0; i < mediaPonderataSeverita.length; i++) {
            if (mediaSeverita < mediaPonderataSeverita[i]) {
                mediaSeverita = i;
            }
        }


        System.out.println("Media Evento: " + mediaEvento);
        System.out.println("Media Severita: " + mediaSeverita);


    }

    public static void main(String[] args) {
        riassuntoEventiAvversi("Pippo");
    }
}
