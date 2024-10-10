package model;

/**
 * this book class represent a book with details
 **/
public class Book implements ShoppingCart{
	private String title;
	private String author;
	private int noOfCopies;
	private double price;
	private int soldCopies;

	
	public Book(String title, String author, int noOfCopies, double price, int soldCopies) {
		this.title = title;
		this.author = author;
		this.noOfCopies = noOfCopies;
		this.price = price;
		this.soldCopies = soldCopies;
	}
	

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public int getNoOfCopies() {
		return noOfCopies;
	}


	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

    @Override
	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}



	@Override
	public void setSoldCopies(int soldCopies) {
		this.soldCopies = soldCopies;
	}
	


	@Override
	public String getDetail() {
		return "BOOK TITLE | " + title + "| AUTHORS | " + author + "| Physical Copies | " + noOfCopies + "| price |" + price
				+ "| SOLD COPIES | " + soldCopies;
	}


	@Override
	public int getsoldCopies() {
		return soldCopies;
	}

	@Override
	public String toString() {
		return "BOOK TITLE | " + title + "| AUTHORS | " + author + "| Physical Copies | " + noOfCopies + "| price |" + price
				+ "| SOLD COPIES | " + soldCopies;
	}

	



}
