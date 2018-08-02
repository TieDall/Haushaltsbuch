function modal() {
    var radios = document.getElementsByName("payments");
    if (radios[0].checked) {  
        var x = document.getElementById("income");
        var y = document.getElementById("outcome");
        x.style.display = "block";
        y.style.display = "none";
    } else {
        var x = document.getElementById("outcome");
        var y = document.getElementById("income");
        x.style.display = "block"; 
        y.style.display = "none";
    }
}

function income() {
    var x = document.getElementById("income");
    var y = document.getElementById("outcome");
    x.style.display = "block";
    y.style.display = "none";
}

function outcome() {
    var x = document.getElementById("outcome");
    var y = document.getElementById("income");
    x.style.display = "block"; 
    y.style.display = "none";
} 

function currentMonth() {
    var months    = ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'];
    var now       = new Date();
    var thisMonth = months[now.getMonth()]; // getMonth method returns the month of the date (0-January :: 11-December)
    console.log(thisMonth);
    var htmlComp = document.getElementById("currentMonth");
    htmlComp.innerHTML = thisMonth;
}

