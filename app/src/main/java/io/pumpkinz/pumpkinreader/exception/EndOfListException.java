package io.pumpkinz.pumpkinreader.exception;


/**
 * Thrown when DataSource is requested to return News outside of what it can return
 * from List of NewsIds it got from SharedPreference or API request
 */
public class EndOfListException extends IndexOutOfBoundsException {

    private static final long serialVersionUID = 7072219552188816963L;

    public EndOfListException() {
        super();
    }

}
