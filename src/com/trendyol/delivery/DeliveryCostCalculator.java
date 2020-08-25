package com.trendyol.delivery;

import com.trendyol.ShoppingCart;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Calculates the delivery cost for the shopping cart
 * Note: An interface can be useful in case new calculator methods are implemented
 */
public class DeliveryCostCalculator {

	// Delivery cost factor for a single delivery
	private double costPerDelivery;
	
	// Delivery cost factor for a single product
	private double costPerProduct;
	
	// Fixed cost addition
	// Note: I would use a constant value if it wasn't given in the constructor parameters on pseudocode
	private double fixedCost;
	
	/*
	 * Constructor
	 */
	public DeliveryCostCalculator(double costPerDelivery, double costPerProduct, double fixedCost) {
		this.costPerDelivery = costPerDelivery;
		this.costPerProduct = costPerProduct;
		this.fixedCost = fixedCost;
	}
	
	/**
	 * Calculates the cost of delivery for the given cart
	 * 
	 * @param cart the shopping cart
	 * @return the delivery cost
	 */
	public double calculateFor(ShoppingCart cart) {
		
		// Take the product and category information from cart
		int numDeliveries = cart.deliveryCount();
		int numProducts = cart.productCount();
		
		// Calculate the delivery cost according to the given formula
		double deliveryCost = costPerDelivery * numDeliveries + costPerProduct * numProducts + fixedCost;
		
		// Return the total delivery cost
		return deliveryCost;
	}
}
