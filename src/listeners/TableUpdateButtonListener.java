package listeners;

import java.util.EventListener;

public interface TableUpdateButtonListener extends EventListener {
    public void tableUpdateButtonClicked( int row, int col );
}
