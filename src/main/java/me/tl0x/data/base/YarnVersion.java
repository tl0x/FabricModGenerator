package me.tl0x.data.base;


public class YarnVersion{
    public final String name;
    public final String maven;
    public final String mcVersion;
    public final int build;
    public final boolean hasV2Mappings;

    public YarnVersion(String name, String maven, String mcVersion, int build, boolean hasV2Mappings){
        this.name = name;
        this.maven = maven;
        this.mcVersion = mcVersion;
        this.build = build;
        this.hasV2Mappings = hasV2Mappings;
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
     * @return the mcVersion
     */
    public String getMcVersion() {
        return mcVersion;
    }

    /**
     * @return the build
     */
    public int getBuild() {
        return build;
    }

    /**
     * @return whether v2 mappings are available
     */
    public boolean getHasV2Mappings(){
        return hasV2Mappings;
    }
}