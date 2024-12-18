package be.heh.projetapphyb.util.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import be.heh.exokotlin.db.MatosRecord
import be.heh.exokotlin.db.MyDB
import be.heh.exokotlin.db.UserRecord
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.util.HashMaker

class MatosDbToolBox
{
    companion object
    {
        val defaultMatos : Matos = Matos();
        /**
         * need to be call from coroutine
         */
        fun getMatosById(context: Context, matosId: Int): Matos
        {
            Log.i("PROJETAPPHYB", "getting matos by id")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.get(matosId)
            Log.i("PROJETAPPHYB", "get matosId trigger")
            val matos = Matos(
                dbL?.matosId ?: -1,
                dbL?.type ?: "INDEFINI",
                dbL?.name ?: "INDEFINI",
                dbL?.link ?: "INDEFINI",
                dbL?.refNumber ?: "INDEFINI",
                dbL?.is_available ?: false
            )
            Log.i("PROJETAPPHYB", "matos got")
            return matos
        }

        fun getMatosByRefNumber(context: Context, refNumber: String): Matos
        {
            Log.i("PROJETAPPHYB", "getting matos by refNumber")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.get(refNumber)
            Log.i("PROJETAPPHYB", "get matosId trigger")
            val matos = Matos(
                dbL?.matosId ?: -1,
                dbL?.type ?: "INDEFINI",
                dbL?.name ?: "INDEFINI",
                dbL?.link ?: "INDEFINI",
                dbL?.refNumber ?: "INDEFINI",
                dbL?.is_available ?: false
            )
            Log.i("PROJETAPPHYB", "matos got")
            return matos
        }
        /**
         * need to be call from coroutine
         */

        fun getAllMatos(context: Context): List<Matos>
        {
            Log.i("PROJETAPPHYB", "getting all matos")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.get()
            Log.i("PROJETAPPHYB", "get all matos trigger")

            var list : ArrayList<Matos> = ArrayList()
            for (matosRecord in dbL)
            {
                var matos = Matos(
                    matosRecord?.matosId ?: -1,
                    matosRecord?.type ?: "INDEFINI",
                    matosRecord?.name ?: "INDEFINI",
                    matosRecord?.link ?: "INDEFINI",
                    matosRecord?.refNumber ?: "INDEFINI",
                    matosRecord?.is_available ?: false
                )
                list.add(matos)
            }
            Log.i("PROJETAPPHYB", "matos list got")
            return list.toList()
        }

        fun getAllMatosByType(context: Context, type : String): List<Matos>
        {
            Log.i("PROJETAPPHYB", "getting all matos by type")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.getAllType(type)
            Log.i("PROJETAPPHYB", "get all matos by type trigger")

            var list : ArrayList<Matos> = ArrayList()
            for (matosRecord in dbL)
            {
                var matos = Matos(
                    matosRecord?.matosId ?: -1,
                    matosRecord?.type ?: "INDEFINI",
                    matosRecord?.name ?: "INDEFINI",
                    matosRecord?.link ?: "INDEFINI",
                    matosRecord?.refNumber ?: "INDEFINI",
                    matosRecord?.is_available ?: false
                )
                list.add(matos)
            }
            Log.i("PROJETAPPHYB", "matos list got")
            return list.toList()
        }

        /**
         * need to be call from coroutine
         */
        fun modifieMatos(context: Context, matosTarget: Matos)
        {
            Log.i("PROJETAPPHYB", "update matos")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            dao.updateMatos(MatosRecord(matosTarget.matosId,
                matosTarget.type,
                matosTarget.name,
                matosTarget.link,
                matosTarget.refNumber,
                matosTarget.isAvailable))
        }
        fun deleteMatos(context: Context, matosTarget: Matos)
        {
            Log.i("PROJETAPPHYB", "delete matos")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            dao.deleteMatos(MatosRecord(matosTarget.matosId,
                matosTarget.type,
                matosTarget.name,
                matosTarget.link,
                matosTarget.refNumber,
                matosTarget.isAvailable))
        }
    }
}