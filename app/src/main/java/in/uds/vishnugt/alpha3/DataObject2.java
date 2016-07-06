package in.uds.vishnugt.alpha3;



public class DataObject2 {
    private String spinner;
    private String condition;
    private String remarks;
    private String titles;


    DataObject2(String spinner, String condition, String remarks, String titles) {
        this.condition = condition;
        this.spinner = spinner;
        this.remarks = remarks;
        this.titles = titles;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }
}
