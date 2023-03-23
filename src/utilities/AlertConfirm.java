/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utilities;

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
     * @return true or false for clicking OK (true) and cancel (false).
     */
    boolean alertConfirm(String title, String contentText);
}
