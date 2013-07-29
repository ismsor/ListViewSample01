package com.dj.listviewsample01;



import com.dj.listviewsample01.Task.taskListener;

import android.os.Bundle;
import android.os.Handler;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getListView().setEmptyView(findViewById(R.id.empty));	
	}

	public void btnGoClicked(View sender){
		Task[] tasks=initTasks();
		MyAdapter adapter = new MyAdapter(this,tasks);
		getListView().setAdapter(adapter);
		
		for(Task t:tasks)
			new Thread(t).start();
	}
	Task[] initTasks() {
		final int count=10;
		Task[] result= new Task[count];
		for (int i=0;i<count;i++){
			result[i]=new Task("TASK::"+i);
		}
		
		return result;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class MyAdapter extends BaseAdapter {
		private final Context context;
		private Task[] tasks;
		private LayoutInflater inflater;

		final static Handler mHandler=new Handler();
		
		public MyAdapter(Context context,Task[] tasks){
			this.context=context;
			this.tasks=tasks;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
		}
		@Override
		public View getView(int position, View convertView, final ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.custom_list_item, parent,false);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			final Task aTask=tasks[position];
			viewHolder.setNewTask(aTask);
			viewHolder.btnAction.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "I'm "+aTask.getDesc(), Toast.LENGTH_SHORT).show();					
				}
				
			});
				
			return convertView;
		}// getView()

		final static class ViewHolder {
			public ImageView imageType;
			public TextView tvTaskDesc;
			public ProgressBar pbTask;
			public Button btnAction;
			
			public Task linkTask;
			public Task.taskListener l;
			
			public void removeListener() {
				if (linkTask!=null && l !=null )
					linkTask.removeListener(l);
			}
			public void addListener() {
				if (linkTask!=null )
					linkTask.addListener(l);
			}
			public void setNewTask(Task t) {
				removeListener();
				this.linkTask=t;
				this.tvTaskDesc.setText(t.getDesc());
				this.pbTask.setProgress(t.getProgress());
				addListener();
			}
			public ViewHolder(View convertView){
				this.imageType = (ImageView) convertView
						.findViewById(R.id.icon);
				this.tvTaskDesc = (TextView) convertView
						.findViewById(R.id.tvTaskDesc);
				this.pbTask = (ProgressBar) convertView
						.findViewById(R.id.pbTask);
				this.btnAction = (Button) convertView
						.findViewById(R.id.btnActive);
				this.l = new taskListener() {					
					@Override
					public void onProgressChanged(final int progress) {
						
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								pbTask.setProgress(progress);
								
							}
						});
					}
				};
				
			}
		}

		@Override
		public int getCount() {
			
			return tasks.length;
		}
		@Override
		public Task getItem(int position) {
			
			return tasks[position];
		}
		@Override
		public long getItemId(int position) {
			
			return position;
		}

	}

	}
