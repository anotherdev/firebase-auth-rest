var firebaseConfig = {
    apiKey: "AIzaSyDz2SCXhveewkkuEC0fu4AiPFfHifeDHNE",
    authDomain: "project-id.firebaseapp.com",
    databaseURL: "https://huawei-dtse.firebaseio.com",
    projectId: "huawei-dtse",
    storageBucket: "huawei-dtse.appspot.com",
    messagingSenderId: "167992156616",
    appId: "1:167992156616:android:d3f72ea442338ff1d8a0fd",
    measurementId: "G-measurement-id",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

console.log(firebase.app().name);

firebase.auth().signInWithEmailAndPassword("a@b.com", "123456").catch(function(error) {
  var errorCode = error.code;
  var errorMessage = error.message;
  console.log(errorCode + errorMessage);
});