package com.SnakeGame.FrameWork;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class IsKeyPressed {
    private static boolean wPressed = false;
    private static boolean sPressed = false;
    private static boolean aPressed = false;
    private static boolean dPressed = false;

    public static boolean is_W_Pressed() {
        synchronized (IsKeyPressed.class) {
            return wPressed;
        }
    }
    public static boolean ia_S_Pressed() {
        synchronized (IsKeyPressed.class) {
            return sPressed;
        }
    }
    public static boolean is_A_Pressed() {
        synchronized (IsKeyPressed.class) {
            return aPressed;
        }
    }
    public static boolean is_D_Pressed() {
        synchronized (IsKeyPressed.class) {
            return dPressed;
        }
    }
    public static void keyEventListener() {
    	KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent key) {
                synchronized (IsKeyPressed.class) {
                    switch (key.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (key.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = true;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_S) {
                            sPressed = true;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_A) {
                            aPressed = true;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_D) {
                            dPressed = true;
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (key.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = false;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_S) {
                            sPressed = false;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_A) {
                            aPressed = false;
                        }
                        if (key.getKeyCode() == KeyEvent.VK_D) {
                            dPressed = false;
                        }
                        break;
                    }
                    return false;
                }
            }
        });
    }
}
