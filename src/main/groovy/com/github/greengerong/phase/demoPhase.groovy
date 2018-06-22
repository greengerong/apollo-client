package com.github.greengerong.phase

class demoPhase implements Phase {

    def exec(def buildConfig, def params) {
        System.out << 'demo'
        return true
    }
}
