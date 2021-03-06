package org.devinprogress.YAIF.Bridges;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Collections;

// Author: Recursive G
// Source released under GPLv2
// Full document under resources/LICENSE

public abstract class BaseActionBridge {
    protected boolean textChangedByBridge=false;
    private DocumentListener documentListener=null;
    public void postGuiInit(){}
    protected void setListenDocumentEvent(JTextField textField){
        documentListener=new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(!textChangedByBridge){
                    textUpdated();
                }
                textChangedByBridge=false;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!textChangedByBridge){
                    textUpdated();
                }
                textChangedByBridge=false;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        textField.getDocument().addDocumentListener(documentListener);
    }

    protected void textUpdated(){}

    public boolean needShow(){
        return true;
    }
    public void bindKeys(JTextField textField){
        textField.getInputMap().clear();
        textField.getActionMap().clear();
        textField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        textField.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
    }
    public void unlink(JTextField t){
        if(documentListener!=null)
            t.getDocument().removeDocumentListener(documentListener);
        t.setText("");
    }
    public void onTabComplete(GuiChat chatScreen){
        throw new RuntimeException("WTF TabComplete?!");
    }

    protected static void bindKey(JTextField txt,int key,Object unique,AbstractAction action){
        txt.getInputMap().put(KeyStroke.getKeyStroke(key,0),unique);
        txt.getActionMap().put(unique,action);
    }
    protected void dispatch(Runnable action){
        Minecraft.getMinecraft().addScheduledTask(action);
    }
    public void releaseObstacleFlag(){
        textChangedByBridge=false;
    }
}
