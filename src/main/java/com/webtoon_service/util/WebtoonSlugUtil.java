package com.webtoon_service.util;

public class WebtoonSlugUtil {
    private WebtoonSlugUtil() {

    }

    /**
     * Checks if two titles match, ignoring case and special characters.
     * Titles match if they are equal or one contains the other.
     */
    public static boolean isTitleMatch(String actual, String expected) {
        if (actual == null || expected == null) return false;

        actual = normalize(actual);
        expected = normalize(expected);

        return actual.equals(expected) || actual.contains(expected) || expected.contains(actual);
    }

    /**
     * Extracts suffix (unique identifier) from an Asura-style slug.
     * Example slug: "nano-machine-0869fe98" → returns "0869fe98"
     */
    public static String extractAsuraSuffix(String slug) {
        validateSlug(slug);
        return slug.substring(slug.lastIndexOf("-") + 1);
    }

    /**
     * Extracts suffix (unique identifier) from a Comick-style slug.
     * Example slug: "4190634673-nano-machine" → returns "4190634673"
     */
    public static String extractComickSuffix(String slug) {
        validateSlug(slug);
        return slug.substring(0, slug.indexOf("-"));
    }

    /**
     * Extracts title from a Comick-style slug.
     * Example slug: "4190634673-nano-machine" → returns "nano-machine"
     */
    public static String extractComickTitle(String slug) {
        if (slug == null || !slug.contains("-")) return slug;
        return slug.substring(slug.indexOf("-") + 1);
    }

    /**
     * Extracts title from an Asura-style slug.
     * Example slug: "nano-machine-0869fe98" → returns "nano-machine"
     */
    public static String extractAsuraTitle(String slug) {
        if (slug == null || !slug.contains("-")) return slug;
        return slug.substring(0, slug.lastIndexOf("-"));
    }

    private static void validateSlug(String slug) {
        if (slug == null || !slug.contains("-")) {
            throw new IllegalArgumentException("Invalid slug: " + slug);
        }
    }

    /**
     * Normalizes a string by removing non-alphanumeric characters and converting to lowercase.
     */
    public static String normalize(String str) {
        return str == null ? "" : str.toLowerCase().replaceAll("[^a-zA-Z0-9]", "").trim();
    }
}
