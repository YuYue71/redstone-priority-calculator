package tw.redstonepriority.data;

public record RedstoneComponent(String id, String displayName, String symbol, String category,
                                Integer delayTicks, String description, EvidenceType evidenceType, String notes) {
    public enum EvidenceType { MODEL, OBSERVED, USER_TESTED }
}
