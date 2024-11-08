package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Classname ChangeLog
 * @Description TODO
 * @Author zjj
 */
class ChangeLog {
    private Lead sourceLead;
    private Lead resultLead;
    private List<FieldChange> changes;
    private String changeType; // "UPDATED" or "DROPPED"

    public ChangeLog(Lead sourceLead, Lead resultLead, String changeType) {
        this.sourceLead = sourceLead;
        this.resultLead = resultLead;
        this.changeType = changeType;
        this.changes = new ArrayList<>();
    }

    public void addFieldChange(String fieldName, String fromValue, String toValue) {
        if (!Objects.equals(fromValue, toValue)) {
            changes.add(new FieldChange(fieldName, fromValue, toValue));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Change Type: ").append(changeType).append("\n");
        sb.append("Source Record: ").append(sourceLead).append("\n");
        if (resultLead != null) {
            sb.append("Result Record: ").append(resultLead).append("\n");
        }
        if (!changes.isEmpty()) {
            sb.append("Field Changes:\n");
            changes.forEach(change -> sb.append("  ").append(change).append("\n"));
        }
        return sb.toString();
    }
}
