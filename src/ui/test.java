package ui;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

public class test {

	public static void main(String[] args){
		JWindow window = new JWindow();
		JLabel loadingLabel = new JLabel("", new ImageIcon("src/ui/images/loadingbar.gif"), SwingConstants.CENTER);
		loadingLabel.setOpaque(false);
		window.getContentPane().add(loadingLabel);
				
		window.setBounds(450, 50, 1050, 950);
		window.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setVisible(false);
		JFrame frame = new JFrame();
		frame.add(new JLabel("Welcome"));
		frame.setVisible(true);
		frame.setSize(1050,950);
		window.dispose();
	}
}
