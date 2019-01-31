/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package animations;


import java.util.Observable;

/**
 * Created by btor on 18/04/2017.
 */
public class TestObservable extends Observable {
   private int id = 0;

   public void setId(int id){
       this.id = id;
       setChanged();
       notifyObservers(this);
   }
}
