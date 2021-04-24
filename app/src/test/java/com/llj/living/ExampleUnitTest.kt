package com.llj.living

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

}

fun main() {
    val nums = intArrayOf(1, 2, 3, 4, 5, 6, 7)
    Test.rotate(nums, 3)
    for (i in nums.indices) {
        println(i)
    }
}