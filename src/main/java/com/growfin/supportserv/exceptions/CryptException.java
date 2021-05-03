package com.growfin.supportserv.exceptions;

public class CryptException extends RuntimeException {

    public CryptException(String msg,Exception e) {
        super(msg,e);
    }

}

