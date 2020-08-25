package com.trendyol.product;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Helper class to store the product information on a single category
 */
public class CategoryInfoHolder {

	// Total count of products
	private int productCount = 0;
	
	// Total price of products
	private double totalPrice = 0;
	
	/*
	 * Adds the given amount of products into the holder
	 */
	public void update(Product product, int count) {
		productCount += count;
		totalPrice += product.getPrice() * count;
	}

	/*
	 * Return the product count
	 */
	public int getProductCount() {
		return productCount;
	}

	/*
	 * Return the total price
	 */
	public double getTotalPrice() {
		return totalPrice;
	}
}
