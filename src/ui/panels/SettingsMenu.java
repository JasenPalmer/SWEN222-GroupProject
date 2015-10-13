package ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.ApplicationWindow;

public class SettingsMenu extends JPanel implements ChangeListener, ActionListener{

	private ApplicationWindow window;
	private static final int MIN_VOLUME = 26;
	private static final int MAX_VOLUME = 86;
	private static final int INIT_VOLUME = 50;

	//Images
	private static final String settingsImage = "src/ui/images/gui/settingsImage.png";
	private static final String musicVolumeImage = "src/ui/images/gui/musicVolume.png";
	private static final String sfxVolumeImage = "src/ui/images/gui/sfxVolume.png";
	private static final String toggleImage = "src/ui/images/gui/toggle.png";

	public SettingsMenu(ApplicationWindow window){
		this.window = window;
		setLayout(null);
		setOpaque(false);
		setBounds(200,200,650,300);

		//Add background
		this.add(new SettingsMenuBackground(), 0,0);

		//Add settings title
		JLabel settingsLabel = new JLabel(new ImageIcon(settingsImage));
		this.add(settingsLabel, 1,0);
		settingsLabel.setBounds(243, 50, 164, 48);
		settingsLabel.setOpaque(false);

		//Add volume label
		JLabel musicVolumeLabel = new JLabel(new ImageIcon(musicVolumeImage));
		this.add(musicVolumeLabel, 1,0);
		musicVolumeLabel.setBounds(100, 120, 150, 26);
		musicVolumeLabel.setOpaque(false);

		//Add volume
		JSlider volumeSlider = new JSlider(MIN_VOLUME, MAX_VOLUME, INIT_VOLUME);
		this.add(volumeSlider, 1,0);
		volumeSlider.setBounds(330, 125, 230,14);
		volumeSlider.setOpaque(false);
		volumeSlider.addChangeListener(this);
		volumeSlider.setName("volumeSlider");

		//Add SFX label
		JLabel sfxVolumeLabel = new JLabel(new ImageIcon(sfxVolumeImage));
		this.add(sfxVolumeLabel, 1,0);
		sfxVolumeLabel.setBounds(100, 180, 150, 24);
		sfxVolumeLabel.setOpaque(false);

		//Add sfx volume
		JSlider sfxVolumeSlider = new JSlider(MIN_VOLUME, MAX_VOLUME, INIT_VOLUME);
		this.add(sfxVolumeSlider, 1,0);
		sfxVolumeSlider.setBounds(330, 185, 230,14);
		sfxVolumeSlider.setOpaque(false);
		sfxVolumeSlider.addChangeListener(this);
		sfxVolumeSlider.setName("sfxVolumeSlider");

		//Add toggle button
		JButton toggleMusic = new JButton(new ImageIcon(toggleImage));
		this.add(toggleMusic,1,0);
		toggleMusic.setBounds(500, 220, 50,50);
		toggleMusic.setOpaque(false);
		toggleMusic.setBorderPainted(false);
		toggleMusic.setContentAreaFilled(false);
		toggleMusic.setFocusPainted(false);
		toggleMusic.setActionCommand("Toggle");
		toggleMusic.addActionListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		switch(source.getName()){
		case "volumeSlider":
			if (!source.getValueIsAdjusting()) {
				int value = (int)source.getValue();
				window.changeVolume(-80 + value);
				window.requestFocus();
			}
			break;
		case "sfxVolumeSlider":
			if (!source.getValueIsAdjusting()) {
				int value = (int)source.getValue();
				window.getInventPanel().changeEffectVolume(-80 + value);
				window.getLootInvent().changeEffectVolume(-80 + value);
				window.changeEffectVolume(-80 + value);
				window.requestFocus();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "Toggle":
			window.toggleMusic();
			window.requestFocus();
			break;
		default:
			break;
		}
	}
}
