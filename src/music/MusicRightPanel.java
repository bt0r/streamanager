package music;

import javax.swing.*;


public class MusicRightPanel extends JPanel {

    MusicRightPanel() {

        String videoURL = "https://www.youtube.com/v/b-Cr0EWwaTk?fs=1";
        /*SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
				JWebBrowser webBrowser = new JWebBrowser();
				JPanel PlayerPanel = new JPanel();
				JPanel SongListPanel = new JPanel();
				BorderLayout Layout = new BorderLayout();
				SongListPanel.setBackground(new Color(0,0,0));
				PlayerPanel.setPreferredSize(new Dimension(400,400));
				PlayerPanel.setMinimumSize(new Dimension(400,400));
				PlayerPanel.add(webBrowser);
				
				setLayout(Layout);
				add(SongListPanel,BorderLayout.CENTER);
				add(PlayerPanel,BorderLayout.SOUTH);
				
				
				
				
				setBackground(new Color(0,0,0));
	        }
        });

	    // don't forget to properly close native components
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        @Override
	        public void run() {
	            NativeInterface.close();
	        }
	    }));*/
    }

}
