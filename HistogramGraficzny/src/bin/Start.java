package bin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import java.util.HashMap;

public class Start {
    JFrame ramka;
    File wybranyPlik;
    FileReader czytacz;
    JFileChooser wybieraczPliku;
    JButton przyciskWczytywania;
    JPanel panelUzytkowy;
    JPanel panelRysunkowy;
    JToolBar panelInformacji;
    JTextField pole1;
    JTextField pole2;
    int liczbaKlas;
    int liczbaZlecen;
    List<Integer> listaLiczb;
    Map<Integer, Integer> histogramPom;
    Graphics grafika;
    int x;
    int y;
    int maxY;
    int maxX;
    int liczbaMaksymalna;
    int mnoznik;
    boolean pierwszyRzut;
    boolean histogramNarysowany;
    double wiekszy;

    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e){
            x = 0;
            maxX = 0;
            maxY = 0;
            rysowanieHistogramu();
            mnoznik = Math.abs(panelRysunkowy.getHeight() + panelRysunkowy.getWidth());
            mnoznik = mnoznik / 100;
        }
    }

    Start(){
        listaLiczb = new ArrayList<Integer>();
        histogramPom = new HashMap<Integer, Integer>();
        mnoznik = 1;
        x = 0;
        y = 0;
        maxX = 0;
        maxY = 0;
        liczbaMaksymalna = 0;
        liczbaKlas = 0;
        histogramNarysowany = false;
    }

    boolean sprawdzWystapienie(int liczba) {
        for(int i = 0; i < histogramPom.size(); i++)
            if(histogramPom.containsKey(liczba) == false) {
                return false;
            }
        return true;
    }

    public void wczytywaniePliku(){
        wybieraczPliku = new JFileChooser();
        wybieraczPliku.setCurrentDirectory(new File("user.home"));
        int result = wybieraczPliku.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            wybranyPlik = wybieraczPliku.getSelectedFile();
            //System.out.println("Wybrano plik: " + wybranyPlik.getAbsolutePath());
        }
    }

    public void wczytywanieDanych() throws IOException{
        czytacz = new FileReader(wybranyPlik);
        BufferedReader odczytLiniami = new BufferedReader(czytacz);
        String tekst;
        int liczbaWczytana;

        x = 0;
        histogramPom.clear();

        tekst = odczytLiniami.readLine();

        while(tekst != null){
            liczbaWczytana = Integer.parseInt(tekst);
            if(liczbaWczytana > liczbaMaksymalna)
                liczbaMaksymalna = liczbaWczytana;
            listaLiczb.add(liczbaWczytana);
            tekst = odczytLiniami.readLine();
        }

        for(int i = 0; i < liczbaMaksymalna; i++) {
            histogramPom.put(i,0);
        }

        for(int i = 0; i < listaLiczb.size(); i++) {
            if(sprawdzWystapienie(listaLiczb.get(i)) == false)
                histogramPom.put(listaLiczb.get(i), 1);
            else {
                int liczba = histogramPom.get(listaLiczb.get(i));
                liczba++;
                histogramPom.remove(listaLiczb.get(i));
                histogramPom.put(listaLiczb.get(i), liczba);
            }
        }

        liczbaKlas = 0;
        for(int i = 0; i < histogramPom.size(); i++) {
            if(histogramPom.get(i) != 0) {
                liczbaKlas++;
            }
        }

        liczbaZlecen = listaLiczb.size();
        odczytLiniami.close();
        czytacz.close();
        listaLiczb.clear();
    }

    public void rysowanieHistogramu() {
        grafika = panelRysunkowy.getGraphics();
        grafika.clearRect(0, 0, panelRysunkowy.getWidth(), panelRysunkowy.getHeight());

        for(int i = 1; i < liczbaMaksymalna + 1; i++) {
            if(histogramPom.get(i) != null) {
                grafika.drawRect(x, panelRysunkowy.getHeight() - (histogramPom.get(i) * mnoznik), mnoznik + 1, histogramPom.get(i) * mnoznik);
                grafika.setColor(Color.GREEN);
                grafika.fillRect(x + 1, y = panelRysunkowy.getHeight() - (histogramPom.get(i) * mnoznik) + 1, mnoznik, histogramPom.get(i) * mnoznik);
                grafika.setColor(Color.BLACK);
                x = x + mnoznik;
                if(maxX < x)
                    maxX = x;
                if(maxY < y)
                    maxY = y;
            }
        }

        if(pierwszyRzut == false) {
            panelInformacji.remove(pole1);
            panelInformacji.remove(pole2);
        }
        String tekstLiczbyKlas = "Liczba klas: " + liczbaKlas;
        String tekstLiczbyZlecen = "Liczba zlecen: " +liczbaZlecen;
        pole1 = new JTextField(tekstLiczbyKlas);
        pole2 = new JTextField(tekstLiczbyZlecen);
        pole1.setBackground(Color.WHITE);
        pole2.setBackground(Color.WHITE);
        panelInformacji.add(pole1);
        panelInformacji.add(pole2);
        pole1.setEditable(false);
        pole2.setEditable(false);
        ramka.revalidate();
        pierwszyRzut = false;
        histogramNarysowany = true;
    }

    public void GUI(){
        ramka = new JFrame( "Zadanie 11 Michal Suchan" );
        ramka.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        ramka.setBounds(100, 100, 800, 600);
        pierwszyRzut = true;

        przyciskWczytywania = new JButton( "OPEN" );
        JButton przyciskRysowania = new JButton( "Rysowanie rysunku ");
        przyciskRysowania.setBackground(Color.WHITE);

        panelUzytkowy = new JPanel();
        panelUzytkowy.setLayout(new BorderLayout());
        panelUzytkowy.setBackground(Color.WHITE);
        panelRysunkowy = new JPanel();
        panelRysunkowy.setLayout(new BorderLayout());
        panelRysunkowy.setBackground(Color.WHITE);
        panelInformacji = new JToolBar();
        panelInformacji.add(przyciskWczytywania);
        panelInformacji.setBackground(Color.WHITE);
        panelUzytkowy.add("South", panelInformacji);
        panelUzytkowy.add("Center", panelRysunkowy);
        panelUzytkowy.setBackground(Color.WHITE);

        panelRysunkowy.addComponentListener(new ResizeListener());
        ramka.setBackground(Color.WHITE);

        przyciskWczytywania.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                wczytywaniePliku();
                try{
                    wczytywanieDanych();
                    rysowanieHistogramu();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            } } );

        ramka.add(panelUzytkowy);
        ramka.setVisible( true );
    }


    public static void main( String[] argv ) {
        Start start = new Start();
        start.GUI();
    }

}
