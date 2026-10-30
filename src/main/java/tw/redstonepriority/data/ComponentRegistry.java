package tw.redstonepriority.data;

import java.util.Map;

public final class ComponentRegistry {
    private static final Map<String, RedstoneComponent> COMPONENTS = Map.of(
        "0", new RedstoneComponent("comparator", "比較器", "0", "紅石元件", 1, "研究標籤", RedstoneComponent.EvidenceType.MODEL, "特殊元件；非全域排序結論"),
        "1", repeater("1", 1), "2", repeater("2", 2), "3", repeater("3", 3), "4", repeater("4", 4));
    private ComponentRegistry() {}
    private static RedstoneComponent repeater(String symbol, int delay) {
        return new RedstoneComponent("repeater_"+delay, "中繼器", symbol, "紅石元件", delay, delay+" RT", RedstoneComponent.EvidenceType.MODEL, "研究標籤；不表示延遲數字可推導通用優先級");
    }
    public static RedstoneComponent find(String symbol) { return COMPONENTS.get(symbol); }
}
