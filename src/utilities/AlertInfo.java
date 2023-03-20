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
 * Interface for all the Alerts that use information as the alert type.
 */
public interface AlertInfo {

    /**
     *
     * @param dialog Sets the Title of the pop up screen.
     * @param message Sets the message to display on the pop up screen.
     */
    void alertInfomation(String dialog, String message);
}
