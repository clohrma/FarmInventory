/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utilities;

import javafx.scene.control.Button;

/**
 *
 * @author Craig Lohrman
 * 
 * Interface for all the Alerts that use confirm as the alert type.
 */
public interface AlertConfirm {

    /**
     *
     * @param title Sets the title to display on the pop up screen.
     * @param contentText Sets the message to display on the pop up screen.
     * @param defaultButton Select the default button
     * @return true or false for clicking OK (true) and cancel (false).
     */
    boolean alertConfirm(String title, String contentText);
}
