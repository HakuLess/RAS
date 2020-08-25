package model

data class SubMitList(
    val is_past: Boolean,
    val submissions: ArrayList<HashMap<Int, Submit>>,
    val total_rank: ArrayList<Rank>
)

data class Submit(val id: String, val date: Long, val submission_id: String)

data class Rank(val username: String, val rank: Int)

data class Code(val id: String, val code: String, val lang: String, val contest_submission: String)

// Custom
data class Commit(val rank: Rank, val submits: HashMap<Int, Submit>, val code: HashMap<Int, String>)