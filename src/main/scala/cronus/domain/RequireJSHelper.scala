package cronus.domain

import com.google.inject.{Inject, Singleton}
import com.twitter.finatra.json.FinatraObjectMapper
import org.webjars.RequireJS

import scala.collection.JavaConversions.{asScalaSet, mapAsScalaMap, seqAsJavaList}

case class Module(name: String, setup: String)

@Singleton
class RequireJSHelper @Inject()(finatraObjectMapper: FinatraObjectMapper) {

  def getRequireJSModules(prefix: String, webjars: Set[String]): Seq[Module] = {
    val prefixMap: java.util.Map[String, java.lang.Boolean] = new java.util.HashMap()
    prefixMap.put(prefix, false)
    val prefixes = asScalaSet(prefixMap.entrySet()).toList
    val setups = mapAsScalaMap(RequireJS.generateSetupJson(seqAsJavaList(prefixes))).retain( (key, _) => webjars(key))
    val writer = finatraObjectMapper.objectMapper.writerWithDefaultPrettyPrinter()
    setups.map(pair => Module(pair._1, writer.writeValueAsString(pair._2))).toList
  }

}