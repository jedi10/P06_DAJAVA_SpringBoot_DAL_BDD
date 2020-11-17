package com.paymybudy.transfer.exception;

/**
 * <b>Specific Exception for Internal Money Transfer Execution</b>
 */
public class IntMoneyTransferExecutionException extends Exception {

    private static final long serialVersionUID = -3128681006635769411L;

    public IntMoneyTransferExecutionException(String message) {
        super(message);
    }
}
