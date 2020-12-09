package PojoTesting;

import io.alexa.menu.food.Order;
import io.alexa.menu.food.Product;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void getItemsOrdered(){

        //given
        List<Product> expected = new ArrayList<>();
        expected.add(new Product("pepsi",3.00,"drink"));
        //when
        Order sampleItem = new Order();
        sampleItem.addItemToOrder(new Product("pepsi",3.00,"drink"));
        List<Product> actual = sampleItem.getItemsOrdered();

        //then
        assertEquals(actual.size(),expected.size());

    }

}
