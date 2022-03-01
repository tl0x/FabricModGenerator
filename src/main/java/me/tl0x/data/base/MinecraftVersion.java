package me.tl0x.data.base;


public class MinecraftVersion{
    public final int index;
    public final String name;
    public final boolean stable;

    public MinecraftVersion(int index, String name, boolean stable){
        this.index = index;
        this.name = name;
        this.stable = stable;
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
     * @return whether stable
     */
    public boolean isStable() {
        return stable;
    }
}