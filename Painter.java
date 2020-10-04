import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;


public class Painter extends Frame implements ActionListener {
	Button line, rect, oval, pen, color;
	Checkbox cb = new Checkbox("Fill");
	PCanvas pcanvas;
	boolean white = false;
	int object = 1;
	int r = 0, g = 0, b = 0;
	Painter(){
		setBounds(0,0,1000,1000);
		setTitle("1073302_Painter");
		setLayout(new GridBagLayout());
		addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent event) { System.exit(0); }});
		
		line = new Button("Line");
		rect = new Button("Rect");
		oval = new Button("Oval");
		pen = new Button("Pen");
		color = new Button();
		color.setBackground(Color.BLACK);
		
		line.addActionListener(this);
		rect.addActionListener(this);
		oval.addActionListener(this);
		pen.addActionListener(this);
		color.addActionListener(this);
		
		line.setFont(new Font("Arial", Font.PLAIN,20));
		rect.setFont(new Font("Arial", Font.PLAIN,20));
		oval.setFont(new Font("Arial", Font.PLAIN,20));
		pen.setFont(new Font("Arial", Font.PLAIN,20));
		cb.setFont(new Font("Arial", Font.PLAIN,20));
		
		//line.setMaximumSize(new Dimension(80,10));
        line.setPreferredSize(new Dimension(80,10));
		
		pcanvas = new PCanvas(this);
		
		GridBagConstraints bag = new GridBagConstraints();
		bag.fill = GridBagConstraints.BOTH;
		bag.anchor = GridBagConstraints.CENTER;
		
		bag.gridx = 0;
		bag.gridy = 0;
		bag.weightx = 0;
		bag.weighty = 1;
		bag.gridwidth = 1;
		bag.gridheight = 1;
		
		add(line, bag);
		
		bag.gridy = 1;
		add(rect, bag);
		
		bag.gridy = 2;
		add(oval, bag);
		
		bag.gridy = 3;
		add(pen, bag);
		
		bag.gridy = 4;
		add(color, bag);
		
		bag.gridy = 5;
		add(cb, bag);
		
		bag.gridy = 0;
		bag.weightx = 1;
		bag.gridwidth = 5;
		bag.gridheight = 5;
		add(pcanvas, bag);
		
		
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		//System.out.print(((Button)e.getSource()).getLabel()+"\n");
		String btn = ((Button)e.getSource()).getLabel();
		if(btn == "Line"){
			object = 1;
		}
		else if(btn == "Rect"){
			object = 2;
		}
		else if(btn == "Oval"){
			object = 3;
		}
		else if(btn == "Pen"){
			object = 4;
		}
		else if(btn == ""){
			white = !white;
			if(white){
				color.setBackground(Color.WHITE);
			}
			else{
				color.setBackground(Color.BLACK);
			}
		}
	}
	static public void main(String[] args){
		Painter painter = new Painter();
	}
}

class PCanvas extends Canvas{
	Painter p;
	private Image iBuffer = null;
	private Graphics gBuffer;
	private Image iPreview = null;
	private Graphics gPreview;
	int p1x, p1y, p2x, p2y;
	boolean draw, fill;
	PCanvas(Painter p){
		this.p = p;
		draw = true;
		fill = false;
		p1x = p1y = p2x = p2y = 0;
		addMouseListener(new MouseClick());
		addMouseMotionListener(new MouseMotion());
		//setBackgroung(Color.BLUE);
		//setSize(new Dimension(WIDTH, HEIGHT));
        //setMinimumSize(new Dimension(WIDTH,HEIGHT));
        //setMaximumSize(new Dimension(WIDTH,HEIGHT));
        //setPreferredSize(new Dimension(WIDTH,HEIGHT));
	}
	public void paint (Graphics g) {
		//System.out.print("paint\n");
		fill = p.cb.getState();
		int fx = 0,fy = 0,sx = 0,sy = 0;
		if(p1x > p2x){
			fx = p2x;
			sx = p1x;
		}
		else{
			sx = p2x;
			fx = p1x;
		}
		if(p1y > p2y){
			fy = p2y;
			sy = p1y;
		}
		else{
			sy = p2y;
			fy = p1y;
		}
		//System.out.print("Pos1 : "+p1x + ", "+p1y+"\n");
		//System.out.print("Pos2 : "+p2x + ", "+p2y+"\n\n");
		if(fill && p.white){
			g.setColor(Color.WHITE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		switch(p.object){
			case 1:
				g.setColor(Color.BLACK);
				g.drawLine(p1x,p1y,p2x,p2y);
			break;
			case 2:
				if(fill){
					g.fillRect(fx,fy,sx-fx,sy-fy);
				}
				else{
					g.drawRect(fx,fy,sx-fx,sy-fy);
				}
			break;
			case 3:
				if(fill){
					g.fillOval(fx,fy,sx-fx,sy-fy);
				}
				else{
					g.drawOval(fx,fy,sx-fx,sy-fy);
				}
			break;
			case 4:
				g.setColor(Color.BLACK);
				g.drawLine(p1x,p1y,p2x,p2y);
				p1x = p2x;
				p1y = p2y;
			break;
		}
    }
	public void update(Graphics scr)
	{
		//System.out.print("update\n");
		if(iBuffer==null)
		{
			//System.out.println("NULL");
			iBuffer=createImage(this.getSize().width,this.getSize().height);
			gBuffer=iBuffer.getGraphics();
		}
		if(iPreview==null)
		{
			//System.out.println("NULL");
			iPreview=createImage(this.getSize().width,this.getSize().height);
			gPreview=iPreview.getGraphics();
		}
		//gPreview = gBuffer.create();
			
		
		if(draw || p.object == 4){
			//System.out.println("Draw\n");
			paint(gBuffer);
			scr.drawImage(iBuffer,0,0,null);
		}
		else{
			//System.out.println("Preview\n");
			//gPreview = gBuffer.create();
			gPreview.setColor(getBackground());
			gPreview.fillRect(0,0,this.getSize().width,this.getSize().height);
			gPreview.drawImage(iBuffer,0,0,null);
			paint(gPreview);
			//scr = gPreview.create();
			scr.drawImage(iPreview,0,0,null);
		}
	}
	
	
	class MouseClick extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			//System.out.print("Pressed X: "+e.getX()+"  Y: "+e.getY()+"\n");
			draw = false;
			p1x = e.getX();
			p1y = e.getY();
		}
		public void mouseReleased(MouseEvent e) {
			draw = true;
			repaint();
		}
    }
	class MouseMotion extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			//System.out.print("Dragged X: "+e.getX()+"  Y: "+e.getY()+"\n");
			p2x = e.getX();
			p2y = e.getY();
			repaint();
		}
    }

}
