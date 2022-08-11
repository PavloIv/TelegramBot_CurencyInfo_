import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductShop {
    String basket = null;

    public ProductShop(String basket) {
        this.basket = basket;
    }

    public char[] parseBasket() {
        return basket.toCharArray();
    }

    public List<CommodityBase> commodityBase() {
        List<CommodityBase> commodityBaseList = new ArrayList<>();
        CommodityBase A = new CommodityBase('A', 1.25F, 3, 3.00F);
        CommodityBase B = new CommodityBase('B', 4.25F, null, null);
        CommodityBase C = new CommodityBase('C', 1.00F, 6, 5.00F);
        CommodityBase D = new CommodityBase('D', 0.75F, null, null);
        commodityBaseList.add(A);
        commodityBaseList.add(B);
        commodityBaseList.add(C);
        commodityBaseList.add(D);
        System.out.println(commodityBaseList);
        return commodityBaseList;
    }

    public Map<Character, Integer> numberOfProductsInBasket() {
        Map<Character, Integer> numberOfProductsInBasket = new HashMap<>();
        for (CommodityBase productInBase : commodityBase()) {
            int numberOfProducts = 0;
            for (char productInBasket : parseBasket()) {
                if (productInBase.getProductName() == productInBasket) {
                    numberOfProducts++;
                }
            }
            numberOfProductsInBasket.put(productInBase.getProductName(), numberOfProducts);
        }
        return numberOfProductsInBasket;
    }

    public float sumBasketPrice() {
        float sumBasketPrice = 0;
        for (CommodityBase productInBase :
                commodityBase()) {
            Character productName = productInBase.getProductName();
            Integer numberForPromotions = productInBase.getNumberForPromotions();
            Float price = productInBase.getPrice();
            Float promotionsPrice = productInBase.getPromotionalPrice();
            if (promotionsPrice == null){ promotionsPrice = Float.valueOf(0);}
            sumBasketPrice += numberProductWithoutPromotions(productName,numberForPromotions)*price +
            numberPromotionAssortment(productName,numberForPromotions)*promotionsPrice;
        }
        return sumBasketPrice;
    }
    public int numberProductWithoutPromotions(Character productName,Integer numberForPromotions){
        if (numberForPromotions == null){
            return numberOfProductsInBasket().get(productName);
        }
        int numberPromotionsProduct = numberOfProductsInBasket().get(productName)%numberForPromotions;
        return numberPromotionsProduct;
    }
    public int numberPromotionAssortment(Character productName,Integer numberForPromotions){
        if (numberForPromotions == null || numberForPromotions > numberOfProductsInBasket().get(productName)){
            return 0;
        }
        int numberPromotionAssortment =
                (numberOfProductsInBasket().get(productName)-numberProductWithoutPromotions(productName,numberForPromotions))
                        /numberForPromotions;
        return numberPromotionAssortment;
    }
}
