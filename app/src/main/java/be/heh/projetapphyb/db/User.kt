package be.heh.projetapphyb.db

class User()
{
    var userId : Int = -1
    var mail : String = "null"
    var pswd : String = "null"
    var hasPrivilege : Boolean = false
    var isAdmin : Boolean = false

    constructor(userId: Int, mail: String, pswd: String, hasPrivilege: Boolean, isAdmin: Boolean) : this()
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