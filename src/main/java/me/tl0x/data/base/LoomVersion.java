package me.tl0x.data.base;


public class LoomVersion {
    public final String name;
    public final int index;
    public final int originalIndex;
    public final int gradle;

    public LoomVersion(String name, int index, int originalIndex) {
        this.name = name;
        this.index = index;
        this.originalIndex = originalIndex;
        this.gradle = originalIndex <= 9 ? 4 : originalIndex == 10 ? 5 : 6;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the originalIndex
     */
    public int getOriginalIndex() {
        return originalIndex;
    }

    /**
     * @return the gradle major version
     */
    public int getGradle() {
        return gradle;
    }
}