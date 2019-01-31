package main.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 30/08/2016.
 */
public class ChatLayout extends BoxLayout {
    /**
     * Creates a layout manager that will lay out components along the
     * given axis.
     *
     * @param target the container that needs to be laid out
     * @param axis   the axis to lay out components along. Can be one of:
     *               <code>BoxLayout.X_AXIS</code>,
     *               <code>BoxLayout.Y_AXIS</code>,
     *               <code>BoxLayout.LINE_AXIS</code> or
     *               <code>BoxLayout.PAGE_AXIS</code>
     *
     * @throws AWTError if the value of <code>axis</code> is invalid
     */
    public ChatLayout(Container target, int axis) {
        super(target, axis);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return new Dimension(500,200);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return new Dimension(500,200);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(500,200);
    }
}
