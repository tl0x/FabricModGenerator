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

        if (originalIndex <= 9) {
            this.gradle = 4;
        } else if (originalIndex == 10) {
            this.gradle = 5;
        } else {
            this.gradle = 7;
        }
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