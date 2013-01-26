package com.example.myapp.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.myapp.MITBAYActivity;
import com.example.myapp.Sellable;

/**
 * This class enable sorting Sellable Object with different field parameter
 * includine name, date, type, condition, date, location.
 * 
 * @author trannguyen
 * 
 */
public class SortingFunction {
	private final static String NUMERIC_REGEX = "[1-9][0-9]*(.)[0-9]+|[1-9][0-9]*";
	private final static HashMap<String, Integer> monthToInt = new HashMap<String, Integer>();
	private final static List<String> month = Arrays.asList("Jan", "Feb",
			"Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov",
			"Dec");
	Comparator<Sellable> nameComparator = new Comparator<Sellable>() {
		@Override
		public int compare(Sellable o1, Sellable o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	Comparator<Sellable> priceComparator = new Comparator<Sellable>() {
		@Override
		public int compare(Sellable o1, Sellable o2) {
			if (o1.getPrice().equalsIgnoreCase("free")
					&& o2.getPrice().equalsIgnoreCase("free"))
				return 0;
			else if (o1.getPrice().equalsIgnoreCase("free"))
				return -1;
			else if (o2.getPrice().equalsIgnoreCase("free"))
				return 1;
			else {
				Pattern pattern = Pattern.compile(NUMERIC_REGEX);
				Matcher match1 = pattern.matcher(o1.getPrice());
				Matcher match2 = pattern.matcher(o2.getPrice());
				if (match1.find() && match2.find())
					return Float.valueOf(o1.getPrice()).compareTo(
							Float.valueOf(o2.getPrice()));
				else
					return o1.getPrice().compareTo(o2.getPrice());

			}

		}
	};

	Comparator<Sellable> dateComparator = new Comparator<Sellable>() {
		@Override
		public int compare(Sellable o1, Sellable o2) {
			String[] date1 = o1.getDate().split("-");
			String[] date2 = o2.getDate().split("-");
			if (Integer.valueOf(date1[2]) < Integer.valueOf(date2[2]))
				return -1;
			else if (Integer.valueOf(date1[2]) > Integer.valueOf(date2[2]))
				return 1;
			else {
				for (int i = 0; i < month.size(); i++) {
					monthToInt.put(month.get(i), i + 1);
				}
				if (monthToInt.get(date1[1]) < monthToInt.get(date2[1]))
					return -1;
				else if (monthToInt.get(date1[1]) > monthToInt.get(date2[1]))
					return 1;
				else {
					return Integer.valueOf(date1[0]).compareTo(
							Integer.valueOf(date2[0]));
				}

			}

		}

	};

	/**
	 * Sort the sellableList depending on the parameter field
	 * 
	 * @param field
	 *            String such as condition, name, etc
	 * @param sellableList
	 *            the ArrayList of sellable objects Multate
	 */
	public void sort(final String field, ArrayList<Sellable> sellableList) {
		if (sellableList != null && sellableList.size() == 0) {
			final Comparator<Sellable> comparator;

			if (field.equals(MITBAYActivity.CONDITION)
					|| field.equals(MITBAYActivity.LOCATION)
					|| field.equals(MITBAYActivity.NAME)
					|| field.equals(MITBAYActivity.TYPE)) {
				comparator = nameComparator;
			} else if (field.equals(MITBAYActivity.PRICE)) {
				comparator = priceComparator;
			} else {
				comparator = dateComparator;
			}

			Collections.sort(sellableList, comparator);
		}
	}
}
