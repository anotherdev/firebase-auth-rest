var firebaseConfig = {
    apiKey: "AIzaSyDz2SCXhveewkkuEC0fu4AiPFfHifeDHNE",
    authDomain: "huawei-dtse.firebaseapp.com",
    databaseURL: "https://huawei-dtse.firebaseio.com",
    projectId: "huawei-dtse",
    storageBucket: "huawei-dtse.appspot.com",
    messagingSenderId: "167992156616",
    appId: "1:167992156616:android:d3f72ea442338ff1d8a0fd",
    measurementId: "G-measurement-id",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    var json = JSON.stringify(user);
    console.log("SIGNED IN: " + json);
    FirebaseAuthJs.onAuthStateChanged(json);
  } else {
    console.log("SIGNED OUT");
  }
});
