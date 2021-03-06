package app.com.chess.ai.enums;

public enum Flows {
    FLOW_TRAINING(1),
    FLOW_LEVEL(2),
    FLOW_TRAINING_SQUARE(3),
    FLOW_TRAINING_PIECES(4);

    Flows(
            int flowID) {
        this.flowID = flowID;
    }

    private final int flowID;

    public int getFlowID() {
        return flowID;
    }
}