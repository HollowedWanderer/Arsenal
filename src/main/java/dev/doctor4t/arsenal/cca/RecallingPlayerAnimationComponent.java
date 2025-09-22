package dev.doctor4t.arsenal.cca;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
    public void readData(NbtCompound readView) {
        this.isRecalling = readView.getBoolean("isrecalling");
    }

    @Override
    public void writeData(NbtCompound writeView) {
        writeView.putBoolean("isrecalling",isRecalling);
    }
}
