window.onload = function() {
    createFloatingBarChart('chart2', [
        { x: 'Group 1', y: 3 },
        { x: 'Group 2', y: 5 },
        { x: 'Group 3', y: 7 },
        { x: 'Group 4', y: 9 }
    ], "line");

    createFloatingBarChart('chart3', [
        { x: 'A', y: 5 },
        { x: 'B', y: 8 },
        { x: 'C', y: 4 },
        { x: 'D', y: 6 }
    ], "line");
};

function createFloatingBarChart(canvasId, chartData, chartType) {
    const data = {
        labels: chartData.map(item => item.x), // x축 레이블 추출
        datasets: [{
            label: `Chart - ${canvasId}`,
            data: chartData.map(item => chartType === "line" ? item.y : item),
            backgroundColor: chartType === "line" ? 'rgba(0, 0, 0, 0)' : 'rgba(75, 192, 192, 0.5)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: chartType === "line" ? 2 : 1,
            fill: chartType === "line" ? false : true,
            tension: chartType === "line" ? 0.4 : 0
        }]
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'top'
            }
        },
        scales: {
            x: {
                type: 'category',
            },
            y: {
                type: 'linear',
                beginAtZero: true
            }
        }
    };

    const ctx = document.getElementById(canvasId).getContext('2d');
    new Chart(ctx, {
        type: chartType,
        data: data,
        options: options
    });
}
