package dev.doctor4t.arsenal.cca;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;


public class RecallingPlayerAnimationComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity playerEntity;
    private boolean isRecalling;

    public RecallingPlayerAnimationComponent(PlayerEntity playerEntity){
        this.playerEntity=playerEntity;
    }
    public static RecallingPlayerAnimationComponent get(PlayerEntity playerEntity){
        return ArsenalComponents.RECALLING_PLAYER_ANIMATION_COMPONENT.get(playerEntity);
    }
    @Override
    public void tick() {
        if (playerEntity.getWorld().isClient()) return;
    }
    public boolean getIsRecalling(){
        return this.isRecalling;
    }
    public void setIsRecalling(boolean isRecalling){
        this.isRecalling=isRecalling;
    }
    public void sync(){
        ArsenalComponents.RECALLING_PLAYER_ANIMATION_COMPONENT.sync(this.playerEntity);
    }


    @Override
    public void readData(ReadView readView) {
        this.isRecalling = readView.getBoolean("isrecalling",false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("isrecalling",isRecalling);
    }
}
