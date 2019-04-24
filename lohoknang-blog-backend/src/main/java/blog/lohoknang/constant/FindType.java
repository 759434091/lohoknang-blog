package blog.lohoknang.constant;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
public enum FindType {
    /**
     * 查询类型
     */
    RAW("raw"), DATE("date"), CATEGORY("category");

    private final String value;

    FindType(String value) {
        this.value = value;
    }

    public static FindType getType(String value) {
        for (FindType findType : FindType.values()) {
            if (findType.value.equals(value)) {
                return findType;
            }
        }
        return null;
    }
}