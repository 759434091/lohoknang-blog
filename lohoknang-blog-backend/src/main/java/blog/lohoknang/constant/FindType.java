package blog.lohoknang.constant;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
public enum FindType {
    /**
     * 查询类型
     */
    RAW("raw"), DATE("date"), CATEGORY("category");

    FindType(String value) {
        this.value = value;
    }

    private static FindType[] values = FindType.values();
    private final String value;

    public static Optional<FindType> getType(String value) {
        return Arrays
                .stream(values)
                .filter(it -> Objects.equals(it.value, value))
                .findFirst();
    }
}