package com.trendyol;

import com.trendyol.delivery.DeliveryCostCalculator;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Main class
 */
public class Main {

	public static void main(String[] args) {
		
		// Create a new empty shopping cart
		ShoppingCart shoppingCart = new ShoppingCart();

		// Create a delivery cost calculator and apply to the shopping cart
		DeliveryCostCalculator calculator = new DeliveryCostCalculator(1.0, 0.5, 2.99);
		shoppingCart.getDeliveryCost(calculator);
	}

}
