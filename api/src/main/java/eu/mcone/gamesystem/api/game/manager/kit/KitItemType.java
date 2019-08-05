package eu.mcone.gamesystem.api.game.manager.kit;

public enum KitItemType {
    HELMET("§7§lHelm"),
    CHESTPLATE("§7§lBrustplatte"),
    LEGGINGS("§7§lHose"),
    BOOTS("§7§lSchuhe"),
    WEAPON("§7§lWaffe");

    final String displayName;

    KitItemType(final String displayName) {
        this.displayName = displayName;
    }
}
