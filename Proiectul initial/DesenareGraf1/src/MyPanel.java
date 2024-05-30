import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel; //container de obiecte grafice -
//contine tipuri geom / butoane/checkbox-uri, tabele

public class MyPanel extends JPanel {
	private int nodeNr = 1; //nr nodului 
	private int node_diam = 30;
	private Vector<Node> listaNoduri;
	private Vector<Arc> listaArce;

	Point pointStart = null;
	Point pointEnd = null;
	boolean isDragging = false; // imi spune daca ma gasesc in curs de desenare a unei linii 
	public MyPanel()
	{
		listaNoduri = new Vector<Node>();
		listaArce = new Vector<Arc>();
	
		// borderul panel-ului
		setBorder(BorderFactory.createLineBorder(Color.black));
		addMouseListener(new MouseAdapter() { // obiecte anonime
			//evenimentul care se produce la apasarea mouse-ului
			public void mousePressed(MouseEvent e) {
				pointStart = e.getPoint();
			}
			
			//evenimentul care se produce la eliberarea mouse-ului
			public void mouseReleased(MouseEvent e) {
//				if (!isDragging) {
//					addNode(e.getX(), e.getY());
//					
//				}
//				else {
//					//Arc arc = new Arc(pointStart, pointEnd);
//					//listaArce.add(arc);					
//				}
//				pointStart = null;
//				isDragging = false;
				
			    Node nodStart = null;
			    Node nodEnd = null;

			    if (isDragging) {
			        for (Node nod : listaNoduri) {
			            if (nod.continePunct(pointStart)) {
			                nodStart = nod;
			            }
			            if (nod.continePunct(e.getPoint())) {
			                nodEnd = nod;
			            }
			        }

			        // Adaugă arc doar dacă atât punctul de start, cât și cel de sfârșit se află în interiorul unui nod
			        if (nodStart != null && nodEnd != null && nodStart != nodEnd) {
			        	Arc arc = new Arc(pointStart, e.getPoint(), nodStart, nodEnd);
			            listaArce.add(arc);
			        }
			    } else {
			        addNode(e.getX(), e.getY());
			    }

			    pointStart = null;
			    isDragging = false;
			    repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			//evenimentul care se produce la drag&drop pe mousse
			public void mouseDragged(MouseEvent e) {
				pointEnd = e.getPoint();
				isDragging = true;
				repaint();
			}
		});
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
		g.drawString("This is my Graph!", 10, 20);
		//deseneaza arcele existente in lista
		/*for(int i=0;i<listaArce.size();i++)
		{
			listaArce.elementAt(i).drawArc(g);
		}*/
		for (Arc a : listaArce)
		{
			a.drawArc(g);
		}
		//deseneaza arcul curent; cel care e in curs de desenare
		if (pointStart != null)
		{
			g.setColor(Color.RED);
			g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
		}
		//deseneaza lista de noduri
		for(int i=0; i<listaNoduri.size(); i++)
		{
			listaNoduri.elementAt(i).drawNode(g, node_diam);
		}
		/*for (Node nod : listaNoduri)
		{
			nod.drawNode(g, node_diam, node_Diam);
		}*/
	}
}
