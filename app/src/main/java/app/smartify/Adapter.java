package app.smartify;

class Adapter {
    private final String title;
    private final String hint;
    private final int img;

    Adapter(String title, String hint,int img) {
        this.title = title;
        this.hint = hint;
        this.img=img;
     }

    public String getTitle() {
        return title;
    }

    String getHint() {
        return hint;
    }

    int getImg(){return img;}
}