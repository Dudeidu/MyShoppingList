package com.example.shoppinglist

import android.util.Log
import java.util.Locale
import kotlin.math.min

data class Icon(
    val name: String,
    val tags: List<String>,
    val resource: Int
)

class IconSearcher {
    private val icons = listOf(
        Icon("almond_milk", listOf("almond milk", "חלב שקדים"), R.drawable.icon_almond_milk),
        Icon("almonds", listOf("almonds", "almond", "שקד", "שקדים"), R.drawable.icon_almonds),
        Icon("aluminum_foil", listOf("aluminum foil", "aluminum", "foil", "אלומיניום"), R.drawable.icon_aluminum_foil),
        Icon("apple_sauce", listOf("applesauce", "רסק תפוחים"), R.drawable.icon_apple_sauce),
        Icon("apple", listOf("apple", "apples", "תפוח", "תפוחים"), R.drawable.icon_apple),
        Icon("apricot", listOf("apricot", "apricots", "משמש"), R.drawable.icon_apricot),
        Icon("arugula", listOf("arugula", "רוקט", "ארוגולה"), R.drawable.icon_arugula),
        Icon("asparagus", listOf("asparagus", "אספרגוס"), R.drawable.icon_asparagus),
        Icon("avocado", listOf("avocado", "avocados", "אבוקדו"), R.drawable.icon_avocado),
        Icon("baby_food", listOf("baby food", "אוכל לתינוקות", "גרבר"), R.drawable.icon_baby_food),
        Icon("baby_formula", listOf("baby formula", "formula", "פורמולה", "מטרנה", "נוטרילון", "סימילאק"), R.drawable.icon_baby_formula),
        Icon("baby_wipes", listOf("baby wipes", "wipes", "wet wipes", "מגבון", "מגבונים", "מגבונים לחים"), R.drawable.icon_baby_wipes),
        Icon("bacon", listOf("bacon", "בייקון"), R.drawable.icon_bacon),
        Icon("bagel", listOf("bagel", "bagels", "בייגלה"), R.drawable.icon_bagel),
        Icon("baking_soda", listOf("baking soda", "סודה לשתייה"), R.drawable.icon_baking_soda),
        Icon("banana", listOf("banana", "bananas", "בננה", "בננות"), R.drawable.icon_banana),
        Icon("band_aids", listOf("band aids", "first aid", "bandage", "bandages", "פלסטרים", "תחבושת", "תחבושות", "עזרה ראשונה"), R.drawable.icon_band_aids),
        Icon("basil", listOf("basil", "בזיליקום"), R.drawable.icon_basil),
        Icon("battery", listOf("battery", "batteries", "סוללה", "סוללות", "בטרייה", "בטריות"), R.drawable.icon_battery),
        Icon("bbq_sauce", listOf("bbq sauce", "barbeque", "bbq", "barbeque sauce", "רוטב ברביקיו"), R.drawable.icon_bbq_sauce),
        Icon("bean_sprouts", listOf("bean sprouts", "sprouts", "נבטים", "נבטי שעועית"), R.drawable.icon_bean_sprouts),
        Icon("beans", listOf("beans", "שעועית"), R.drawable.icon_beans),
        Icon("beer_bottle", listOf("beer bottle", "beer", "alcohol", "vodka", "whiskey", "בירה", "אלכוהול", "וודקה", "ערק", "ויסקי", "שנדי"), R.drawable.icon_beer_bottle),
        Icon("beer_can", listOf("beer can", "beer"), R.drawable.icon_beer_can),
        Icon("beet", listOf("beet", "beets", "סלק"), R.drawable.icon_beet),
        Icon("bell_pepper_green", listOf("green bell pepper", "pepper", "bell pepper", "פלפל ירוק", "גמבה", "פלפל"), R.drawable.icon_bell_pepper_green),
        Icon("birthday_cake", listOf("birthday cake", "cake", "עוגה", "עוגות"), R.drawable.icon_birthday_cake),
        Icon("bleach", listOf("bleach", "cleaning", "אקונומיקה", "מלבין", "מסיר כתמים"), R.drawable.icon_bleach),
        Icon("blueberry", listOf("blueberry", "blueberries", "אוכמניות"), R.drawable.icon_blueberry),
        Icon("bourekas", listOf("bourekas", "בורקס"), R.drawable.icon_bourekas),
        Icon("bread", listOf("bread", "baguette", "לחם", "בגט", "גבטה", "ג'בטה"), R.drawable.icon_bread),
        Icon("bread_crumbs", listOf("bread crumbs", "crumbs", "פירורי לחם", "פנקו"), R.drawable.icon_bread_crumbs),
        Icon("broccoli", listOf("broccoli", "ברוקולי"), R.drawable.icon_broccoli),
        Icon("broth", listOf("broth", "soup", "stock", "מרק", "אבקת מרק"), R.drawable.icon_broth),
        Icon("bubblegum", listOf("bubblegum", "gum", "chewing gum", "מסטיק", "גומי לעיסה", "בזוקה"), R.drawable.icon_bubblegum),
        Icon("buckwheat", listOf("buckwheat", "כוסמת"), R.drawable.icon_buckwheat),
        Icon("buns", listOf("buns", "hamburger bun", "bun", "burger bun", "לחמניה", "לחמנייה", "לחמניות", "באנים"), R.drawable.icon_buns),
        Icon("burger_patty", listOf("burger patty", "patty", "hamburger patty", "קציצת", "קציצות", "קציצה"), R.drawable.icon_burger_patty),
        Icon("butter", listOf("butter", "חמאה", "מחמאה", "מרגרינה"), R.drawable.icon_butter),
        Icon("cabbage", listOf("cabbage", "כרוב"), R.drawable.icon_cabbage),
        Icon("candy", listOf("candy", "סוכריות", "ממתקים"), R.drawable.icon_candy),
        Icon("canned_tomatoes", listOf("canned tomatoes", "רוטב עגבניות", "רסק עגבניות"), R.drawable.icon_canned_tomatoes),
        Icon("canned_tuna", listOf("canned tuna", "tuna", "tuna can", "טונה"), R.drawable.icon_canned_tuna),
        Icon("carrot", listOf("carrot", "carrots", "גזר", "גזרים"), R.drawable.icon_carrot),
        Icon("cashew", listOf("cashew", "cashews", "קשיו"), R.drawable.icon_cashew),
        Icon("cat_food", listOf("cat food", "pet food", "אוכל לחתול", "חתול", "מזון לחתולים"), R.drawable.icon_cat_food),
        Icon("cat_litter", listOf("cat litter", "pet supplies", "חול לחתול"), R.drawable.icon_cat_litter),
        Icon("cauliflower", listOf("cauliflower", "cauliflowers", "כרובית"), R.drawable.icon_cauliflower),
        Icon("celery", listOf("celery", "סלרי"), R.drawable.icon_celery),
        Icon("cereal", listOf("cereal", "cornflakes", "קורנפלס", "דגני בוקר"), R.drawable.icon_cereal),
        Icon("charcoals", listOf("charcoals", "coal", "פחמים", "פחם"), R.drawable.icon_charcoals),
        Icon("cheese", listOf("cheese", "גבינה", "גבינת"), R.drawable.icon_cheese),
        Icon("cherry", listOf("cherry", "cherries", "דובדנים", "דובדנן"), R.drawable.icon_cherry),
        Icon("cherry_tomato", listOf("cherry tomato", "שרי", "עגבניות שרי"), R.drawable.icon_cherry_tomato),
        Icon("chicken", listOf("whole chicken", "עוף שלם"), R.drawable.icon_chicken),
        Icon("chicken_breast", listOf("chicken breast", "schnitzel", "שניצל", "חזה עוף"), R.drawable.icon_chicken_breast),
        Icon("chicken_thigh", listOf("chicken thigh", "thighs", "שוקיים"), R.drawable.icon_chicken_thigh),
        Icon("chicken_wings", listOf("chicken wings", "כנפיים", "כנפי עוף"), R.drawable.icon_chicken_wings),
        Icon("chili_pepper", listOf("chili pepper", "hot peppers", "צילי", "צ'ילי", "פלפל חריף", "פלפל צ'ילי"), R.drawable.icon_chili_pepper),
        Icon("chips", listOf("chips", "potato chips", "crisps", "ציפס", "תפוציפס", "צ'יפס", "תפוצ'יפס", "פרינגלס"), R.drawable.icon_chips),
        Icon("chocolate", listOf("chocolate", "candy bar", "שוקולד", "מקופלת", "כיף כף", "נוטלה"), R.drawable.icon_chocolate),
        Icon("chocolate_milk", listOf("chocolate milk", "שוקו"), R.drawable.icon_chocolate_milk),
        Icon("cigarettes", listOf("cigarettes", "cigs", "smokes", "סיגריות"), R.drawable.icon_cigarettes),
        Icon("cinnamon", listOf("cinnamon", "קינמון"), R.drawable.icon_cinnamon),
        Icon("cinnamon_roll", listOf("cinnamon roll"), R.drawable.icon_cinnamon_roll),
        Icon("cleaner", listOf("cleaner", "חומר ניקוי"), R.drawable.icon_cleaner),
        Icon("cleaning_supplies", listOf("cleaning supplies", "ניקיון", "ניקוי", "כביסה"), R.drawable.icon_cleaning_supplies),
        Icon("coconut_milk", listOf("coconut milk", "חלב קוקוס"), R.drawable.icon_coconut_milk),
        Icon("coconut", listOf("coconut", "קוקוס"), R.drawable.icon_coconut),
        Icon("coffee", listOf("coffee", "קפה"), R.drawable.icon_coffee),
        Icon("coffee_capsules", listOf("capsules", "espresso", "קפסולות", "אספרסו", "נספרסו"), R.drawable.icon_coffee_capsules),
        Icon("coke", listOf("coke", "cola", "soda", "קולה", "זירו"), R.drawable.icon_coke),
        Icon("conditioner", listOf("conditioner", "קונדישינר", "מרכך"), R.drawable.icon_conditioner),
        Icon("cookies", listOf("cookies", "עוגיות", "עוגייה"), R.drawable.icon_cookies),
        Icon("coriander", listOf("coriander", "כוסברה"), R.drawable.icon_coriander),
        Icon("corn", listOf("corn", "תירס"), R.drawable.icon_corn),
        Icon("cottage_cheese", listOf("cottage cheese", "cottage", "קוטג", "קוטג'"), R.drawable.icon_cottage_cheese),
        Icon("couscous", listOf("couscous", "קוסקוס", "פתיתיס"), R.drawable.icon_couscous),
        Icon("crab", listOf("crab", "סרטן"), R.drawable.icon_crab),
        Icon("crackers", listOf("crackers", "קרקר", "קרקרים", "פריכיות"), R.drawable.icon_crackers),
        Icon("cream_cheese", listOf("cream cheese", "גבינה לבנה", "שמנת", "סקי"), R.drawable.icon_cream_cheese),
        Icon("cucumber", listOf("cucumber", "cucumbers", "מלפפון", "מלפפונים"), R.drawable.icon_cucumber),
        Icon("cumin", listOf("cumin", "כמון"), R.drawable.icon_cumin),
        Icon("cupcake", listOf("cupcake", "מאפין", "מאפינס"), R.drawable.icon_cupcake),
        Icon("cutlery", listOf("cutlery", "dishware", "סכום", "סכו\"ם", "חד פעמי", "מזלג" ,"סכין"), R.drawable.icon_cutlery),
        Icon("deodorant", listOf("deodorant", "axe", "דאודורנט", "דורדורנט", "אקס"), R.drawable.icon_deodorant),
        Icon("diapers", listOf("diapers", "חיתולים", "טיטולים"), R.drawable.icon_diapers),
        Icon("dill", listOf("dill", "שמיר"), R.drawable.icon_dill),
        Icon("dim_sum", listOf("dim sum", "dumplings", "כופתאות", "דים סאם"), R.drawable.icon_dim_sum),
        Icon("dish_soap", listOf("dish soap", "סבון כלים", "נוזל כלים"), R.drawable.icon_dish_soap),
        Icon("dog_food", listOf("dog food", "pet food", "כלבים", "אוכל לכלבים", "בונזו"), R.drawable.icon_dog_food),
        Icon("dog_treat", listOf("dog treat", "pet food", "treats", "עצם"), R.drawable.icon_dog_treat),
        Icon("dough", listOf("dough", "בצק", "בצק פילו", "בצק פריך"), R.drawable.icon_dough),
        Icon("doughnut", listOf("doughnut", "סופגניות", "דונאטס"), R.drawable.icon_doughnut),
        Icon("dragon_fruit", listOf("dragon", "דרקון", "פיטאיה"), R.drawable.icon_dragon_fruit),
        Icon("dried_fruits", listOf("dried fruits", "raisins", "פירות יבשים", "מיובש", "תמרים", "תאנים"), R.drawable.icon_dried_fruits),
        Icon("durian", listOf("durian", "דוריאן"), R.drawable.icon_durian),
        Icon("eggplant", listOf("eggplant", "aubergine", "חציל", "חצילים"), R.drawable.icon_eggplant),
        Icon("eggs", listOf("eggs", "ביצה", "ביצים"), R.drawable.icon_eggs),
        Icon("energy_drink", listOf("energy drink", "sports drink", "משקה אנרגיה", "אקסל"), R.drawable.icon_energy_drink),
        Icon("fish_food", listOf("fish food", "pet food", "אוכל לדגים"), R.drawable.icon_fish_food),
        Icon("fish", listOf("fish", "seafood", "דג", "לוקוס", "אמנון", "דניס", "נילוס", "קרפיון", "דגים"), R.drawable.icon_fish),
        Icon("flour", listOf("flour", "קמח"), R.drawable.icon_flour),
        Icon("french_fries", listOf("french fries", "fries", "ציפס", "צ'יפס"), R.drawable.icon_french_fries),
        Icon("frozen_food", listOf("frozen", "קפוא"), R.drawable.icon_frozen_food),
        Icon("fruit_pudding", listOf("pudding", "פודינג", "מילקי", "מעדן"), R.drawable.icon_fruit_pudding),
        Icon("garlic", listOf("garlic", "שום"), R.drawable.icon_garlic),
        Icon("granola_bar", listOf("granola bar", "energy bar", "חטיף אנרגיה", "גרנולה"), R.drawable.icon_granola_bar),
        Icon("grapefruit", listOf("grapefruit", "אשכולית", "אשכוליות"), R.drawable.icon_grapefruit),
        Icon("grapes", listOf("grapes", "ענבים"), R.drawable.icon_grapes),
        Icon("grapes_green", listOf("green grapes", "ענבים ירוקים"), R.drawable.icon_grapes_green),
        Icon("green_beans", listOf("green beans", "שעועית ירוקה"), R.drawable.icon_green_beans),
        Icon("green_chili_pepper", listOf("green chili", "chili", "פלפל חריף", "צ'ילי"), R.drawable.icon_green_chili_pepper),
        Icon("green_onion", listOf("green onion", "בצל ירוק", "עירית"), R.drawable.icon_green_onion),
        Icon("ground_beef", listOf("ground beef", "בקר טחון"), R.drawable.icon_ground_beef),
        Icon("halva", listOf("halva", "חלווה"), R.drawable.icon_halva),
        Icon("ham", listOf("ham", "pork", "חזיר"), R.drawable.icon_ham),
        Icon("hand_sanitizer", listOf("hand sanitizer", "sanitizer", "אלקוג'ל", "מחטא ידיים"), R.drawable.icon_hand_sanitizer),
        Icon("hazelnut", listOf("hazelnut", "nuts", "אגוז לוז", "אגוזי לוז"), R.drawable.icon_hazelnut),
        Icon("herbs", listOf("herbs", "עשבי תיבול"), R.drawable.icon_herbs),
        Icon("honey", listOf("honey", "דבש"), R.drawable.icon_honey),
        Icon("hops", listOf("hops", "כשות"), R.drawable.icon_hops),
        Icon("hot_dog", listOf("hotdog", "נקניקייה", "קבנוס", "נקניקיות"), R.drawable.icon_hot_dog),
        Icon("hot_sauce", listOf("hot sauce", "sauce", "hotsauce", "tabasco", "sriracha", "רוטב חריף", "סרירצ'ה", "טבסקו"), R.drawable.icon_hot_sauce),
        Icon("hummus", listOf("hummus", "חומוס"), R.drawable.icon_hummus),
        Icon("ice_cream_bar", listOf("popsicle", "ארטיק", "קרטיב", "מגנום"), R.drawable.icon_ice_cream_bar),
        Icon("ice_cream_cup", listOf("ice cream", "icecream", "גלידה", "קרמיסימו"), R.drawable.icon_ice_cream_cup),
        Icon("ice_cubes", listOf("ice cubes", "ice", "קרח", "קוביות קרח"), R.drawable.icon_ice_cubes),
        Icon("instant_noodles", listOf("instant noodles", "ramen", "ראמן"), R.drawable.icon_instant_noodles),
        Icon("jam", listOf("jam", "spread", "ריבה"), R.drawable.icon_jam),
        Icon("kebab", listOf("kebab", "קבב", "קבבים"), R.drawable.icon_kebab),
        Icon("ketchup", listOf("ketchup", "tomato sauce", "קטשופ"), R.drawable.icon_ketchup),
        Icon("kiwi", listOf("kiwi", "קיווי"), R.drawable.icon_kiwi),
        Icon("kohlrabi", listOf("kohlrabi", "קולורבי"), R.drawable.icon_kohlrabi),
        Icon("laundry_detergent", listOf("laundry detergent", "laundry", "detergent", "נוזל כביסה", "מרכך כביסה", "כביסה"), R.drawable.icon_laundry_detergent),
        Icon("leek", listOf("leek", "כרישה"), R.drawable.icon_leek),
        Icon("lemon", listOf("lemon", "lemons", "לימון", "לימונים"), R.drawable.icon_lemon),
        Icon("lemonglass", listOf("lemongrass", "למונגראס", "לימונית"), R.drawable.icon_lemonglass),
        Icon("lettuce", listOf("lettuce", "חסה", "עלי בייבי"), R.drawable.icon_lettuce),
        Icon("light_bulb", listOf("light bulb", "bulb", "נורה", "מנורה"), R.drawable.icon_light_bulb),
        Icon("lime", listOf("lime", "limes", "ליים"), R.drawable.icon_lime),
        Icon("liquid_soap", listOf("liquid soap", "soap", "סבון נוזלי", "סבון"), R.drawable.icon_liquid_soap),
        Icon("lotion", listOf("lotion", "body cream", "קרם גוף", "קרם פנים"), R.drawable.icon_lotion),
        Icon("macaroni", listOf("macaroni", "pasta", "פסטה", "רביולי"), R.drawable.icon_macaroni),
        Icon("mango", listOf("mango", "mangos", "מנגו"), R.drawable.icon_mango),
        Icon("mangold", listOf("mangold", "מנגולד"), R.drawable.icon_mangold),
        Icon("matches", listOf("matches", "גפרורים", "גפרור", "מדליק פחמים"), R.drawable.icon_matches),
        Icon("mayo", listOf("mayo", "mayonnaise", "מיונז"), R.drawable.icon_mayo),
        Icon("melon", listOf("melon", "melons", "מלון"), R.drawable.icon_melon),
        Icon("mesclun", listOf("mesclun", "salad greens", "עלי בייבי"), R.drawable.icon_mesclun),
        Icon("milk", listOf("milk", "חלב"), R.drawable.icon_milk),
        Icon("mint", listOf("mint", "מנטה"), R.drawable.icon_mint),
        Icon("mushroom", listOf("mushroom", "mushrooms", "portabello", "champignon", "פטריות" ,"פורטבלו", "שמפיניון"), R.drawable.icon_mushroom),
        Icon("mustard", listOf("mustard", "חרדל"), R.drawable.icon_mustard),
        Icon("nachos", listOf("nachos", "doritos", "נאצ'וס", "נאצוס", "דוריטוס"), R.drawable.icon_nachos),
        Icon("nectarine", listOf("nectarine", "נקטרינה", "נקטרינות"), R.drawable.icon_nectarine),
        Icon("noodles", listOf("noodles", "spaghetti", "ספגטי", "נודלס"), R.drawable.icon_noodles),
        Icon("oat_milk", listOf("oatmilk", "oat milk", "חלב שיבולת שועל"), R.drawable.icon_oat_milk),
        Icon("oats", listOf("oats", "oatmeal", "שיבולת שועל", "קוואקר" ,"קווקר"), R.drawable.icon_oats),
        Icon("octopus", listOf("octopus", "תמנון"), R.drawable.icon_octopus),
        Icon("olive", listOf("olive", "olives", "זיתים"), R.drawable.icon_olive),
        Icon("olive_oil", listOf("olive oil", "oil", "שמן זית"), R.drawable.icon_olive_oil),
        Icon("orange", listOf("orange", "oranges", "תפוז", "תפוזים"), R.drawable.icon_orange),
        Icon("oregano", listOf("oregano", "אורגנו"), R.drawable.icon_oregano),
        Icon("pacifier", listOf("pacifier", "מוצץ", "מוצצים"), R.drawable.icon_pacifier),
        Icon("pancake", listOf("pancake", "pancakes", "פנקייק"), R.drawable.icon_pancake),
        Icon("paper_cup", listOf("paper cup", "cup", "כוסות נייר", "כוסות קלקר"), R.drawable.icon_paper_cup),
        Icon("paper_towel", listOf("paper towel", "towels", "נייר סופג", "נייר מגבת"), R.drawable.icon_paper_towel),
        Icon("parsley", listOf("parsley", "פטרוזיליה"), R.drawable.icon_parsley),
        Icon("peach", listOf("peach", "peaches", "אפרסק", "אפרקסים"), R.drawable.icon_peach),
        Icon("peanut_butter", listOf("peanut butter", "skippy", "jif", "חמאת בוטנים", "סקיפי"), R.drawable.icon_peanut_butter),
        Icon("peanut_snack", listOf("peanut snack", "bamba", "במבה", "חטיף בוטנים"), R.drawable.icon_peanut_snack),
        Icon("peanuts", listOf("peanuts", "בוטנים"), R.drawable.icon_peanuts),
        Icon("pear", listOf("pear", "pears", "אגס", "אגסים"), R.drawable.icon_pear),
        Icon("peas", listOf("peas", "אפונה"), R.drawable.icon_peas),
        Icon("pecan", listOf("pecan", "pecans", "פקאן", "פקאנים"), R.drawable.icon_pecan),
        Icon("pepper_shaker", listOf("black pepper", "פלפל שחור"), R.drawable.icon_pepper_shaker),
        Icon("peppermint", listOf("peppermint", "spearmint", "נענע"), R.drawable.icon_peppermint),
        Icon("pepsi", listOf("pepsi", "soda", "פפסי", "סודה"), R.drawable.icon_pepsi),
        Icon("pet_shampoo", listOf("pet shampoo", "שמפו לחיות"), R.drawable.icon_pet_shampoo),
        Icon("pet_toys", listOf("pet toys", "צעצועים לחיות"), R.drawable.icon_pet_toys),
        Icon("pickles", listOf("pickles", "מלפפון חמוץ", "מלפפונים חמוצים", "חמוצים"), R.drawable.icon_pickles),
        Icon("pie", listOf("pie", "פאי"), R.drawable.icon_pie),
        Icon("pineapple", listOf("pineapple", "אננס", "אננסים"), R.drawable.icon_pineapple),
        Icon("pistachio", listOf("pistachio", "פיסטוק", "פיסטוקים"), R.drawable.icon_pistachio),
        Icon("pizza", listOf("pizza", "פיצה", "פיצות"), R.drawable.icon_pizza),
        Icon("plastic_bag", listOf("plastic bag", "שקיות ניילון", "שקיות סנדוויץ"), R.drawable.icon_plastic_bag),
        Icon("plastic_cups", listOf("plastic cups", "כוסות חד פעמיים", "כוסות פלסטיק"), R.drawable.icon_plastic_cups),
        Icon("plastic_wrap", listOf("plastic wrap", "ניילון נצמד"), R.drawable.icon_plastic_wrap),
        Icon("plates", listOf("plates", "צלחות"), R.drawable.icon_plates),
        Icon("plum", listOf("plum", "plums", "שזיף", "שזיפים"), R.drawable.icon_plum),
        Icon("pomegranate", listOf("pomegranate", "רימון", "רימונים"), R.drawable.icon_pomegranate),
        Icon("popcorn", listOf("popcorn", "פופקורן"), R.drawable.icon_popcorn),
        Icon("potato", listOf("potato", "potatoes", "תפוח אדמה", "תפוחי אדמה", "תפו\"א"), R.drawable.icon_potato),
        Icon("pretzel", listOf("pretzel", "pretzels", "פרצל", "בייגלה"), R.drawable.icon_pretzel),
        Icon("pumpkin", listOf("pumpkin", "דלעת"), R.drawable.icon_pumpkin),
        Icon("quinoa", listOf("quinoa", "קינואה"), R.drawable.icon_quinoa),
        Icon("rack_of_lamb", listOf("lamb", "כבש", "טלה"), R.drawable.icon_rack_of_lamb),
        Icon("radish", listOf("radish", "radishes", "צנון", "צנונית", "צנוניות"), R.drawable.icon_radish),
        Icon("raspberry", listOf("raspberry", "raspberries", "פטל"), R.drawable.icon_raspberry),
        Icon("razor_blade", listOf("razor blade", "shaving", "סכין גילוח", "גילוח", "סכיני גילוח"), R.drawable.icon_razor_blade),
        Icon("red_bell_pepper", listOf("red bell pepper", "bell pepper", "bellpepper", "פלפל", "גמבה", "פלפל אדום", "פלפלים"), R.drawable.icon_red_bell_pepper),
        Icon("red_cabbage", listOf("red cabbage", "כרוב אדום"), R.drawable.icon_red_cabbage),
        Icon("red_onion", listOf("red onion", "red onions", "בצל אדום", "בצלים אדומים"), R.drawable.icon_red_onion),
        Icon("rice_bowl", listOf("rice", "אורז", "אורז פרסי", "אורז בסמטי"), R.drawable.icon_rice_bowl),
        Icon("rubber_bands", listOf("rubber bands", "גומיות"), R.drawable.icon_rubber_bands),
        Icon("sack_of_flour", listOf("flour", "קמח"), R.drawable.icon_sack_of_flour),
        Icon("salami", listOf("salami", "prosciutto", "deli meat", "pastrami", "נקניק", "נקניקים", "פסטרמה", "סלמי"), R.drawable.icon_salami),
        Icon("salmon", listOf("salmon", "דג סלמון", "סלמון"), R.drawable.icon_salmon),
        Icon("salt_shaker", listOf("salt", "מלח", "מלח גס", "מלח שולחן"), R.drawable.icon_salt_shaker),
        Icon("sanitary_pad", listOf("sanitary pad", "pad", "פדים", "תחבושות היגייניות"), R.drawable.icon_sanitary_pad),
        Icon("sardines", listOf("sardines", "סרדינים", "סרדין"), R.drawable.icon_sardines),
        Icon("sauce", listOf("sauce", "רוטב"), R.drawable.icon_sauce),
        Icon("sausage", listOf("sausage", "wieners", "kielbasa", "נקניק", "קבנוס", "צוריסו", "צ'וריסו"), R.drawable.icon_sausage),
        Icon("sesame", listOf("sesame", "שומשום"), R.drawable.icon_sesame),
        Icon("shampoo", listOf("shampoo", "hairwash", "שמפו", "ג'ל רחצה"), R.drawable.icon_shampoo),
        Icon("shrimp", listOf("shrimp", "prawns", "שרימפס", "חסילון", "חסילונים"), R.drawable.icon_shrimp),
        Icon("sliced_bread", listOf("sliced bread", "לחם פרוס", "לחם אחיד", "לחם פרוס אחיד"), R.drawable.icon_sliced_bread),
        Icon("sliced_cheese", listOf("sliced cheese", "גבינה צהובה", "גבינת עמק", "קשקבל", "גאודה", "צ'דר", "מוצרלה"), R.drawable.icon_sliced_cheese),
        Icon("snacks", listOf("snack", "חטיף"), R.drawable.icon_snacks),
        Icon("soap_bar", listOf("soap bar", "bar of soap", "סבון רחצה"), R.drawable.icon_soap_bar),
        Icon("soda", listOf("soda", "סודה"), R.drawable.icon_soda),
        Icon("soy_milk", listOf("soy milk", "חלב סויה"), R.drawable.icon_soy_milk),
        Icon("soy_sauce", listOf("soy sauce", "soy", "סויה", "רוטב סויה"), R.drawable.icon_soy_sauce),
        Icon("spices", listOf("spices", "paprika", "cardamom", "cloves", "nutmeg", "turmeric", "תבלין", "תבלינים", "פפריקה", "כורכום"), R.drawable.icon_spices),
        Icon("spinach", listOf("spinach", "תרד"), R.drawable.icon_spinach),
        Icon("sponge", listOf("sponge", "ספוג"), R.drawable.icon_sponge),
        Icon("squash", listOf("squash", "דלורית"), R.drawable.icon_squash),
        Icon("steak", listOf("steak", "meat", "ribeye", "steaks", "beef", "סטייק", "סינטה", "אנטריקוט", "בקר"), R.drawable.icon_steak),
        Icon("strawberries", listOf("strawberries", "strawberry", "תות", "תותים"), R.drawable.icon_strawberry),
        Icon("sugar", listOf("sugar", "sweetener", "סוכר", "סוכרזית", "ממתיק"), R.drawable.icon_sugar),
        Icon("sugar_cubes", listOf("sugar cubes", "קוביות סוכר"), R.drawable.icon_sugar_cube),
        Icon("sunscreen", listOf("sunscreen", "spf", "sun screen", "קרם הגנה", "שמש"), R.drawable.icon_sunscreen),
        Icon("sushi", listOf("sushi", "סושי"), R.drawable.icon_sushi),
        Icon("sweet_potato", listOf("sweet potato", "yams", "בטטה", "בטטות"), R.drawable.icon_sweet_potato),
        Icon("taco", listOf("taco", "tacos", "טאקו"), R.drawable.icon_taco),
        Icon("tahini", listOf("tahini", "טחינה"), R.drawable.icon_tahini),
        Icon("tampon", listOf("tampons", "tampon", "טמפון", "טמפונים"), R.drawable.icon_tampon),
        Icon("tea", listOf("tea", "tea bags", "תה", "ויסוצקי", "יסמין"), R.drawable.icon_tea),
        Icon("thyme", listOf("thyme", "טימין", "קורנית"), R.drawable.icon_thyme),
        Icon("tin_can", listOf("canned", "שימורים"), R.drawable.icon_tin_can),
        Icon("tissues", listOf("tissues", "tissue", "cleanex", "טישו", "קלינקס"), R.drawable.icon_tissues),
        Icon("toilet_paper", listOf("toilet paper", "tp", "נייר טואלט"), R.drawable.icon_toilet_paper),
        Icon("tomato", listOf("tomato", "tomatoes", "tomatos", "עגבנייה", "עגבניות"), R.drawable.icon_tomato),
        Icon("toothpaste", listOf("toothpaste", "משחת שיניים"), R.drawable.icon_toothpaste),
        Icon("toothpicks", listOf("toothpicks", "קיסמים", "קיסמי שיניים"), R.drawable.icon_toothpicks),
        Icon("tortilla", listOf("tortilla", "tortillas", "טורטייה", "טורטיות"), R.drawable.icon_tortilla),
        Icon("trash_bag", listOf("trash", "trash bag", "garbage", "garbage bag", "שקיות אשפה", "אשפה", "שקיות זבל"), R.drawable.icon_trash_bag),
        Icon("turkey", listOf("turkey", "הודו"), R.drawable.icon_turkey),
        Icon("vanilla", listOf("vanilla", "וניל", "תמצית וניל"), R.drawable.icon_vanilla),
        Icon("vegetable_oil", listOf("oil", "vegetable oil", "cooking oil", "canola oil", "שמן", "שמן קנולה", "שמן לטיגון"), R.drawable.icon_vegetable_oil),
        Icon("vinegar", listOf("vinegar", "balsamic", "חומץ", "בלסמי", "חומץ אורז", "חומץ תפוחים", "חומץ בלסמי"), R.drawable.icon_vinegar),
        Icon("walnut", listOf("walnut", "walnuts", "אגוז מלך", "אגוזי מלך"), R.drawable.icon_walnut),
        Icon("water_bottle", listOf("water", "water bottle", "bottle of water","מים", "בקבוק מים", "נביעות", "מי עדן"), R.drawable.icon_bottle_of_water),
        Icon("watermelon", listOf("watermelon", "אבטיח", "אבטיחים"), R.drawable.icon_watermelon),
        Icon("whipped_cream", listOf("whipped cream", "cool whip", "קצפת", "שמנת להקצפה"), R.drawable.icon_whipped_cream),
        Icon("whole_grain_bread", listOf("whole bread", "whole grain", "whole grain bread", "לחם מלא", "לחם קל", "חיטה מלאה"), R.drawable.icon_whole_grain_bread),
        Icon("wine_bottle", listOf("wine bottle", "wine", "champagne", "בקבוק יין", "יין", "שמפנייה"), R.drawable.icon_wine_bottle),
        Icon("yeast", listOf("yeast", "שמרים", "שמרים יבשים"), R.drawable.icon_yeast),
        Icon("yellow_bellpepper", listOf("yellow bell pepper", "yellow bellpepper", "פלפל צהוב", "פלפלים צהובים"), R.drawable.icon_yellow_bell_pepper),
        Icon("yellow_onion", listOf("yellow onion", "yellow onions", "בצל", "בצלים", "בצל לבן"), R.drawable.icon_yellow_onion),
        Icon("yogurt", listOf("yogurt", "יוגורט", "יופלה", "יוגורט יווני"), R.drawable.icon_yogurt),
        Icon("ziplock_bag", listOf("ziplock", "zip lock", "שקית זיפר", "שקיות זיפר", "זיפלוק"), R.drawable.icon_ziplock_bag),
        Icon("zucchini", listOf("zucchini", "זוקיני", "קישוא", "קישואים"), R.drawable.icon_zucchini),
    )
    private val scoreThreshold = 70

    fun makeWordList() : List<String> {
        val wordSet: MutableSet<String> = mutableSetOf()
        for (icon: Icon in icons) {
            wordSet.addAll(icon.tags)
        }

        // Create a temporary set to hold the modified words
        val updatedSet = wordSet.map { word ->
            word.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
        }.toSet()

        // Clear the original set and add all updated words
        wordSet.clear()
        wordSet.addAll(updatedSet)

        return wordSet.sorted()
    }

    // Function to calculate relevance score
    fun calculateRelevanceScore(query: String, tags: List<String>): Int {
        // Convert query to lowercase for case-insensitive comparison
        val queryLower = query.lowercase()

        var highestScore = 0

        for (tag in tags) {
            // Convert tag to lowercase for case-insensitive comparison
            val tagLower = tag.lowercase()

            // Check for exact match
            if (queryLower == tagLower) {
                return 100
            }

            // Calculate matching characters for partial matches
            val queryChars = queryLower.toSet()
            val tagChars = tagLower.toSet()
            val matchingChars = queryChars.intersect(tagChars).size
            val totalQueryChars = queryChars.size

            // Base score for partial matches
            val baseScore =
                if (tagLower.contains(queryLower)) 50
                else if (queryLower.contains(tagLower)) 30
                else 0

            // Calculate additional score based on the number of matching characters
            val additionalScore = min(1.0, matchingChars.toDouble() / tagChars.size) * 50

            val score = (baseScore + additionalScore).toInt()
            if (score > highestScore) {
                highestScore = score
            }
        }

        return highestScore
    }

    fun findIconByQuery(query: String): Int {
        var bestMatch: Int = R.drawable.icon_food
        var highestScore = 0

        for (item in icons) {
            val score = calculateRelevanceScore(query, item.tags)
            if (score == 100) {
                Log.d("ITEM", score.toString())
                return item.resource
            } else if (score > scoreThreshold && score > highestScore) {
                highestScore = score
                bestMatch = item.resource
            }
        }
        Log.d("ITEM", highestScore.toString())
        return bestMatch
    }
}