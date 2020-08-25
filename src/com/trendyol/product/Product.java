package com.trendyol.product;

import java.util.Objects;

/**
 * @author cbayar
 * @since 25.08.2020
 *
 * Products are added to the shopping card
 */
public class Product {

	// Name of the product - assuming it is unique and can be used as identifier
	private String title;
	
	// Price of the product
	// Note: I would normally use BigDecimal for price
	private double price;
	
	// Category or tree of categories in which the product belongs
	private Category category;
	
	/*
	 * Constructor
	 */
	public Product(String title, double price, Category category) {
		this.title = title;
		this.price = price;
		this.category = category;
	}

	/*
	 * Returns the name of the product
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * Returns the price of the product
	 */
	public double getPrice() {
		return price;
	}

	/*
	 * Returns the category of the product
	 */
	public Category getCategory() {
		return category;
	}

	/*
	 * {@hashCode} method is overridden with the assumption of titles are unique
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	/*
	 * {@equals} method is overridden with the assumption of titles are unique
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Product)) {
			return false;
		}
		
		Product other = (Product) obj;
		return Objects.equals(title, other.getTitle());
	}
}
