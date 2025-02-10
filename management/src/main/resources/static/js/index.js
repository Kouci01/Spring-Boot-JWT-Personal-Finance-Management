// scripts.js
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// Extract the token from the URL
const token = getQueryParam("token");

if (token) {
    // Save the token to sessionStorage
    sessionStorage.setItem("jwt", token);

    // Optionally remove the token from the URL
    const url = new URL(window.location.href);
    url.searchParams.delete("token");
    window.history.replaceState(null, "", url); // Update URL without refreshing
}else if(sessionStorage.getItem("jwt") === null){
    window.location.href = "login.html";
}

document.addEventListener("DOMContentLoaded", async () => {
    // Sample data for the dashboard
    let date = new Date();
    let firstDay = new Date(date.getFullYear(), date.getMonth(), 2).toISOString().split('T')[0];
    let lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).toISOString().split('T')[0];;

    const url = "http://localhost:8080/api/finance/transactions"
    const token = sessionStorage.getItem("jwt")  // Replace with your actual token

    const userResponse = await fetch("http://localhost:8080/auth/user-info", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    if(userResponse.ok){
        const userData = await userResponse.json();
        document.getElementById("user-label").innerHTML = userData.name;
    }

    let response = await fetch(url+"/summary?startDate="+ firstDay + "&endDate=" +lastDay, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        mode: "cors"
    });

    if(response.status === 403){
        alert("Session expired please login again");
        sessionStorage.removeItem("jwt")
        window.location.href = "login.html";
    } else if (!response.ok){
        throw new Error('Network response was not ok');
    }

    let datas = await response.json();
    let dataLabels = [];
    let dataAmount = [];
    let monthlyIncome = 0;
    let monthlyExpenses = 0;
    datas.forEach(data =>{
        dataLabels.push(data.categoryName);
        dataAmount.push(data.total);
        if(data.categoryName === "Income"){
            monthlyIncome+=data.total;
        }else{
            monthlyExpenses+=data.total;
        }
    });
    let totalBalance = monthlyIncome - monthlyExpenses;

    const sampleData = {
        totalBalance: 5000.00,
        monthlyIncome: 2000.00,
        monthlyExpenses: 1500.00,
        transactions: [
            {date: "2025-01-01", description: "Grocery Shopping", category: "Food", amount: -100.00},
            {date: "2025-01-05", description: "Salary", category: "Income", amount: 2000.00},
            {date: "2025-01-10", description: "Utility Bill", category: "Bills", amount: -150.00}
        ]
    };

    // Update Overview Section
    document.getElementById("total-balance").textContent = `IDR ${totalBalance.toFixed(2)}`;
    document.getElementById("monthly-income").textContent = `IDR ${monthlyIncome.toFixed(2)}`;
    document.getElementById("monthly-expenses").textContent = `IDR ${monthlyExpenses.toFixed(2)}`;

    // Populate Transactions Table
    const tabsContainer = document.getElementById('category-tabs');
    const transactionList = document.getElementById('transaction-list');
    const transactionSummary = document.getElementById('transaction-summary');

    response = await fetch(url+"/goals?startDate="+ firstDay + "&endDate=" +lastDay, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        mode: "cors"
    });

    datas = await response.json();
    // console.log(datas);

    // Function to create tabs dynamically based on transaction categories
    function generateTabs() {
        const categories = ['All', ...new Set(datas.map(goal => goal.goalName))];

        categories.forEach(category => {
            const tabButton = document.createElement('button');
            tabButton.classList.add('tab-button');
            tabButton.textContent = category;
            tabButton.dataset.category = category;

            tabButton.addEventListener('click', () => {
                // Remove active class from all tabs
                document.querySelectorAll('.tab-button').forEach(button => button.classList.remove('active'));
                // Add active class to the clicked tab
                tabButton.classList.add('active');

                // Filter transactions based on the selected category
                filterTransactions(category);
            });

            // Add the first active class to the "All" tab
            if (category === 'All') {
                tabButton.classList.add('active');
            }

            tabsContainer.appendChild(tabButton);
        });
    }

    // Function to filter and display transactions based on selected category
    function filterTransactions(category) {
        transactionList.innerHTML = ''; // Clear current transactions
        transactionSummary.innerHTML = '';

        const filteredTransactions = category === 'All' ? datas : datas.filter(goal => goal.goalName === category);

        let totalAmount = 0;
        const status = category === 'All' ? '-' : filteredTransactions[0].status;

        filteredTransactions.forEach(txn => {
            const row = `
                <tr>
                    <td>${txn.date}</td>
                    <td>${txn.description}</td>
                    <td>${txn.goalName}</td>
                    <td>IDR ${txn.amount}</td>
                </tr>
            `;
            transactionList.insertAdjacentHTML('beforeend', row);
            totalAmount += txn.amount;
        });

        const summaryRow = `
            <tr>
                <td colspan="3" style="text-align: end"><strong>Total</strong></td>
                <td><strong>IDR ${totalAmount}</strong></td>
            </tr>
            <tr>
                <td colspan="3" style="text-align: end"><strong>Status</strong></td>
                <td><strong>${status}</strong></td>
            </tr>
        `;
        transactionSummary.insertAdjacentHTML('beforeend', summaryRow);
    }

    // Initialize the tabs and display transactions for "All" category by default
    generateTabs();
    filterTransactions('All');

    // Implement Doughnut Chart using Chart.js
    const chartCanvas = document.getElementById("report-chart");
    if (chartCanvas) {
        chartCanvas.style.maxWidth = "400px";
        chartCanvas.style.margin = "0 auto";

        const ctx = chartCanvas.getContext("2d");

        // Example data for the doughnut chart
        const data = {
            labels: dataLabels,
            datasets: [{
                data: dataAmount, // Example amounts
                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                borderWidth: 0 // Remove border for a smooth doughnut effect
            }]
        };

        new Chart(ctx, {
            type: 'doughnut',
            data: data,
            options: {
                responsive: true,
                animation: {
                    animateScale: true, // Scale animation
                    animateRotate: true // Rotation animation
                },
                plugins: {
                    legend: {
                        position: 'top',
                        labels: {
                            font: {
                                family: 'Arial, sans-serif', // Custom font
                                size: 16
                            }
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function (tooltipItem) {
                                return `${tooltipItem.label}: $${tooltipItem.raw.toFixed(2)}`;
                            }
                        }
                    }
                },
                layout: {
                    padding: {
                        top: 10,
                        bottom: 10
                    }
                },
                hover: {
                    mode: 'nearest',
                    intersect: false,
                    animationDuration: 400, // Add hover animation duration
                    onHover: function (event, chartElement) {
                        if (chartElement.length) {
                            chartElement[0]._model.backgroundColor = "#FFDD00"; // Highlight color on hover
                        }
                    }
                }
            }
        });
    }

    // Settings Form Handler
    const settingsForm = document.getElementById("settings-form");
    settingsForm.addEventListener("submit", (event) => {
        event.preventDefault();
        const currency = document.getElementById("currency").value;
        alert(`Settings saved. Currency: ${currency}`);
    });

    const dropdownButton = document.getElementById('user-dropdown-button');
    const dropdownMenu = document.getElementById('user-dropdown-menu');

    // Toggle dropdown menu visibility
    dropdownButton.addEventListener('click', () => {
        const dropdown = dropdownButton.parentElement;
        dropdown.classList.toggle('active');
    });

    // Close dropdown menu when clicking outside
    document.addEventListener('click', (event) => {
        if (!event.target.closest('.user-dropdown')) {
            document.querySelectorAll('.user-dropdown').forEach(dropdown => {
                dropdown.classList.remove('active');
            });
        }
    });

    document.getElementById("logout").addEventListener("click", ()=>{
        sessionStorage.removeItem("jwt");
        alert("Logout successful");
        window.location.href = "login.html";
    });
});
