import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Arc
{
	private Point start;
	private Point end;
	private Node nodStart;//
	private Node nodEnd;//
	                                       //          //
	public Arc(Point start, Point end, Node nodStart, Node nodEnd)
	{
		 this.start = start;
	     this.end = end;
	     this.nodStart = nodStart; //
	     this.nodEnd = nodEnd; //
	}
	
	public void drawArc(Graphics g)
	{
		if (start != null)
		{
            g.setColor(Color.RED);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
	}
}
