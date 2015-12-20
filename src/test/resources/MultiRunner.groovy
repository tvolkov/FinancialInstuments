import com.infusion.App

// a script for running application multiple times in order to make sure it always returns 0
println args
def times = 500
println "running application for ${times} times"

for (int i = 0; i < times; i++){
    println "executing time ${i}"
    App.main(args[0])
}
