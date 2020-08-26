package com.trendyol;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.trendyol.delivery.DeliveryCostCalculator;
import com.trendyol.discount.Campaign;
import com.trendyol.discount.Coupon;
import com.trendyol.discount.DiscountType;
import com.trendyol.product.Category;
import com.trendyol.product.Product;

/**
 * @author cbayar
 * @since 26.08.2020
 *
 * Tests ShoppingCart.java
 */
public class ShoppingCartTest {
	
	// Create a delivery cost calculator
	private DeliveryCostCalculator calculator = new DeliveryCostCalculator(2.0, 0.5, 2.99);
	
	// Categories that are used throughout the tests
	private Category mainCategory = new Category("Movies, Books and Games");
	private Category booksCategory = new Category("Books", mainCategory);
	private Category moviesCategory = new Category("Movies", mainCategory);
	private Category videoGamesCategory = new Category("Video Games", mainCategory);
	private Category musicCategory = new Category("Music", mainCategory);

	/*
	 * Tests @{ShoppingCart.addItem} method
	 */
	@Test
	public void testAddItem() {
		
		// Create the cart
		ShoppingCart shoppingCart = new ShoppingCart(calculator);
		
		// Create the books
		Product book1 = new Product("The Lord Of The Rings", 20.0, booksCategory);
		Product book2 = new Product("Da Vinci Code", 15.0, booksCategory);
		
		// Null case
		shoppingCart.addItem(null, 0);
		Assert.assertTrue(shoppingCart.isEmpty());
		
		// Invalid count
		shoppingCart.addItem(book1, -1);
		Assert.assertTrue(shoppingCart.isEmpty());
		
		// Adding the first book
		shoppingCart.addItem(book1, 1);
		Assert.assertFalse(shoppingCart.isEmpty());
		Assert.assertEquals(1, shoppingCart.productCount());
		Assert.assertEquals(20.0, shoppingCart.getTotalPrice(), ShoppingCart.EPSILON);
		
		// Adding the same product again
		shoppingCart.addItem(book1, 1);
		Assert.assertEquals(1, shoppingCart.productCount());
		Assert.assertEquals(40.0, shoppingCart.getTotalPrice(), ShoppingCart.EPSILON);
		
		// Adding the second book
		shoppingCart.addItem(book2, 1);
		Assert.assertEquals(2, shoppingCart.productCount());
		Assert.assertEquals(55.0, shoppingCart.getTotalPrice(), ShoppingCart.EPSILON);
		Assert.assertEquals(1, shoppingCart.deliveryCount());
		
		// Create the movies
		Product movie1 = new Product("Fight Club", 7.99, moviesCategory);
		
		// Add a movie
		shoppingCart.addItem(movie1, 1);
		Assert.assertEquals(3, shoppingCart.productCount());
		Assert.assertEquals(62.99, shoppingCart.getTotalPrice(), ShoppingCart.EPSILON);
		Assert.assertEquals(2, shoppingCart.deliveryCount());
		
		// Coverage call to print method :)
		shoppingCart.print();
	}
	
	/*
	 * Tests @{ShoppingCart.applyDiscounts} and @{ShoppingCart.getCampaignDiscount} methods
	 */
	@Test
	public void testApplyDiscounts() {
		
		// Create a sample shopping cart
		ShoppingCart shoppingCart = createShoppingCart();
		Assert.assertEquals(shoppingCart.getTotalPrice(), shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
		
		// Define some campaigns
		Campaign bookCampaign = new Campaign(booksCategory, 20.0, 5, DiscountType.RATE);
		Campaign movieCampaign = new Campaign(moviesCategory, 25.0, 10, DiscountType.RATE);
		Campaign videoGameCampaign = new Campaign(videoGamesCategory, 15.0, 5, DiscountType.AMOUNT);
		Campaign musicCampaign = new Campaign(musicCategory, 25.0, 10, DiscountType.RATE);
		
		// Apply %20 campaign on books
		shoppingCart.applyDiscounts(bookCampaign);
		double discount = 115.0 * 20.0 / 100;
		Assert.assertEquals(discount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
		
		// Apply %25 discount on movies (ineffective)
		shoppingCart.applyDiscounts(movieCampaign);
		Assert.assertEquals(discount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);

		// Apply invalid discounts
		shoppingCart.applyDiscounts(musicCampaign, null);
		Assert.assertEquals(discount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
		
		// Apply 15 TL discount on video games
		shoppingCart.applyDiscounts(videoGameCampaign);
		discount += 15.0;
		Assert.assertEquals(discount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
	
		// Coverage call to print method :)
		shoppingCart.print();
	}
	
	/*
	 * Tests @{ShoppingCart.applyDiscounts} and @{ShoppingCart.getCampaignDiscount} methods
	 */
	@Test
	public void testApplyDiscountsParentCategory() {
		
		// Create a sample shopping cart
		ShoppingCart shoppingCart = createShoppingCart();
		
		// Apply a campaign on the main category
		Campaign campaign = new Campaign(mainCategory, 20.0, 5, DiscountType.RATE);
		shoppingCart.applyDiscounts(campaign);
		
		// Control the discount amount
		double discount = shoppingCart.getTotalPrice() * 20.0 / 100;
		Assert.assertEquals(discount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
	
		// Coverage call to print method :)
		shoppingCart.print();
	}
	
	/*
	 * Tests @{ShoppingCart.applyCoupon} and @{ShoppingCart.getCouponDiscount} methods
	 */
	@Test
	public void testApplyCoupon() {
		
		// Create a sample shopping cart
		ShoppingCart shoppingCart = createShoppingCart();
		
		// Create both types of coupons
		Coupon coupon1 = new Coupon(300.0, 25.0, DiscountType.AMOUNT);
		Coupon coupon2 = new Coupon(300.0, 10.0, DiscountType.RATE);
		Coupon invalidCoupon = new Coupon(500.0, 25.0, DiscountType.AMOUNT);
		
		// Apply the invalid coupon
		shoppingCart.applyCoupon(invalidCoupon);
		Assert.assertEquals(shoppingCart.getTotalPrice(), shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
		
		// Apply the first coupon
		shoppingCart.applyCoupon(coupon1);
		Assert.assertEquals(25.0, shoppingCart.getCouponDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - 25.0, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
		
		// Apply the second coupon
		shoppingCart.applyCoupon(coupon2);
		double discount = shoppingCart.getTotalPrice() * 10.0 / 100;
		Assert.assertEquals(discount, shoppingCart.getCouponDiscount(), ShoppingCart.EPSILON);
		Assert.assertEquals(shoppingCart.getTotalPrice() - discount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
	
		// Coverage call to print method :)
		shoppingCart.print();
	}
	
	/*
	 * Tests @{ShoppingCart.applyDiscounts} and @{ShoppingCart.applyCoupon} methods
	 */
	@Test
	public void testApplyDiscountAndCoupon() {
		
		// Create a sample shopping cart
		ShoppingCart shoppingCart = createShoppingCart();
		
		// Create a campaign and a coupon
		Campaign campaign = new Campaign(booksCategory, 20.0, 5, DiscountType.RATE);
		Coupon coupon = new Coupon(300.0, 10.0, DiscountType.RATE);
		
		// Apply the discount and the coupon
		shoppingCart.applyDiscounts(campaign);
		shoppingCart.applyCoupon(coupon);
		
		// Check the campaign discount
		double campaignDiscount = 115.0 * 20.0 / 100;
		Assert.assertEquals(campaignDiscount, shoppingCart.getCampaignDiscount(), ShoppingCart.EPSILON);
		
		// Check the coupon discount
		double couponDiscount = (shoppingCart.getTotalPrice() - campaignDiscount) * 10.0 / 100;
		Assert.assertEquals(couponDiscount, shoppingCart.getCouponDiscount(), ShoppingCart.EPSILON);
		
		// Check the final price
		double totalDiscount = campaignDiscount + couponDiscount;
		Assert.assertEquals(shoppingCart.getTotalPrice() - totalDiscount, shoppingCart.getTotalAmountAfterDiscounts(), ShoppingCart.EPSILON);
	
		// Coverage call to print method :)
		shoppingCart.print();
	}
	
	/*
	 * Tests @{ShoppingCart.getDeliveryCost} method
	 */
	@Test
	public void testGetDeliveryCost() {
		
		// Create the cart with no calculator
		ShoppingCart shoppingCart = new ShoppingCart(null);
		Assert.assertEquals(0, shoppingCart.getDeliveryCost(), ShoppingCart.EPSILON);
		
		// Create the cart with the calculator
		shoppingCart = createShoppingCart();
		Assert.assertEquals(calculator.calculateFor(shoppingCart), shoppingCart.getDeliveryCost(), ShoppingCart.EPSILON);
	}
	
	/*
	 * Helper method to fill shopping cart
	 * Note: Didn't use @Before because of the first test case
	 */
	private ShoppingCart createShoppingCart() {
		
		// Create a new empty shopping cart
		ShoppingCart shoppingCart = new ShoppingCart(calculator);
		
		// Create example books
		Product book1 = new Product("The Lord Of The Rings", 20.0, booksCategory);
		Product book2 = new Product("Da Vinci Code", 15.0, booksCategory);
		Product book3 = new Product("War And Peace", 25.0, booksCategory);
		
		// Create example movies
		Product movie1 = new Product("Fight Club", 7.99, moviesCategory);
		Product movie2 = new Product("The Matrix", 6.99, moviesCategory);
		
		// Create example video
		Product game1 = new Product("The Witcher 3", 40.0, videoGamesCategory);
		Product game2 = new Product("Red Dead Redemption 2", 60.0, videoGamesCategory);
		Product game3 = new Product("The Last Of Us", 20.0, videoGamesCategory);
		
		// Add all products into the shopping cart
		shoppingCart.addItem(book1, 3);
		shoppingCart.addItem(book2, 2);
		shoppingCart.addItem(book3, 1);
		shoppingCart.addItem(movie1, 5);
		shoppingCart.addItem(movie2, 4);
		shoppingCart.addItem(game1, 2);
		shoppingCart.addItem(game2, 1);
		shoppingCart.addItem(game3, 4);
		
		// Return the cart
		return shoppingCart;
	}
}
