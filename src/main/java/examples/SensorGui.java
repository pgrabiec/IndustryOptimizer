package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SensorGui extends JFrame {
	private Sensor myAgent;

	private JButton addButton, phaseButton;
	private JTextField titleField, valueField, unitField;
	private JLabel phaseLabel;
	private int phase;
	private String[] phaseName = new String[]{"Oczekujacy", "Poczatek procesu",
			"W trakcie wykonywania", "Koniec procesu", "Agent wylaczony"};
		
	SensorGui(Sensor a) {
		super(a.getLocalName());
			
		myAgent = a;

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 2));

		p.add(new JLabel("Etap:"));
		phase = 0;
		phaseLabel = new JLabel(phaseName[phase]);
		p.add(phaseLabel);

		p.add(new JLabel("Surowiec:"));
		titleField = new JTextField(15);
		p.add(titleField);

		p.add(new JLabel("Wartosc:"));
		valueField = new JTextField(15);
		p.add(valueField);

		p.add(new JLabel("Jednostka:"));
		unitField = new JTextField(15);
		p.add(unitField);

		getContentPane().add(p, BorderLayout.CENTER);

		addButton = new JButton("Dodaj");
		addButton.setEnabled(false);
		phaseButton = new JButton("Nastepny etap");

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(phase < 4) {
					try {
						String name = titleField.getText().trim();
						String value = valueField.getText().trim();
						String unit = unitField.getText().trim();
						myAgent.sendDataMessage(name, value, unit);
						titleField.setText("");
						valueField.setText("");
						unitField.setText("");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(SensorGui.this, "Nieprawidlowe wartosci. " + e.getMessage(), "B�ad", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(phase == 4){

				}
			}
		});
		phaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					myAgent.sendNotice();
					titleField.setText("");
					valueField.setText("");
					unitField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(SensorGui.this, "Nieprawidlowe wartosci. " + e.getMessage(), "B�ad", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		p = new JPanel();
		p.add(addButton);
		p.add(phaseButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
			
		setResizable(false);
	}
	
	public void display() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		setVisible(true);
	}

	public void update(int phase){
		this.phase = phase;
		if(phase > 0 && phase < 4)addButton.setEnabled(true);
		else addButton.setEnabled(false);
		phaseLabel.setText(phaseName[phase]);
	}
}

