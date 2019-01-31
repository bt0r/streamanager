package chat;

import listeners.UserMouseListener;
import main.component.jxtable.renderer.CellRenderer;
import main.component.jxtable.renderer.MessageCellRenderer;
import main.component.jxtable.renderer.UserCellRenderer;
import org.jdesktop.swingx.JXTable;
import services.ConfigService;
import services.LanguageService;

import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ChatJXTable extends JXTable {
    private LanguageService trans  = LanguageService.getInstance();
    private ConfigService   config = ConfigService.getInstance();

    public ChatJXTable() {
        super();
        Object[] columnModel = {
                trans.getProp("chatTab.header.date"),
                trans.getProp("chatTab.header.points"),
                trans.getProp("chatTab.header.username"),
                trans.getProp("chatTab.header.message")};
        DefaultTableModel model = new DefaultTableModel(null, columnModel);
        setModel(model);
        getColumnModel().getColumn(0).setMinWidth(110);
        getColumnModel().getColumn(2).setMinWidth(140);
        getColumnModel().getColumn(3).setMinWidth(400);
        //setBackground(new Color(255,255,255));
        //setForeground(new Color(80,80,80));
        setCellSelectionEnabled(true);
        setShowGrid(false);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));

        setSortable(true);
        getTableHeader().setOpaque(true);
        getTableHeader().setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        getTableHeader().setReorderingAllowed(false);
        addMouseListener(new UserMouseListener());

        getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new UserCellRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new MessageCellRenderer());
        getColumnExt(1).setVisible(false);



        /*String longText = "Plusieurs variations de Lorem Ipsum peuvent �tre trouv�es ici ou l�, mais la majeure partie d'entre elles a �t� alt�r�e par l'addition d'humour ou de mots al�atoires qui ne ressemblent pas une seconde � du texte standard. Si vous voulez utiliser un passage du Lorem Ipsum, vous devez �tre s�r qu'il n'y a rien d'embarrassant cach� dans le texte. Tous les g�n�rateurs de Lorem Ipsum sur Internet tendent � reproduire le m�me extrait sans fin, ce qui fait de lipsum.com le seul vrai g�n�rateur de Lorem Ipsum. Iil utilise un dictionnaire de plus de 200 mots latins, en combinaison de plusieurs structures de phrases, pour g�n�rer un Lorem Ipsum irr�prochable. Le Lorem Ipsum ainsi obtenu ne contient aucune r�p�tition, ni ne contient des mots farfelus, ou des touches d'humour.";

		for(int i=0;i<100;i++){
			
			Date messageDate = new Date();
			String date = messageDate.getYear()+"-"+messageDate.getMonth()+"-"+messageDate.getDay()+" "+messageDate.getHours()+":"+messageDate.getMinutes()+":"+messageDate.getSeconds();
			Object[] testObject = {date,"b",longText};
			model.addRow(testObject);
		}*/


    }


    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
