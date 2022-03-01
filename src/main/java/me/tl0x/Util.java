package me.tl0x;

import java.util.regex.Pattern;

public class Util{

    public static final String[] KEYWORDS = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while", "continue"};
    public static final Pattern PACKAGE_REGEX = Pattern.compile("([A-Za-z$_][A-Za-z0-9$_]*\\.)*[A-Za-z$_][A-Za-z0-9$_]*");
    public static final Pattern IDENT_REGEX = Pattern.compile("[A-Za-z$_][A-Za-z0-9$_]*");

    public static final Pattern SEMVER_REGEX = Pattern.compile("(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?");

    public static <T> T ensureExists(T object, String name){
        if(object == null)
            throw new IllegalArgumentException(name + " must be provided");
        return object;
    }

    public static String ensureNotEmpty(String s, String name){
        if(s == null)
            throw new IllegalArgumentException(name + " must be provided");
        if(isEmpty(s))
            throw new IllegalArgumentException(name + " must not be empty");
        return s;
    }

    public static String ensureSemVer(String s, String name){
        ensureNotEmpty(s, name);
        if(!isSemVer(s))
            throw new IllegalArgumentException(name + " must obey SemVer");
        return s;
    }

    public static String[] ensureValidPackage(String s, String name){
        ensureNotEmpty(s, name);
        if(!PACKAGE_REGEX.matcher(s).matches())
            throw new IllegalArgumentException(name + " must be a valid Java package");
        String[] idents = s.split("\\.");
        for(String ident : idents){
            if(contains(KEYWORDS, ident))
                throw new IllegalArgumentException(name + " cannot contain " + ident + " as it is a reserved word");
        }
        return idents;
    }

    public static String ensureValidClass(String s, String name){
        ensureNotEmpty(s, name);
        if(!IDENT_REGEX.matcher(s).matches())
            throw new IllegalArgumentException(name + " must be a valid Java identifier");
        if(contains(KEYWORDS, s))
            throw new IllegalArgumentException(name + " cannot be " + s + " as it is a reserved word");
        return s;
    }

    public static <T> boolean contains(T[] array, T object){
        for(T t : array){
            if(t.equals(object))
                return true;
        }
        return false;
    }

    public static boolean isEmpty(String s){
        return s.matches("\\s*");
    }

    public static boolean isSemVer(String s){
        return SEMVER_REGEX.matcher(s).matches();
    }
}