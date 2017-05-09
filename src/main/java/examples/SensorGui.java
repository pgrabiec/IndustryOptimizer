package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SensorGui extends JFrame {	
	private Sensor myAgent;
		
	private JTextField titleField, priceField;
		
	SensorGui(Sensor a) {
		super(a.getLocalName());
			
		myAgent = a;
			
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Surowiec:"));
		titleField = new JTextField(15);
		p.add(titleField);
		p.add(new JLabel("Wartosc:"));
		priceField = new JTextField(15);
		p.add(priceField);
		getContentPane().add(p, BorderLayout.CENTER);
			
		JButton addButton = new JButton("Dodaj");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String name = titleField.getText().trim();
					String value = priceField.getText().trim();
					myAgent.sendMessage(name, Integer.parseInt(value));
					titleField.setText("");
					priceField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(SensorGui.this, "Nieprawidlowe wartosci. " + e.getMessage(), "B�ad", JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		p = new JPanel();
		p.add(addButton);
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
}

