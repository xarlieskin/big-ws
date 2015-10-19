package translator.domain;

public class TranslatedText {
    private String from;
    private String to;
    private String translation;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String googleTranslation) {
        this.translation = googleTranslation;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TranslatedText{");
        sb.append("from='").append(from).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append(", translation='").append(translation).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
