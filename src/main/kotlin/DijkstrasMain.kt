import java.lang.Exception
import java.lang.NumberFormatException
import kotlin.system.exitProcess

const val NO_PARENT = -1

fun main(){

    val adjacencyMatrix : Array<Array<Int>> = arrayOf(
        arrayOf(0, 4, 0, 0, 0, 0, 0, 8, 0),
        arrayOf(4, 0, 8, 0, 0, 0, 0, 11, 0),
        arrayOf(0, 8, 0, 7, 0, 4, 0, 0, 2),
        arrayOf(0, 0, 7, 0, 9, 14, 0, 0, 0),
        arrayOf(0, 0, 0, 9, 0, 10, 0, 0, 0),
        arrayOf(0, 0, 4, 0, 10, 0, 2, 0, 0),
        arrayOf(0, 0, 0, 14, 0, 2, 0, 1, 6),
        arrayOf(8, 11, 0, 0, 0, 0, 1, 0, 7),
        arrayOf(0, 0, 2, 0, 0, 0, 6, 7, 0)
    )

    val initialVertex : Int
    val noFinal : Int

    try {
        print("Digite o nó inicial (default e min 0) : ")
        initialVertex = readLine()?.toInt()!!
        if(initialVertex !in 0..adjacencyMatrix.count()) throw IndexOutOfBoundsException()

        print("Digite o último nó (default e max 8): ")
        noFinal = readLine()?.toInt()!!
        if(noFinal !in 0..adjacencyMatrix.count()) throw IndexOutOfBoundsException()

    } catch (e : Exception){
        val erroText = when (e) {
            is IndexOutOfBoundsException -> "Não está no range"
            is NumberFormatException -> "Não digitou um número"
            else -> "O que diabos vc fez?"
        }
        println("$erroText, olha a cagada em baixo :( ")
        e.printStackTrace()
        exitProcess(400)
    }

    dijkstra(
        adjacencyMatrix,
        initialVertex,
        noFinal
    )
}

fun dijkstra(adjacencyMatrix : Array<Array<Int>>, initialVertex : Int, noFinal : Int) {
    val nVertices = adjacencyMatrix.count()
    val shortestDistances = IntArray(nVertices)

    val added = BooleanArray(nVertices)

    for (vertexIndex in adjacencyMatrix.indices){
        shortestDistances[vertexIndex] = Int.MAX_VALUE
        added[vertexIndex] = false
    }

    shortestDistances[initialVertex] = 0

    val parents = IntArray(nVertices)

    parents[initialVertex] = NO_PARENT

    for (i in adjacencyMatrix.indices){

        var nearestVertex = -1
        var shortestDistance = Int.MAX_VALUE

        for (vertexIndex in adjacencyMatrix.indices){
            if(!added[vertexIndex] &&
                    shortestDistances[vertexIndex] < shortestDistance){
                nearestVertex = vertexIndex
                shortestDistance = shortestDistances[vertexIndex]
            }
        }

        added[nearestVertex] = true

        for (vertexIndex in adjacencyMatrix.indices){
            val edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex]

            if (edgeDistance > 0
                && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex])){
                parents[vertexIndex] = nearestVertex
                shortestDistances[vertexIndex] = shortestDistance + edgeDistance
            }
        }
    }

    printSolution(initialVertex, noFinal, shortestDistances, parents)
}

fun printSolution(initialVertex : Int, noFinal: Int, distances : IntArray, parents : IntArray){
    for (vertexIndex in distances.indices){
        if(vertexIndex == noFinal){
            print("\n $initialVertex - >")
            print("$vertexIndex \t\t ")
            print("${distances[vertexIndex]} \t\t")
            printPath(vertexIndex, parents)
        }
    }
}

fun printPath(currentVertex : Int, parents : IntArray){
    if (currentVertex == NO_PARENT){
        return
    }
    printPath(parents[currentVertex], parents)
    print("$currentVertex")
}