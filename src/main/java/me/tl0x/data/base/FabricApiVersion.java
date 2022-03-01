package me.tl0x.data.base;

import me.tl0x.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FabricApiVersion{
    private static Pattern mcVersionPattern = Pattern.compile("\\[(([^/\\]]+)(?:/[^\\]]+)?)\\]");
    private static Pattern buildPattern = Pattern.compile("build (\\d+)");
    private static Pattern oldFabricPattern = Pattern.compile("fabric-(.+)\\.jar");
    private static Pattern newFabricPattern = Pattern.compile("fabric-api-(.+)\\.jar");

    public final String name;
    public final String displayMcVersion;
    public final String mcVersion;
    public final SemVer semVer;
    public final int build;
    public final String maven;
    public final String mavenLocation;
    public final String mavenVersion;

    public FabricApiVersion(String name, String fileName){
        this.name = name;
        Matcher mcMatcher = mcVersionPattern.matcher(name);
        if(mcMatcher.find()){
            this.displayMcVersion = mcMatcher.group(1);
            this.mcVersion = mcMatcher.group(2);
        }else{
            throw new IllegalArgumentException("The name '" + name + "' doesn't seem to have the Minecraft version");
        }
        Matcher verMatcher = Util.SEMVER_REGEX.matcher(name);
        if(verMatcher.find(name.indexOf("]"))){
            this.semVer = new SemVer(verMatcher.group(1), verMatcher.group(2), verMatcher.group(3), verMatcher.group(4), verMatcher.group(5));
        }else{
            throw new IllegalArgumentException("The name '" + name + "' doesn't seem to have a version conforming to SemVer");
        }
        Matcher buildMatcher = buildPattern.matcher(name);
        if(buildMatcher.find()){
            this.build = Integer.parseInt(buildMatcher.group(1));
        }else{
            this.build = Integer.MAX_VALUE;
        }
        Matcher newMatcher = newFabricPattern.matcher(fileName);
        if(newMatcher.matches()){
            this.mavenLocation = "net.fabricmc.fabric-api:fabric-api";
            this.mavenVersion = newMatcher.group(1);
            this.maven = mavenLocation + ":" + mavenVersion;
            return;
        }
        Matcher oldMatcher = oldFabricPattern.matcher(fileName);
        if(oldMatcher.matches()){
            this.mavenLocation = "net.fabricmc:fabric";
            this.mavenVersion = oldMatcher.group(1);
            this.maven = mavenLocation + ":" + mavenVersion;
            return;
        }
        throw new IllegalArgumentException("The file name '" + fileName + "' doesn't seem to be of the right format");
    }

    public FabricApiVersion(FabricApiVersion apiVersion){
        name = apiVersion.name;
        displayMcVersion = apiVersion.displayMcVersion;
        mcVersion = apiVersion.mcVersion;
        semVer = apiVersion.semVer;
        build = apiVersion.build;
        maven = apiVersion.maven;
        mavenLocation = apiVersion.mavenLocation;
        mavenVersion = apiVersion.mavenVersion;
    }

    /**
     * @return the displayMcVersion
     */
    public String getDisplayMcVersion() {
        return displayMcVersion;
    }

    /**
     * @return the mcVersion
     */
    public String getMcVersion() {
        return mcVersion;
    }

    /**
     * @return the SemVer
     */
    public SemVer getSemVer(){
        return semVer;
    }

    /**
     * @return the build
     */
    public int getBuild() {
        return build;
    }

    /**
     * @return the maven
     */
    public String getMaven() {
        return maven;
    }

    /**
     * @return the mavenLocation
     */
    public String getMavenLocation() {
        return mavenLocation;
    }

    /**
     * @return the mavenVersion
     */
    public String getMavenVersion() {
        return mavenVersion;
    }

    @Override
    public String toString() {
        return name;
    }
}