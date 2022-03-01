package me.tl0x.builder;

import java.net.URL;

import me.tl0x.data.base.*;

public class FabricModBuilder{
    private MinecraftVersion mcVersion;
    private String modName;
    private String modId;
    private String modDescription;
    private String modVersion;
    private String author;
    private URL homepage;
    private URL sources;
    private License license;
    private String nameOnLicense;
    private String mainPackage;
    private String mainClass;
    private boolean mixin;
    private boolean fabricApi;
    private FabricApiVersion apiVersion;
    private YarnVersion yarnVersion;
    private LoomVersion loomVersion;
    private LoaderVersion loaderVersion;
    private String mavenGroup;
    private String archiveName;

    public void setMcVersion(MinecraftVersion mcVersion) {
        this.mcVersion = mcVersion;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModDescription(String modDescription) {
        this.modDescription = modDescription;
    }

    public void setModVersion(String modVersion) {
        this.modVersion = modVersion;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHomepage(URL homepage) {
        this.homepage = homepage;
    }

    public void setSources(URL sources) {
        this.sources = sources;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public void setNameOnLicense(String nameOnLicense) {
        this.nameOnLicense = nameOnLicense;
    }

    public void setMainPackage(String mainPackage) {
        this.mainPackage = mainPackage;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public void setMixin(boolean mixin) {
        this.mixin = mixin;
    }

    public void setFabricApi(boolean fabricApi) {
        this.fabricApi = fabricApi;
    }

    public void setApiVersion(FabricApiVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setYarnVersion(YarnVersion yarnVersion) {
        this.yarnVersion = yarnVersion;
    }

    public void setLoomVersion(LoomVersion loomVersion) {
        this.loomVersion = loomVersion;
    }

    public void setLoaderVersion(LoaderVersion loaderVersion) {
        this.loaderVersion = loaderVersion;
    }

    public void setMavenGroup(String mavenGroup) {
        this.mavenGroup = mavenGroup;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public FabricMod build(){
        try{
            FabricMod mod = new FabricMod(mcVersion, modName, modId, modDescription, modVersion, author, homepage, sources, license, nameOnLicense, mainPackage, mainClass, mixin, fabricApi, apiVersion, yarnVersion, loomVersion, loaderVersion, mavenGroup, archiveName);
            return mod;
        }catch(IllegalArgumentException e){
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}