package org.example;

/**
 * @Classname FieldChange
 * @Description TODO
 * @Author zjj
 */
class FieldChange {
    private String fieldName;
    private String fromValue;
    private String toValue;

    public FieldChange(String fieldName, String fromValue, String toValue) {
        this.fieldName = fieldName;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    @Override
    public String toString() {
        return String.format("%s: %s → %s", fieldName, fromValue, toValue);
    }
}
