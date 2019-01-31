/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package Exception;/**
 * Created by btor on 09/03/2017.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        return "User not found";
    }
}
