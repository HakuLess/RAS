import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import model.Code
import model.Commit
import model.SubMitList

fun main(args: Array<String>) {
    println("Start RAS")
//    check("weekly-contest-203")
    check("biweekly-contest-33")
}

private fun check(context: String, region: String = "global") {
    val arr = Array<Commit?>(5000) { null }
    for (page in 1..190) {
        "https://leetcode-cn.com/contest/api/ranking/$context/?pagination=$page&region=$region".httpGet()
            .responseObject<SubMitList> { req, _, it ->
                println(req.url)
                val subs = it.get().submissions
                val ranks = it.get().total_rank
                for (i in it.get().submissions.indices) {
                    if (ranks[i].rank - 1 in arr.indices)
                        arr[ranks[i].rank - 1] = Commit(ranks[i], subs[i], HashMap())
                }
                println("size is ${arr.filterNotNull().size}")
            }.join()
        Thread.sleep(1000L)
    }

    val ans = ArrayList<Triple<Int, Int, Int>>()
    for (i in arr.indices) {
        if (arr[i] == null || arr[i]?.submits.isNullOrEmpty()) break
        arr[i]?.submits?.forEach {
            "https://leetcode-cn.com/api/submissions/${it.value.submission_id}/".httpGet()
                .responseObject<Code> { _, _, result ->
                    println("get code rank $i, problem ${it.key}")
                    for (k in 0 until i) {
                        if (arr[k]?.code?.getOrDefault(it.key, "") == result.get().code) {
                            println("copy problem is ${it.key} & code is ${result.get().code}")
                            ans.add(Triple(k, i, it.key))
                        } else {
//                            println("cmp left is ${arr[k]?.code?.getOrDefault(it.key, "")} right is ${result.get().code}")
                        }
                    }
                    arr[i]?.code?.put(it.key, result.get().code)
                }
            Thread.sleep(1000L)
        }
    }

    println("end is ${ans.size}")
    ans.forEach {
        println(
            "rank ${arr[it.first]?.rank} copy rank ${arr[it.second]?.rank} in problem ${it.third}" +
                    " with the code ${arr[it.first]?.code?.getOrDefault(it.third, "")} "
        )
    }
}