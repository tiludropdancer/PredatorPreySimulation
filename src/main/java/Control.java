import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Control implements ActionListener{
	
	private static final int RAB = 0;
	private static final int FOX = 1;
	private static final int COY = 2;
	private static final int HUN = 3;

	private List<Simulator> history = new ArrayList<Simulator>();
	private Simulator s;
	private JFrame frame;
	private List<Dictionary> dict = new ArrayList<Dictionary>();
	private int steps =  500;
	private JPanel p1;
	private JPanel p2;
	private JPanel p3;
	private JPanel p4;
	private JPanel p5;
	private JPanel p6;
	private JSlider max_age1;
	private JSlider max_age2;
	private JSlider max_age3;
	private JSlider max_age4;
	private JSlider breeding_age1;
	private JSlider breeding_age2;
	private JSlider breeding_age3;
	private JSlider hunt_age4;
	private JTextField litter1;
	private JTextField litter2;
	private JTextField litter3;
	private JTextField steps1;
	private JButton enter;
	private JButton clear;
	private JButton quit;
	
	public Control()
	{
		newDict();
		makeFrame();
	}
	
	private void newDict()
	{
		for(int i = 0; i < 4; i++) {
			Dictionary d = new Dictionary();
			switch (i) {
				case RAB: // Rabbit
					d.setBreedAge(4);
					d.setMaxAge(40);
					d.setMaxLitter(4);
					break;
				case FOX: // Fox
					d.setBreedAge(15);
					d.setMaxAge(90);
					d.setMaxLitter(3);
					break;
				case COY: // Coyote
					d.setBreedAge(15);
					d.setMaxAge(70);
					d.setMaxLitter(5);
					break;
				case HUN: // Hunter
					d.setHuntAge(18);
					d.setMaxAge(120);
					break;
			}
			dict.add(d);
		}
	}
	
	private void makeFrame()
	{	
		
		//main frame setup
		frame = new JFrame("Simulation Controls");
		Container contentPane = frame.getContentPane();
		
		//add panels to frame
		JPanel Controls = new JPanel(new GridLayout(2, 3));
		contentPane.add(Controls);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		

		Dictionary rabDict = dict.get(RAB);
		Dictionary foxDict = dict.get(FOX);
		Dictionary coyDict = dict.get(COY);
		Dictionary hunDict = dict.get(HUN);

		//components used in frame
		JLabel actor1 = new JLabel("<HTML><U>Rabbit<U><HTML>");
		JLabel label5 = new JLabel("Choose Maximum Lifespan:");
		max_age1 = new JSlider(JSlider.HORIZONTAL, 0, steps, 250);
		max_age1.setMajorTickSpacing(steps/5);
		max_age1.setMinorTickSpacing(steps/50);
		max_age1.setPaintTicks(true);
		max_age1.setPaintLabels(true);
		max_age1.setValue(rabDict.getMaxAge());
		JLabel label6 = new JLabel("Choose Breeding Age:");
		breeding_age1 = new JSlider(JSlider.HORIZONTAL, 0, steps, 125);
		breeding_age1.setMajorTickSpacing(steps/5);
		breeding_age1.setMinorTickSpacing(steps/50);
		breeding_age1.setPaintTicks(true);
		breeding_age1.setPaintLabels(true);
		breeding_age1.setValue(rabDict.getBreedAge());
		JLabel label8 = new JLabel("Input Maximum Litter Size:");
		litter1 = new JTextField(5);
		litter1.setText(Integer.toString(rabDict.getMaxLitter()));
		
		
		//layout of panels in frame
		p1 = new JPanel(new GridLayout(7, 1)); //7 rows 1 columns - filled T2B
		p1.add(actor1);
		p1.add(label5);
		p1.add(max_age1);
		p1.add(label6);
		p1.add(breeding_age1);
		p1.add(label8);
		p1.add(litter1);
		
		Controls.add(p1);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//components used in frame
		JLabel actor2 = new JLabel("<HTML><U>Fox<U><HTML>");
		JLabel label9 = new JLabel("Choose Maximum Lifespan:");
		max_age2 = new JSlider(JSlider.HORIZONTAL, 0, steps, 250);
		max_age2.setMajorTickSpacing(steps/5);
		max_age2.setMinorTickSpacing(steps/50);
		max_age2.setPaintTicks(true);
		max_age2.setPaintLabels(true);
		max_age2.setValue(foxDict.getMaxAge());
		JLabel label10 = new JLabel("Choose Breeding Age:");
		breeding_age2 = new JSlider(JSlider.HORIZONTAL, 0, steps, 125);
		breeding_age2.setMajorTickSpacing(steps/5);
		breeding_age2.setMinorTickSpacing(steps/50);
		breeding_age2.setPaintTicks(true);
		breeding_age2.setPaintLabels(true);
		breeding_age2.setValue(foxDict.getBreedAge());
		JLabel label12 = new JLabel("Input Maximum Litter Size:");
		litter2 = new JTextField(5);
		litter2.setText(Integer.toString(foxDict.getMaxLitter()));
		
		
		//layout of panels in frame
		p2 = new JPanel(new GridLayout(7, 1)); //7 rows 1 columns - filled T2B
		p2.add(actor2);
		p2.add(label9);
		p2.add(max_age2);
		p2.add(label10);
		p2.add(breeding_age2);
		p2.add(label12);
		p2.add(litter2);
		
		Controls.add(p2);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JLabel actor5 = new JLabel("<HTML><U>Simulation Settings<U><HTML>");
		JLabel label21 = new JLabel("Length of Simulation:");
		steps1 = new JTextField(5);
		steps1.setText("500");
		
		p5 = new JPanel(new GridLayout(7, 1));
		p5.add(actor5);
		p5.add(label21);
		p5.add(steps1);
		
		Controls.add(p5);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//components used in frame
		JLabel actor3 = new JLabel("<HTML><U>Coyote<U><HTML>");
		JLabel label13 = new JLabel("Choose Maximum Lifespan:");
		max_age3 = new JSlider(JSlider.HORIZONTAL, 0, steps, 250);
		max_age3.setMajorTickSpacing(steps/5);
		max_age3.setMinorTickSpacing(steps/50);
		max_age3.setPaintTicks(true);
		max_age3.setPaintLabels(true);
		max_age3.setValue(coyDict.getMaxAge());
		JLabel label14 = new JLabel("Choose Breeding Age:");
		breeding_age3 = new JSlider(JSlider.HORIZONTAL, 0, steps, 125);
		breeding_age3.setMajorTickSpacing(steps/5);
		breeding_age3.setMinorTickSpacing(steps/50);
		breeding_age3.setPaintTicks(true);
		breeding_age3.setPaintLabels(true);
		breeding_age3.setValue(coyDict.getBreedAge());
		JLabel label16 = new JLabel("Input Maximum Litter Size:");
		litter3 = new JTextField(5);
		litter3.setText(Integer.toString(coyDict.getMaxLitter()));
		
		
		//layout of panels in frame
		p3 = new JPanel(new GridLayout(7, 1));
		p3.add(actor3);
		p3.add(label13);
		p3.add(max_age3);
		p3.add(label14);
		p3.add(breeding_age3);
		p3.add(label16);
		p3.add(litter3);
		
		Controls.add(p3);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

		//components used in frame
		JLabel actor4 = new JLabel("<HTML><U>Hunter<U><HTML>");
		JLabel label17 = new JLabel("Choose Maximum Lifespan:");
		max_age4 = new JSlider(JSlider.HORIZONTAL, 0, steps, 250);
		max_age4.setMajorTickSpacing(steps/5);
		max_age4.setMinorTickSpacing(steps/50);
		max_age4.setPaintTicks(true);
		max_age4.setPaintLabels(true);
		max_age4.setValue(hunDict.getMaxAge());
		JLabel label18 = new JLabel("Choose Hunting Age:");
		hunt_age4 = new JSlider(JSlider.HORIZONTAL, 0, steps, 125);
		hunt_age4.setMajorTickSpacing(steps/5);
		hunt_age4.setMinorTickSpacing(steps/50);
		hunt_age4.setPaintTicks(true);
		hunt_age4.setPaintLabels(true);
		hunt_age4.setValue(hunDict.getHuntAge());
		
		
		//layout of panels in frame
		p4 = new JPanel(new GridLayout(7, 1));
		p4.add(actor4);
		p4.add(label17);
		p4.add(max_age4);
		p4.add(label18);
		p4.add(hunt_age4);
		
		Controls.add(p4);
		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		enter = new JButton("Run Simulation");
		enter.addActionListener(this);
		clear = new JButton("Clear All");
		clear.addActionListener(this);
		quit = new JButton("Quit Simulation");
		quit.addActionListener(this);
		
		p6 = new JPanel(new GridLayout(3, 1));
		p6.add(enter);
		p6.add(clear);
		p6.add(quit);
		
		Controls.add(p6);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == quit)
		{
			System.exit(0);
		}
		else if (event.getSource() == clear)
		{
			for(Simulator s : history) {
				s.close();
			}
			history.clear();
		}
		else if (event.getSource() == enter)
		{
			Dictionary rabDict = dict.get(RAB);
			Dictionary foxDict = dict.get(FOX);
			Dictionary coyDict = dict.get(COY);
			Dictionary hunDict = dict.get(HUN);

			steps = (Integer.parseInt(steps1.getText()));
	
			rabDict.setMaxLitter(Integer.parseInt(litter1.getText()));
			foxDict.setMaxLitter(Integer.parseInt(litter2.getText()));
			coyDict.setMaxLitter(Integer.parseInt(litter3.getText()));
			
			rabDict.setMaxAge(max_age1.getValue());
			foxDict.setMaxAge(max_age2.getValue());
			coyDict.setMaxAge(max_age3.getValue());
			hunDict.setMaxAge(max_age4.getValue());

			rabDict.setBreedAge(breeding_age1.getValue());
			foxDict.setBreedAge(breeding_age2.getValue());
			coyDict.setBreedAge(breeding_age3.getValue());
			hunDict.setHuntAge(hunt_age4.getValue());

			s = new Simulator(this);
			history.add(s);
			s.simulate(steps);
		}
	}
	
	public List<Dictionary> getCollection()
	{
		return dict;
	}
	
	public JFrame getFrame() 
	{
		return frame;
	}
}