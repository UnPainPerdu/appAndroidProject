package be.heh.projetapphyb.db

class Matos()
{
    var matosId : Int = -1
    var type : String = "null"
    var name : String = "null"
    var link : String = "null"
    var refNumber : String = "null"
    var isAvailable : Boolean = false

    constructor(matosId: Int, type: String, name: String, link: String, refNumber: String, isAvailable: Boolean) : this()
    {
        this.matosId = matosId
        this.type = type
        this.name = name
        this.link = link
        this.refNumber = refNumber
        this.isAvailable = isAvailable
    }
    override fun toString() : String
    {
        val sb = StringBuilder()
        sb.append("ID : " + this.matosId.toString() + "\n" +
                "Type : " + this.type + "\n" +
                "Name : " + this.name + "\n" +
                "Link : " + this.link + "\n" +
                "N° de référence : " + this.refNumber + "\n" +
                "Is available : " + this.isAvailable
        )
        return sb.toString()
    }
}