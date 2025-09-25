package com.webtoon_service.constants;

public final class  XPathConstants {
    private XPathConstants() {}

    public static final String XPATH_CHAPTER_INPUT_ASURA = "//input[@placeholder and contains(@placeholder, 'Chapter')]";
    public static final String XPATH_CHAPTER_INPUT_COMICK = "//input[@placeholder='Goto chap']";

    public static final String XPATH_CHAPTER_LINK_TEMPLATE = "//a[contains(., 'Chapter %s')]";
    public static final String XPATH_IMAGE_ELEMENT = "//img[@src or @data-src]";
}