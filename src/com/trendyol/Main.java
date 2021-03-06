package com.trendyol;

import com.trendyol.delivery.DeliveryCostCalculator;
import com.trendyol.discount.Campaign;
import com.trendyol.discount.Coupon;
import com.trendyol.discount.DiscountType;
import com.trendyol.product.Category;
import com.trendyol.product.Product;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Main class
 */
public class Main {

	public static void main(String[] args) {
		
		// Important Note: I would normally use BigDecimal for currency calculations
		// For the sake of code simplicity, I used Double values in whole application
		// As I recall, Double values are precise enough for two decimal places, so it shall be okay

		// Create a delivery cost calculator
		DeliveryCostCalculator calculator = new DeliveryCostCalculator(2.0, 0.5, 2.99);
		
		// Create a new empty shopping cart
		ShoppingCart shoppingCart = new ShoppingCart(calculator);
		
		// Create categories of the products
		Category mainCategory = new Category("Movies, Books and Games");
		Category booksCategory = new Category("Books", mainCategory);
		Category moviesCategory = new Category("Movies", mainCategory);
		Category videoGamesCategory = new Category("Video Games", mainCategory);
		
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
		
		// Create campaigns for each category (2 applicable)
		Campaign bookCampaign = new Campaign(booksCategory, 20.0, 5, DiscountType.RATE);
		Campaign movieCampaign = new Campaign(moviesCategory, 25.0, 10, DiscountType.RATE);
		Campaign videoGameCampaign = new Campaign(videoGamesCategory, 15.0, 5, DiscountType.AMOUNT);
		
		// Create a coupon
		Coupon coupon = new Coupon(300.0, 25.0, DiscountType.AMOUNT);
		
		// Apply the campaigns and coupons
		shoppingCart.applyDiscounts(bookCampaign, movieCampaign, videoGameCampaign);
		shoppingCart.applyCoupon(coupon);
		
		// Print the final state of the cart
		shoppingCart.print();
	}

}
