package a2ews.takx.plugin.cot;

public class CoTMessage {
    // Define the properties of a CoT message
    private String type;
    private String data;

    // Constructor
    public CoTMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

      public void setType(String type) {
        this.type = type;
    }


    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CoTMessage{" +
               "type='" + type + '\'' +
               ", data='" + data + '\'' +
               '}';
    }
}

