package com.trendyol.discount;

import com.trendyol.product.Category;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Campaigns are applied on the given category for price discounts
 */
public class Campaign {
	
	// On which category the discount is applied
	private Category category;
	
	// If discount type is DiscountType.RATE, this value is the percentage of discount on given category
	// Else, it is equal to the amount of discount
	private double discount;
	
	// Minimum number of items on given category in order to apply the campaign
	private int minItemCount;
	
	// Type of discount changes how the discount will be applied
	private DiscountType discountType;

	/*
	 * Constructor
	 */
	public Campaign(Category category, double discount, int minItemCount, DiscountType discountType) {
		this.category = category;
		this.discount = discount;
		this.minItemCount = minItemCount;
		this.discountType = discountType;
	}

	/*
	 * Returns the category
	 */
	public Category getCategory() {
		return category;
	}

	/*
	 * Returns the discount amount/rate (depending on discount type)
	 */
	public double getDiscount() {
		return discount;
	}

	/*
	 * Returns the minimum item count
	 */
	public int getMinItemCount() {
		return minItemCount;
	}
	
	/*
	 * Returns the discount type
	 */
	public DiscountType getDiscountType() {
		return discountType;
	}
}
