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
    var displayName = user.displayName;
    var email = user.email;
    var emailVerified = user.emailVerified;
    var photoURL = user.photoURL;
    var isAnonymous = user.isAnonymous;
    var uid = user.uid;
    var providerData = user.providerData;
    console.log("SIGNED IN: " + uid + " " + email + " " + user.refreshToken);
  } else {
    console.log("SIGNED OUT");
    firebase.auth().signInWithEmailAndPassword("a@b.com", "123456").catch(function(error) {
      var errorCode = error.code;
      var errorMessage = error.message;
      console.log(errorCode + errorMessage);
    });
  }
});
