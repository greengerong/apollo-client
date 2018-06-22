package com.github.greengerong.phase


interface Phase {
    def exec(def buildConfig, def params)
}