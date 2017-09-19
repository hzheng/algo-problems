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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(this, sb);
        return sb.toString();
    }

    private void toString(NestedInteger obj, StringBuilder sb) {
        if (obj.isInteger()) {
            sb.append(obj.getInteger());
            return;
        }
        sb.append("[");
        List<NestedInteger> list = obj.getList();
        if (list != null) {
            for (NestedInteger ni : list) {
                if (ni.isInteger()) {
                    sb.append(ni.getInteger());
                } else {
                    toString(ni, sb);
                }
                sb.append(",");
            }
        }
        int last = sb.length() - 1;
        if (sb.charAt(last) == ',') {
            sb.setCharAt(last, ']');
        } else {
            sb.append("]");
        }
    }
}
