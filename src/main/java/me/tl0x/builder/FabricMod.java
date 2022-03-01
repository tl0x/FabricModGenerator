package me.tl0x.builder;

import me.tl0x.data.base.*;

import java.net.URL;

import static me.tl0x.Util.*;


public class FabricMod {
    private final MinecraftVersion mcVersion;
    private final String modName;
    private final String modId;
    private final String modDescription;
    private final String modVersion;
    private final String author;
    private final URL homepage;
    private final URL sources;
    private final License license;
    private final String nameOnLicense;
    private final String[] mainPackage;
    private final String mainClass;
    private final boolean mixin;
    private final boolean fabricApi;
    private final FabricApiVersion apiVersion;
    private final YarnVersion yarnVersion;
    private final LoomVersion loomVersion;
    private final LoaderVersion loaderVersion;
    private final String mavenGroup;
    private final String archiveName;

    public FabricMod(MinecraftVersion mcVersion, String modName, String modId, String modDescription, String modVersion,
                     String author, URL homepage, URL sources, License license, String nameOnLicense, String mainPackage,
                     String mainClass, boolean mixin, boolean fabricApi, FabricApiVersion apiVersion, YarnVersion yarnVersion,
                     LoomVersion loomVersion, LoaderVersion loaderVersion, String mavenGroup, String archiveName) {
        this.mcVersion = ensureExists(mcVersion, "Minecraft version");
        this.modName = ensureNotEmpty(modName, "Mod name");
        this.modId = ensureNotEmpty(modId, "Mod id");
        this.modDescription = ensureNotEmpty(modDescription, "Mod description");
        this.modVersion = ensureSemVer(modVersion, "Mod version");
        this.author = ensureNotEmpty(author, "Author");
        this.homepage = homepage;
        this.sources = sources;
        this.license = ensureExists(license, "License");
        if(license.requiresName)
            ensureNotEmpty(nameOnLicense, "Name on license");
        this.nameOnLicense = nameOnLicense;
        this.mainPackage = ensureValidPackage(mainPackage, "Main package");
        this.mainClass = ensureValidClass(mainClass, "Main class");
        this.mixin = mixin;
        this.fabricApi = fabricApi;
        if(fabricApi)
            ensureExists(apiVersion, "Api version");
        this.apiVersion = apiVersion;
        this.yarnVersion = ensureExists(yarnVersion, "Yarn mappings");
        this.loomVersion = ensureExists(loomVersion, "Loom version");
        this.loaderVersion = ensureExists(loaderVersion, "Loader version");
        this.mavenGroup = String.join(".", ensureValidPackage(mavenGroup, "Maven group"));
        this.archiveName = ensureNotEmpty(archiveName, "Archive base name");
    }

    /**
     * @return the mcVersion
     */
    public MinecraftVersion getMcVersion() {
        return mcVersion;
    }

    /**
     * @return the modName
     */
    public String getModName() {
        return modName;
    }

    /**
     * @return the modId
     */
    public String getModId() {
        return modId;
    }

    /**
     * @return the modDescription
     */
    public String getModDescription() {
        return modDescription;
    }

    /**
     * @return the modVersion
     */
    public String getModVersion() {
        return modVersion;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the homepage
     */
    public URL getHomepage() {
        return homepage;
    }

    /**
     * @return the sources
     */
    public URL getSources() {
        return sources;
    }

    /**
     * @return the license
     */
    public License getLicense() {
        return license;
    }

    /**
     * @return the nameOnLicense
     */
    public String getNameOnLicense() {
        return nameOnLicense;
    }

    /**
     * @return the mainPackage
     */
    public String[] getMainPackage() {
        return mainPackage;
    }

    /**
     * @return the mainClass
     */
    public String getMainClass() {
        return mainClass;
    }

    /**
     * @return the mixin
     */
    public boolean isMixin() {
        return mixin;
    }

    /**
     * @return the fabricApi
     */
    public boolean isFabricApi() {
        return fabricApi;
    }

    /**
     * @return the apiVersion
     */
    public FabricApiVersion getApiVersion() {
        return apiVersion;
    }

    /**
     * @return the yarnVersion
     */
    public YarnVersion getYarnVersion() {
        return yarnVersion;
    }

    /**
     * @return the loomVersion
     */
    public LoomVersion getLoomVersion() {
        return loomVersion;
    }

    /**
     * @return the loaderVersion
     */
    public LoaderVersion getLoaderVersion() {
        return loaderVersion;
    }

    /**
     * @return the mavenGroup
     */
    public String getMavenGroup() {
        return mavenGroup;
    }

    /**
     * @return the archiveName
     */
    public String getArchiveName() {
        return archiveName;
    }

}