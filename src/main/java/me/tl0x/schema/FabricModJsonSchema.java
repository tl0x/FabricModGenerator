package me.tl0x.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FabricModJsonSchema {
    public int schemaVersion = 1;
    public String id;
    public String version;
    public String name;
    public String description;
    public List<String> authors = new ArrayList<>();
    public List<String> contributors = new ArrayList<>();
    public Contact contact = new Contact();
    public String license;
    public String icon;
    public String environment;
    public Entrypoints entrypoints = new Entrypoints();
    public List<String> mixins = new ArrayList<>();
    public HashMap<String, String> depends = new LinkedHashMap<>();
}