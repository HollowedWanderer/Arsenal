package dev.doctor4t.arsenal.item;

public interface CustomNameColorItem {
    default int getNameColor() {
        // TODO: Custom name color depending on skin
        return 0xFFFFFF;
    }
}
