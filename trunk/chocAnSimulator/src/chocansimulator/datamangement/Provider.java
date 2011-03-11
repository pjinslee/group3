/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chocansimulator.datamangement;

import chocansimulator.ChocAnUserInterface;

/**
 *
 * @author tman
 */
public class Provider extends Contact {
    public Provider() {
        super();

        int number = Id.singletonId(ChocAnUserInterface.chocAnId).getProviderId() + 1;
        setNumber(number);
    }

   public Provider(String input) {
        super(input);
    }

}
