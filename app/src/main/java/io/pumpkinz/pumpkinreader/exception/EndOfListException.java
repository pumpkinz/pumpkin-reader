package io.pumpkinz.pumpkinreader.exception;


import java.io.Serializable;

/**
 * Thrown when DataSource is requested to return News outside of what it can return
 * from List of NewsIds it got from SharedPreference or API request
 */
public class EndOfListException extends IndexOutOfBoundsException implements Serializable {

    public EndOfListException() {
        super();
    }

}
