package com.trendyol.delivery;

import com.trendyol.ShoppingCart;

/**
 * @author cbayar
 * @since 26.08.2020
 *
 * An interface for different delivery cost calculation methods
 * Note: This interface is not actually necessary since there is a single method
 * However, seeing delivery method is not an input into shopping cart, I thought different methods could be introduced
 */
public interface DeliveryMethod {

	/**
	 * Calculates the cost of delivery for the given cart
	 * 
	 * @param cart the shopping cart
	 * @return the delivery cost
	 */
	double calculateFor(ShoppingCart cart);
	
}
