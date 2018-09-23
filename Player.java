import java.io.Serializable;
import java.rmi.RemoteException;

public class Player implements IPlayer, Serializable {
    
    private String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            if (o != null && (o instanceof IPlayer) && this.name.equals(((IPlayer) o).getName())) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}