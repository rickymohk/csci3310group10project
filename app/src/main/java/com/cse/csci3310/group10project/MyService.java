package com.cse.csci3310.group10project;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.Calendar;
import java.util.Collection;

public class MyService extends Service implements BeaconConsumer {

    private BeaconManager beaconManager;
    protected static final String TAG = "BeaconsEverywhere";
    final static String beacon = "Beacon";

    String uuid1, uuid2, uuid3, uuid4;
    double distance1, distance2, distance3, distance4;
    int rssi1, rssi2, rssi3, rssi4;

    final org.altbeacon.beacon.Region region1 = new org.altbeacon.beacon.Region("myBeacon", Identifier.parse("B5B182C7-EAB1-4988-AA99-B5C1517008D9"), null, null);
    final org.altbeacon.beacon.Region region2 = new org.altbeacon.beacon.Region("myBeacon2", Identifier.parse("B5B182C7-EAB1-4988-AA99-B5C1517008D8"), null, null);
    final org.altbeacon.beacon.Region region3 = new org.altbeacon.beacon.Region("myBeacon3", Identifier.parse("DA2BFB06-7CF3-5B40-BE96-8F1EC8891CE8"), null, null);
 //   final org.altbeacon.beacon.Region region4 = new org.altbeacon.beacon.Region("myBeacon3", Identifier.parse("XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"), null, null);

    @Override
    public void onCreate() {

        beaconManager = BeaconManager.getInstanceForApplication(this);
        broadcastThread BroadcastThread = new broadcastThread();
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.bind(this);
        BroadcastThread.start();



    }

    public class broadcastThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(500);

                    Intent beaconIntent = new Intent();
                    beaconIntent.setAction(beacon);

                    Calendar c = Calendar.getInstance();
                    int seconds = c.get(Calendar.SECOND);
                    int minutes = c.get(Calendar.MINUTE);
                    beaconIntent.putExtra("seconds", seconds);
                    beaconIntent.putExtra("minutes", minutes);

                    beaconIntent.putExtra("uuid1", uuid1);
                    beaconIntent.putExtra("distance1", distance1);
                    beaconIntent.putExtra("rssi1", rssi1);

                    beaconIntent.putExtra("uuid2", uuid2);
                    beaconIntent.putExtra("distance2", distance2);
                    beaconIntent.putExtra("rssi2", rssi2);

                    beaconIntent.putExtra("uuid3", uuid3);
                    beaconIntent.putExtra("distance3", distance3);
                    beaconIntent.putExtra("rssi3", rssi3);

                    beaconIntent.putExtra("uuid4", uuid4);
                    beaconIntent.putExtra("distance4", distance4);
                    beaconIntent.putExtra("rssi4", rssi4);

                    sendBroadcast(beaconIntent);


                }
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Beacon Searching.", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onBeaconServiceConnect() {
        // AprilBeacon1: B5B182C7-EAB1-4988-AA99-B5C1517008D9
        // AprilBeacon2: B5B182C7-EAB1-4988-AA99-B5C1517008D8
        // iPadBeacon: DA2BFB06-7CF3-5B40-BE96-8F1EC8891CE8
//        final org.altbeacon.beacon.Region region = new org.altbeacon.beacon.Region("myBeacon", Identifier.parse("B5B182C7-EAB1-4988-AA99-B5C1517008D9"), null, null);
//        final org.altbeacon.beacon.Region region2 = new org.altbeacon.beacon.Region("myBeacon", Identifier.parse("B5B182C7-EAB1-4988-AA99-B5C1517008D8"), null, null);
        beaconManager.addMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(org.altbeacon.beacon.Region region) {
                try {
                    Log.i(TAG, "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(org.altbeacon.beacon.Region region) {
                try {
                    Log.i(TAG, "didExitRegion");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, org.altbeacon.beacon.Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    Log.i(TAG, "distance: " + oneBeacon.getDistance() + " id: " +  oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                    if(oneBeacon.getId1().toString().equals("b5b182c7-eab1-4988-aa99-b5c1517008d9")) {
                        uuid1 = oneBeacon.getId1().toString();
                        distance1 = oneBeacon.getDistance();
                        rssi1 = oneBeacon.getRssi();

                    }
                    else if(oneBeacon.getId1().toString().equals("b5b182c7-eab1-4988-aa99-b5c1517008d8")){
                        uuid2 = oneBeacon.getId1().toString();
                        distance2 = oneBeacon.getDistance();
                        rssi2 = oneBeacon.getRssi();
                    }
                    else if(oneBeacon.getId1().toString().equals("da2bfb06-7cf3-5b40-be96-8f1ec8891ce8")){
                        uuid3 = oneBeacon.getId1().toString();
                        distance3 = oneBeacon.getDistance();
                        rssi3 = oneBeacon.getRssi();
                    }
                    else {
                        uuid4 = oneBeacon.getId1().toString();
                        distance4 = oneBeacon.getDistance();
                        rssi4 = oneBeacon.getRssi();
                    }

                }
            }
        });
        try {
            beaconManager.startMonitoringBeaconsInRegion(region1);
            beaconManager.startMonitoringBeaconsInRegion(region2);
            beaconManager.startMonitoringBeaconsInRegion(region3);
 //           beaconManager.startMonitoringBeaconsInRegion(region4);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        stopSelf();
    }

}

