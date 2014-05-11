/* ECE 155 - LAB 4
 * (ANNA MA 20458438)
 * (RAFIC DALATI 20526978)
 * (CHAITANYA KHANNA 20542268)
 * MARCH 21, 2014
 */

package ca.uwaterloo.lab4_203_28;

import java.util.ArrayList;
import java.util.List;

import mapper.MapLoader;
import mapper.MapView;
import mapper.NavigationalMap;
import mapper.PositionListener;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener, PositionListener {
	
	// DECLARING GLOBAL VARIABLES
	LinearLayout ll;
	int position = 0;
	int steps = 0;
	MapView mv;
	PositionListener pos;
	float [] smoothedAccel;
	float[] angle;
	float azimut;
	List<PointF> myList = new ArrayList<PointF>();
	PointF a1 = new PointF(3,9);
	PointF a2 = new PointF(7,9);
	PointF a3 = new PointF(11,9);
	PointF a4 = new PointF(15,9);
	PointF random;

	
	NavigationalMap map;
	
	float xcor;
	float ycor;
	
	double north = 0;
	double east = 0;
	
	@Override
	public void onCreateContextMenu ( ContextMenu menu , View v, ContextMenuInfo menuInfo ) {
	super.onCreateContextMenu (menu , v, menuInfo );
	mv.onCreateContextMenu (menu , v, menuInfo );
	}
	@Override
	public boolean onContextItemSelected ( MenuItem item ) {
	return super.onContextItemSelected ( item ) || mv.onContextItemSelected ( item );
	}
      
	// VARIABLES TO SHOW SENSOR VALUES
	TextView acceltv;
	
	@Override
	public void onClick(View V){
		steps = 0;
		north = 0;
		east = 0;
		xcor = 0;
		ycor = 0;
	}
	// MAIN BODY CODE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      
        
		// CHANGES LAYOUT TO BE LINEAR
        ll = (LinearLayout) findViewById(R.id.label2);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(0xff66FFF6);
		pos = new MainActivity();
		mv = new MapView ( getApplicationContext (), 1920 , 720 , 50, 50);
		mv.addListener(pos);
		
		registerForContextMenu (mv);
		
		// DISPLAY ACCELEROMETER'S SENSOR
		
		map = MapLoader.loadMap(Environment.getExternalStorageDirectory(), "Lab-room-peninsula.svg");
		mv.setMap(map);
		ll.addView(mv);
		
		acceltv= new TextView(getApplicationContext());
		ll.addView(acceltv);
		
		// ------- SENSORS -------------	
		// -----------------------------

		// ORIENTATION SENSOR
		
		SensorManager orientManager = (SensorManager) getSystemService(SENSOR_SERVICE);		
		Sensor orientSensor = orientManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);		
		SensorEventListener o = new OrientSensorEventListener(acceltv);
		orientManager.registerListener(o, orientSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		//LINEAR ACCELERATION
		
		SensorManager accelmanager = (SensorManager) getSystemService(SENSOR_SERVICE);	
		Sensor linearaccel = accelmanager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		SensorEventListener c = new accelSensorEventListener(acceltv);
		accelmanager.registerListener(c, linearaccel, accelmanager.SENSOR_DELAY_FASTEST);
		
		// CLEAR BUTTON IMPLEMENTATION
		
    }

    
// ---------- SENSOR CLASSES ----------- 

class OrientSensorEventListener implements SensorEventListener {
    TextView output;
    	
    public OrientSensorEventListener(TextView outputView){
    	output = outputView;
   	}	
    public void onAccuracyChanged(Sensor s, int i) {}
   
   	public void onSensorChanged(SensorEvent ori) {
   		if (ori.sensor.getType() == Sensor.TYPE_ORIENTATION) 
   		{
   			angle = ori.values;
   			azimut = angle[0];
    	}
   	}
}     
    
// ACCELEROMETER SENSOR CLASS

class accelSensorEventListener implements SensorEventListener {
	TextView output;
	
	public accelSensorEventListener(TextView outputView){
		output = outputView;
	}
	
	public void onAccuracyChanged(Sensor s, int i) {}
	
	public void findpath(){
		myList.clear();
		if(map.calculateIntersections(mv.getUserPoint(),a1).size()==0){
			   myList.add(mv.getUserPoint());
			   myList.add(a1);
			   mv.setUserPath(myList);
			   if(map.calculateIntersections(a1, mv.getDestinationPoint()).size()==0){
				   if(map.calculateIntersections(a1, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a1);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a2,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a2, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a2);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a2);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a3,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a3, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a3);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a3);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
				   
			   }
			   else{
				   if(map.calculateIntersections(a4, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a4);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a4);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
		}
		else if(map.calculateIntersections(mv.getUserPoint(),a2).size()==0){
			myList.add(mv.getUserPoint());
			   myList.add(a2);
			   mv.setUserPath(myList);
			   if(map.calculateIntersections(a2, mv.getDestinationPoint()).size()==0){
				   if(map.calculateIntersections(a2, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a2);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a3,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a3, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a3);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a3);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a4,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a4, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a4);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a4);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
				   
			   }
			   else{
				   if(map.calculateIntersections(a1, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a1);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a1);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
		}
		else if(map.calculateIntersections(mv.getUserPoint(),a3).size()==0){
			myList.add(mv.getUserPoint());
			   myList.add(a3);
			   mv.setUserPath(myList);
			   if(map.calculateIntersections(a3, mv.getDestinationPoint()).size()==0){
				   if(map.calculateIntersections(a3, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a3);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a4,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a4, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a4);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a4);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a1,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a1, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a1);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a1);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else{
				   if(map.calculateIntersections(a2, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a2);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a2);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
			   }
		}
		else if(map.calculateIntersections(mv.getUserPoint(),a4).size()==0){
			myList.add(mv.getUserPoint());
			   myList.add(a4);
			   mv.setUserPath(myList);
			   if(map.calculateIntersections(a4, mv.getDestinationPoint()).size()==0){
				   if(map.calculateIntersections(a4, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a4);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a1,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a1, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a1);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a1);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
				   
			   }
			   else if(map.calculateIntersections(a2,mv.getDestinationPoint()).size() == 0){
				   if(map.calculateIntersections(a2, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a2);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a2);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }  
			   }
			   else{
				   if(map.calculateIntersections(a3, mv.getUserPoint()).size()==0){
					   myList.clear();
					   myList.add(mv.getDestinationPoint());
					   myList.add(a3);
					   myList.add(mv.getUserPoint());
					   mv.setUserPath(myList);
				   }
				   else{
					   myList.add(a3);
					   myList.add(mv.getDestinationPoint());
					   mv.setUserPath(myList);
				   }
			   }
		}
	}
	
	public void onSensorChanged(SensorEvent event) 
	{
		
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			
			// LOW-PASS FILTER
			smoothedAccel = event.values;
			smoothedAccel[1] += (event.values[1] - smoothedAccel[1]) / 2.5;
			smoothedAccel[0] += (event.values[0] - smoothedAccel[0]) / 2.5;
			smoothedAccel[2] += (event.values[2] - smoothedAccel[2]) / 2.5;
	
			// STATE MACHINE
			if (position == 0 && smoothedAccel[1] >= 0.1 && smoothedAccel[1] <= 0.2)
			{
				 position= 1;				// RISING STATE
			}
			if (position == 1 && smoothedAccel[1] >= 0.6 && smoothedAccel[1] <= 1.5)
			{
				position = 2; 				// PEAK STATE
			}
			if (position == 2 && smoothedAccel[1] >= 0.6 && smoothedAccel[1] <= 1.2)
			{
				position = 3;				//FALLING STATE
			}
			if (position == 3)
			{	
				xcor = mv.getUserPoint().x;
				ycor = mv.getUserPoint().y;
				steps++;
				random = new PointF((float)(xcor + (Math.sin((azimut+20)*(Math.PI/180)))*0.71),(float)(ycor - (Math.cos((azimut+20)*(Math.PI/180)))*0.71));
				if (map.calculateIntersections(mv.getUserPoint(),random).size() == 0) {
					if(map.calculateIntersections(mv.getUserPoint(), mv.getDestinationPoint()).size()==0){
						myList.clear();	
						myList.add(mv.getUserPoint());
						myList.add(mv.getDestinationPoint());
						mv.setUserPath(myList);
				  }
				
				else
					findpath();
					
				north += (Math.cos(azimut*(Math.PI/180)));
				east += (Math.sin(azimut*(Math.PI/180)));
				xcor += (Math.sin((azimut+15)*(Math.PI/180)))*0.71;
				ycor -= (Math.cos((azimut+15)*(Math.PI/180)))*0.71;
				
				
				mv.setUserPoint(xcor,ycor);
				position = 0;	
				}
				if((Math.abs(mv.getUserPoint().x - mv.getDestinationPoint().x)) <= 0.5 && 
						(Math.abs(mv.getUserPoint().y - mv.getDestinationPoint().y)) <= 0.5
						&&mv.getUserPoint().x != 0 && mv.getDestinationPoint().x != 0){
					output.setText("You have Reached");
				}
			}
		}
		//output.setText(String.format("counter: %d \nPosition: %d \ndegree: %.0f \nnorth: %f \neast: %f \nx-cor %f \ny-cor %f", steps, position, azimut, north, east, xcor, ycor));
		output.setTextSize(16);
		output.setGravity(Gravity.CENTER_HORIZONTAL);
		output.setTextColor(Color.RED);
		output.setTypeface(output.getTypeface(), Typeface.BOLD);
	}
}

@Override
public void originChanged(MapView source, PointF loc) {
	
	source.setUserPoint(source.getOriginPoint());

	// TODO Auto-generated method stub
	
}
@Override
public void destinationChanged(MapView source, PointF dest) {
	// TODO Auto-generated method stub
	
}
}



