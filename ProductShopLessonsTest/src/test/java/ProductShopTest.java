import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ProductShopTest {


    @Test
    void testParseBasketAABAACDD() {
        String basket = "AABAACDD";
        ProductShop testProductShop = new ProductShop(basket);
        char[] actualResult = testProductShop.parseBasket();
        char[] expectedArray = {'A', 'A', 'B', 'A', 'A', 'C', 'D', 'D'};
        Assertions.assertArrayEquals(expectedArray, actualResult);
    }

    @Test
    void testParseBasketDCADDBBA() {
        String basket = "DCADDBBA";
        ProductShop testProductShop = new ProductShop(basket);
        char[] actualResult = testProductShop.parseBasket();
        char[] expectedArray = {'D', 'C', 'A', 'D', 'D', 'B', 'B', 'A'};
        Assertions.assertArrayEquals(expectedArray, actualResult);
    }

    @Test
    void testCommodityBase() {
        String basket = "DCADDBBA";
        ProductShop testProductShop = new ProductShop(basket);
        testProductShop.commodityBase();
    }

    //    @Test
//    void testNumberOfProductsInBasket(){
//        String basket = "AABAACDD";
//        ProductShop testProductShop = new ProductShop(basket);
//        Map<Character, Integer> actualResult = testProductShop.numberOfProductsInBasket();
//        Map<Character, Integer> expectedMap = new HashMap<>();
//        expectedMap.put('A',4);
//        expectedMap.put('B',1);
//        expectedMap.put('C',1);
//        expectedMap.put('D',2);
//    }
    @Test
    void testSumBasketPrise() {
        String basket = "ABCDABA";
        ProductShop testProductShop = new ProductShop(basket);
        Float actualResult = testProductShop.sumBasketPrice();
        Float expectedPrice = 13.25F;
        Assertions.assertEquals(actualResult, expectedPrice);
    }

    @Test
    void numberProductWithoutPromotions() {
        String basket = "ABCDABAA";
        ProductShop testProductShop = new ProductShop(basket);
        int actualResult = testProductShop.numberProductWithoutPromotions('A', 3);
        int expectedNumberProductWithoutPromotions = 1;
        Assertions.assertEquals(expectedNumberProductWithoutPromotions, actualResult);

    }

    @Test
    void numberPromotionAssortment() {
        String basket = "ABCDABAA";
        ProductShop testProductShop = new ProductShop(basket);
        int actualResult = testProductShop.numberPromotionAssortment('A', 3);
        int expectedNumberProductWithoutPromotions = 1;
        Assertions.assertEquals(expectedNumberProductWithoutPromotions, actualResult);
    }
}