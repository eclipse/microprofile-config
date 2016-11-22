/** 
 * @start_prolog@
 * Version: %Z% %I% %W% %E% %U% [%H% %T%]
 * ===========================================================================
 * IBM Confidential OCO Source Material
 * 5724-I63, 5724-H88, 5724-H89, 5655-N02, 5724-J08 Copyright IBM Corp. 2006
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 * ===========================================================================
 * @end_prolog@
 * 
 * Change activity:
 * 
 * Reason          Date        Origin   Description
 * --------------- ----------- -------- --------------------------------------
 * Creation        18 Nov 2016 Emily   Original
 * TODO BuddyCheck 
 * ===========================================================================
 */

package io.microprofile.config.spi;

public class ConvertException extends Exception {

	/**
	 * The exception was thrown when unable to convert to the specified type. 
	 */
	private static final long serialVersionUID = 1L;


	public ConvertException(String message) {
		super(message);
	}
	public ConvertException(Throwable throwable) {
		super(throwable);
	}
	public ConvertException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
