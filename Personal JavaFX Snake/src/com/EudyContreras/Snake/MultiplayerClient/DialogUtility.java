package com.EudyContreras.Snake.MultiplayerClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Class which mimics the JOptionPane class
 * mostly useful for creating custom dialogs
 * meant to expand on the flexibility when creating
 * dialogs such as confirm and message dialogs and allows
 * further customization
 * @author Eudy Contreras
 *
 */
public class DialogUtility {

	public static final int YES_OPTION = 1;
	public static final int NO_OPTION = 2;
	private static String optionChoice;
	private static JButton[] button;
	private static int confirmChoice;

	public static int showConfirmationDialog(Component window, String text, String name, String[] names) {
		JDialog dialog = new JDialog(new JFrame(), ModalityType.APPLICATION_MODAL);
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JButton yes = new JButton();
		JButton no = new JButton();
		JLabel title = new JLabel(name);
		JLabel label = new JLabel(" "+text+" ");
		title.setFont(new Font("Dialog", Font.BOLD, 15));
		label.setFont(new Font("DIALOG", Font.BOLD, 12));
		if(names == null){
			yes.setText("Yes");
			no.setText("No");
		}
		else{
			yes.setText(names[0]);
			no.setText(names[1]);
		}
		north.add(title);
		center.add(label, BorderLayout.NORTH);
		yes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setConfirmChoice(1);
				dialog.dispose();
			}

		});
		no.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setConfirmChoice(2);
				dialog.dispose();
			}

		});
		south.add(yes);
		south.add(no);
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.add(north, BorderLayout.NORTH);
		dialog.add(center, BorderLayout.CENTER);
		dialog.add(south, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(window);
		dialog.setVisible(true);

		return confirmChoice;

	}
	public static void showMessageDialog(Component window,String windowName, String message, String buttonText) {
		JDialog dialog = new JDialog(new JFrame(), ModalityType.APPLICATION_MODAL);
		GridLayout layout = new GridLayout(1,1,1,1);
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JLabel title = new JLabel(windowName);
		JButton button = new JButton(buttonText);
		String[] newLines = message.split("\n");
		JLabel[] label = new JLabel[newLines.length];
		title.setFont(new Font("Dialog", Font.BOLD, 15));
		center.setLayout(layout);
		for(int i = 0; i<newLines.length; i++){
			label[i] = new JLabel("  "+newLines[i]+"  ");
			label[i].setFont(new Font("DIALOG", Font.BOLD, 12));
			center.add(label[i]);
			layout.setRows(i+1);
			center.revalidate();
		}
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}

		});
		south.add(button);
		north.add(title);
		dialog.setLayout(new BorderLayout(10,15));
		dialog.add(north, BorderLayout.NORTH);
		dialog.add(center, BorderLayout.CENTER);
		dialog.add(south, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(window);
		dialog.setVisible(true);

	}
	public static String OptionsBox(JComponent window, String text, String[] options) {
		JDialog dialog = new JDialog(new JFrame(), ModalityType.APPLICATION_MODAL);
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel();
		JLabel title = new JLabel("Notification");
		JLabel label = new JLabel(text);
		button = new JButton[options.length];
		north.add(title);
		center.add(label, BorderLayout.NORTH);
		for (int i = 0; i < button.length; i++) {
			button[i] = new JButton(options[i]);
			button[i].addActionListener(new Listener());
			south.add(button[i]);
		}
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.add(north, BorderLayout.NORTH);
		dialog.add(center, BorderLayout.CENTER);
		dialog.add(south, BorderLayout.SOUTH);
		dialog.setLocation(window.getX() + window.getWidth() / 2 - (dialog.getWidth() / 2),
				window.getY() + window.getHeight() / 2 - (dialog.getHeight() / 2));
		dialog.pack();
		dialog.setVisible(true);

		return optionChoice;

	}
	public static void setChoice(String newChoice){
		optionChoice = newChoice;
	}
	public static void setConfirmChoice(int newChoice){
		confirmChoice = newChoice;
	}
	private static class Listener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i<button.length; i++){
				if(e.getSource() == button[i]){
					setChoice (button[i].getText());
				}

			}

		}

	}

}
