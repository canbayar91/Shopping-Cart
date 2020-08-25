package com.trendyol.discount;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Coupons are applied to the shopping cart for price discounts
 */
public class Coupon {

	// Minimum total price of items on the shopping cart
	private double minPriceTotal;
	
	// If discount type is DiscountType.RATE, this value is the percentage of discount on the cart
	// Else, it is equal to the amount of discount
	// Note: I made an assumption here, because of the DiscountType parameter
	private double discount;
	
	// Type of discount changes how the discount will be applied
	private DiscountType discountType;

	/*
	 * Constructor
	 */
	public Coupon(double minPriceTotal, double discount, DiscountType discountType) {
		this.minPriceTotal = minPriceTotal;
		this.discount = discount;
		this.discountType = discountType;
	}

	/*
	 * Returns the minimum price total
	 */
	public double getMinPriceTotal() {
		return minPriceTotal;
	}

	/*
	 * Returns the discount amount/rate (depending on discount type)
	 */
	public double getDiscount() {
		return discount;
	}

	/*
	 * Returns the discount type
	 */
	public DiscountType getDiscountType() {
		return discountType;
	}
}
