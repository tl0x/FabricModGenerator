package me.tl0x.data.base;

import me.tl0x.Util;

import java.util.regex.Matcher;

public class SemVer implements Comparable<SemVer>{
    private final String version;
    private final int major;
    private final int minor;
    private final int patch;
    private final String pre;
    private final String build;

    public SemVer(String semVer){
        this.version = semVer;
        Matcher matcher = Util.SEMVER_REGEX.matcher(semVer);
        if(!matcher.matches()){
            throw new IllegalArgumentException("The string provided does not conform to SemVer");
        }else{
            this.major = Integer.parseInt(matcher.group(1));
            this.minor = Integer.parseInt(matcher.group(2));
            this.patch = Integer.parseInt(matcher.group(3));
            this.pre = matcher.group(4);
            this.build = matcher.group(5);
        }
    }

    public SemVer(String major, String minor, String patch, String pre, String build){
        String version = major + "." + minor + "." + patch;
        if(pre != null){
            version = version + "-" + pre;
        }
        if(build != null){
            version = version + "+" + build;
        }
        this.version = version;
        this.major = Integer.parseInt(major);
        this.minor = Integer.parseInt(minor);
        this.patch = Integer.parseInt(patch);
        this.pre = pre;
        this.build = build;
    }

    /**
     * @return the major version
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * @return the minor version
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * @return the patch version
     */
    public int getPatch() {
        return this.patch;
    }

    /**
     * @return the pre-release information
     */
    public String getPre() {
        return this.pre;
    }

    /**
     * @return the build information
     */
    public String getBuild() {
        return this.build;
    }

    // This is technically not correct, but works for this particular use case
    @Override
    public int compareTo(SemVer o) {
        if(major == o.major){
            if(minor == o.minor){
                if(patch == o.patch){
                    if(pre == null && o.pre == null){
                        if(build == null && o.build == null){
                            return 0;
                        }else if(build == null){
                            return 1;
                        }else if(o.build == null){
                            return -1;
                        }else{
                            return build.compareTo(o.build);
                        }
                    }else if(pre == null){
                        return 1;
                    }else if(o.pre == null){
                        return -1;
                    }else{
                        return pre.compareTo(o.pre);
                    }
                }
                return patch - o.patch;
            }
            return minor - o.minor;
        }
        return major - o.major;
    }

    @Override
    public String toString() {
        return this.version;
    }
}