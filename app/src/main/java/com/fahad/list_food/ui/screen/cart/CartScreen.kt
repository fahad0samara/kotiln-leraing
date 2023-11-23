package com.fahad.list_food.ui.screen.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahad.list_food.R

import com.fahad.list_food.data.local.entities.Item
import com.fahad.list_food.model.FoodViewModel
import com.fahad.list_food.ui.theme.dimens

@Composable
fun CartScreen(
  viewModel: FoodViewModel
) {
  val cartItems by viewModel.cart.collectAsState(emptyList())

  var totalPrice by remember { mutableDoubleStateOf(0.0) }
  val taxRate = 0.1 // 10% tax rate

  // Calculate the initial total price based on cart items
  if (cartItems.isNotEmpty()) {
    totalPrice = cartItems.map { it.price * it.quantity }.reduce { acc, price -> acc + price }
  }

  val taxAmount = totalPrice * taxRate
  val totalPriceWithTax = totalPrice + taxAmount

  Column(
    modifier = Modifier
      .fillMaxSize()
      .navigationBarsPadding()
      .padding(bottom = MaterialTheme.dimens.large)
  ) {
    if (cartItems.isNotEmpty()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimens.medium1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Cart", fontSize = 20.sp, fontWeight = FontWeight.Bold
        )
        Button(
          onClick = {
            viewModel.clearCart()
            totalPrice = 0.0
          },
          modifier = Modifier.padding(MaterialTheme.dimens.medium1),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red, contentColor = Color.White
          ),
        ) {
          Text(text = "Clear Cart")
        }
      }

      if (cartItems.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.weight(1f)
        ) {
          items(cartItems) { item ->
            CartItemRow(item, viewModel) { priceChange ->
              totalPrice += priceChange
            }
          }
        }
      }


      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimens.medium1)
          .shadow(4.dp, RoundedCornerShape(16.dp))
          .clip(RoundedCornerShape(16.dp))
          .background(MaterialTheme.colorScheme.surface)

      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Subtotal",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "$${totalPrice}", fontSize = 16.sp, fontWeight = FontWeight.Normal
          )
        }

        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Tax (${(taxRate * 100).toInt()}%)",
            fontSize = MaterialTheme.typography.titleMedium.fontSize

          )
          Text(
            text = "$${taxAmount}", fontSize = MaterialTheme.typography.titleMedium.fontSize
          )
        }

        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 2.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Total Price",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "$${totalPriceWithTax}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
          )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
          onClick = {
            // Implement checkout logic here
          },
          modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Green, contentColor = Color.White
          ),
        ) {
          Text(text = "Checkout")
        }
      }

    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(MaterialTheme.dimens.medium1),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.medium1),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Your cart is empty ",

            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Add some items to your cart to see them here",

            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontWeight = FontWeight.Bold
          )

        }

      }
    }
  }
}

@Composable
fun CartItemRow(
  item: Item, viewModel: FoodViewModel, onPriceChange: (Double) -> Unit
) {
  var quantity by remember { mutableIntStateOf(item.quantity) }
  var priceChange by remember { mutableDoubleStateOf(0.0) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(4.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(1.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Image(
        painter = painterResource(id = item.imageResId),
        contentDescription = null,
        modifier = Modifier
          .size(100.dp)
          .padding(8.dp),
        contentScale = ContentScale.Crop
      )

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        Text(
          text = "Price: $${item.price}",
          fontSize = 16.sp,
          color = if (isSystemInDarkTheme()) Color.Gray else Color.Black
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ActionIconButton(onClick = {
            if (quantity > 1) {
              viewModel.decrementItem(item)
              quantity--
              priceChange -= item.price
              onPriceChange(priceChange)
            }
          }, content = {
            Image(
              painter = painterResource(id = R.drawable.baseline_remove_24),

              contentDescription = "Decrement",

              )
          }, backgroundColor = Color.Red // Background color for the decrement button
          )

          Text(text = quantity.toString())

          ActionIconButton(
            onClick = {
              viewModel.incrementItem(item)
              quantity++
              priceChange += item.price
              onPriceChange(priceChange)
            },
            content = {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increment",
                tint = Color.White
              )
            },
            backgroundColor = Color.Green // Background color for the increment button
          )
        }
      }

      IconButton(onClick = {
        viewModel.deleteItem(item)
        priceChange = -item.price * quantity
        onPriceChange(priceChange)
      }, modifier = Modifier.padding(end = 8.dp, top = 8.dp), content = {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete",
          tint = Color.Red
        )
      })
    }

  }
}

@Composable
fun ActionIconButton(
  onClick: () -> Unit, content: @Composable () -> Unit, backgroundColor: Color
) {
  Box(
    modifier = Modifier
      .size(25.dp)
      .background(backgroundColor, CircleShape)
      .clickable { onClick() }, contentAlignment = Alignment.Center
  ) {
    content()
  }
}
















