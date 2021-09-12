package com.christophedurand.go4lunch.ui.workmatesView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.UserManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class WorkmatesViewModel extends AndroidViewModel {

    private final MutableLiveData<WorkmatesViewState> workmatesViewStateMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<WorkmatesViewState> getWorkmatesViewStateMutableLiveData() {
        return workmatesViewStateMutableLiveData;
    }

    private final LifecycleOwner owner;


    public WorkmatesViewModel(@NonNull Application application, LifecycleOwner owner) {
        super(application);

        this.owner = owner;

        workmatesViewStateMutableLiveData.setValue(new WorkmatesViewState(createWorkmatesDataSource()));

    }

    private FirestoreRecyclerOptions<User> createWorkmatesDataSource() {
        createUsers();
        Query query = UserManager.getInstance().getAllUsers();
        return new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).setLifecycleOwner(owner).build();
    }

    private void createUsers() {
        UserManager.getInstance().createUser("0", "Phuong My Love", "batman@gmail.com", "", "", "");
        UserManager.getInstance().createUser("1", "Superman", "superman@gmail.com", "", "", "");
        UserManager.getInstance().createUser("2", "Iron Man", "iron-man@gmail.com", "", "", "");
        UserManager.getInstance().createUser("3", "Spiderman Le petit neveu", "spiderman@gmail.com", "", "", "");
        UserManager.getInstance().createUser("4", "Wonder Woman", "wonder-woman@gmail.com", "", "", "");
        UserManager.getInstance().createUser("5", "Aquaman", "aquaman@gmail.com", "", "", "");
        UserManager.getInstance().createUser("6", "Obi-Wan", "obi-wan@gmail.com", "", "", "");
        UserManager.getInstance().createUser("7", "Dark Vador", "dark.vador@gmail.com", "", "", "");
        UserManager.getInstance().createUser("8", "Son Goku", "songoku@gmail.com", "", "", "");
        UserManager.getInstance().createUser("9", "Vegeta", "vegeta@gmail.com", "", "", "");
    }

}