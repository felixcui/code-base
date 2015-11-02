/**
 * Created by cuixiaofeng on 15/9/23.
 */

class Person(name: String, age: Int) {
  def this(name: String) = this(name, 20)

  def getName() = name
  def getAge() = age
}



object HelloWorld {

  def max(x : Int, y : Int) : Int = {
    if (x > y) x
    else y
  }

  val max1 = (x: Int, y: Int) => if (x > y) x else y

  def main(args: Array[String]): Unit = {
    var args1 = Array("1", "2", "3")
    var args = new Array[String](3);
    args(0) = "1"


    println("Hello World!")
  }

}
