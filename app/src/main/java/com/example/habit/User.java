package com.example.habit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class denoting a single User, having a list of Habits
 */
public class User {

    private String displayName;
    private String userName;
    private ArrayList<Habit> habits;

    /**
     * Constructor for user with habit list
     * @param displayName Non-unique name used throughout app
     * @param userName Unique name used for adding followers
     * @param habits List of habits
     */
    public User(String displayName, String userName, ArrayList<Habit> habits) {
        this.displayName = displayName;
        this.userName = userName;
        this.habits = habits;
    }

    /**
     * Constructor for user with no habits, simply creates user with empty habit list
     */
    public User(String displayName, String userName) {
        this(displayName, userName, new ArrayList<Habit>());
    }

    /**
     * @return The display name for the user, not necessarily unique in the system.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName New display name for a user, changing what they see in the app
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Username of user, which is unique in the system.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName Username to check for in system, throw error if it does otherwise set username
     */
    public void setUserName(String userName) {
        // TODO: Check if username exists here, should this be done in user class or another?
        this.userName = userName;
    }

    /**
     * @return all of User's habits
     */
    public ArrayList<Habit> getHabits() {
        return habits;
    }

    /**
     * Add a habit object to the habits collection and add a reference to that habit to a users
     * habits list.
     * @param uuid
     * @param habit
     */
    public static void addHabit(String uuid, Habit habit) {
        String habitId = addHabitToHabits(habit);
        addHabitToUser(uuid, habitId);
    }

    /**
     * Add a Habit object to the habits collection. Do not call this directly.
     * @param habit
     */
    private static String addHabitToHabits(Habit habit) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newHabit = db.collection("habits").document();
        newHabit.set(habit);
        return newHabit.getId();
    }

    /**
     * Add habit ID to a users habits list. Do not call this directly.
     * @param uuid
     * @param habitID
     */
    private static void addHabitToUser(String uuid, String habitID) {
        // String uuid, String habitID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("users").document(uuid);
        thisUser.update("habits", FieldValue.arrayUnion(habitID));
    }

    /**
     * @param habit Habit to delete from this user's list if it exists
     * @throws NoSuchElementException Thrown if habit does not exist in this User's list
     */
    public void deleteHabit(Habit habit) {
        if (this.habits.contains(habit)) {
            this.habits.remove(habit);
        } else {
            throw new NoSuchElementException("Habit to be deleted not found in habits list!");
        }
    }
}
