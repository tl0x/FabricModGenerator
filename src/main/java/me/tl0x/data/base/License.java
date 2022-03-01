package me.tl0x.data.base;

public class License {
    public final String name;
    public final String value;
    public final boolean requiresName;

    public License(String name, String value, boolean requiresName){
        this.name = name;
        this.value = value;
        this.requiresName = requiresName;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return whether name is required
     */
    public boolean getRequiresName(){
        return requiresName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof License){
            License other = (License)obj;
            return other.name.equals(name) && other.value.equals(value) && other.requiresName == requiresName;
        }
        return false;
    }
}
