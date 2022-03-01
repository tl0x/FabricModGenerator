package me.tl0x.data.base;

public class LoaderVersion{
    public final String name;
    public final String maven;
    public final int build;

    public LoaderVersion(String name, String maven, int build){
        this.name = name;
        this.maven = maven;
        this.build = build;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the maven
     */
    public String getMaven() {
        return maven;
    }

    /**
     * @return the build
     */
    public int getBuild() {
        return build;
    }
}
