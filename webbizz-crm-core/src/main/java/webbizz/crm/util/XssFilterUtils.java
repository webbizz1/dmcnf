package webbizz.crm.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS 필터링 유틸리티
 *
 * @author TAEROK HWANG
 * @version 2.0
 */
public class XssFilterUtils {

    private static final Map<String, Set<String>> tagWhiteList = new HashMap<>();

    private static final Pattern TAG_PATTERN =
            Pattern.compile("<\\s*(/?)\\s*(\\w+)([^>]*?)>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_PATTERN =
            Pattern.compile("<script\\b[^<]*(?:(?!</script>)<[^<]*)*</script>", Pattern.CASE_INSENSITIVE);

    // 정적 초기화: 허용된 태그와 속성을 설정
    static {
        Set<String> attributes = new HashSet<>();
        attributes.add("class");
        attributes.add("id");
        attributes.add("style");

        Set<String> aAttributes = new HashSet<>(attributes);
        aAttributes.add("download");
        aAttributes.add("href");
        aAttributes.add("rel");
        aAttributes.add("target");
        aAttributes.add("title");

        Set<String> blockquoteAttributes = new HashSet<>(attributes);
        blockquoteAttributes.add("cite");

        Set<String> colAttributes = new HashSet<>(attributes);
        colAttributes.add("span");
        colAttributes.add("width");

        Set<String> iframeAttributes = new HashSet<>(attributes);
        iframeAttributes.add("allow");
        iframeAttributes.add("allowfullscreen");
        iframeAttributes.add("frameborder");
        iframeAttributes.add("height");
        iframeAttributes.add("referrerpolicy");
        iframeAttributes.add("src");
        iframeAttributes.add("title");
        iframeAttributes.add("width");

        Set<String> imgAttributes = new HashSet<>(attributes);
        imgAttributes.add("align");
        imgAttributes.add("alt");
        imgAttributes.add("height");
        imgAttributes.add("src");
        imgAttributes.add("title");
        imgAttributes.add("width");

        Set<String> oembedAttributes = new HashSet<>(attributes);
        oembedAttributes.add("url");

        Set<String> olAttributes = new HashSet<>(attributes);
        olAttributes.add("start");
        olAttributes.add("type");

        Set<String> tableAttributes = new HashSet<>(attributes);
        tableAttributes.add("summary");
        tableAttributes.add("width");

        Set<String> tdAttributes = new HashSet<>(attributes);
        tdAttributes.add("abbr");
        tdAttributes.add("axis");
        tdAttributes.add("colspan");
        tdAttributes.add("rowspan");
        tdAttributes.add("width");

        Set<String> thAttributes = new HashSet<>(attributes);
        thAttributes.add("abbr");
        thAttributes.add("axis");
        thAttributes.add("colspan");
        thAttributes.add("rowspan");
        thAttributes.add("scope");
        thAttributes.add("width");

        Set<String> ulAttributes = new HashSet<>(attributes);
        ulAttributes.add("type");

        // 각 태그와 그에 대한 허용 속성을 추가
        tagWhiteList.put("a", aAttributes);
        tagWhiteList.put("b", attributes);
        tagWhiteList.put("blockquote", blockquoteAttributes);
        tagWhiteList.put("br", new HashSet<>());
        tagWhiteList.put("caption", attributes);
        tagWhiteList.put("cite", attributes);
        tagWhiteList.put("code", attributes);
        tagWhiteList.put("col", colAttributes);
        tagWhiteList.put("colgroup", colAttributes);
        tagWhiteList.put("dd", attributes);
        tagWhiteList.put("div", attributes);
        tagWhiteList.put("dl", attributes);
        tagWhiteList.put("dt", attributes);
        tagWhiteList.put("em", attributes);
        tagWhiteList.put("figure", attributes);
        tagWhiteList.put("h1", attributes);
        tagWhiteList.put("h2", attributes);
        tagWhiteList.put("h3", attributes);
        tagWhiteList.put("h4", attributes);
        tagWhiteList.put("h5", attributes);
        tagWhiteList.put("h6", attributes);
        tagWhiteList.put("i", attributes);
        tagWhiteList.put("iframe", iframeAttributes);
        tagWhiteList.put("img", imgAttributes);
        tagWhiteList.put("li", attributes);
        tagWhiteList.put("oembed", oembedAttributes);
        tagWhiteList.put("ol", olAttributes);
        tagWhiteList.put("p", attributes);
        tagWhiteList.put("pre", attributes);
        tagWhiteList.put("q", blockquoteAttributes);
        tagWhiteList.put("small", attributes);
        tagWhiteList.put("span", attributes);
        tagWhiteList.put("strike", attributes);
        tagWhiteList.put("strong", attributes);
        tagWhiteList.put("sub", attributes);
        tagWhiteList.put("sup", attributes);
        tagWhiteList.put("table", tableAttributes);
        tagWhiteList.put("tbody", attributes);
        tagWhiteList.put("td", tdAttributes);
        tagWhiteList.put("tfoot", attributes);
        tagWhiteList.put("th", thAttributes);
        tagWhiteList.put("thead", attributes);
        tagWhiteList.put("tr", attributes);
        tagWhiteList.put("u", attributes);
        tagWhiteList.put("ul", ulAttributes);
    }


    /**
     * XSS 필터링
     *
     * @param input 입력 문자열
     * @return String XSS 필터링된 문자열
     * @author TAEROK HWANG
     */
    public static String filter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // <script> 태그를 제거
        String withoutScripts = SCRIPT_PATTERN.matcher(input).replaceAll("");

        // NginX sub_filter 로 인해 발생하는 중복 /admin/api/v1/attachment 문자열 치환 (에디터 이미지 첨부)
        withoutScripts = withoutScripts.replaceAll("/admin/api/v1/attachment", "/api/v1/attachment");

        StringBuilder cleanOutput = new StringBuilder();
        Matcher matcher = TAG_PATTERN.matcher(withoutScripts);

        int lastEnd = 0;
        while (matcher.find()) {
            cleanOutput.append(withoutScripts, lastEnd, matcher.start());

            String slash = matcher.group(1); // "/" if closing tag
            String tagName = matcher.group(2).toLowerCase();
            String attributes = matcher.group(3);

            // 허용할 태그만 남김
            if (tagWhiteList.containsKey(tagName)) {
                cleanOutput.append("<").append(slash).append(tagName);

                if (!slash.equals("/")) { // 여는 태그만 속성을 처리
                    cleanOutput.append(processAttributes(tagName, attributes));
                }

                cleanOutput.append(">");
            }

            lastEnd = matcher.end();
        }

        cleanOutput.append(withoutScripts, lastEnd, withoutScripts.length());
        return cleanOutput.toString();
    }

    /**
     * 속성 필터링
     *
     * @param tagName 태그 이름
     * @param attributes 속성 문자열
     * @return String 필터링된 속성 문자열
     * @author TAEROK HWANG
     */
    private static String processAttributes(String tagName, String attributes) {
        StringBuilder validAttributes = new StringBuilder();
        Set<String> allowedAttributes = tagWhiteList.get(tagName);

        // 속성 이름을 감지하는 정규 표현식
        Pattern attrPattern = Pattern.compile("(\\w+|aria-[\\w-]+|data-[\\w-]+)\\s*=\\s*(['\"])(.*?)\\2");
        Matcher attrMatcher = attrPattern.matcher(attributes);

        while (attrMatcher.find()) {
            String attrName = attrMatcher.group(1).toLowerCase();
            String attrValue = attrMatcher.group(3);

            // 일반 허용 속성 또는 aria-, data-로 시작하는 속성 확인
            if (allowedAttributes.contains(attrName) || attrName.startsWith("aria-") || attrName.startsWith("data-")) {
                validAttributes.append(" ").append(attrName).append("=\"").append(attrValue).append("\"");
            }
        }

        return validAttributes.toString();
    }

    /**
     * 태그 제거
     *
     * @param input 입력 문자열
     * @return String 태그가 제거된 문자열
     */
    public static String stripTags(String input) {
        return input.replaceAll("<[^>]*>", "");
    }

}
