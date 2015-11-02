/**
 * Created by cuixiaofeng on 15/9/24.
 */



object AdsEtl {

  def main (args: Array[String]) {


    val sparkConf = new SparkConf().setAppName("Log Query")
    val sc = new SparkContext(sparkConf)

    val dataSet =
      if (args.length == 1) sc.textFile(args(0)) else sc.parallelize(exampleApacheLogs)
    // scalastyle:off
    val apacheLogRegex =
      """^([\d.]+) (\S+) (\S+) \[([\w\d:/]+\s[+\-]\d{4})\] "(.+?)" (\d{3}) ([\d\-]+) "([^"]+)" "([^"]+)".*""".r
    // scalastyle:on
    /** Tracks the total query count and number of aggregate bytes for a particular group. */
    class Stats(val count: Int, val numBytes: Int) extends Serializable {
      def merge(other: Stats): Stats = new Stats(count + other.count, numBytes + other.numBytes)
      override def toString: String = "bytes=%s\tn=%s".format(numBytes, count)
    }

    def extractKey(line: String): (String, String, String) = {
      apacheLogRegex.findFirstIn(line) match {
        case Some(apacheLogRegex(ip, _, user, dateTime, query, status, bytes, referer, ua)) =>
          if (user != "\"-\"") (ip, user, query)
          else (null, null, null)
        case _ => (null, null, null)
      }
    }

    def extractStats(line: String): Stats = {
      apacheLogRegex.findFirstIn(line) match {
        case Some(apacheLogRegex(ip, _, user, dateTime, query, status, bytes, referer, ua)) =>
          new Stats(1, bytes.toInt)
        case _ => new Stats(1, 0)
      }
    }

    dataSet.map(line => (extractKey(line), extractStats(line)))
      .reduceByKey((a, b) => a.merge(b))
      .collect().foreach{
      case (user, query) => println("%s\t%s".format(user, query))}

    sc.stop()

    println("abcd")
    println(args)
  }

}
