package dev.doctor4t.arsenal.util;

import dev.doctor4t.arsenal.entity.AnchorbladeEntity;

public interface AnchorOwner {
    void arsenal$setAnchor(AnchorbladeEntity anchor);

    AnchorbladeEntity arsenal$getAnchor(boolean reeling);

    boolean arsenal$isAnchorActive(boolean reeling);
}
