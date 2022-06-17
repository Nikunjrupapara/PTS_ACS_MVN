package com.yesmarketing.ptsacs.utils;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasNoJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.hasItem;

import org.assertj.core.api.HamcrestCondition;
import org.assertj.core.api.SoftAssertions;

public class JsonUtils {
    public static void valueIsJson(SoftAssertions softly, String jsonStr, String description) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(isJson()));
    }

    public static void jsonPathEquals(SoftAssertions softly, String jsonStr, String description, String path, String expected) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(hasJsonPath(path, equalTo(expected))));
    }

    public static void jsonPathEquals(SoftAssertions softly, String jsonStr, String description, String path, Integer expected) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(hasJsonPath(path, equalTo(expected))));
    }

    public static void jsonPathNotNull(SoftAssertions softly, String jsonStr, String description, String path) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(hasJsonPath(path, is(notNullValue()))));
    }

    public static void jsonPathNotExists(SoftAssertions softly, String jsonStr, String description, String path) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(hasNoJsonPath(path)));
    }

    public static void jsonPathHasItem(SoftAssertions softly, String jsonStr, String description, String path, String expected) {
        softly.assertThat(jsonStr).as(description).satisfies(new HamcrestCondition<>(hasJsonPath(path, hasItem(expected))));
    }
}
