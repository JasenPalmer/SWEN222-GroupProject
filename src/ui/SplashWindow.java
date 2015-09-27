package ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashWindow extends JWindow{

	public SplashWindow(){
		this.setSize(400, 400);
		setLayout(null);
		setBounds(750, 300, 400, 400);

		JLabel loadingTextImage = new JLabel(new ImageIcon("src/ui/images/splash/splashTextImage.gif"));
		loadingTextImage.setBounds(0,25, 405, 200);
		this.add(loadingTextImage);

		JLabel loadingLabel = new JLabel(new ImageIcon("src/ui/images/splash/loadingbar.gif"));
		loadingLabel.setBounds(50,325,305,15);
		loadingLabel.setOpaque(false);
		this.add(loadingLabel);

		JLabel backgroundLabel = new JLabel(new ImageIcon("src/ui/images/splash/splashBackgroundImageTemp.jpg"));
		backgroundLabel.setBounds(0,0,400,400);
		this.add(backgroundLabel);
		
		setVisible(true);
		try {
			Thread.sleep(3000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setVisible(false);
		
		this.dispose();
	}
	
	public static void main(String args[]){
		SplashWindow window = new SplashWindow();
		window.setVisible(true);
		window.dispose();
	}
}
