import java.util.concurrent.ThreadLocalRandom

println 'generating large input file'

File file = new File('src/test/resources/large_file.txt')
if (file.exists()){
    println 'file already exists, returning'
    return
}

def numberOfLines = 50000000
for (int i = 0; i < numberOfLines; i++){
    "${ThreadLocalRandom.current().nextInt(28)}-${getMonthName(ThreadLocalRandom.current().nextInt(12))}-${getRandomYear()}"

    file << "INSTRUMENT${instrumentId},${date},${price}"
}

def getInstrumentId(){
    def maxInstrumentId = 10000
    String.valueOf(ThreadLocalRandom.current().nextInt(maxInstrumentId))
}

String getDate(){
    def maxMonth = 12
    "${ThreadLocalRandom.current().nextInt(28)}-${getMonthName(ThreadLocalRandom.current().nextInt(maxMonth))}-${getRandomYear()}"
}

def getPrice(){
    ThreadLocalRandom.current().nextDouble(2.000, 200.000)
}

def getMonthName(int number){
    def months = [1: "Jan", 2: "Feb", 3: "Mar", 4: "Apr", 5: "May", 6: "Jun", 7: "Jul", 8: "Aug", 9: "Sep", 10: "Oct", 11: "Nov", 12: "Dec"]
    months[number]
}

def getRandomYear(){
    int minYear = 1996, maxYear = 2014
    ThreadLocalRandom.current().nextInt(minYear, maxYear + 1)
}