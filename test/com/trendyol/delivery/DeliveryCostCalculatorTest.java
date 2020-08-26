package com.trendyol.delivery;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.trendyol.ShoppingCart;
import com.trendyol.product.Category;
import com.trendyol.product.Product;

/**
 * @author cbayar
 * @since 26.08.2020
 *
 * Tests DeliveryCostCalculator.java
 */
public class DeliveryCostCalculatorTest {
	
	// Calculator instance
	private DeliveryCostCalculator calculator = new DeliveryCostCalculator(1.5, 0.5, 5.99);

	/*
	 * Tests @{DeliveryCostCalculator.calculateFor} method
	 */
	@Test
	public void testCalculateFor() {
		
		// Null check
		Assert.assertEquals(0, calculator.calculateFor(null), ShoppingCart.EPSILON);
		
		// Empty cart
		ShoppingCart shoppingCart = new ShoppingCart(calculator);
		Assert.assertEquals(0, calculator.calculateFor(shoppingCart), ShoppingCart.EPSILON);
		
		// Create the books category
		Category booksCategory = new Category("Books");
		Product book1 = new Product("The Lord Of The Rings", 20.0, booksCategory);
		Product book2 = new Product("Da Vinci Code", 15.0, booksCategory);
		
		// Result after books added into the cart
		shoppingCart.addItem(book1, 1);
		shoppingCart.addItem(book2, 2);
		Assert.assertEquals(8.49, calculator.calculateFor(shoppingCart), ShoppingCart.EPSILON);
		
		// Create the movies category
		Category moviesCategory = new Category("Movies");
		Product movie1 = new Product("Fight Club", 7.99, moviesCategory);
		
		// Result after movies added into the cart
		shoppingCart.addItem(movie1, 3);
		Assert.assertEquals(10.49, calculator.calculateFor(shoppingCart), ShoppingCart.EPSILON);
	}

}
