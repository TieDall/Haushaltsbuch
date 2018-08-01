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