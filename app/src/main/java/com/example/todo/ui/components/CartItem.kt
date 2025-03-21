package com.example.todo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.todo.model.CartItem

//import androidx.constraintlayout.compose.ConstraintLayout
@Composable
fun CartItem(
    cartRecipe:CartItem,
    onQuantityChange: (Long) -> Unit,
    navController: NavController
    ) {
    val colorScheme = MaterialTheme.colorScheme

    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(1.dp, colorScheme.primary, shape = RoundedCornerShape(10.dp)) // 使用主题调整边框颜色
    ) {
        val (pic, titleTxt, quantityRow) = createRefs()

        // 商品图片
        Image(
            painter = rememberAsyncImagePainter(cartRecipe.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(135.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        // 商品名称
        Text(
            text = cartRecipe.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2, // 限制最多两行
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 8.dp, top = 8.dp)
                .clickable {
                    navController.navigate("recipes/${cartRecipe.recipeId}")
                },

            softWrap = true
        )

        // 计数控制区
        Row(
            modifier = Modifier
                .constrainAs(quantityRow) {
                    end.linkTo(titleTxt.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        if (cartRecipe.quantity > 0) {
                            onQuantityChange(cartRecipe.quantity - 1)
                        }                    }
                    .background(colorScheme.primary, shape = RoundedCornerShape(5.dp)) // 使用主题背景颜色
            ) {
                Text(
                    "-",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // 按钮上使用白色文本
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Text(
                text = " ${cartRecipe.quantity} ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface, // 使用主题中的文本颜色
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onQuantityChange(cartRecipe.quantity + 1)
                    }
                    .background(colorScheme.primary, shape = RoundedCornerShape(5.dp)) // 使用主题背景颜色
            ) {
                Text(
                    "+",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // 按钮上使用白色文本
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}



//@Composable
//fun CartItem(
//    cartItems: List<Recipe>,
//    ){
//    ConstraintLayout(
//        modifier = Modifier
//            .padding(vertical = 8.dp)
//            .fillMaxWidth()
//            .border(1.dp, colorResource(R.color.grey), shape = RoundedCornerShape(10.dp))
//    ){
//        val (pic,titleTxt,feeEachTime,totalEachItem,quantity)=createRefs()
//        var numberInCart by remember { mutableState0f(item.numberInCart)}
//        val decimalFormat=DecimalFormat( pattern:"#.00")
//        Image(
//            painter =rememberAsyncImagePainter(item.ImagePath),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .width(135.dp)
//                .height(100.dp)
//                .background(
//                    colorResource(),
//                    shape =  RoundedCornerShape(10.dp)
//                )
//                .clip(RoundedCornerShape(10.dp))
//                .constrainAs(pic) {
//                    start.linkTo(parent.start)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                }
//        )
//        Text(
//            text=item.Title,
//            fontWeight = FontWeight.Bold,
//            frontsize = 16.sp,
//            modifier = Modifier
//                .constrainAs(titleTxt){
//                    start.linkTo(pic.end)
//                    top.linkTo(pic.top)
//                }
//                .padding(start = 8.dp, top = 8.dp)
//        )
//        ConstraintLayout(
//            modifier = Modifier
//                .width(100.dp)
//                .padding(start = 8.dp)
//                .constrainAs(quantity){
//                    start.linkTo(titleTxt.start)
//                    bottom.linkTo(parent.bottom)
//                }
//        ){
//            val (plusCartBtn, minusCartBtn, numberItemText)= createRefs()
//            Text(text=item.numberInCart.toString(),
//                color = colorResource(R.color.darkPurple),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.constrainAs(numberItemText){
//                    end.linkTo(parent.end)
//                    start.linkTo(parent.start)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                }
//            )
//            Box(modifier = Modifien
//                .padding(2.dp)
//                .size(28.dp)
//                .constrainAs(plusCartBtn){
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)}
//                    .clickable {
//                        managmentCart.plusItem(cartItems,cartItems.index0f(item)){onItemchange()}
//                        numberInCart++
//                        item.numberInCart=numberInCart
//                    }
//            ){
//                Text(
//                    text = "+",
//                    color = colorResource(R.color.orange),
//                    fontsize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.Center),
//                    textAlign = TextAlign.Center
//                )
//            }
//            Box(modifier = Modifien
//                .padding(2.dp)
//                .size(28.dp)
//                .constrainAs(minusCartBtn){
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)}
//                .clickable {
//                    managmentCart.minusItem(cartItems,cartItems.index0f(item)){onItemchange()}
//                    numberInCart--
//                    item.numberInCart=numberInCart
//                }
//            ){
//                Text(
//                    text = "-",
//                    color = colorResource(R.color.orange),
//                    fontsize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.Center),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//        }
//    }
//}

//
//@Composable
//fun CartItem(modifier: Modifier = Modifier,) {
//    var numberInCart by remember { mutableIntStateOf(2) }
//    val item = Recipe(
//        title = "Example Dish",
//        imagePath = "https://example.com/image.jpg",
//        numberInCart = numberInCart
//    )
//
//    ConstraintLayout(
////        modifier = modifier
////            .padding(vertical = 8.dp)
////            .fillMaxWidth()
////            .border(1.dp, Color(0xFFB0B0B0),
////                shape = RoundedCornerShape(10.dp)
//    ) {
//        val (pic, titleTxt, quantity, plusBtn, minusBtn) = createRefs()
//
//        Image(
//            painter = rememberAsyncImagePainter(item.imagePath),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .width(135.dp)
//                .height(100.dp)
//                .clip(RoundedCornerShape(10.dp))
//                .constrainAs(pic) {
//                    start.linkTo(parent.start)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                }
//        )
//
//        Text(
//            text = item.title,
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp,
//            modifier = Modifier
//                .constrainAs(titleTxt) {
//                    start.linkTo(pic.end)
//                    top.linkTo(pic.top)
//                }
//                .padding(start = 8.dp, top = 8.dp)
//        )
//
//        Row(
//            modifier = Modifier
//                .constrainAs(quantity) {
//                    start.linkTo(titleTxt.start)
//                    top.linkTo(titleTxt.bottom)
//                }
//                .padding(start = 8.dp, top = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(28.dp)
//                    .clickable {
//                        if (numberInCart > 1) numberInCart--
//                    }
//            ) {
//                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFA500), textAlign = TextAlign.Center) // 橙色
//            }
//            Text(
//                text = "Quantity: $numberInCart",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color(0xFF4B0082), // 深紫色
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//            Box(
//                modifier = Modifier
//                    .size(28.dp)
//                    .clickable {
//                        numberInCart++
//                    }
//            ) {
//                Text("+",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFA500),
//                    textAlign = TextAlign.Center) // 橙色
//            }
//        }
//    }
//}
