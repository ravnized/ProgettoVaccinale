package com.cittadini;

import com.centrivaccinali.CentriVaccinali;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Cittadini {
    private static final String CSV_SEPARATOR = ";";
    public Evento evento;
    public int severita = 0;
    public String note = "";
    public String type = "";
    String nomeCognome = "", codiceFiscale = "", email = "", userID = "", password = "";
    int idVaccinazione = 0;
    public Cittadini(String type, Evento evento, int severita, String note) {
        this.evento = evento;
        this.severita = severita;
        this.note = note;
        this.type = type;
    }

    public Cittadini(String nomeCognome, String codiceFiscale, String email, String userID, String password, int idVaccinazione) {
        this.nomeCognome = nomeCognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userID = userID;
        this.password = password;
        this.idVaccinazione = idVaccinazione;
    }

    public static LinkedList<Cittadini> leggiRegistrati() {
        String filepath = "data/Cittadini_Registrati.dati";
        LinkedList<Cittadini> registrati = new LinkedList<>();

        File file = new File(filepath);
        if (!file.exists()) return registrati;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali = null;

            while (true) {
                int i = 0;
                String readerLiner = reader.readLine();
                if (readerLiner == null || readerLiner.equals("")) break;
                campiTotali = readerLiner.split(";");
                Cittadini registrato = new Cittadini(campiTotali[i], campiTotali[i + 1], campiTotali[i + 2], campiTotali[i + 3], campiTotali[i + 4], Integer.parseInt(campiTotali[i + 5]));
                registrati.addLast(registrato);
            }
            return registrati;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeCittadiniRegistrati(LinkedList<Cittadini> cittadiniRegistrati) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/Cittadini_Registrati.dati"), StandardCharsets.UTF_8));

            for (Cittadini cittadino : cittadiniRegistrati) {
                String oneLine =
                        cittadino.nomeCognome +
                                CSV_SEPARATOR +
                                cittadino.codiceFiscale +
                                CSV_SEPARATOR +
                                cittadino.email +
                                CSV_SEPARATOR +
                                cittadino.userID +
                                CSV_SEPARATOR +
                                cittadino.password +
                                CSV_SEPARATOR +
                                cittadino.idVaccinazione +
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

    private static boolean registraCittadino(String nomeCognome, String codiceFiscale, String email, String userid, String password, int idVaccinazione) {
        Cittadini cittadinoRegistrato = new Cittadini(nomeCognome, codiceFiscale, email, userid, password, idVaccinazione);
        LinkedList<Cittadini> cittadinilist = Cittadini.leggiRegistrati();
        if (cittadinilist != null) {
            cittadinilist.addLast(cittadinoRegistrato);
            writeCittadiniRegistrati(cittadinilist);
        } else {
            System.out.println("Errore File Cittadini");
        }


        return true;

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
                if (readerLiner == null || readerLiner.equals("")) break;
                campiTotali = readerLiner.split(";");
                if (campiTotali[i].equals("Evento")) {
                    Cittadini eventoAvverso = new Cittadini(campiTotali[i], Evento.valueOf(campiTotali[i + 1]), Integer.parseInt(campiTotali[i + 2]), campiTotali[i + 3]);
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
                if (readerLiner == null || readerLiner.equals("")) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length; i += 3) {
                    if (campiTotali[i].equals(nomeCentro)) {
                        centro = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], CentriVaccinali.Tipologia.valueOf(campiTotali[i + 2]));
                        centriVaccinaliList.addLast(centro);
                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return centro;

    }

    private static boolean cercaCentroVaccinaleFromViaTipologia(String via, String tipologia) {
        String filepath = "data/CentriVaccinali.dati";
        LinkedList<CentriVaccinali> centriVaccinaliList = new LinkedList<>();
        CentriVaccinali centro = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String[] campiTotali;

            while (true) {
                String readerLiner = reader.readLine();
                if (readerLiner == null || readerLiner.equals("")) break;
                campiTotali = readerLiner.split(";");
                for (int i = 0; i < campiTotali.length; i += 3) {
                    if (campiTotali[i + 1].equals(via) && CentriVaccinali.Tipologia.valueOf(campiTotali[i + 2]).equals(tipologia)) {
                        centro = new CentriVaccinali(campiTotali[i], campiTotali[i + 1], CentriVaccinali.Tipologia.valueOf(campiTotali[i + 2]));
                        centriVaccinaliList.addLast(centro);
                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return centro != null;

    }

    private static boolean inserisciEventiAvversi(String nomeCentro, String evento, int severita, String note) {
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        Cittadini eventoAvverso = new Cittadini("Evento", Evento.valueOf(evento), severita - 1, note);
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

    public static void visualizzaInfoCentroVaccinale(String nomeCentro) {
        CentriVaccinali centro = cercaCentroVaccinaleFromNome(nomeCentro);
        System.out.println("Centro nome: " + centro.nome);
        System.out.println("Centro via: " + centro.via);
        System.out.println("Centro tipo: " + centro.tipologia);
        riassuntoEventiAvversi(nomeCentro);
    }

    public static void riassuntoEventiAvversi(String nomeCentro) {
        String filepath = "data/Vaccinati_" + nomeCentro + ".dati";
        LinkedList<Cittadini> eventiList = leggiEventi(filepath);
        int[] mediaPonderataEvento = new int[6];
        int[] mediaPonderataSeverita = new int[5];
        int mediaEventoQt = 0, mediaEventoPos = 0, mediaSeveritaQt = 0, mediaSeveritaPos = 0;
        for (Cittadini evento : eventiList) {
            mediaPonderataEvento[evento.evento.ordinal()] += 1;
            mediaPonderataSeverita[evento.severita] += 1;
        }

        for (int i = 0; i < mediaPonderataEvento.length; i++) {
            if (mediaEventoQt < mediaPonderataEvento[i]) {
                mediaEventoPos = i;
                mediaEventoQt = mediaPonderataEvento[i];
            }
        }
        for (int i = 0; i < mediaPonderataSeverita.length; i++) {
            if (mediaSeveritaQt < mediaPonderataSeverita[i]) {
                mediaSeveritaPos = i;
                mediaSeveritaQt = mediaPonderataSeverita[i];
            }
        }

        System.out.println("Evento comune: " + eventoTipoVisualizer(mediaEventoPos));
        System.out.println("Severita comune: " + mediaSeveritaPos);

    }

    public static String eventoTipoVisualizer(int posizione) {
        switch (posizione) {
            case 0:
                return "Mal di Testa";
            case 1:
                return "Febbre";
            case 2:
                return "Dolori muscolari e articolari";
            case 3:
                return "Linfoadenopatia";
            case 4:
                return "Tachicardia";
            case 5:
                return "Crisi ipertensiva";
        }
        return "";
    }

    public static void main(String[] args) {

        //inserisciEventiAvversi("Pippo", "FEBBRE", 3, "SBURRA");
        //riassuntoEventiAvversi("Pippo");
        //cercaCentroVaccinaleFromViaTipologia("Via Luigi Settembrini, 1, 20017 Rho MI","OSPEDALIERO");
        //cercaCentroVaccinaleFromNome("POT di Bollate");
        //visualizzaInfoCentroVaccinale("Parco Trenno");
        //registraCittadino("Gianpaolo Torino","NessunaVerifica","gtorino@studenti.uninsubria.it","ravnized", "aaaaaaa", 1);
    }

    public enum Evento {
        MAL_TESTA,
        FEBBRE,
        DOLORI_MUSCOLARI,
        LINFOADENOPATIA,
        TACHICARDIA,
        CRISI_IPERTENSIVA
    }


}
