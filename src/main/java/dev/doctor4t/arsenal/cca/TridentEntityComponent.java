package dev.doctor4t.arsenal.cca;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class TridentEntityComponent implements AutoSyncedComponent, CommonTickingComponent {
    public TridentEntity tridentEntity;
    private boolean isDropped;
    private int slotDroppedFrom;

    public TridentEntityComponent(TridentEntity tridentEntity){
        this.tridentEntity=tridentEntity;
        this.slotDroppedFrom=-1;
    }
    public static TridentEntityComponent get(TridentEntity trident){
        return ArsenalComponents.TRIDENT_ENTITY_COMPONENT.get(trident);
    }
    @Override
    public void tick() {
        if (tridentEntity.getWorld().isClient()) return;
    }
    public void sync(){
        ArsenalComponents.TRIDENT_ENTITY_COMPONENT.sync(this.tridentEntity);
    }
    public Boolean getisDropped() {
        return this.isDropped;
    }

    public void setisDropped(Boolean isDropped) {
        this.isDropped = isDropped;
    }
    public int getslotDroppedFrom() {
        return this.slotDroppedFrom;
    }

    public void setslotDroppedFrom(int slotDroppedFrom) {
        this.slotDroppedFrom = slotDroppedFrom;
    }


    @Override
    public void readData(ReadView readView) {
        readView.getBoolean("isdropped",false);
        readView.getInt("slotdroppedfrom",-1);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("isdropped",isDropped);
        writeView.putInt("slotdroppedfrom",slotDroppedFrom);
    }
}
