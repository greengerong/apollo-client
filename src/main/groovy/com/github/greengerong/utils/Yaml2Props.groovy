package com.github.greengerong.utils

import javafx.util.Pair
import org.apache.commons.lang3.StringUtils
import org.yaml.snakeyaml.Yaml

class Yaml2Props {

    static def toProps(def text) {
        final data = new Yaml().load text
        parse(data, '').flatten().collectEntries() {
            [it.key, it.value]
        }
    }

    static List parse(Object data, def prefix) {
        final dataType = data.getClass()
        if (Map.class.isAssignableFrom(dataType)) {
            parseMap data, prefix
        } else if (List.class.isAssignableFrom(dataType)) {
            parseList data, prefix
        } else {
            [new Pair(prefix, data)]
        }
    }

    private static parseList(List data, String prefix) {
        data.withIndex().collect { def it, int i ->
            def key = StringUtils.isBlank(prefix) ? i : "${prefix}[${i}]"
            parse it, key
        }
    }

    private static parseMap(Map data, String prefix) {
        data.collect {
            def key = StringUtils.isBlank(prefix) ? it.key : "${prefix}.${it.key}"
            parse it.value, key
        }
    }
}
