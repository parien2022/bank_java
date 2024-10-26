package org.ftm;

import java.io.FileNotFoundException;

//Main class for the bank
public class Main {
    //main method for the bank
    public static void main(String[] args) throws FileNotFoundException {

    //New instance of the Financial Transaction Manager
    FinancialTransactionManager FTM = new FinancialTransactionManager();
    //Call main function for the FTM instance
    FTM.main();

    }

}

