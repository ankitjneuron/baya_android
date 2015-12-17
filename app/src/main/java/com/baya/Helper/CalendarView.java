package com.baya.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.baya.R;


public class CalendarView extends Fragment  {
	protected final Calendar calendar;
	private final Locale locale;
	private ViewSwitcher calendarSwitcher;
	private TextView currentMonth;
    public static TextView selected_date_time =null;
     private CalendarAdapter calendarAdapter;
	DateFormat f=null;
	String str_selected_date="";
	Calendar calen=null;
	String current_date="";
	static OnDateSelect mListener=null;
 	public CalendarView() {
		calendar = Calendar.getInstance();
		locale = Locale.getDefault();
	}
 	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 final RelativeLayout calendarLayout = (RelativeLayout)inflater.inflate(R.layout.calendar_view, null);
		 final GridView calendarDayGrid = (GridView)calendarLayout.findViewById(R.id.calendar_days_grid);
		 final GestureDetector swipeDetector = new GestureDetector(getActivity(), new SwipeGesture(getActivity()));
		 final GridView calendarGrid = (GridView)calendarLayout.findViewById(R.id.calendar_grid);
		 calendarSwitcher = (ViewSwitcher)calendarLayout.findViewById(R.id.calendar_switcher);
		 currentMonth = (TextView)calendarLayout.findViewById(R.id.current_month);

         selected_date_time = (TextView)calendarLayout.findViewById(R.id.selected_date_day);
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		 f = new SimpleDateFormat("EEEE MMM dd yyyy");

          calen=Calendar.getInstance();

		  str_selected_date=f.format(calen.getTime());
		  current_date=output.format(calen.getTime());
		current_date=current_date+"T00:00:00.000Z";
	 	selected_date_time.setText(str_selected_date);
		calendarAdapter = new CalendarAdapter(getActivity(), calendar,current_date);
		final TextView nextMonth = (TextView) calendarLayout.findViewById(R.id.next_month);
		nextMonth.setOnClickListener(new NextMonthClickListener());
		final TextView prevMonth = (TextView) calendarLayout.findViewById(R.id.previous_month);
		prevMonth.setOnClickListener(new PreviousMonthClickListener());


		updateCurrentMonth();
          calendarGrid.setOnItemClickListener(new DayItemClickListener());
		calendarGrid.setAdapter(calendarAdapter);
		calendarGrid.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return swipeDetector.onTouchEvent(event);
			}
		});
		calendarDayGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.day_item, getResources().getStringArray(R.array.days_array)));
		return calendarLayout;
	}
 	protected void updateCurrentMonth() {
		calendarAdapter.refreshDays();
		currentMonth.setText(String.format(locale, "%tB", calendar) + " " + calendar.get(Calendar.YEAR));
	}
 	private final class DayItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final TextView dayView = (TextView)view.findViewById(R.id.date);
			final CharSequence text = dayView.getText();
			if (text != null && !"".equals(text)) {
				calendarAdapter.setSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Integer.valueOf(String.valueOf(text)));
                Calendar cal=Calendar.getInstance();
				Calendar cal1=Calendar.getInstance();
				System.out.println("date  "+calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+Integer.valueOf(String.valueOf(text)));
				Date d=null,d2=null;
				try {
				      d2=new SimpleDateFormat("yyyy/MM/dd").parse(""+cal1.get(Calendar.YEAR)+"/"+(cal1.get(Calendar.MONTH)+1)+"/"+(cal1.get(Calendar.DAY_OF_MONTH)));
					 d=new SimpleDateFormat("yyyy/MM/dd").parse(""+calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+Integer.valueOf(String.valueOf(text)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				System.out.println("date  "+d.toString()+" "+d2.toString());
				if(!d.before(d2)) {
					str_selected_date = f.format(d);
					selected_date_time.setText(str_selected_date);
					if (mListener != null) {
						mListener.onDateSelected(calendar);
					}
				}else
				{
					Constants.showMessage("Please select todays and future date",getContext());
				}
			}
		}
	}
	public static  void setListener(@Nullable OnDateSelect listener) {
		mListener = listener;
	}
	public interface OnDateSelect {

		public void onDateSelected(Calendar date);

	}
 	protected final void onNextMonth() {

		calendarSwitcher.showNext();
		if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
			calendar.set((calendar.get(Calendar.YEAR) + 1), Calendar.JANUARY, 1);
		} else {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		}
		updateCurrentMonth();
	}

	protected final void onPreviousMonth() {

		calendarSwitcher.showPrevious();
		if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
			calendar.set((calendar.get(Calendar.YEAR) - 1), Calendar.DECEMBER, 1);
		} else {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		}
		updateCurrentMonth();
	}

	private final class NextMonthClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			onNextMonth();
		}
	}

	private final class PreviousMonthClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			onPreviousMonth();
		}
	}

	private final class SwipeGesture extends SimpleOnGestureListener {
		private final int swipeMinDistance;
		private final int swipeThresholdVelocity;

		public SwipeGesture(Context context) {
			final ViewConfiguration viewConfig = ViewConfiguration.get(context);
			swipeMinDistance = viewConfig.getScaledTouchSlop();
			swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
	            onNextMonth();
	        }  else if (e2.getX() - e1.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity) {
	            onPreviousMonth();
	        }
	        return false;
		}
	}

}
