package com.github.greengerong.utils

class Shell {
    
    static boolean execute(def command) {
        def execute = command.execute()
        System.out << execute.inputStream
        execute.waitForProcessExit(Integer.MAX_VALUE)
        return execute.exitcode == 0
    }
}
