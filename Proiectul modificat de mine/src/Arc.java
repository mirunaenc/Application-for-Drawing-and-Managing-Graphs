import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Arc
{
    private Point start;
    private Point end;
    private Node nodStart;
    private Node nodEnd;
    private MyPanel myPanel;
    public Arc(Point start, Point end, Node nodStart, Node nodEnd, MyPanel myPanel) {
        this.start = start;
        this.end = end;
        this.nodStart = nodStart;
        this.nodEnd = nodEnd;
        this.myPanel = myPanel;
    }
    public void actualizeazaPozitie() {
        double dx = nodEnd.getCentru().x - nodStart.getCentru().x;
        double dy = nodEnd.getCentru().y - nodStart.getCentru().y;
        double angle = Math.atan2(dy, dx);
        start = new Point(nodStart.getCentru().x + (int)(myPanel.getNode_diam()/2 * Math.cos(angle)), nodStart.getCentru().y + (int)(myPanel.getNode_diam()/2  * Math.sin(angle)));
        end = new Point(nodEnd.getCentru().x - (int)(myPanel.getNode_diam()/2  * Math.cos(angle)), nodEnd.getCentru().y - (int)(myPanel.getNode_diam()/2  * Math.sin(angle)));
    }
    public Node getNodStart() {
        return this.nodStart;
    }

    public Node getNodEnd() {
        return this.nodEnd;
    }
    public void drawArc(Graphics g) {
        if (start != null && myPanel.getStatusGraph()) {
            g.setColor(Color.RED);
            g.drawLine(start.x, start.y, end.x, end.y);

            if(myPanel.getTypeGraph())
            {double angle = Math.atan2(end.y - start.y, end.x - start.x);
            int arrowLength = 10;  // lungimea capatului sagetii
            Point arrowTip = new Point(end.x, end.y);
            Point arrowCorner1 = new Point(
                    (int)(end.x - arrowLength * Math.cos(angle - Math.PI / 6)),
                    (int)(end.y - arrowLength * Math.sin(angle - Math.PI / 6))
            );
            Point arrowCorner2 = new Point(
                    (int)(end.x - arrowLength * Math.cos(angle + Math.PI / 6)),
                    (int)(end.y - arrowLength * Math.sin(angle + Math.PI / 6))
            );

            int[] xPoints = {arrowTip.x, arrowCorner1.x, arrowCorner2.x};
            int[] yPoints = {arrowTip.y, arrowCorner1.y, arrowCorner2.y};
            g.fillPolygon(xPoints, yPoints, 3);
        }

        }
    }

    }


