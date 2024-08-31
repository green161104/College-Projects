package Custom_CollectionsED.list;

import Custom_CollectionsED.list.ListADT;

public interface UnorderedListADT<T> extends ListADT<T> {

    public void addToFront(T element);

    public void addToRear(T element);

    public void addAfter(T elementToAdd, T target);
}
