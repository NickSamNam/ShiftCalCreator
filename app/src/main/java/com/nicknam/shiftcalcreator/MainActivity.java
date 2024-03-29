package com.nicknam.shiftcalcreator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {
    public static  final int REQUEST_CODE_ABOUT = 1;
    private DatePickerDialogFragment dpdf;
    private ArrayList<Shift> shifts;
    private final SingleToast toast = new SingleToast();
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Set up ads
        MobileAds.initialize(this, getString(R.string.admob_id));
        interstitialAd = new InterstitialAd(this);
        // TODO: 18-7-2017 replace line below with: interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        final TextView tvName = (TextView) findViewById(R.id.toolbarSchedule_et_name);
        final TextView tvDateFrom = (TextView) findViewById(R.id.mainActivity_tv_dateFrom);
        final TextView tvDateTo = (TextView) findViewById(R.id.mainActivity_tv_dateTo);
        final LinearLayout contDateFrom = (LinearLayout) findViewById(R.id.activityMain_cont_dateFrom);
        final LinearLayout contDateTo = (LinearLayout) findViewById(R.id.activityMain_cont_dateTo);
        final ImageButton btnRestart = (ImageButton) findViewById(R.id.toolbarSchedule_btn_restart);
        final ImageButton btnInfo = (ImageButton) findViewById(R.id.toolbarSchedule_btn_info);
        final RecyclerView rvShifts = (RecyclerView) findViewById(R.id.activityMain_rv_shifts);
        final Button btnAddShift = (Button) findViewById(R.id.activityMain_btn_addShift);
        final Button btnAddDayOff = (Button) findViewById(R.id.activityMain_btn_addDayOff);
        final Button btnAddToCal = (Button) findViewById(R.id.activityMain_btn_addToCal);

//        Create date format
        final DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(this);

//        Recover saved instance state
        if (savedInstanceState != null) {
            dpdf = (DatePickerDialogFragment) getFragmentManager().findFragmentByTag("datePicker");

            if (dpdf == null)
                dpdf = new DatePickerDialogFragment();

            dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).setTime((Date) savedInstanceState.getSerializable("from"));
            dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).setTime((Date) savedInstanceState.getSerializable("to"));

            shifts = (ArrayList<Shift>) savedInstanceState.getSerializable("shifts");
        } else {
            dpdf = new DatePickerDialogFragment();
            shifts = new ArrayList<>();
        }

//        Set listeners
        dpdf.setCalFromSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet() {
                Calendar from = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                Calendar to = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO);
                tvDateFrom.setText(dateFormat.format(from.getTime()));
                if (from.after(to)) {
                    to.setTime(from.getTime());
                    to.add(Calendar.YEAR, 1);
                    tvDateTo.setText(dateFormat.format(to.getTime()));
                }
            }
        });

        dpdf.setCalToSetListener(new DatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet() {
                Calendar from = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);
                Calendar to = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO);
                if (to.before(from)) {
                    to.setTime(from.getTime());
                    toast.showText(MainActivity.this, R.string.errorToBeforeFrom, Toast.LENGTH_LONG);
                }
                tvDateTo.setText(dateFormat.format(to.getTime()));
            }
        });

//        Set click action for dateFrom container
        contDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setSelected(DatePickerDialogFragment.CALENDAR_FROM);
                dpdf.show(getFragmentManager(), "datePicker");
            }
        });

//        Set click action for dateTo container
        contDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpdf.setSelected(DatePickerDialogFragment.CALENDAR_TO);
                dpdf.show(getFragmentManager(), "datePicker");
            }
        });

//        Display date
        tvDateFrom.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).getTime()));
        tvDateTo.setText(dateFormat.format(dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).getTime()));

//        Restart button
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.restart)
                        .setMessage(R.string.restartMessage)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = MainActivity.this.getIntent();
                                MainActivity.this.finish();
                                startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

//        Setup List of shifts
        rvShifts.setLayoutManager(new LinearLayoutManager(this));
        final ShiftAdapter shiftAdapter = new ShiftAdapter(shifts);
        shiftAdapter.setOnItemClickListener(new ShiftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Bundle args = new Bundle();
                args.putSerializable("shift", shifts.get(position));
                ShiftDialogFragment shiftDialogFragment = new ShiftDialogFragment();
                shiftDialogFragment.setArguments(args);
                shiftDialogFragment.setCancelable(false);
                shiftDialogFragment.setOnShiftCreatedListener(new ShiftDialogFragment.OnShiftCreatedListener() {
                    @Override
                    public void onShiftCreated(Shift shift) {
                        shifts.set(position, shift);
                        shiftAdapter.notifyItemChanged(position);
                    }
                });
                shiftDialogFragment.show(getFragmentManager(), "shiftCreator");
            }
        });
        rvShifts.setAdapter(shiftAdapter);
        new ItemTouchHelper(new ItemTouchHelperCallback(shiftAdapter)).attachToRecyclerView(rvShifts);

//        AddShift button
        btnAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiftDialogFragment shiftDialogFragment = new ShiftDialogFragment();
                shiftDialogFragment.setCancelable(false);
                shiftDialogFragment.setOnShiftCreatedListener(new ShiftDialogFragment.OnShiftCreatedListener() {
                    @Override
                    public void onShiftCreated(Shift shift) {
                        shifts.add(shift);
                        shiftAdapter.notifyDataSetChanged();

//                        Show tutorial 2
                        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                        if (!preferences.getBoolean("tut2", false)) {
                            preferences.edit().putBoolean("tut2", true).apply();
                            getTut2().show(MainActivity.this);
                        }
                    }
                });
                shiftDialogFragment.show(getFragmentManager(), "shiftCreator");
            }
        });

//        AddDayOff button
        btnAddDayOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putBoolean("isDayOff", true);
                ShiftDialogFragment shiftDialogFragment = new ShiftDialogFragment();
                shiftDialogFragment.setArguments(args);
                shiftDialogFragment.setCancelable(false);
                shiftDialogFragment.setOnShiftCreatedListener(new ShiftDialogFragment.OnShiftCreatedListener() {
                    @Override
                    public void onShiftCreated(Shift shift) {
                        shifts.add(shift);
                        shiftAdapter.notifyDataSetChanged();
                    }
                });
                shiftDialogFragment.show(getFragmentManager(), "shiftCreator");
            }
        });

//        Lose focus on done
        tvName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findViewById(R.id.activityMain_root).requestFocus();
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

//        Add to calendar button
        btnAddToCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Check for correct input
                if (tvName.getText().toString().equals(""))
                    toast.showText(MainActivity.this, R.string.errorNoCalName, Toast.LENGTH_LONG);
                else {
//                    Check for min of 1 shift.
                    boolean noShifts = true;
                    for (Shift shift : shifts) {
                        if (!shift.isDayOff()) {
                            noShifts = false;
                            break;
                        }
                    }
                    if (noShifts) {
                        toast.showText(MainActivity.this, R.string.errorMoreShifts, Toast.LENGTH_LONG);
                        return;
                    }

//                    Export calendar
                    final ICal iCal = new ICal(MainActivity.this, tvName.getText().toString());
                    iCal.setOnExportResultListener(new ICal.OnExportResultListener() {
                        @Override
                        public void onSaved(final File file) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                                    String mime = getContentResolver().getType(uri);
                                    final Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(uri, mime);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                    if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                                        interstitialAd.loadAd(new AdRequest.Builder().build());
                                        startActivity(intent);
                                    } else
                                        toast.showText(MainActivity.this, R.string.noCalSupport, Toast.LENGTH_LONG);
                                }
                            });
                        }

                        @Override
                        public void onFailed(Exception e) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toast.showText(MainActivity.this, R.string.error, Toast.LENGTH_LONG);
                                }
                            });
                        }
                    });


//                Set correct date to shifts
                    int dFrom = 0;
                    for (int i = 0; i < shifts.size(); i++) {
                        Shift shift = shifts.get(i);

                        Calendar timeStart = shift.getTimeStart();
                        Calendar timeEnd = shift.getTimeEnd();
                        Calendar dateStart = dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM);

                        Calendar shiftStart = (Calendar) dateStart.clone();
                        Calendar shiftEnd = (Calendar) dateStart.clone();

                        shiftStart.set(dateStart.get(Calendar.YEAR), dateStart.get(Calendar.MONTH), dateStart.get(Calendar.DAY_OF_MONTH));
                        shiftStart.add(Calendar.DAY_OF_MONTH, dFrom);
                        shiftEnd.set(dateStart.get(Calendar.YEAR), dateStart.get(Calendar.MONTH), dateStart.get(Calendar.DAY_OF_MONTH));
                        shiftEnd.add(Calendar.DAY_OF_MONTH, dFrom);

                        if (!shift.isDayOff()) {
                            shiftStart.set(Calendar.HOUR_OF_DAY, timeStart.get(Calendar.HOUR_OF_DAY));
                            shiftStart.set(Calendar.MINUTE, timeStart.get(Calendar.MINUTE));
                            shiftStart.set(Calendar.SECOND, timeStart.get(Calendar.SECOND));
                            shiftStart.set(Calendar.MILLISECOND, timeStart.get(Calendar.MILLISECOND));
                            shiftEnd.set(Calendar.HOUR_OF_DAY, timeEnd.get(Calendar.HOUR_OF_DAY));
                            shiftEnd.set(Calendar.MINUTE, timeEnd.get(Calendar.MINUTE));
                            shiftEnd.set(Calendar.SECOND, timeEnd.get(Calendar.SECOND));
                            shiftEnd.set(Calendar.MILLISECOND, timeEnd.get(Calendar.MILLISECOND));

                            if (shiftEnd.before(shiftStart))
                                shiftEnd.add(Calendar.DAY_OF_MONTH, 1);

                            try {
                                Shift prevShift = shifts.get(i - 1);
                                if (!prevShift.isDayOff()) {
                                    Calendar timeWindow = (Calendar) prevShift.getTimeEnd().clone();
                                    timeWindow.add(Calendar.DAY_OF_MONTH, prevShift.getRepetition() - 1);
                                    timeWindow.add(Calendar.HOUR_OF_DAY, 12);
                                    if (!shiftStart.after(timeWindow)) {
                                        shiftStart.add(Calendar.DAY_OF_MONTH, 1);
                                        shiftEnd.add(Calendar.DAY_OF_MONTH, 1);
                                        dFrom++;
                                    }
                                }
                            } catch (IndexOutOfBoundsException ignored) {}
                        }

                        dFrom += shift.getRepetition();
                        shift.setTimeStart(shiftStart);
                        shift.setTimeEnd(shiftEnd);
                    }

//                    Set recurrence
                    iCal.setRecurrence(dFrom, dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO));

//                    Create cal.ics
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            iCal.addEvents(shifts);
                            iCal.exportToCache();
                        }
                    }).start();
                }
            }
        });

//        Show about page
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AboutActivity.class), REQUEST_CODE_ABOUT);
            }
        });

//        Show tutorial 1
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (!preferences.getBoolean("tut1", false)) {
            preferences.edit().putBoolean("tut1", true).apply();
            getTut1().start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("from", dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_FROM).getTime());
        outState.putSerializable("to", dpdf.getCalendar(DatePickerDialogFragment.CALENDAR_TO).getTime());
        outState.putSerializable("shifts", shifts);
    }

    @Override
    protected void onPause() {
        super.onPause();
        toast.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (interstitialAd.isLoaded())
            interstitialAd.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.exit)
                .setMessage(R.string.exitMessage)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.super.onBackPressed();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ABOUT && resultCode == Activity.RESULT_OK && data.getBooleanExtra("tut", false)) {
            if (shifts.size() == 0) {
                getTut1().start();
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                preferences.edit()
                        .putBoolean("tut2", false)
                        .apply();
            } else
                getTut1().addSequenceItem(getTut2()).start();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private MaterialShowcaseSequence getTut1() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(getResources().getColor(R.color.colorTutorialMask));
        config.setDismissTextColor(Color.BLACK);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);
        sequence.addSequenceItem(findViewById(R.id.activityMain_cont_dateFrom), getString(R.string.tut1_from), getString(R.string.tut_btn_done));
        sequence.addSequenceItem(findViewById(R.id.activityMain_cont_dateTo), getString(R.string.tut1_to), getString(R.string.tut_btn_done));
        sequence.addSequenceItem(findViewById(R.id.activityMain_btn_addShift), getString(R.string.tut1_addShift), getString(R.string.tut_btn_done));
        return sequence;
    }

    private MaterialShowcaseView getTut2() {
        return new MaterialShowcaseView.Builder(MainActivity.this)
                .withRectangleShape()
                .setMaskColour(getResources().getColor(R.color.colorTutorialMask))
                .setDismissTextColor(Color.BLACK)
                .setTarget(findViewById(R.id.activityMain_anker_rv_shifts))
                .setContentText(R.string.tut2_op)
                .setDismissText(R.string.tut_btn_done)
                .build();
    }
}