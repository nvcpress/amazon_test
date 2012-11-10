package org.jaykyung.amazonTest;


//* This class defines all UI elements of NVC web sites.  Every elements should be defined 
//	here as a central location for efficient maintenance
public class WebElements {

	//home page
	public static final String pizzaAppLogoId = "siteHeaderTitleLinkLogo";
	public static final String pizzasLeftNavXpath = "//*[@id=\"siteHeaderNavBoxPizzas\"]/a/span";
	public static final String saladesLeftNavXpath = "//*[@id=\"siteHeaderNavBoxSalads\"]/a/span";
	public static final String desertsLeftNavXpath = "//*[@id=\"siteHeaderNavBoxDesserts\"]/a/span";
	public static final String boissonsLeftNavXpath = "//*[@id=\"siteHeaderNavBoxBeverages\"]/a/span";
	public static final String bannerPreviousButtonXpath = "//*[@id=\"billboard\"]/div/a[1]";
	public static final String bannerNextButtonXpath = "//*[@id=\"billboard\"]/div/a[2]";
	public static final String bannerNewYorkPizzaID = "quadNY";
	public static final String bannerBleueSaladsID = "quadBleue";
	public static final String bannerItalianSaladID = "quadItalienne";
	public static final String bannerProvenPizzaID = "quadProvencaou";
	public static final String orderSumAmountXpath = "//*[@id=\"itemsFooterAmount\"]/span";
	public static final String subOrderAmountXpath = "//*[@id=\"items\"]/li/article/header/div[1]/span[3]";
	public static String phoneId;
	public static String messageId;
	public static String submitId;
	public static String nameId;
	public static String emailId;

}