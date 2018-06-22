package com.github.greengerong.utils

class Pipeline {

    static def pipelineNumber() {
        def pipelineNumber = System.getenv('PIPELINE_NUMBER')
        pipelineNumber ? pipelineNumber : System.getenv('BUILD_NUMBER')
    }

    static def version() {
        def pipelineEnv = System.getenv('PIPELINE_ENV')
        if (['CI', 'DEV'].contains(pipelineEnv?.toUpperCase())) {
            def pipelineName = System.getenv('PIPELINE_NAME')
            "${pipelineName}-${Pipeline.pipelineNumber()}"
        } else {
            def pipelineBranch = System.getenv('PIPELINE_BRANCH')
            def split = pipelineBranch.split('/')
            split[split.length - 1]
        }
    }
}
