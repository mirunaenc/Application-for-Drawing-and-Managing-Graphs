import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class Node
{
	private int coordX;
	private int coordY;
	private int number;
	
	public Node(int coordX, int coordY, int number)
	{
		this.coordX = coordX;
		this.coordY = coordY;
		this.number = number;
	}
	
	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	boolean continePunct(Point p) {//
	    double distanta = Math.sqrt(Math.pow(getCoordX() - p.x, 2) + 
	    		Math.pow(getCoordY() - p.y, 2));
	    return distanta <= 30;
	}

	public void drawNode(Graphics g, int node_diam)
	{//clasa pusa la dispoz de catre Java, in pachetul AWT
		g.setColor(Color.red);
		g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g.fillOval(coordX, coordY, node_diam, node_diam); // diam egale pt cerc
        g.setColor(Color.WHITE);
        g.drawOval(coordX, coordY, node_diam, node_diam); //contor
        if(number < 10)
        	g.drawString(((Integer)number).toString(), coordX+13, coordY+20);
        else
        	g.drawString(((Integer)number).toString(), coordX+8, coordY+20);	
	}
}
