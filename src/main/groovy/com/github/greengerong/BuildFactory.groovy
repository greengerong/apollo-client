package com.github.greengerong

import com.github.greengerong.phase.Phase
import com.github.greengerong.tools.ToolResolver
import org.yaml.snakeyaml.Yaml


class BuildFactory {

    static final String DEVOPS_FILE = 'devops.yml'

    def exec(String phase, def params) {
        def file = new File(DEVOPS_FILE)
        if (!file.exists()) {
            println 'Please setup devops.yml config to your project root.'
            return false
        }

        def buildConfig = parseBuildConfig file
        if (!buildConfig) {
            return false
        }
        try {
            def phaseClass = Class.forName "${this.class.getPackage().name}.phase.${phase.capitalize()}Phase"
            Phase instance = (Phase) phaseClass.newInstance()
            return instance.exec(buildConfig, params)
        } catch (e) {
            println "Sorry, `devops ${phase}` still in the fly."
            return false
        }
    }

    private def parseBuildConfig(File file) {
        def userConfig = yml file.text
        if (!userConfig.language) {
            println "Sorry, please setup language in your devops.yml."
            return
        }

        def tools = resolveTools userConfig
        if (!tools) {
            return userConfig
        }

        def stream = this.getClass().getResourceAsStream "/config/${userConfig.language}-${tools}.yml"
        if (stream) {
            def defaultConfig = yml stream.text
            mixUserConfig defaultConfig, userConfig
        } else {
            userConfig
        }
    }

    private def mixUserConfig(Map buildConfig, Map userConfig) {
        userConfig.entrySet().each {
            if (it.value instanceof Map) {
                def buildConfigValue = buildConfig.get(it.key) ? buildConfig.get(it.key) : new LinkedHashMap()
                buildConfig.put(it.key, buildConfigValue)
                buildConfig.put(it.key, mixUserConfig(buildConfigValue, it.value))
            } else {
                buildConfig.put(it.key, userConfig.get(it.key))
            }

        }
        buildConfig
    }

    private String resolveTools(userConfig) {
        if (userConfig.tools) {
            userConfig.tools
        } else {
            try {
                def resolverClass = Class.forName "${this.class.getPackage().name}.tools." +
                        "${userConfig.language.capitalize()}ToolResolver"
                ToolResolver instance = (ToolResolver) resolverClass.newInstance()
                instance.resolve(userConfig.language)
            } catch (e) {
            }
        }

    }

    private def yml(String text) {
        new Yaml().load(text)
    }
}
