package com.doi_tien_te;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class TienTeHandler extends DefaultHandler {

	private List<String> nameMoney = new ArrayList<String>();
	private List<String> codeMoney = new ArrayList<String>();
	private List<String> buyMoney = new ArrayList<String>();
	private List<String> sellMoney = new ArrayList<String>();
	private List<String> transferMoney = new ArrayList<String>();

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(arg0, arg1, arg2);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException{

		if (qName.equals("Exrate")) {
			nameMoney.add(attributes.getValue("CurrencyName"));
			codeMoney.add(attributes.getValue("CurrencyCode"));
			buyMoney.add(attributes.getValue("Buy"));
			transferMoney.add(attributes.getValue("Transfer"));
			sellMoney.add(attributes.getValue("Sell"));
		}
	}// close startElement

	public List<String> getNameMoney() {
		return nameMoney;
	}

	public void setNameMoney(List<String> nameMoney) {
		this.nameMoney = nameMoney;
	}

	public List<String> getCodeMoney() {
		return codeMoney;
	}

	public void setCodeMoney(List<String> codeMoney) {
		this.codeMoney = codeMoney;
	}

	public List<String> getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(List<String> buyMoney) {
		this.buyMoney = buyMoney;
	}

	public List<String> getSellMoney() {
		return sellMoney;
	}

	public void setSellMoney(List<String> sellMoney) {
		this.sellMoney = sellMoney;
	}

	public List<String> getTransferMoney() {
		return transferMoney;
	}

	public void setTransferMoney(List<String> transferMoney) {
		this.transferMoney = transferMoney;
	}

}
