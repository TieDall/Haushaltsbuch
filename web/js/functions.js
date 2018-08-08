function modal(payments, income, outcome) {
    var radios = document.getElementsByName(payments);
    if (radios[0].checked) {  
        var x = document.getElementById(income);
        var y = document.getElementById(outcome);
        x.style.display = "block";
        y.style.display = "none";
        var z = document.getElementById("radios");
        z.style.display = "block";
    } else {
        var x = document.getElementById(outcome);
        var y = document.getElementById(income);
        x.style.display = "block"; 
        y.style.display = "none";
        var z = document.getElementById("radios");
        z.style.display = "block";
    }
}

function dashboard(first, second) {
    radiobtn = document.getElementById("radio_" + first);
    radiobtn.checked = true;
    var x = document.getElementById(first);
    var y = document.getElementById(second);
    var z = document.getElementById("radios");
    x.style.display = "block";
    y.style.display = "none";   
    z.style.display = "none";
}

function income(income, outcome) {
    var x = document.getElementById(income);
    var y = document.getElementById(outcome);
    x.style.display = "block";
    y.style.display = "none";
}

function outcome(outcome, income) {
    var x = document.getElementById(outcome);
    var y = document.getElementById(income);
    x.style.display = "block"; 
    y.style.display = "none";
} 

function respNav() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
        $('.collapse').collapse("hide");
    }
}

function currentMonth() {
    var months    = ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'];
    var now       = new Date();
    var thisMonth = months[now.getMonth()]; // getMonth method returns the month of the date (0-January :: 11-December)
    var htmlComp = document.getElementById("currentMonth");
    htmlComp.innerHTML = thisMonth;
}