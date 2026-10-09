
package com.example.centsational.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.*


val categoryIcons: Map<String, ImageVector> = linkedMapOf(

    // Comida e compras
    "restaurant" to Lucide.Utensils,
    "fastfood" to Lucide.Sandwich,
    "local_pizza" to Lucide.Pizza,
    "local_cafe" to Lucide.Coffee,
    "local_bar" to Lucide.Beer,
    "cake" to Lucide.CakeSlice,
    "bakery_dining" to Lucide.Croissant,
    "local_grocery_store" to Lucide.ShoppingBasket,
    "shopping_cart" to Lucide.ShoppingCart,
    "shopping_bag" to Lucide.ShoppingBag,
    "store" to Lucide.Store,
    "storefront" to Lucide.Store,

    // Transportes
    "directions_car" to Lucide.CarFront,
    "directions_bus" to Lucide.Bus,
    "train" to Lucide.TrainFront,
    "flight" to Lucide.Plane,
    "local_gas_station" to Lucide.Fuel,
    "two_wheeler" to Lucide.Bike,
    "directions_bike" to Lucide.Bike,
    "local_taxi" to Lucide.Car,
    "local_parking" to Lucide.CircleParking,

    // Casa
    "home" to Lucide.House,
    "apartment" to Lucide.Building2,
    "bed" to Lucide.BedDouble,
    "chair" to Lucide.Armchair,
    "lightbulb" to Lucide.Lightbulb,
    "water_drop" to Lucide.Droplets,
    "wifi" to Lucide.Wifi,
    "phone" to Lucide.Phone,
    "build" to Lucide.Wrench,
    "handyman" to Lucide.Hammer,
    "cleaning_services" to Lucide.SprayCan,
    "local_laundry_service" to Lucide.WashingMachine,

    // Lazer
    "movie" to Lucide.Clapperboard,
    "music_note" to Lucide.Music,
    "sports_esports" to Lucide.Gamepad2,
    "sports_soccer" to Lucide.Trophy,
    "fitness_center" to Lucide.Dumbbell,
    "theaters" to Lucide.Theater,
    "celebration" to Lucide.PartyPopper,
    "park" to Lucide.Trees,
    "beach_access" to Lucide.Umbrella,
    "hotel" to Lucide.Hotel,
    "casino" to Lucide.Dices,
    "headphones" to Lucide.Headphones,
    "camera_alt" to Lucide.Camera,

    // Saúde e família
    "favorite" to Lucide.Heart,
    "local_hospital" to Lucide.Hospital,
    "medical_services" to Lucide.Stethoscope,
    "medication" to Lucide.Pill,
    "spa" to Lucide.Sprout,
    "healing" to Lucide.Bandage,
    "pets" to Lucide.PawPrint,
    "child_care" to Lucide.Baby,

    // Educação e trabalho
    "school" to Lucide.GraduationCap,
    "menu_book" to Lucide.BookOpen,
    "work" to Lucide.BriefcaseBusiness,
    "computer" to Lucide.Laptop,
    "smartphone" to Lucide.Smartphone,
    "brush" to Lucide.Paintbrush,
    "palette" to Lucide.Palette,

    // Dinheiro
    "payments" to Lucide.Wallet,
    "attach_money" to Lucide.DollarSign,
    "savings" to Lucide.PiggyBank,
    "account_balance" to Lucide.Landmark,
    "credit_card" to Lucide.CreditCard,
    "card_giftcard" to Lucide.Gift,
    "receipt" to Lucide.ReceiptText,
    "paid" to Lucide.BadgeDollarSign,
    "trending_up" to Lucide.TrendingUp,
    "subscriptions" to Lucide.Repeat,

    // Outros
    "checkroom" to Lucide.Shirt,
    "content_cut" to Lucide.Scissors,
    "local_florist" to Lucide.Flower2,
    "eco" to Lucide.Leaf,
    "volunteer_activism" to Lucide.HandHeart,
    "emoji_events" to Lucide.Trophy,
    "star" to Lucide.Star,
    "category" to Lucide.Shapes,
    "more_horiz" to Lucide.Ellipsis
)

fun iconFor(key: String): ImageVector =
    categoryIcons[key] ?: Lucide.Shapes

/*
 * Paleta mais intensa e profissional.
 * Mantém a lista para não alterar a lógica
 * que atribui cores às categorias existentes.
 */
val categoryColors: List<Long> = listOf(
    // Cores principais
    0xFF166534,
    0xFF1D4ED8,
    0xFF6D28D9,
    0xFFBE123C,
    0xFFB45309,
    0xFF0F766E,

    // Azuis
    0xFF1E40AF,
    0xFF0369A1,
    0xFF075985,
    0xFF4338CA,

    // Verdes
    0xFF15803D,
    0xFF047857,
    0xFF166534,
    0xFF3F6212,

    // Roxos e rosas
    0xFF7E22CE,
    0xFF86198F,
    0xFF9D174D,
    0xFFBE185D,

    // Vermelhos e laranjas
    0xFFB91C1C,
    0xFFC2410C,
    0xFF9A3412,
    0xFF92400E,

    // Neutros
    0xFF374151,
    0xFF475569,
    0xFF57534E,
    0xFF334155
)

@Composable
fun CategoryBadge(icon: String, colorHex: Long, modifier: Modifier = Modifier, size: Dp = 40.dp)
{
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(colorHex)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconFor(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.52f)
        )
    }
}