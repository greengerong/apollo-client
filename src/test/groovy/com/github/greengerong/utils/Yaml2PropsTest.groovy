package com.github.greengerong.utils

import org.apache.commons.io.IOUtils
import spock.lang.Specification

/******************************************
 *                                        *
 * Auth: green gerong                     *
 * Date: 2018                             *
 * blog: http://greengerong.github.io/    *
 * github: https://github.com/greengerong *
 *                                        *
 ******************************************/

class Yaml2PropsTest extends Specification {
    def "Yaml to properties"() {
        given: "yml file"
        def text = IOUtils.toString(this.getClass().getResourceAsStream("/config.yml"), "UTF-8")

        when: "convert to props"
        def map = Yaml2Props.toProps text

        then: "get props with size 15"
        println map.collect { "${it.key}=${it.value}" }.join("\n")
        map.size() == 15
    }
}
