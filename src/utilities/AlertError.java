/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utilities;

/**
 *
 * @author Craig Lohrman
 * 
 * Interface for all the Alerts that use error as the alert type.
 */
public interface AlertError {

    /**
     *
     * @param dialog Sets the Title of the pop up screen.
     * @param message Sets the message to display on the pop up screen.
     */
    void ErrorAlert(String dialog, String message);
}
