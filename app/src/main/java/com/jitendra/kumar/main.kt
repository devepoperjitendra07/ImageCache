package com.jitendra.kumar
//import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield


import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.mp.KoinPlatform.getKoin
// Koin module for dependency injection
import org.koin.dsl.module

// Main function to demonstrate usage
import org.koin.core.context.startKoin
//import org.koin.core.context.getKoin

// Operation interface
interface Operation {
    fun perform(a: Double, b: Double): Double
}

// Concrete operation implementations
class Addition : Operation {
    override fun perform(a: Double, b: Double): Double = a + b
}

class Subtraction : Operation {
    override fun perform(a: Double, b: Double): Double = a - b
}

class Multiplication : Operation {
    override fun perform(a: Double, b: Double): Double = a * b
}

class Division : Operation {
    override fun perform(a: Double, b: Double): Double {
        if (b == 0.0) throw ArithmeticException("Division by zero")
        return a / b
    }
}

// Calculator class
class Calculator(private val operation: Operation) {
    fun calculate(a: Double, b: Double): Double = operation.perform(a, b)
}



fun main() {
    // Koin module for dependency injection with factory methods for each operation
    val appModule = module {
        single { Addition() }
        single { Subtraction() }
        single { Multiplication() }
        single { Division() }
    }

    // Initialize Koin
    startKoin {
        modules(appModule)
    }

    // Get the Calculator instance
    val addition = Calculator(getKoin().get<Addition>())
    // Perform a sample calculation
    println("Addition: ${addition.calculate( 10.0, 5.0)}")

    // Get the Calculator instance
    val sub = Calculator(getKoin().get<Subtraction>())
    // Perform a sample calculation
    println("Subtraction: ${sub.calculate( 10.0, 5.0)}")


    //Give other couroutines a chance to run
    runBlocking {
        val jobA = launch { doWork("A") }
        val jobB = launch { doWork("B") }
        jobA.join()
        jobB.join()
    }

    //returnFunctionFromFunction
    val addFunction = returnFunctionFromFunction()
    println("returnFunctionFromFunction - addition ${addFunction(15,8)}")


    //val simpleArray = arrayOf("a", "b", "c", "c")
    //val nullArray: Array<Any?> = arrayOf(1, null)
    

    // Step 5: Create a few students and add them to the linked list
    var head: ListNod? = null
    head = addStudentToList(head, Student(1, "John"))
    head = addStudentToList(head, Student(2, "Alice"))
    head = addStudentToList(head, Student(3, "Bob"))
    head = addStudentToList(head, Student(4, "Charlie"))

    println("Original Linked List:")
    printList(head)

    // Step 6: Reverse the linked list
    head = reverseList(head)

    println("\nReversed Linked List:")
    printList(head)

}

//Give other couroutines a chance to run
suspend fun doWork(name: String){
    for(i in 1..5){
        println("$name - iteration $i")
        yield() //Give other couroutines a chance to run
    }
}

fun add(a: Int, b: Int) : Int{
    return a+b;
}

fun returnFunctionFromFunction() : ((Int,Int) -> Int){
    return ::add
}

fun twoSum(nums: IntArray, target: Int): IntArray {
    val map = mutableMapOf<Int, Int>() // Maps number to its index
    for (i in nums.indices) {
        val complement = target - nums[i]
        if (map.containsKey(complement)) {
            return intArrayOf(map[complement]!!, i)
        }
        map[nums[i]] = i
    }
    return intArrayOf() // No solution (though problem guarantees one)
}


fun greatestTwoSum(nums: IntArray): IntArray {
    if (nums.size < 2) return intArrayOf() // Handle arrays with < 2 elements

    // Create list of pairs (number, index)
    val indexedNums = nums.mapIndexed { index, num -> num to index }

    // Sort by number in descending order
    val sorted = indexedNums.sortedByDescending { it.first }

    // Return indices of the two largest numbers
    return intArrayOf(sorted[0].second, sorted[1].second)
}

// Optional: Return the greatest sum value
fun greatestTwoSumValue(nums: IntArray): Int {
    if (nums.size < 2) return 0 // Handle arrays with < 2 elements

    val sorted = nums.sortedDescending()
    return sorted[0] + sorted[1]
}




// Step 1: Define the Node class to represent a linked list node
class ListNod(var student: Student) {
    var next: ListNod? = null
}

// Step 2: Define the Student class
data class Student(val id: Int, val name: String)

// Step 3: Function to add student data to the linked list
fun addStudentToList(head: ListNod?, student: Student): ListNod {
    val newNode = ListNod(student)
    if (head == null) {
        return newNode
    }
    var current = head
    while (current?.next != null) {
        current = current.next
    }
    current?.next = newNode
    return head
}

// Step 4: Function to reverse the linked list
fun reverseList(head: ListNod?): ListNod? {
    var prev: ListNod? = null
    var current = head
    while (current != null) {
        val next = current.next
        current.next = prev
        prev = current
        current = next
    }
    return prev
}

// Function to print the linked list
fun printList(head: ListNod?) {
    var current = head
    while (current != null) {
        println("Student ID: ${current.student.id}, Name: ${current.student.name}")
        current = current.next
    }
}