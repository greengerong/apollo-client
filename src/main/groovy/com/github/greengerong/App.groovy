package com.github.greengerong

import com.github.greengerong.phase.Phase
import groovyjarjarcommonscli.BasicParser
import groovyjarjarcommonscli.OptionBuilder
import org.reflections.Reflections

class App {
    static final VERSION = "1.0.0"

    static void main(def args) {
        dummyBanners()
        def phase = args ? args[0] : ''
        if (!phase) {
            println "Please input your build phase. You can use `devops phase` to find all phases"
            System.exit 1
        }
        dummyVersion phase
        dummyPhase phase
        def params = parseParams args
        def factory = new BuildFactory()
        def result = factory.exec phase, params
        if (!result) {
            println 'DevOps build failure, exit with 1.'
            System.exit 1
        }

    }

    static def dummyBanners() {
        System.out << App.class.getResourceAsStream('/banners/banner.txt').text
    }

    static def dummyPhase(def phase) {
        if (phase == 'phase') {
            println "DevOps build phase:"

            new Reflections(Phase.class.getPackage().name)
                    .getSubTypesOf(Phase.class)
                    .each {
                println "devops ${it.simpleName.replaceAll('Phase$', '').uncapitalize()}"
            }
            System.exit 0
        }
    }

    static def dummyVersion(phase) {
        if (phase == 'version') {
            println "DevOps build version ${VERSION}."
            System.exit 0
        }
    }

    private static def parseParams(args) {
        final options = new OptionBuilder()

                .withArgName("phase")
                .hasArg(false)
                .create()

        def parse = new BasicParser().parse(options, args)

        def paramList = args.size() > 1 ? args[1..-1] : []
        paramList.collectEntries {
            def sp = it.split '='
            [sp[0], sp[1]]
        }
    }
}
