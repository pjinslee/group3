package chocansimulator.datamangement;

import chocansimulator.ChocAnUserInterface;


/**
 *
 * @author tman
 */
public class Member extends Contact {

    public Member() {
        super();

        int number = Id.singletonId(ChocAnUserInterface.chocAnId).getMemberId() + 1;
        setNumber(number);

    }

    public Member(String input) {
        super(input);
    }



}
