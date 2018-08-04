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

function drawPieIn() {
    var ctx = document.getElementById("piechartIncome");
    var data = {
        datasets: [{
            data: [
                1400, 
                194, 
                20,
            ],
            backgroundColor: [
                'rgb(54, 162, 235)',
                'rgb(255, 159, 64)',
                'rgb(255, 205, 86)',
            ],
            label: "Einnahmen"
        }],
        labels: [
            'Gehalt',
            'Sozialleistung',
            'Geschenk',
        ]
    };
    var options = {
	responsive: false
    };
    var myPieChart = new Chart(ctx,{
        type: 'pie',
        data: data,
        options: options
    });
}

function drawPieOut() {
    var ctx = document.getElementById("piechartOutcome");
    var data = {
        datasets: [{
            data: [
                151.2, 
                400, 
                52.30,
            ],
            backgroundColor: [
                'rgb(54, 162, 235)',
                'rgb(255, 159, 64)',
                'rgb(255, 205, 86)',
            ],
            label: "Ausgaben"
        }],
        labels: [
            'Lebensmittel',
            'Miete',
            'Bahn',
        ]
    };
    var options = {
	responsive: false
    };
    var myPieChart = new Chart(ctx,{
        type: 'pie',
        data: data,
        options: options
    });
}

function drawTrend() {
    var ctx = document.getElementById("chartTrend");    
    var options = {
	responsive: false
    };
    var data = {
        datasets: [{
            data: [
                {x:'März', y:-15}, 
                {x:'April', y:-111}, 
                {x:'Mai', y:20}, 
                {x:'Juni', y:70}, 
                {x:'Juli', y:2}, 
                {x:'August', y:13},
                {x:'September', y:13}],
            backgroundColor: [
                'rgb(54, 162, 235)',
                'rgb(255, 159, 64)',
                'rgb(255, 205, 86)',
                'rgb(54, 162, 235)',
                'rgb(255, 159, 64)',
                'rgb(255, 205, 86)',
                'rgb(54, 162, 235)',
                'rgb(54, 162, 235)',
            ],
            label: "Bilanz"
        }],
        labels: [
            'März',
            'April',
            'Mai',
            'Juni',
            'Juli',
            'August',
            'September',
        ]
    };
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
    });    
}