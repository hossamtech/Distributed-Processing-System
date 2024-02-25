public enum OperationType {
    ADD("Addition"),
    MULTIPLY("Multiplication"),
    SUBTRACT("Subtraction");

    private final String operationName;

    OperationType(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return this.operationName;
    }
}