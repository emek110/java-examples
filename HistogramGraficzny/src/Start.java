/**
 * Created by emek on 15.01.2018.
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;


class SimpleGUI extends JFrame{

    LinkedList<Integer[]> lista = new LinkedList<>();
    JFrame ramka;
    JPanel srodek ;
    JPanel rysunek;
    JPanel dolnaBelka;
    JPanel txtFields;
    JButton przycisk1;
    JTextField liczbaKlas;
    JTextField liczbaZliczen;
    Graphics grafika;

    class otworzPlik implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            JFileChooser c = new JFileChooser();
            int rVal = c.showOpenDialog(SimpleGUI.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                try {
                    readData(c.getSelectedFile());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void readData(File plik) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(plik));
        lista = new LinkedList<>();
        String line = reader.readLine();

        boolean notFound = true;
        int tmp, sum = 0;

        while(line != null) {
            tmp = Integer.parseInt(line);
            for (int i = 0; i < lista.size(); i++) {
                if(lista.get(i)[0] == tmp){
                 notFound = false;
                 lista.get(i)[1] += 1;
                 sum += 1;
                 break;
                }
            }

            if(notFound) {
                lista.addLast(new Integer[2]);
                lista.getLast()[0] = tmp;
                lista.getLast()[1] = 1;
                sum += 1;
            } else
                notFound = true;


            line = reader.readLine();
        }

        liczbaKlas.setText("Liczba klas : " + lista.size());
        liczbaZliczen.setText("Liczba Zliczen : " + sum);

        reader.close();

        rysowanieHistogramu();
    }

    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            rysowanieHistogramu();
            rysunek.revalidate();
            ramka.revalidate();
        }
    }

    public void rysowanieHistogramu() {

        int tmpX=0;
        int szerokosc = 100;
        int wysokosc = 100;
        grafika = rysunek.getGraphics();
        grafika.clearRect(0, 0, rysunek.getWidth(), rysunek.getHeight());

        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i)[0] > maxX)
                maxX = lista.get(i)[0];
            if(lista.get(i)[1] > maxY)
                maxY = lista.get(i)[1];
        }

        if(maxX * szerokosc > rysunek.getWidth())
            szerokosc = rysunek.getWidth()/maxX -1;

        if(maxY * wysokosc > rysunek.getHeight())
            wysokosc = rysunek.getHeight()/maxY -1;

        boolean found = false;
        int tmp;

        for(int iterator = 1,i=0, j=0; j < lista.size() ; iterator++) {

            for (int i1 = 0; i1 < lista.size(); i1++) {
                if(lista.get(i1)[0] == iterator) {
                    j++;
                    i = i1;
                    found = true;
                    break;
                }
            }

            if (found)
                tmp = lista.get(i)[1];
            else tmp = 0;

                grafika.drawRect(tmpX, rysunek.getHeight() - (tmp * wysokosc), szerokosc + 1, tmp * wysokosc);
                grafika.setColor(Color.GREEN);
                grafika.fillRect(tmpX + 1, rysunek.getHeight() - (tmp * wysokosc) + 1, szerokosc , tmp * wysokosc);
                grafika.setColor(Color.BLACK);
                tmpX+=szerokosc;
                found = false;
        }
    }

    public void work() {
        ramka = new JFrame( "Histogram" );
        ramka.setBounds(100, 100, 800, 600);
        ramka.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        ramka.setMinimumSize(new Dimension(250,200));

        srodek = new JPanel();
        rysunek = new JPanel();
        dolnaBelka = new JPanel();
        txtFields = new JPanel();
        przycisk1 = new JButton( "OPEN" );
        liczbaKlas = new JTextField("Liczba klas : ");
        liczbaZliczen = new JTextField("Liczba zliczen : ");

        txtFields.setLayout(new BorderLayout());
        liczbaKlas.setEditable(false);
        txtFields.add(BorderLayout.NORTH,liczbaKlas);
        liczbaZliczen.setEditable(false);
        txtFields.add(BorderLayout.SOUTH,liczbaZliczen);

        dolnaBelka.setLayout(new BoxLayout(dolnaBelka,BoxLayout.X_AXIS));
        dolnaBelka.add(przycisk1);
        dolnaBelka.add(txtFields);

        srodek.setLayout(new BorderLayout());
        srodek.add(BorderLayout.CENTER,rysunek);
        srodek.add(BorderLayout.SOUTH,dolnaBelka);
        przycisk1.addActionListener( new otworzPlik() );
        rysunek.addComponentListener(new ResizeListener());

        ramka.add(srodek);
        ramka.setVisible( true );

    }


}

public class Start {
    public static void main( String[] argv ) {
        SimpleGUI g = new SimpleGUI();
        g.work();
    }
}