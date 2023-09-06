package com.example.messenger;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }

                if (currentUser.getUid() == null)
                    return;

//                Log.e("UsersViewModel", currentUser.toString());
//                Log.e("UsersViewModel", currentUser.getUid());
//
//                Log.e("UsersViewModel", "currentUser != null" );
                List<User> usersList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!Objects.equals(user.getId(), currentUser.getUid())) {
                        Log.e("UsersViewModel", user.getId() + "  - user from FB");
                        Log.e("UsersViewModel", currentUser.getUid() + "  - current user");
                        if (user.getId() != null) {
                            usersList.add(user);
                        }
                        Log.e("UsersViewModel", user.toString());
                    }

                }
                users.setValue(usersList);
                Log.e("UsersViewModel", usersList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void logOut() {setUserOnline(false);
        auth.signOut();
    }

    public void setUserOnline(Boolean isOnline) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        userReference.child(firebaseUser.getUid()).child("online").setValue(isOnline);
    }

//    public void setUserOnline(Boolean isOnline) {
//        referenceUsers.child(currentUserId).child("online").setValue(isOnline);
//    }

}
