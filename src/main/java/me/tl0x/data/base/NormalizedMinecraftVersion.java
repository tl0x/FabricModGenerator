package me.tl0x.data.base;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalizedMinecraftVersion extends MinecraftVersion{

    public static final Pattern SNAPSHOT_PATTERN = Pattern.compile("(\\d+)w(\\d+)(.+)");
    public static final Pattern PRE_PATTERN = Pattern.compile("((\\d+)\\.(\\d+)(?:\\.(\\d+))?)(?:-pre| pre-release )(\\d+)", Pattern.CASE_INSENSITIVE);
    public static final Pattern RC_PATTERN = Pattern.compile("((\\d+)\\.(\\d+)(?:\\.(\\d+))?)(?:-rc)(\\d+)", Pattern.CASE_INSENSITIVE);
    public static final Pattern RELEASE_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");
    public static final Pattern CONTAINS_RELEASE_PATTERN = Pattern.compile("((\\d+)\\.(\\d+)(?:\\.(\\d+))?)(?:[-\\s\\.](.+))?");

    public static final Pattern NORMALIZED_RC_PATTERN = Pattern.compile("((\\d+)\\.(\\d+)(?:\\.(\\d+))?)(?:-rc)\\.(\\d+)", Pattern.CASE_INSENSITIVE);

    public final String normalized;

    public NormalizedMinecraftVersion(MinecraftVersion mcVersion, List<MinecraftVersion> mcVersions){
        super(mcVersion.index, mcVersion.name, mcVersion.stable);

        Matcher matcher = RELEASE_PATTERN.matcher(this.name);
        if(matcher.matches()){
            normalized = this.name;
            return;
        }
        matcher = RC_PATTERN.matcher(this.name);
        if(matcher.matches()){
            int rc = getPreviousRC(mcVersion, mcVersions) + 1;
            if(rc == -1){
                rc = Integer.parseInt(matcher.group(5));
            }
            normalized = matcher.group(1) + "-rc." + rc;
            return;
        }
        matcher = PRE_PATTERN.matcher(this.name);
        if(matcher.matches()){
            normalized = matcher.group(1) + "-rc." + Integer.parseInt(matcher.group(5));
            return;
        }
        matcher = SNAPSHOT_PATTERN.matcher(this.name);
        if(matcher.matches()){
            int year = Integer.parseInt(matcher.group(1));
            int week = Integer.parseInt(matcher.group(2));
            String sub = matcher.group(3);
            String suffix = String.format("-alpha.%d.%d.%s", year, week, sub);
            normalized = getLikelyRelease(mcVersion, mcVersions) + suffix;
            return;
        }
        normalized = null;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + this.normalized + ")";
    }

    public static String getRelease(MinecraftVersion mcVersion, List<MinecraftVersion> mcVersions){
        Matcher releaseMatcher = CONTAINS_RELEASE_PATTERN.matcher(mcVersion.name);
        if(releaseMatcher.matches()){
            return releaseMatcher.group(1);
        }
        for(int i = mcVersion.index - 1; i >= 0; i--){
            MinecraftVersion testing = mcVersions.get(i);
            Matcher matcher = CONTAINS_RELEASE_PATTERN.matcher(testing.name);
            if(matcher.matches()){
                return matcher.group(1);
            }
        }
        return null;
    }

    public static String getLikelyRelease(MinecraftVersion mcVersion, List<MinecraftVersion> mcVersions){
        String release = getRelease(mcVersion, mcVersions);
        if(release != null)
            return release;
        for(int i = mcVersion.index + 1; i < mcVersions.size(); i++){
            MinecraftVersion testing = mcVersions.get(i);
            Matcher matcher = CONTAINS_RELEASE_PATTERN.matcher(testing.name);
            if(matcher.matches()){
                int major = Integer.parseInt(matcher.group(2));
                int minor = Integer.parseInt(matcher.group(3));
                return String.format("%d.%d", major, minor+1);
            }
        }
        return null;
    }

    private static int getPreviousRC(MinecraftVersion mcVersion, List<MinecraftVersion> mcVersions){
        for(int i = mcVersion.index + 1; i < mcVersions.size(); i++){
            MinecraftVersion testing = mcVersions.get(i);
            Matcher matcher = RELEASE_PATTERN.matcher(testing.name);
            if(matcher.matches()){
                return -1;
            }
            matcher = RC_PATTERN.matcher(testing.name);
            if(!matcher.matches()){
                matcher = PRE_PATTERN.matcher(testing.name);
            }
            if(matcher.matches()){
                NormalizedMinecraftVersion normalized = new NormalizedMinecraftVersion(testing, mcVersions);
                Matcher normalizedMatcher = NORMALIZED_RC_PATTERN.matcher(normalized.normalized);
                if(normalizedMatcher.matches()){
                    return Integer.parseInt(matcher.group(5));
                }
            }
        }
        return -1;
    }

}