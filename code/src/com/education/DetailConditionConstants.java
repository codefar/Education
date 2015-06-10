package com.education;

import com.education.entity.ConditionItem;

public class DetailConditionConstants {

	public static final String SELECTED_ITEM_TAG = "condition_selected";

	public static final int PROVINCE = 0;
	public static final int SCHOOL_TYPE = 1;
	public static final int MAJOR = 2;
	public static final int LUQU_PICI = 3;
	public static final int LUQU_SCORE = 4;
	
	public static final int CONDITION_ITEM_SELECTE_CONFIRM_RESULE_CODE = 1;

	public static final ConditionItem provinces[] = {
			new ConditionItem(R.drawable.arrow_right, "吉林"),
			new ConditionItem(R.drawable.arrow_right, "黑龙江"),
			new ConditionItem(R.drawable.arrow_right, "辽宁"),
			new ConditionItem(R.drawable.arrow_right, "北京") };

	public static final ConditionItem school_type[] = {
			new ConditionItem(R.drawable.arrow_right, "军事"),
			new ConditionItem(R.drawable.arrow_right, "医疗"),
			new ConditionItem(R.drawable.arrow_right, "媒体"),
			new ConditionItem(R.drawable.arrow_right, "生活") };

	public static final ConditionItem major[] = {
			new ConditionItem(R.drawable.arrow_right, "计算机应用"),
			new ConditionItem(R.drawable.arrow_right, "电子信息"),
			new ConditionItem(R.drawable.arrow_right, "土木工程"),
			new ConditionItem(R.drawable.arrow_right, "软件工程") };

	public static final ConditionItem luqu_grade[] = {
			new ConditionItem(R.drawable.arrow_right, "第一批A段"),
			new ConditionItem(R.drawable.arrow_right, "第一批B段"),
			new ConditionItem(R.drawable.arrow_right, "第二批A段"),
			new ConditionItem(R.drawable.arrow_right, "第二批B段") };

}
