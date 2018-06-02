package com.example.asus.mediudoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleRequestAppointment extends AppCompatActivity {

    private DatabaseReference mDatabase, mPatientDatabase, mDocDatabase, mAptConfirmDocDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgressDialog;
    private TextView mReason, mDate, mTime, mPatient_name, mStatus;
    private Button mConfirm, mDecline, mReschedule;
    private String patient_id, apt_id;
    private String reason, date, time, patientName, status, doctor_id,doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_request_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        apt_id = getIntent().getStringExtra("apt_id");
        mReason= (TextView)findViewById(R.id.tvReason);
        mDate= (TextView)findViewById(R.id.tvDate);
        mTime=(TextView)findViewById(R.id.tvTime);
        mPatient_name=(TextView)findViewById(R.id.tvPatientName);
        mStatus= (TextView)findViewById(R.id.tvStatus);
        mConfirm= (Button)findViewById(R.id.btConfirmApt);
        mDecline = (Button)findViewById(R.id.btDeclintApt);
        mReschedule = (Button)findViewById(R.id.btRescheduleApt);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Appointments").child(apt_id);
        mDocDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor_Users").child(mCurrentUser.getUid()).child("Appointments").child("Request").child(apt_id);
        mPatientDatabase= FirebaseDatabase.getInstance().getReference().child("Patient_Users");
        mAptConfirmDocDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor_Users").child(mCurrentUser.getUid()).child("Appointments").child("Confirm").child(apt_id);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reason= dataSnapshot.child("reason").getValue().toString();
                date= dataSnapshot.child("date").getValue().toString();
                time= dataSnapshot.child("time").getValue().toString();
                patientName= dataSnapshot.child("patient_name").getValue().toString();
                status= dataSnapshot.child("status").getValue().toString();
                patient_id= dataSnapshot.child("patient_id").getValue().toString();
                doctor_id=dataSnapshot.child("doctor_id").getValue().toString();
                doctorName= dataSnapshot.child("doctor_name").getValue().toString();

                mPatient_name.setText(patientName);
                mReason.setText(reason);
                mDate.setText(date);
                mTime.setText(time);
                mStatus.setText(status);

                if(status.equals("declined")){
                    mDecline.setVisibility(View.GONE);
                    mReschedule.setVisibility(View.GONE);
                    mConfirm.setVisibility(View.GONE);
                }
                if(status.equals("confirmed")){
                    mConfirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();

            }
        });

        mDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("status").setValue("declined");
                mDocDatabase.removeValue();
                mPatientDatabase.child(patient_id).child("Appointments").child("Request").child(apt_id).removeValue();

                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("doctor_name").setValue(doctorName);
                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("doctor_id").setValue(doctor_id);
                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("reason").setValue(reason);
                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("date").setValue(date);
                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("time").setValue(time);
                mPatientDatabase.child(patient_id).child("Appointments").child("Declined").child(apt_id).child("status").setValue("declined");


//                mDocDatabase.child("reason").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            mPatientDatabase.child(doc_id).child("Appointments").child("Request").child(apt_id).child("reason").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(SingleRequestAppointment.this, "Appointment Declined", Toast.LENGTH_SHORT);
//                                        startActivity(new Intent(getApplicationContext(), RequestedAptListActivity.class));
//                                    }else {
//                                        Toast.makeText(SingleRequestAppointment.this, "Appointment not declined", Toast.LENGTH_SHORT);
//                                    }
//                                }
//                            });
//                        }else {
//                            Toast.makeText(SingleRequestAppointment.this, "Appointment not declined", Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
//                Toast.makeText(SingleRequestAppointment.this, "Appointment not declined", Toast.LENGTH_SHORT);



            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("status").setValue("confirmed");
                mDocDatabase.removeValue();
                mPatientDatabase.child(patient_id).child("Appointments").child("Request").child(apt_id).removeValue();

                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("doctor_name").setValue(doctorName);
                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("doctor_id").setValue(doctor_id);
                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("reason").setValue(reason);
                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("date").setValue(date);
                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("time").setValue(time);
                mPatientDatabase.child(patient_id).child("Appointments").child("Confirm").child(apt_id).child("status").setValue("confirm");

                mAptConfirmDocDatabase.child("patient_name").setValue(patientName);
                mAptConfirmDocDatabase.child("patient_id").setValue(patient_id);
                mAptConfirmDocDatabase.child("reason").setValue(reason);
                mAptConfirmDocDatabase.child("date").setValue(date);
                mAptConfirmDocDatabase.child("time").setValue(time);
                mAptConfirmDocDatabase.child("status").setValue("confirm");
            }
        });

        mReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(SingleRequestAppointment.this, SingleRescheduleAppointment.class);
                profileIntent.putExtra("apt_id", apt_id);
                startActivity(profileIntent);
            }
        });
    }
}
