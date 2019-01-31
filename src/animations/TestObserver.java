/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package animations;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by btor on 18/04/2017.
 */
public class TestObserver implements Observer {
    private int id = 0;
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg.getClass());
    }

    public void setId(int id){
        this.id = id;

    }
}
