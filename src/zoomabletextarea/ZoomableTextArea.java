/*
 * ZoomableTextArea.java
 * Create Date: Feb 18, 2019
 * Initial-Author: Janos Aron Kiss
 */

package zoomabletextarea;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseWheelListener;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class extends {@link JTextArea} to support font size changing by CTRL + mouse wheel moving.
 * Also supports CTRL + +/- keys to increase/decrease font size.
 * @version $Revision$ $LastChangedDate$
 * @author $Author$
 */
public class ZoomableTextArea extends JTextArea {

    private final static float FONT_SIZE_MULTIPLIER = 1.1f;  
    private final static int SCROLL_LINES = 3;
    private final static int PLUS_KEY = 107;
    private final static int MINUS_KEY = 109;
    
    public ZoomableTextArea() {        
        super();
        addMouseWheelListener(new ZoomMouseWheelListener());                
        addKeyListener(new ZoomKeyAdapter());        
    }
    
    private void changeFontSizeByRotation(double rotation) {
        if (rotation < 0)
            increaseFontSize();
        else if (rotation > 0)
            decreaseFontSize();
    }    

    private void changeFontSizeByKey(int key) {
        if (key == PLUS_KEY)
            increaseFontSize();
        else if (key == MINUS_KEY)
            decreaseFontSize();
    }    

    private void increaseFontSize() {
        setFont(getFont().deriveFont(getFont().getSize() * FONT_SIZE_MULTIPLIER));
    }
    
    private void decreaseFontSize() {
        setFont(getFont().deriveFont(getFont().getSize() / FONT_SIZE_MULTIPLIER));
    }
        
    private void scrollParentByRotation(double rotation) {
        int step = getFont().getSize() * SCROLL_LINES;
        if ( rotation < 0 ) 
            step *= -1;
        
        JScrollPane parentPane = getScrollPane(getParent());
        if ( parentPane != null )
            scrollParentPaneVertically(parentPane, step);
    }
    
    @Nullable
    private JScrollPane getScrollPane(@Nullable Container container) {
        if ( container instanceof JScrollPane || container == null )
            return (JScrollPane) container;
        
        return getScrollPane(container.getParent());
    }

    private void scrollParentPaneVertically(@Nonnull JScrollPane scrollPane, int value) {
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();        
        scrollBar.setValue(scrollBar.getValue() + value);
    }
        
    
    
    private static class ZoomMouseWheelListener implements MouseWheelListener {
        
        @Override
        public void mouseWheelMoved(@Nonnull java.awt.event.MouseWheelEvent evt) {            
            Object source = evt.getSource();
            if ( source instanceof ZoomableTextArea )
                mouseWheelMoved((ZoomableTextArea) source, evt);
        }        
        
        private void mouseWheelMoved(@Nonnull ZoomableTextArea textArea, @Nonnull java.awt.event.MouseWheelEvent evt) {            
            Double rotation = evt.getPreciseWheelRotation();
            if (evt.isControlDown())
                textArea.changeFontSizeByRotation(rotation);
            else 
                textArea.scrollParentByRotation(rotation);
        }

    }

    private static class ZoomKeyAdapter extends KeyAdapter {
                
        @Override
        public void keyReleased(@Nonnull java.awt.event.KeyEvent evt) {
            Object source = evt.getSource();
            if ( source instanceof ZoomableTextArea )
                keyReleased((ZoomableTextArea) source, evt);                        
        }

        public void keyReleased(@Nonnull ZoomableTextArea textArea, @Nonnull java.awt.event.KeyEvent evt) {
            if (evt.isControlDown())
                textArea.changeFontSizeByKey(evt.getKeyCode());
        }
        
    }   
    
}
