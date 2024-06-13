package com.elfilibustero.uidesigner.enums;

public enum ResourceType {
    ANDROID_ATTR("?android:attr/"),
    ATTR("?attr/"),
    ANDROID_COLOR("@android:color/"),
    COLOR("@color/"),
    HEX_COLOR("#"),
    ANDROID_DIMEN("@android:dimen/"),
    DIMEN("@dimen/"),
    ANDROID_DRAWABLE("@android:drawable/"),
    DRAWABLE("@drawable/"),
    ANDROID_STRING("@android:string/"),
    STRING("@string/"),
    ANDROID_STYLE("@android:style/"),
    STYLE("@style/");

    private final String prefix;

    ResourceType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static ResourceType fromPrefix(String prefix) {
        for (ResourceType type : ResourceType.values()) {
            if (type.prefix.equals(prefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No ResourceType found for prefix: " + prefix);
    }
}
