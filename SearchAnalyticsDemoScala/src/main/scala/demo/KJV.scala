package demo

case class KJV(book:String, chapter:Int, verse:Int, body:String)

object KJV {
  def apply(input:Array[String]):KJV = {
    KJV(input(0).trim, Integer.parseInt(input(1)), Integer.parseInt(input(2)), input(3).trim)
  }
}

