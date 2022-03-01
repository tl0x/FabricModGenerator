package me.tl0x.data;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import me.tl0x.data.base.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataProvider {

    public static final int LOOM_OLD = 9;

    public static final License[] LICENSES = {
            new License("No License (Copyrighted)", "All-Rights-Reserved", true),
            new License("MIT", "MIT", true),
            new License("Internet Systems Consortium (ISC) License", "ISC", true),
            new License("BSD 2-Clause (FreeBSD) License", "BSD-2-Clause-FreeBSD", true),
            new License("BSD 3-Clause (NewBSD) License", "BSD-3-Clause", true),
            new License("Apache 2.0", "Apache-2.0", true),
            new License("Mozilla Public License 2.0", "MPL-2.0", true),
            new License("GNU LGPL 3.0", "LGPL-3.0", true),
            new License("GNU GPL 3.0", "GPL-3.0", true),
            new License("GNU AGPL 3.0", "AGPL-3.0", true),
            new License("Unlicense", "unlicense", true)
    };

    private ArrayList<MinecraftVersion> mcVersions;
    private ArrayList<NormalizedMinecraftVersion> normalizedMcVersions;
    private ArrayList<FabricApiVersion> apiVersions;
    private ArrayList<IndexedFabricApiVersion> sortedApiVersions;
    private ArrayList<YarnVersion> yarnVersions;
    private HashMap<String, ArrayList<YarnVersion>> filteredYarnVersions = new HashMap<>();
    private ArrayList<LoomVersion> loomVersions;
    private HashMap<String, ArrayList<LoaderVersion>> filteredLoaderVersions = new HashMap<>();
    private ArrayList<LoaderVersion> loaderVersions;

    private LoomVersion loom_old;
    private LoomVersion loom_default;

    public ArrayList<MinecraftVersion> getMinecraftVersions() throws IOException{
        if(mcVersions != null)
            return mcVersions;
        JsonArray mcVersionsData = jsonFromUrl("https://meta.fabricmc.net/v2/versions/game").getAsJsonArray();
        ArrayList<MinecraftVersion> mcVersions = new ArrayList<>(mcVersionsData.size());
        for(int i = 0; i < mcVersionsData.size(); i++){
            JsonObject versionData = mcVersionsData.get(i).getAsJsonObject();
            String name = versionData.get("version").getAsString();
            boolean stable = versionData.get("stable").getAsBoolean();
            mcVersions.add(new MinecraftVersion(i, name, stable));
        }
        this.mcVersions = mcVersions;
        return mcVersions;
    }

    public MinecraftVersion getLatestMinecraftVersion(boolean stableOnly) throws IOException{
        if(mcVersions == null)
            getMinecraftVersions();
        if(stableOnly){
            for(MinecraftVersion mcVersion : mcVersions){
                if(mcVersion.isStable())
                    return mcVersion;
            }
        }
        return mcVersions.get(0);
    }

    public MinecraftVersion getMinecraftVersion(String versionName) throws IOException{
        if(mcVersions == null)
            getMinecraftVersions();
        for(MinecraftVersion mcVersion : mcVersions){
            if(mcVersion.name.equals(versionName))
                return mcVersion;
        }
        return null;
    }

    public ArrayList<NormalizedMinecraftVersion> getNormalizedMinecraftVersions() throws IOException{
        if(normalizedMcVersions != null)
            return normalizedMcVersions;
        if(mcVersions == null)
            getMinecraftVersions();
        ArrayList<NormalizedMinecraftVersion> normalizedMcVersions = new ArrayList<>(mcVersions.size());
        for(MinecraftVersion mcVersion : mcVersions){
            normalizedMcVersions.add(new NormalizedMinecraftVersion(mcVersion, mcVersions));
        }
        this.normalizedMcVersions = normalizedMcVersions;
        return normalizedMcVersions;
    }

    public NormalizedMinecraftVersion getNormalizedMinecraftVersion(MinecraftVersion mcVersion) throws IOException{
        if(normalizedMcVersions != null)
            return normalizedMcVersions.get(mcVersion.index);
        if(mcVersions == null)
            getMinecraftVersions();
        return new NormalizedMinecraftVersion(mcVersion, mcVersions);
    }

    public NormalizedMinecraftVersion getNormalizedMinecraftVersion(String versionName) throws IOException{
        return getNormalizedMinecraftVersion(getMinecraftVersion(versionName));
    }

    public ArrayList<FabricApiVersion> getFabricApiVersions() throws IOException{
        if(apiVersions != null)
            return apiVersions;
        JsonArray apiVersionsData = jsonFromUrl("https://addons-ecs.forgesvc.net/api/v2/addon/306612/files").getAsJsonArray();
        ArrayList<FabricApiVersion> apiVersions = new ArrayList<>();
        for(int i = 0; i < apiVersionsData.size(); i++){
            JsonObject versionData = apiVersionsData.get(i).getAsJsonObject();
            String name = versionData.get("displayName").getAsString();
            String fileName = versionData.get("fileName").getAsString();
            try{
                apiVersions.add(new FabricApiVersion(name, fileName));
            }catch(IllegalArgumentException e){}
        }
        this.apiVersions = apiVersions;
        return apiVersions;
    }

    public ArrayList<IndexedFabricApiVersion> getSortedFabricApiVersions() throws IOException{
        if(sortedApiVersions != null)
            return sortedApiVersions;
        if(mcVersions == null)
            getMinecraftVersions();
        if(apiVersions == null)
            getFabricApiVersions();
        ArrayList<IndexedFabricApiVersion> sortedApiVersions = apiVersions.stream()
                .map(apiVersion -> new IndexedFabricApiVersion(apiVersion, mcVersions))
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
        this.sortedApiVersions = sortedApiVersions;
        return sortedApiVersions;
    }

    public int getDefaultFabricApiVersion(MinecraftVersion mcVersion) throws IOException {
        if(sortedApiVersions == null)
            getSortedFabricApiVersions();
        int defaultApi = 0;
        for (int i = 0; i < sortedApiVersions.size(); i++) {
            if (sortedApiVersions.get(i).mcVersionIndex >= mcVersion.index) {
                defaultApi = i;
                break;
            }
        }
        return defaultApi;
    }

    public ArrayList<YarnVersion> getYarnVersions() throws IOException{
        if(yarnVersions != null)
            return yarnVersions;
        JsonArray yarnVersionsData = jsonFromUrl("https://meta.fabricmc.net/v2/versions/yarn").getAsJsonArray();
        ArrayList<YarnVersion> yarnVersions = new ArrayList<>(yarnVersionsData.size());
        boolean v2 = true;
        for(int i = 0; i < yarnVersionsData.size(); i++){
            JsonObject version = yarnVersionsData.get(i).getAsJsonObject();
            String mcVersion = version.get("gameVersion").getAsString();
            int build = version.get("build").getAsInt();
            if(v2 && mcVersion.equals("1.14.4") && build == 14)
                v2 = false;
            String maven = version.get("maven").getAsString();
            String name = version.get("version").getAsString();
            yarnVersions.add(new YarnVersion(name, maven, mcVersion, build, v2));
        }
        this.yarnVersions = yarnVersions;
        return yarnVersions;
    }

    public ArrayList<YarnVersion> getFilteredYarnVersions(MinecraftVersion mcVersion) throws IOException{
        if(filteredYarnVersions.containsKey(mcVersion.name))
            return filteredYarnVersions.get(mcVersion.name);
        if(yarnVersions == null)
            getYarnVersions();
        ArrayList<YarnVersion> filtered = yarnVersions.stream()
                .filter(version->version.mcVersion.equals(mcVersion.name))
                .collect(Collectors.toCollection(ArrayList::new));
        filteredYarnVersions.put(mcVersion.name, filtered);
        return filtered;
    }

    public ArrayList<LoomVersion> getLoomVersions() throws IOException, SAXException, ParserConfigurationException {
        if(loomVersions != null)
            return loomVersions;
        Document loomData = xmlFromUrl("https://maven.fabricmc.net/net/fabricmc/fabric-loom/maven-metadata.xml");
        NodeList loomVersionsData = loomData.getElementsByTagName("version");
        ArrayList<LoomVersion> loomVersions = new ArrayList<>(loomVersionsData.getLength());
        for(int i = 0; i < loomVersionsData.getLength(); i++){
            int originalIndex = loomVersionsData.getLength()-i-1;
            LoomVersion version = new LoomVersion(loomVersionsData.item(originalIndex).getTextContent(), i, originalIndex);
            loomVersions.add(version);
        }
        this.loomVersions = loomVersions;
        return loomVersions;
    }

    public LoomVersion getDefaultLoomVersion(YarnVersion yarn)
            throws IOException, SAXException, ParserConfigurationException {
        if(loomVersions == null)
            getLoomVersions();
        if(loom_old == null){
            for(LoomVersion loomVersion : loomVersions){
                if(loomVersion.originalIndex == LOOM_OLD){
                    loom_old = loomVersion;
                    break;
                }
            }
        }
        if(loom_default == null){
            for(LoomVersion loomVersion : loomVersions){
                if(loomVersion.name.endsWith("SNAPSHOT")){
                    loom_default = loomVersion;
                    break;
                }
            }
        }
        return yarn.hasV2Mappings ? loom_default : loom_old;
    }

    public ArrayList<LoaderVersion> getLoaderVersions() throws IOException{
        if(loaderVersions != null)
            return loaderVersions;
        JsonArray loaderVersionsData = jsonFromUrl("https://meta.fabricmc.net/v2/versions/loader").getAsJsonArray();
        ArrayList<LoaderVersion> loaderVersions = new ArrayList<>(loaderVersionsData.size());
        for(int i = 0; i < loaderVersionsData.size(); i++){
            JsonObject version = loaderVersionsData.get(i).getAsJsonObject();
            int build = version.get("build").getAsInt();
            String maven = version.get("maven").getAsString();
            String name = version.get("version").getAsString();
            loaderVersions.add(new LoaderVersion(name, maven, build));
        }
        this.loaderVersions = loaderVersions;
        return loaderVersions;
    }

    public ArrayList<LoaderVersion> getFilteredLoaderVersions(LoomVersion loom) throws IOException{
        if(filteredLoaderVersions.containsKey(loom.name))
            return filteredLoaderVersions.get(loom.name);
        if(loaderVersions == null)
            getLoaderVersions();
        if(loom.originalIndex >= 10)
            return loaderVersions;
        ArrayList<LoaderVersion> filtered = loaderVersions.stream()
                .filter(version->version.build <= 170)
                .collect(Collectors.toCollection(ArrayList::new));
        filteredLoaderVersions.put(loom.name, filtered);
        return filtered;
    }

    public License[] getSupportedLicenses(){
        return LICENSES;
    }

    public static Document xmlFromUrl(String urlString) throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL(urlString);
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
    }

    public static JsonElement jsonFromUrl(String url) throws IOException{
        return JsonParser.parseString(getUrl(url));
    }

    public static String getUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        return readAllFromStream(url.openStream());
    }

    public static String readAllFromStream(InputStream stream) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
            result.append("\n");
        }
        reader.close();
        return result.toString();
    }

}