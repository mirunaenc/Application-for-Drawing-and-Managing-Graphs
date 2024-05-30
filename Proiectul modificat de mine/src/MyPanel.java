import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel; //container de obiecte grafice -
//contine tipuri geom / butoane/checkbox-uri, tabele
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class MyPanel extends JPanel {
    private int nodeNr = 1;
    private int node_diam = 30;
    private Vector<Node> listaNoduri;
    private Vector<Arc> listaArce;
    Point pointStart = null;
    Point pointEnd = null;
    boolean isDragging = false; // imi spune daca ma gasesc in curs de desenare a unei linii
    private boolean esteGrafOrientat = false;
    private boolean esteGrafInitializat = false;
    Node nodDeMutat = null;
    public boolean getTypeGraph() {
        return this.esteGrafOrientat;
    }
    public boolean getStatusGraph() {
        return this.esteGrafInitializat;
    }

    public int getNode_diam(){
        return this.node_diam;
    }

    public MyPanel() {
        listaNoduri = new Vector<Node>();
        listaArce = new Vector<Arc>();

        // borderul panel-ului
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && esteGrafInitializat) { // Verific daca este un dublu click
                    for (Node nod : listaNoduri) {
                        if (nod.continePunct(e.getPoint())) {
                            nodDeMutat = nod; // setez nodul pentru mutare
                            break;
                        }
                    }
                }

            }
            public void mousePressed(MouseEvent e) {
                if (esteGrafInitializat) {
                    pointStart = e.getPoint();
                }
            }

            //evenimentul care se produce la eliberarea mouse-ului
            public void mouseReleased(MouseEvent e) {
                Node nodStart = null;
                Node nodEnd = null;

                if (isDragging && esteGrafInitializat) {
                    for (Node nod : listaNoduri) {
                        if (nod.continePunct(pointStart)) {
                            nodStart = nod;
                        }
                        if (nod.continePunct(e.getPoint())) {
                            nodEnd = nod;
                        }
                    }
                    // adaug arc doar daca atat punctul de start, cat si cel de sfarsit se afla in interiorul unui nod
                    if (nodStart != null && nodEnd != null && nodStart != nodEnd
                    && !existaArc(nodStart,nodEnd)) //nu mai mult de 2 arce intre nodurile
                        // orientate si nu mai mult de unul intre nodurile neorientate
                    {
                        Point middleStart;
                        Point middleEnd;
                        if(esteGrafOrientat) {
                            double dx = nodEnd.getCentru().x - nodStart.getCentru().x;
                            double dy = nodEnd.getCentru().y - nodStart.getCentru().y;
                            double angle = Math.atan2(dy, dx);

                            // calculez punctul de pe marginea nodului de start care este pe aceeasi linie cu centrul nodului si centrul nodului tinta
                            middleStart = new Point(nodStart.getCentru().x + (int)(node_diam/2* Math.cos(angle)), nodStart.getCentru().y + (int)(node_diam/2 * Math.sin(angle)));
                            middleEnd = new Point(nodEnd.getCentru().x - (int)(node_diam/2* Math.cos(angle)), nodEnd.getCentru().y - (int)(node_diam/2 * Math.sin(angle)));
                        }
                        else {
                             middleStart = new Point(nodStart.getCentru().x, nodStart.getCentru().y);
                             middleEnd = new Point(nodEnd.getCentru().x, nodEnd.getCentru().y);

                        }
                        Arc arc = new Arc(middleStart, middleEnd, nodStart, nodEnd,MyPanel.this);
                        listaArce.add(arc);
                        salveazaGraful();
                    }
                }else if (!isDragging) {
                    addNode(e.getX(), e.getY());
                }

                pointStart = null;
                isDragging = false;
                repaint();
                nodDeMutat = null;
            }


        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (nodDeMutat != null) { // daca exista un nod selectat pentru mutare
                    nodDeMutat.setCoordX(e.getX()); // actualizez poziția nodului
                    nodDeMutat.setCoordY(e.getY());

                    for (Arc arc : listaArce) { // actualizez arcele asociate cu nodul
                        if (arc.getNodStart() == nodDeMutat || arc.getNodEnd() == nodDeMutat) {
                            arc.actualizeazaPozitie();

                        }
                    }
                }
                else if (esteGrafInitializat) {
                    pointEnd = e.getPoint();
                    isDragging = true;
                }
                repaint();
            }
        });

        JButton butonOrientat = new JButton("Graf orientat");
        butonOrientat.addActionListener(e -> {
            esteGrafOrientat = true;
            esteGrafInitializat = true;
        });
        this.add(butonOrientat);

        JButton butonNeorientat = new JButton("Graf neorientat");
        butonNeorientat.addActionListener(e -> {
            esteGrafOrientat = false;
            esteGrafInitializat = true;
        });
        this.add(butonNeorientat);
    }


    //metoda care se apeleaza la eliberarea mouse-ului
    private void addNode(int x, int y) {
        for (Node nod : listaNoduri) {//
            double distanta = Math.sqrt(Math.pow(nod.getCoordX() - x, 2) +
                    Math.pow(nod.getCoordY() - y, 2));
            if (distanta <= 2 * node_diam) {
                return; // nu adauga nodul daca este prea aproape de un alt nod
            }
        }//
        Node node = new Node(x, y, nodeNr);
        listaNoduri.add(node);
        nodeNr++;
        repaint();
    }

    //se executa atunci cand apelam repaint()
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);//apelez metoda paintComponent din clasa de baza
        //super cuv rez face referire la obiectul parinte
      //  g.setColor(getBackground());
       // g.fillRect(0, 0, getWidth(), getHeight());

        g.drawString("This is my Graph!", 10, 20);
        for (Arc a : listaArce)
        {
            a.drawArc(g);
        }
        //deseneaza arcul curent; cel care e in curs de desenare
        if (pointStart != null && nodDeMutat == null) // a doua conditie trateaza cazul in care se desenau arce in timpul repozitionarii
        {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

        }
        //deseneaza lista de noduri
        for(int i=0; i<listaNoduri.size(); i++)
        {
            listaNoduri.elementAt(i).drawNode(g, node_diam);
        }

    }
    boolean existaArc(Node a, Node b) {
        for (Arc arc : listaArce) {
            if ((esteGrafOrientat && (arc.getNodStart() == a && arc.getNodEnd() == b)) ||
                    (!esteGrafOrientat && ((arc.getNodStart() == a && arc.getNodEnd() == b)
                            || (arc.getNodStart() == b && arc.getNodEnd() == a)))){
                return true;
            }

        }
        return false;
    }
    void salveazaGraful() { //
        try {
            // Deschide fisierul pentru scriere
            PrintWriter out = new PrintWriter(new FileWriter("matricea_de_adiacenta1.txt"));

            out.print("  ");
            for (int i = 0; i < listaNoduri.size(); i++) {
                out.print(listaNoduri.get(i).getNumber() + " ");
            }
            out.println();
            // parcurge lista de noduri si scrie matricea de adiacenta
            for (int i = 0; i < listaNoduri.size(); i++) {
                out.print(listaNoduri.get(i).getNumber() + " ");
                for (int j = 0; j < listaNoduri.size(); j++) {
                    if (existaArc(listaNoduri.get(i), listaNoduri.get(j))) {
                        out.print("1 ");
                    } else {
                        out.print("0 ");
                    }
                }
                out.println();
            }

            // inchide fis
            out.close();
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisier: " + e.getMessage());
        }
    }
}

