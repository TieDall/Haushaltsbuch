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



function drawChart() {
    //income
    var dataIn = google.visualization.arrayToDataTable([
        ['Kategorie', '€'],
        ['Gehalt', 1400.32],
        ['Sozialhilfen', 192]
        ]);

    var options = {
        is3D: true,
        legend: 'bottom'
    };

    var chartIn = new google.visualization.PieChart(document.getElementById('piechartIncome'));
    chartIn.draw(dataIn, options);
    
    // outcome
    var dataOut = google.visualization.arrayToDataTable([
        ['Kategorie', '€'],
        ['Miete', 410],
        ['Kleidung', 80]
        ]);

    var chartOut = new google.visualization.PieChart(document.getElementById('piechartOutcome'));
    chartOut.draw(dataOut, options);
}