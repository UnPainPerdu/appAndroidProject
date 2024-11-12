package be.heh.projetapphyb.db

class User(userId: Int, mail: String, pswd: String, hasPrivilege: Boolean, isAdmin: Boolean)
{
    var userId : Int = -1
        private set
    var mail : String = "null"
        private set
    var pswd : String = "null"
        private set
    var hasPrivilege : Boolean = false
        private set
    var isAdmin : Boolean = false
        private set
    init
    {
        this.userId = userId
        this.mail = mail
        this.pswd = pswd
        this.hasPrivilege = hasPrivilege
        this.isAdmin = isAdmin
    }
    override fun toString() : String
    {
        val sb = StringBuilder()
        sb.append("ID : " + userId.toString() + "\n" +
                "Mail : " + mail + "\n" +
                "Encrypted pswd : " + pswd + "\n" +
                "Has privilege : " + hasPrivilege + "\n" +
                "Is admin : " + isAdmin
        )
        return sb.toString()
    }
}