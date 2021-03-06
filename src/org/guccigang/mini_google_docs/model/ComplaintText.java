package org.guccigang.mini_google_docs.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class ComplaintText {

    private IntegerProperty complaintId;
    private IntegerProperty docID;
    private IntegerProperty version;
    private StringProperty owner;
    private StringProperty complainer;
    private StringProperty message;


    public ComplaintText() {
        complaintId = new SimpleIntegerProperty(0);
        docID = new SimpleIntegerProperty(0);
        version =new SimpleIntegerProperty(0);
        owner = new SimpleStringProperty("");
        complainer = new SimpleStringProperty("");
        message = new SimpleStringProperty("");
    }

    public ComplaintText(IntegerProperty complaintId,
                         IntegerProperty docID,
                         IntegerProperty version,
                         StringProperty owner,
                         StringProperty complainer,
                         StringProperty message) {
        this.complaintId = complaintId;
        this.docID = docID;
        this.version = version;
        this.owner = owner;
        this.complainer = complainer;
        this.message = message;
    }

    public int getComplaintId() {
        return complaintId.get();
    }

    public IntegerProperty complaintIdProperty() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId.set(complaintId);
    }

    public int getDocID() {
        return this.docID.get();
    }

    public void setDocID(int docID) {
        this.docID.set(docID);
    }

    public IntegerProperty docIDProperty() {
        return docID;
    }

    public int getVersion() {
        return version.get();
    }

    public IntegerProperty versionProperty() {
        return version;
    }

    public void setVersion(int version) {
        this.version.set(version);
    }

    public String getOwner() {
        return owner.get();
    }

    public StringProperty ownerProperty() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public String getComplainer() {
        return complainer.get();
    }

    public StringProperty complainerProperty() {
        return complainer;
    }

    public void setComplainer(String complainer) {
        this.complainer.set(complainer);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public StringProperty docComplaintDocIDProperty() {
        return new SimpleStringProperty(docID.toString());
    }
    public StringProperty docComplaintDocVersionProperty() {
        return new SimpleStringProperty(version.toString());
    }

    public ObservableValue<String> docComplaintDocOwnerProperty() {
        return owner;
    }

    public ObservableValue<String> docComplaintDocComplainerProperty() {
        return complainer;
    }

    public ObservableValue<String> docComplainerMessageComplaintDocProperty() {
        return message;
    }
}
