package tfar.chickenvshunter;

import java.util.UUID;

public interface ChickenDuck {

    int getReinforcementTime();
    void setReinforcementTime(int time);
    void setOwnerUUID(UUID ownerUUID);

}
