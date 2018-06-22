package com.github.greengerong.tools


class JavaToolResolver implements ToolResolver {
    private tools = ['maven': 'pom.xml', 'gradle': 'build.gradle']

    @Override
    def resolve(Object language) {
        tools.find { new File(it.value).exists() }.key
    }
}
