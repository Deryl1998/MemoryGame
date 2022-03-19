package kamil.frac.memorygame

import android.widget.ImageView
import kotlinx.coroutines.*


class PictureTile(image: ImageView, id: Int , resource : Int) {
    private val image : ImageView = image
    private var hiddenResource : Int = R.drawable.hidden
    var imageID : Int = id
    var imageResource : Int = resource
    var isHidden = false

    fun hideImage(){
        GlobalScope.async{
            delay(600)
            image.setImageResource(hiddenResource)
            isHidden=false
        }
    }

     fun displayImage(){
         image.setImageResource(imageResource)
         isHidden = true
    }

    fun getImageSourceID():Int{
        return image.id
    }

    fun swap(item: PictureTile?){
        this.imageID = item!!.imageID.also{ item.imageID = this.imageID}
        this.imageResource = item.imageResource.also{ item.imageResource = this.imageResource}
    }

}