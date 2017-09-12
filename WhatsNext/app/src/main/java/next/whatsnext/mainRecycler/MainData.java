package next.whatsnext.mainRecycler;

@SuppressWarnings("unused")
class MainData {

    public MainData() {
    }

    String mname;
    String mmessage;
    String mdate;


    public MainData(String mname, String mmessage, String mdate) {
        this.mname = mname;
        this.mmessage = mmessage;
        this.mdate = mdate;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMmessage() {
        return mmessage;
    }

    public void setMmessage(String mmessage) {
        this.mmessage = mmessage;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
