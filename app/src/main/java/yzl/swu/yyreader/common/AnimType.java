package yzl.swu.yyreader.common;

public enum AnimType {
    NONE(0),SLIDE(1),COVER(2),ALIKE(3);
    final int type;
    AnimType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


}
