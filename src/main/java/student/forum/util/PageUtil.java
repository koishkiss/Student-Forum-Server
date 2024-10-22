package student.forum.util;

public class PageUtil {

    public static int calculateOffset(int page, int pageSize) {
        return (page-1) * pageSize;
    }

    public static int calculateOffset(int page, int pageSize, int startOffset) {
        return (page-1) * pageSize + startOffset;
    }

    public static int calculatePageNum(int dataNum, int pageSize) {
        return dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
    }

    public static int calculatePageNum(int dataNum, int pageSize, int startOffset) {
        dataNum -= startOffset;
        return dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
    }

}
