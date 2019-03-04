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
    private final static int PLUS_KEY = 107;
    private final static int MINUS_KEY = 109;
    
    public ZoomableTextArea() {        
        super();
        addMouseWheelListener(new ZoomMouseWheelListener());                
        addKeyListener(new ZoomKeyAdapter());        
    }
    
    /**
     * Invoked when a key has been released.
     * Override this method if you want to add more functionality to the textarea.
     * Do not forget to call super.keyReleased() to keep the original behavior of this input.
     */
    protected void keyReleased(@Nonnull java.awt.event.KeyEvent evt) {
        if (evt.isControlDown())
            changeFontSizeByKey(evt.getKeyCode());        
    }    
    
    /**
     * Invoked when the mouse wheel is rotated.
     * Override this method if you want to add more functionality to the textarea.
     * Do not forget to call super.mouseWheelMoved() to keep the original behavior of this input.
     */
    protected void mouseWheelMoved(@Nonnull java.awt.event.MouseWheelEvent evt) {
        Double rotation = evt.getPreciseWheelRotation();
        if (evt.isControlDown())
            changeFontSizeByRotation(rotation);
        else 
            passmouseWheelMovedEventToParent(evt);
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
    
    private void passmouseWheelMovedEventToParent(@Nonnull java.awt.event.MouseWheelEvent evt) {
        JScrollPane parentPane = getScrollPane(getParent());
        if ( parentPane != null )            
            parentPane.dispatchEvent(evt);
    }
            
    @Nullable
    private JScrollPane getScrollPane(@Nullable Container container) {
        if ( container instanceof JScrollPane || container == null )
            return (JScrollPane) container;
        
        return getScrollPane(container.getParent());
    }
            
    
    private static class ZoomMouseWheelListener implements MouseWheelListener {
        
        @Override
        public void mouseWheelMoved(@Nonnull java.awt.event.MouseWheelEvent evt) {            
            ((ZoomableTextArea) evt.getSource() ).mouseWheelMoved(evt);
        }        
        
    }

    private static class ZoomKeyAdapter extends KeyAdapter {
                
        @Override
        public void keyReleased(@Nonnull java.awt.event.KeyEvent evt) {
            ((ZoomableTextArea) evt.getSource() ).keyReleased(evt);                        
        }
        
    }   
    
}
