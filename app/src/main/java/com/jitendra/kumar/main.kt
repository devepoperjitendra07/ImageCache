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
import java.util.Stack

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
   /* val appModule = module {
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



    //getSecondLargest
    val arr = intArrayOf(12, 35, 1, 10, 34, 1)
    println(findSecondLargest(arr))
    println("\n")*/


    //moveAllZeroToEnd
//    moveAllZeroToEnd(intArrayOf(1, 0, 1, 0, 0, 1))
//    println("\n")
//    moveAllZeroToEnd(intArrayOf(1, 1, 1, 1, 0, 1))
//    println("\n")
//    moveAllZeroToEnd(intArrayOf(1, 2, 0, 4, 3, 0, 5, 0))
//    println("\n")
//    moveAllZeroToEnd(intArrayOf(0, 0, 0, 1, 0, 1))


   /* //maxConsecutiveOnes
    println("maxConsecutiveOnes ${maxConsecutiveOnes(intArrayOf(11, 0, 11, 0, 0, 11, 11, 10, 11, 11, 0, 11))}")
    println("\n")

    //findThirdLargest
    println(findThirdLargest(intArrayOf(1, 14, 2, 16, 10, 20)))
    println("\n")



    //prefixSumArray
    println(prefixSumArray(intArrayOf(10, 20, 10, 5, 15)))
    println("\n")*/


    println(nextRightLargerElement(intArrayOf(6, 8, 0, 1, 3)))
    //input - [6, 8, 0, 1, 3]
    //output- [8,-1, 1, 3,-1]//reverse
    println("\n")

    println(nextRightSmallElement(intArrayOf(6, 8, 0, 1, 3)))
    //input - [6, 8, 0, 1, 3]
    //output- [0, 0,-1,-1,-1]// reverse
    println("\n")


    println(nextLeftLargerElement(intArrayOf(6, 8, 0, 1, 3)))
    //input - [6, 8, 0, 1, 3]
    //output- [-1,-1,8, 8, 8]
    println("\n")

    println(nextLeftSmallerElement(intArrayOf(6, 8, 0, 1, 3)))
    //input - [6, 8, 0, 1, 3]
    //output- [-1,6,-1, 0, 1]
    println("\n")

}

fun nextRightLargerElement(arr: IntArray): List<Int> {
    //Nearest Greater to right | Next Largest Element
    val n = arr.size
    val res = mutableListOf<Int>()
    val stk = Stack<Int>()

    // Traverse the array from right to left
    for (i in n - 1 downTo 0) {
        if(stk.size == 0){
            res.add(-1)
        }else if(stk.peek() > arr[i]){
            res.add(stk.peek())
        }else {
            while (stk.size > 0 && stk.peek() <= arr[i]){
                stk.pop()
            }
            if(stk.size == 0){
                res.add(-1)
            }else{
                res.add(stk.peek())
            }
        }

        stk.push(arr[i])
    }
    return res
}

fun nextRightSmallElement(arr: IntArray): List<Int> {
    //Nearest Smaller to right | Next Smaller Element
    val n = arr.size
    val res = mutableListOf<Int>()
    val stk = Stack<Int>()

    // Traverse the array from right to left
    for (i in n - 1 downTo 0) {
        if(stk.size == 0){
            res.add(-1)
        }else if(stk.peek() < arr[i]){
            res.add(stk.peek())
        }else {
            while (stk.size > 0 && stk.peek() >= arr[i]){
                stk.pop()
            }
            if(stk.size == 0){
                res.add(-1)
            }else{
                res.add(stk.peek())
            }
        }

        stk.push(arr[i])
    }
    return res
}

fun nextLeftLargerElement(arr: IntArray): List<Int> {
    val n = arr.size
    val res = mutableListOf<Int>()
    val stk = Stack<Int>()

    // Traverse the array from left to right
    for (i in 0 until n) {
        if (stk.isEmpty()) {
            res.add(-1)
        } else if (stk.peek() > arr[i]) {
            res.add(stk.peek())
        } else {
            while (stk.isNotEmpty() && stk.peek() <= arr[i]) {
                stk.pop()
            }
            if (stk.isEmpty()) {
                res.add(-1)
            } else {
                res.add(stk.peek())
            }
        }

        stk.push(arr[i])
    }

    return res
}


fun nextLeftSmallerElement(arr: IntArray): List<Int> {
    val n = arr.size
    val res = mutableListOf<Int>()
    val stk = Stack<Int>()

    // Traverse the array from left to right
    for (i in 0 until n) {
        if (stk.isEmpty()) {
            res.add(-1)
        } else if (stk.peek() < arr[i]) {
            res.add(stk.peek())
        } else {
            while (stk.isNotEmpty() && stk.peek() >= arr[i]) {
                stk.pop()
            }
            if (stk.isEmpty()) {
                res.add(-1)
            } else {
                res.add(stk.peek())
            }
        }

        stk.push(arr[i])
    }

    return res
}


fun prefixSumArray(arr: IntArray): IntArray{
    //Input: arr[] = [10, 20, 10, 5, 15]
    //Output: 10 30 40 45 60

    //var temp = 0;

    for (i in 1 until arr.size){
        //val temp = arr[i-1]
        arr[i] = arr[i] + arr[i-1]
    }

    for(i in 0..arr.size-1){
        println("prefixSumArray  ->  ${arr[i]}")

    }

    return arr
}

fun maxConsecutiveOnes(arr: IntArray): Int{
    // val arr = intArrayOf(11, 0, 11, 0, 0, 11, 11, 10, 11, 11, 0, 11)
    var maxConsecutiveOnes = 0
    var tempCount = 0
    for (i in 1 until arr.size){
        if(arr[i] == arr[i-1]){
            tempCount++
        }else{
            maxConsecutiveOnes = Math.max(maxConsecutiveOnes, tempCount)
            tempCount = 1
        }
    }
    return Math.max(maxConsecutiveOnes, tempCount)

}

fun moveAllZeroToEnd(arr: IntArray){

    // val arr = intArrayOf(1, 0, 1, 0, 0, 1)

    //0 index -> i = 0, count = 0, temp(arr[i]) = 1, arr[i] = arr[count], arr[count] = temp, =>  count = 1
    //---(1, 0, 1, 0, 0, 1)

    //1 index -> i = 1, count = 1,
    //---(1, 0, 1, 0, 0, 1)

    //2 index -> i = 2, count = 1, temp(arr[i]) = 1, arr[i] = arr[count], arr[count] = temp,   =>  count = 2
    //---(1, 1, 0, 0, 0, 1)

    //3 index -> i = 3, count = 2,
    //---(1, 1, 0, 0, 0, 1)

    //4 index -> i = 4, count = 2,
    //---(1, 1, 0, 0, 0, 1)

    //5 index -> i = 5, count = 2,


    var count = 0
    for(i in arr.indices){
        if(arr[i] != 0){
            val temp = arr[i]
            arr[i] = arr[count]
            arr[count] =  temp
            count++
        }
    }


    for(i in 0..arr.size-1){
        println("moveAllZeroToEnd  ->  ${arr[i]}")

    }



}

fun findSecondLargest(arr: IntArray): Any {
    if (arr.size < 2) {
        return "No second largest exists"
    }
    var largest = Int.MIN_VALUE
    var secondLargest = Int.MIN_VALUE
    // finding the second largest element
    for (num in arr) {
        // If arr[i] > largest, update second largest with
        // largest and largest with arr[i]
        if (num > largest) {
            secondLargest = largest
            largest = num
        }
        // If arr[i] < largest and arr[i] > second largest,
        // update second largest with arr[i]
        else if (num < largest && num > secondLargest) {
            secondLargest = num
        }
    }

    println("largest $largest")

    return if (secondLargest == Int.MIN_VALUE) {
        "No second largest exists"
    } else {
        "secondLargest $secondLargest"
    }
}

fun findThirdLargest(arr: IntArray): Any {
    if (arr.size < 3) {
        return "No thirdLargest largest exists"
    }
    var largest = Int.MIN_VALUE
    var secondLargest = Int.MIN_VALUE
    var thirdLargest = Int.MIN_VALUE
    // finding the second largest element
    for (num in arr) {
        // If arr[i] > largest, update second largest with
        // largest and largest with arr[i]
        if (num > largest) {
            thirdLargest = secondLargest
            secondLargest = largest
            largest = num
        }
        // If arr[i] is greater than second,
        // set third to second and second
        // to arr[i].
        else if (num > secondLargest) {
            thirdLargest = secondLargest
            secondLargest = num
        }

        // If arr[i] is greater than third,
        // set third to arr[i].
        else if (num > thirdLargest) {
            thirdLargest = num
        }
    }

    return if (thirdLargest == Int.MIN_VALUE) {
        "No thirdLargest largest exists"
    } else {
        "thirdLargest $thirdLargest"
    }
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



//====Linked List
// Step 1: Define the Node class to represent a linked list node
class ListNod(var student: Student) {
    var next: ListNod? = null
}

class Node(student: Student){
    var next: Node? = null
}

fun addStudentNode(head: Node?, student: Student): Node{
    val newNode = Node(student)
    if(head == null){
        return  newNode
    }
    var current = head
    while (current?.next != null){
        current = current.next
    }
    current?.next = newNode
    return head
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