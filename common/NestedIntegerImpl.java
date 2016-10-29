package common;

import java.util.List;
import java.util.ArrayList;

public class NestedIntegerImpl implements NestedInteger {
    List<NestedInteger> list;
    Integer num;

    // Constructor initializes an empty nested list.
    public NestedIntegerImpl() {
    }

    // Constructor initializes a single integer.
    public NestedIntegerImpl(Integer nested) {
        setInteger(nested);
    }

    public NestedIntegerImpl(Object[] nested) {
        list = new ArrayList<>();
        for (Object obj : nested) {
            if (obj instanceof Integer) {
                list.add(new NestedIntegerImpl((Integer)obj));
            } else {
                list.add(new NestedIntegerImpl((Object[])obj));
            }
        }
    }

    public boolean isInteger() {
        return num != null;
    }

    public Integer getInteger() {
        return num;
    }

    public List<NestedInteger> getList() {
        return list;
    }

    public void setInteger(int value) {
        num = value;
        list = null;
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    public void add(NestedInteger ni) {
        if (list == null) {
            list = new ArrayList<>();
            num = null;
        }
        list.add(ni);
    }
}
