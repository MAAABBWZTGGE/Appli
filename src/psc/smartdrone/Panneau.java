import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
 
public class Panneau extends JPanel { 
	double[] input,output;
	public void paintComponent(Graphics g){
		int dx = this.getWidth()/input.length;
		int dy = this.getHeight()/50;
		for (int i =0; i< input.length-1; i++ ) {
			g.setColor(Color.black);
			g.drawString("input", 10, 10);
			g.drawLine(dx*i, (int)(dy*input[i])+this.getHeight()/2, dx*(i+1), (int)(dy*input[i+1])+this.getHeight()/2);
			g.setColor(Color.blue);
			g.drawString("output",10, 20);
			g.drawLine(dx*i, (int)(dy*output[i]+this.getHeight()/2), dx*(i+1), (int)(dy*output[i+1]+this.getHeight()/2));
		}
	} 
}