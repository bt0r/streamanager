/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

/**
 * Created by btor on 22/01/2017.
 */
public class PingCommand extends PokeCommand {
    String COMMAND_NAME = "poke";

    public PingCommand() {
        setName(this.COMMAND_NAME);
    }

}
