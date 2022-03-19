package kamil.frac.memorygame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val picturesTable = Array(4){arrayOfNulls<PictureTile>(4)}
    private var gameScore = 0
    private var lastCLickedItem : PictureTile? = null
    private var countMoves = 0
    private var level = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTable()
        initialize()
    }

    private fun setTable(){
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val fruits = intArrayOf (R.drawable.banana,R.drawable.black_cherry,R.drawable.blueberry,
            R.drawable.grapefruit,R.drawable.mandarine,R.drawable.red_cherry,R.drawable.strawberry,
            R.drawable.watermelon)
        var idFruits=0
        for (j in 0..3) {
            val row = tableLayout.getChildAt(j) as TableRow
            for (i in 0..3) {
                val imageView = row.getChildAt(i) as ImageView
                picturesTable[j][i]=PictureTile(imageView,idFruits,fruits[idFruits])
                if(i%2==1) idFruits++
            }

        }
    }

    private fun initialize(){
        gameScore = 0
        lastCLickedItem = null
        countMoves = 0
        updateApproachesText()
        var maxIter = when(level){
            1 -> 1
            2 -> 10
            else->{
                100
            }
        }
        mixTable(maxIter)
        displayAll()
    }

    private fun randValue(column : Int, row:Int): PictureTile {
        val maxColIndex =  if(column == 3) 4 else column+1
        val maxRowIndex =  if(row == 3) 4 else row+1
        val randCol = if(column == 0) Random.nextInt(0,maxColIndex) else Random.nextInt(column-1,maxColIndex)
        val randRow = if(row == 0) Random.nextInt(0,maxRowIndex) else Random.nextInt(row-1,maxRowIndex)
        return picturesTable[randCol][randRow]!!
    }

    private fun mixTable(maxIteration:Int){
        for(mainLoop in 0..maxIteration){
            for(i in 0..3)
                 for(j in 0..3)
                     picturesTable[i][j]?.swap(randValue(i,j))
        }
    }

    private fun findPictureTile(wanted:Int) : PictureTile? {
        for(row in picturesTable)
            for(item in row)
                if(item!!.getImageSourceID() == wanted) return item

        return null
    }

     fun userClick(v: View){
        val item = findPictureTile(v.id)
        if(!item!!.isHidden){
            item.displayImage()
            if (lastCLickedItem != null) {
                if (item.imageID != lastCLickedItem!!.imageID) {
                    item.hideImage()
                    lastCLickedItem!!.hideImage()
                }
                else gameScore++
                lastCLickedItem = null
                countMoves++
                updateApproachesText()
            }
            else lastCLickedItem = item
        }
        if(gameScore==8) endGame()
    }


    private fun endGame(){
        if(level<1000)
            level++
        updateLevelText()
        initialize()
    }

    private fun updateApproachesText(){
        val approachesText = findViewById<TextView>(R.id.approaches)
        approachesText.text = countMoves.toString()
    }

    private fun updateLevelText(){
        val levelText = findViewById<TextView>(R.id.level)
        levelText.text = level.toString()
    }

    private fun displayAll(){
        for(row in picturesTable)
            for(image in row)
                image?.displayImage()
    }

    private fun hideAll(){
        for(row in picturesTable)
            for(image in row)
                image?.hideImage()
    }

}




