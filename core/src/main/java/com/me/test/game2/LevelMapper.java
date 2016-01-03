package com.me.test.game2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LevelMapper {
	
	public static int lastStandard = 60;

	public static int standard(int levnumber){
		if(levnumber>lastStandard){
			return mapExtras.get(levnumber-lastStandard);
		}
		
		return  map.get(Integer.valueOf(levnumber));
	}
	


	private static Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> mapExtras = new HashMap<Integer, Integer>();

	static{map.put(1, 1);
	map.put(1, 1);
	map.put(2, 2);
	map.put(3, 3);
	map.put(4, 4);
	map.put(5, 5);
	map.put(6, 6);
	map.put(7, 7);
	map.put(8, 8);
	map.put(9, 9);
	map.put(10, 10);
	map.put(11, 11);
	map.put(12, 12);
	map.put(13, 13);
	map.put(14, 14);
	map.put(15, 15);
	map.put(16, 16);
	map.put(17, 17);
	map.put(18, 18);
	map.put(19, 56);
	map.put(20, 58);
	map.put(21, 19);
	map.put(22, 20);
	map.put(23, 21);
	map.put(24, 22);
	map.put(25, 25);
	map.put(26, 28);
	map.put(27, 29);
	map.put(28, 30);
	map.put(29, 31);
	map.put(30, 32);
	map.put(31, 34);
	map.put(32, 82);
	map.put(33, 38);
	map.put(34, 40);
	map.put(35, 42);
	map.put(36, 43);
	map.put(37, 44);
	map.put(38, 45);
	map.put(39, 75);
	map.put(40, 48);
	map.put(41, 49);
	map.put(42, 50);
	map.put(43, 53);
	map.put(44, 55);
	map.put(45, 57);
	map.put(46, 59);
	map.put(47, 60);
	map.put(48, 61);
	map.put(49, 62);
	map.put(50, 63);
	map.put(51, 64);
	map.put(52, 65);
	map.put(53, 66);
	map.put(54, 67);
	map.put(55, 68);
	map.put(56, 69);
	map.put(57, 71);
	map.put(58, 72);
	map.put(59, 73);
	map.put(60, 70);
	mapExtras.put(1, 1002);
	mapExtras.put(2, 1004);
	mapExtras.put(3, 1005);
	mapExtras.put(4, 1006);
	mapExtras.put(5, 74);
	mapExtras.put(6, 77);
	mapExtras.put(7, 78);
	mapExtras.put(8, 79);
	mapExtras.put(9, 80);
	mapExtras.put(10, 83);
	mapExtras.put(11, 86);
	mapExtras.put(12, 91);
	mapExtras.put(13, 92);
	mapExtras.put(14, 93);
	mapExtras.put(15, 94);
	mapExtras.put(16, 96);
	mapExtras.put(17, 98);
	mapExtras.put(18, 100);
	mapExtras.put(19, 102);
	mapExtras.put(20, 104);
	mapExtras.put(21, 107);
	mapExtras.put(22, 108);
	mapExtras.put(23, 109);
	mapExtras.put(24, 110);
	mapExtras.put(25, 111);
	mapExtras.put(26, 23);
	mapExtras.put(27, 24);
	mapExtras.put(28, 26);
	mapExtras.put(29, 27);
	mapExtras.put(30, 33);
	mapExtras.put(31, 35);
	mapExtras.put(32, 36);
	mapExtras.put(33, 37);
	mapExtras.put(34, 39);
	mapExtras.put(35, 41);
	mapExtras.put(36, 46);
	mapExtras.put(37, 47);
	mapExtras.put(38, 51);
	mapExtras.put(39, 52);
	mapExtras.put(40, 54);
	mapExtras.put(41, 76);
	mapExtras.put(42, 81);
	mapExtras.put(43, 84);
	mapExtras.put(44, 85);
	mapExtras.put(45, 87);
	mapExtras.put(46, 88);
	mapExtras.put(47, 89);
	mapExtras.put(48, 90);
	mapExtras.put(49, 95);
	mapExtras.put(50, 97);
	mapExtras.put(51, 99);
	mapExtras.put(52, 103);
	mapExtras.put(53, 105);
	mapExtras.put(54, 106);
	mapExtras.put(55, 101);
	mapExtras.put(56, 113);
	mapExtras.put(57, 114);
	mapExtras.put(58, 116);
	mapExtras.put(59, 115);
	mapExtras.put(60, 1);
	
	
	}
	
	public static void main(String[] argc){
/*		int c = 0;
		for(int i = 1; i<sequence.size(); i++){
			if(c>0){
				System.out.print(",");
			}
			System.out.print(i);
			c++;
		}*/
		
		for(int i = 0; i<sequence.size(); i++){
			int j = sequence.get(i);
			System.out.println(String.format("map.put(%d, %d);", i+1,j));
		}
		
		for(int i = 0; i<extras.size(); i++){
			int j = extras.get(i);
			System.out.println(String.format("mapExtras.put(%d, %d);", i+1,j));
		}
		
		System.out.println(sequence.size());
		System.out.println(extras.size());
	}

	private static List<Integer> sequence =Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,56,58,19,20,21,22,25,28,29,30,31,32,34,82,38,40,42,43,44,45,75,48,49,50,53,55,57,59,60,61,62,63,64,65,66,67,68,69,71,72,73,70);
	private static List<Integer> extras = Arrays.asList(1002, 1004, 1005, 1006, 74,77,78,79,80,83,86,91,92,93,94,96,98,100,102,104,107,108,109,110,111,23,24,26,27,33,35,36,37,39,41,46,47,51,52,54,76,81,84,85,87,88,89,90,95,97,99,103,105,106,101, 113,114,116,115,1);
}
